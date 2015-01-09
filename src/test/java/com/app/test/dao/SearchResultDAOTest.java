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

package com.app.test.dao;

import com.app.dao.impl.SearchResultDAOImpl;
import com.app.exception.DatabaseConnectionException;
import com.app.model.SearchResultModel;
import com.app.test.BaseDatabaseTestCase;

import java.sql.SQLException;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jonathan McCann
 */
public class SearchResultDAOTest extends BaseDatabaseTestCase {

	@Override
	public void doSetUp() throws DatabaseConnectionException {
		_searchResultDAOImpl = new SearchResultDAOImpl();
	}

	@Test
	public void testSearchResultDAO()
		throws DatabaseConnectionException, SQLException {

		// Test add with constructor

		Date endingTime = new Date();

		SearchResultModel searchResultModel = new SearchResultModel(
			1, "1234", "First Item", 14.99, 14.99,
			"http://www.ebay.com/itm/1234", "http://www.ebay.com/123.jpg",
			endingTime, "Auction");

		_searchResultDAOImpl.addSearchResult(searchResultModel);

		searchResultModel = new SearchResultModel(
			2, "2345", "Second Item", 14.99, 14.99,
			"http://www.ebay.com/itm/2345", "http://www.ebay.com/234.jpg",
			endingTime, "FixedPrice");

		_searchResultDAOImpl.addSearchResult(searchResultModel);

		// Test get

		SearchResultModel searchResult = _searchResultDAOImpl.getSearchResult(
			1);

		Assert.assertEquals(1, searchResult.getSearchResultId());
		Assert.assertEquals(1, searchResult.getSearchQueryId());
		Assert.assertEquals("1234", searchResult.getItemId());
		Assert.assertEquals("First Item", searchResult.getItemTitle());
		Assert.assertEquals(14.99, searchResult.getAuctionPrice(), 0);
		Assert.assertEquals(14.99, searchResult.getFixedPrice(), 0);
		Assert.assertEquals(
			"http://www.ebay.com/itm/1234", searchResult.getItemURL());
		Assert.assertEquals(
			"http://www.ebay.com/123.jpg", searchResult.getGalleryURL());
		Assert.assertEquals(endingTime, searchResult.getEndingTime());
		Assert.assertEquals("Auction", searchResult.getTypeOfAuction());

		// Test get multiple

		List<SearchResultModel> searchResultModels =
			_searchResultDAOImpl.getSearchResults();

		Assert.assertEquals(2, searchResultModels.size());

		SearchResultModel secondSearchResult = searchResultModels.get(1);

		Assert.assertEquals(2, secondSearchResult.getSearchResultId());
		Assert.assertEquals(2, secondSearchResult.getSearchQueryId());
		Assert.assertEquals("2345", secondSearchResult.getItemId());
		Assert.assertEquals("Second Item", secondSearchResult.getItemTitle());
		Assert.assertEquals(14.99, secondSearchResult.getAuctionPrice(), 0);
		Assert.assertEquals(14.99, secondSearchResult.getFixedPrice(), 0);
		Assert.assertEquals(
			"http://www.ebay.com/itm/2345", secondSearchResult.getItemURL());
		Assert.assertEquals(
			"http://www.ebay.com/234.jpg", secondSearchResult.getGalleryURL());
		Assert.assertEquals(endingTime, secondSearchResult.getEndingTime());
		Assert.assertEquals(
			"FixedPrice", secondSearchResult.getTypeOfAuction());

		// Test delete

		_searchResultDAOImpl.deleteSearchResult(1);

		searchResultModels = _searchResultDAOImpl.getSearchResults();

		Assert.assertEquals(1, searchResultModels.size());

		// Test find by search query

		searchResultModels = _searchResultDAOImpl.getSearchQueryResults(2);

		Assert.assertEquals(1, searchResultModels.size());

		_searchResultDAOImpl.deleteSearchQueryResults(2);

		searchResultModels = _searchResultDAOImpl.getSearchQueryResults(2);

		Assert.assertEquals(0, searchResultModels.size());
	}

	private static SearchResultDAOImpl _searchResultDAOImpl =
		new SearchResultDAOImpl();

}