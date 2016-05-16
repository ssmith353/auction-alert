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

import java.util.ArrayList;
import java.util.List;

import com.app.dao.CategoryDAO;
import com.app.model.Category;
import com.app.test.BaseTestCase;

import com.app.util.CategoryUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.statistics.StatisticsGateway;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jonathan McCann
 */
@ContextConfiguration("/test-dispatcher-servlet.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryDAOCacheTest extends BaseTestCase {

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private CategoryDAO categoryDAO;

	@Before
	public void setUp() throws Exception {
		setUpDatabase();
	}

	@Test
	public void testGetCategories() throws Exception {
		Cache cache = cacheManager.getCache("categories");

		StatisticsGateway statistics = cache.getStatistics();

		addCategory("1", "test1");
		addCategory("2", "test2");

		List<Category> categories = categoryDAO.getCategories();

		assertCategories(categories);

		categories = categoryDAO.getCategories();

		assertCategories(categories);

		Assert.assertEquals(1, statistics.cacheMissCount());
		Assert.assertEquals(1, statistics.cacheHitCount());

		categoryDAO.deleteCategories();

		categories = categoryDAO.getCategories();

		Assert.assertTrue(categories.isEmpty());

		categories = categoryDAO.getCategories();

		Assert.assertTrue(categories.isEmpty());

		Assert.assertEquals(2, statistics.cacheMissCount());
		Assert.assertEquals(2, statistics.cacheHitCount());
	}

	private void addCategory(String categoryId, String categoryName)
		throws Exception {

		List<Category> categories = new ArrayList<>();

		Category category = new Category(categoryId, categoryName);

		categories.add(category);

		categoryDAO.addCategories(categories);
	}

	private void assertCategories(List<Category> categories) {
		Assert.assertEquals(2, categories.size());

		Category firstCategory = categories.get(0);
		Category secondCategory = categories.get(1);

		Assert.assertEquals("1", firstCategory.getCategoryId());
		Assert.assertEquals("test1", firstCategory.getCategoryName());

		Assert.assertEquals("2", secondCategory.getCategoryId());
		Assert.assertEquals("test2", secondCategory.getCategoryName());
	}
}