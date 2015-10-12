/**
 * Copyright (c) 2014-present Jonathan McCann
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */

package com.app.test.controller;

import com.app.exception.DatabaseConnectionException;
import com.app.model.Category;
import com.app.model.SearchQuery;
import com.app.model.SearchResult;
import com.app.test.BaseTestCase;
import com.app.util.CategoryUtil;
import com.app.util.DatabaseUtil;
import com.app.util.PropertiesKeys;
import com.app.util.SearchQueryPreviousResultUtil;
import com.app.util.SearchQueryUtil;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.app.util.SearchResultUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Jonathan McCann
 */
@ContextConfiguration("/test-dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class SearchQueryControllerTest extends BaseTestCase {

	@Override
	public void doSetUp() throws DatabaseConnectionException, IOException {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@After
	public void tearDown() throws Exception {
		CategoryUtil.deleteCategories();

		SearchQueryUtil.deleteSearchQueries();

		SearchResultUtil.deleteSearchResult(1);

		SearchQueryPreviousResultUtil.deleteSearchQueryPreviousResults(1);
	}

	@Test
	public void testDeleteSearchQuery() throws Exception {
		String[] searchQueryIds = new String[] { "1" };

		SearchQueryUtil.addSearchQuery("First test keywords");

		SearchResult searchResult = new SearchResult(
			1, "1234", "itemTitle", 14.99, 14.99,
			"http://www.ebay.com/itm/1234", "http://www.ebay.com/123.jpg",
			new Date(), "Buy It Now");

		SearchResultUtil.addSearchResult(searchResult);

		SearchQueryPreviousResultUtil.addSearchQueryPreviousResult(1, "100");

		this.mockMvc.perform(post("/delete_search_query")
			.param("searchQueryIds", searchQueryIds))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:view_search_queries"));

		try {
			SearchQueryUtil.getSearchQuery(1);
		}
		catch (SQLException sqle) {
			Assert.assertEquals(SQLException.class, sqle.getClass());
		}

		List<SearchResult> searchResults =
			SearchResultUtil.getSearchQueryResults(1);

		List<String> searchQueryPreviousResults =
			SearchQueryPreviousResultUtil.getSearchQueryPreviousResults(1);

		Assert.assertEquals(0, searchResults.size());
		Assert.assertEquals(0, searchQueryPreviousResults.size());
	}
//
	@Test
	public void testDeleteSearchQueryWithNullSearchQueryIds() throws Exception {
		SearchQueryUtil.addSearchQuery("First test keywords");

		SearchResult searchResult = new SearchResult(
			1, "1234", "itemTitle", 14.99, 14.99,
			"http://www.ebay.com/itm/1234", "http://www.ebay.com/123.jpg",
			new Date(), "Buy It Now");

		SearchResultUtil.addSearchResult(searchResult);

		SearchQueryPreviousResultUtil.addSearchQueryPreviousResult(1, "100");

		this.mockMvc.perform(post("/delete_search_query"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:view_search_queries"));

		List<SearchQuery> searchQueries = SearchQueryUtil.getSearchQueries();

		List<SearchResult> searchResults =
			SearchResultUtil.getSearchQueryResults(1);

		List<String> searchQueryPreviousResults =
			SearchQueryPreviousResultUtil.getSearchQueryPreviousResults(1);

		Assert.assertEquals(1, searchQueries.size());
		Assert.assertEquals(1, searchResults.size());
		Assert.assertEquals(1, searchQueryPreviousResults.size());
	}

	@Test
	public void testGetAddSearchQuery() throws Exception {
		List<Category> categories = new ArrayList<>();

		Category category = new Category("100", "Category Name");

		categories.add(category);

		category = new Category("200", "Category Name2");

		categories.add(category);

		CategoryUtil.addCategories(categories);

		this.mockMvc.perform(get("/add_search_query"))
			.andExpect(status().isOk())
			.andExpect(view().name("add_search_query"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/add_search_query.jsp"))
			.andExpect(model().attribute(
				"searchQuery", hasProperty("searchQueryId", is(0))))
			.andExpect(model().attributeExists("searchQueryCategories"))
			.andExpect(model().attributeDoesNotExist("disabled"));
	}

	@Test
	public void testGetAddSearchQueryExceedingTotalNumberOfQueriesAllows()
		throws Exception {

		SearchQueryUtil.addSearchQuery("First test keywords");
		SearchQueryUtil.addSearchQuery("Second test keywords");

		this.mockMvc.perform(get("/add_search_query"))
			.andExpect(status().isOk())
			.andExpect(view().name("add_search_query"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/add_search_query.jsp"))
			.andExpect(model().attribute(
				"searchQuery", hasProperty("searchQueryId", is(0))))
			.andExpect(model().attributeExists("searchQueryCategories"))
			.andExpect(model().attribute("disabled", true));
	}

	@Test
	public void testHandleError() throws Exception {
		DatabaseUtil.setDatabaseProperties("test", "test", "test");

		this.mockMvc.perform(get("/add_search_query"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:error.jsp"));

		String databasePassword = System.getProperty(
			PropertiesKeys.JDBC_DEFAULT_PASSWORD);
		String databaseURL = System.getProperty(
			PropertiesKeys.JDBC_DEFAULT_URL);
		String databaseUsername = System.getProperty(
			PropertiesKeys.JDBC_DEFAULT_USERNAME);

		DatabaseUtil.setDatabaseProperties(
			databaseURL, databaseUsername, databasePassword);
	}

	@Test
	public void testPostAddSearchQueryExceedingTotalNumberOfQueriesAllows()
		throws Exception {

		SearchQueryUtil.addSearchQuery("First test keywords");
		SearchQueryUtil.addSearchQuery("Second test keywords");

		this.mockMvc.perform(post("/add_search_query"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:add_search_query"))
			.andExpect(model().attributeDoesNotExist("searchQueries"))
			.andExpect(model().attributeDoesNotExist("searchQueryCategories"))
			.andExpect(model().attribute("disabled", true));
	}

	@Test
	public void testPostAddSearchQueryWithNullSearchQuery() throws Exception {
		this.mockMvc.perform(post("/add_search_query"))
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:error.jsp"))
			.andExpect(model().attributeDoesNotExist("searchQueries"))
			.andExpect(model().attributeDoesNotExist("disabled"));
	}

	@Test
	public void testPostAddSearchQueryWithSearchQuery() throws Exception {
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(
			"/add_search_query");

		request.param("keywords", "First test keywords");

		this.mockMvc.perform(request)
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:view_search_queries"))
			.andExpect(model().attributeExists("searchQueries"))
			.andExpect(model().attributeDoesNotExist("disabled"));

		List<SearchQuery> searchQueries = SearchQueryUtil.getSearchQueries();

		Assert.assertEquals(1, searchQueries.size());

		SearchQuery searchQuery = searchQueries.get(0);

		Assert.assertEquals("First test keywords", searchQuery.getKeywords());
	}

	@Test
	public void testPostAddSearchQueryWithSearchQueryAndCategory()
		throws Exception {

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(
			"/add_search_query");

		request.param("keywords", "First test keywords");
		request.param("categoryId", "100");

		ResultActions resultActions = this.mockMvc.perform(request);

		resultActions.andExpect(status().isFound());
		resultActions.andExpect(view().name("redirect:view_search_queries"));
		resultActions.andExpect(model().attributeExists("searchQueries"));
		resultActions.andExpect(model().attributeDoesNotExist("disabled"));

		List<SearchQuery> searchQueries = SearchQueryUtil.getSearchQueries();

		Assert.assertEquals(1, searchQueries.size());

		SearchQuery searchQuery = searchQueries.get(0);

		Assert.assertEquals("First test keywords", searchQuery.getKeywords());
		Assert.assertEquals("100", searchQuery.getCategoryId());
	}

	@Test
	public void testViewSearchQueries() throws Exception {
		this.mockMvc.perform(get("/query"))
			.andExpect(status().isOk())
			.andExpect(view().name("view_search_queries"))
			.andExpect(forwardedUrl("/WEB-INF/jsp/view_search_queries.jsp"))
			.andExpect(model().attributeExists("searchQueries"));
	}

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

}