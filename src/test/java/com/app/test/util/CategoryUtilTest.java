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

package com.app.test.util;

import com.app.model.Category;
import com.app.test.BaseTestCase;
import com.app.util.CategoryUtil;
import com.app.util.ReleaseUtil;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.call.GetCategoriesCall;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jonathan McCann
 */
@ContextConfiguration("/test-dispatcher-servlet.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
public class CategoryUtilTest extends BaseTestCase {

	@BeforeClass
	public static void setUpClass() throws Exception {
		_clazz = Class.forName(CategoryUtil.class.getName());

		_classInstance = _clazz.newInstance();

		setUpApiContext();
	}

	@Before
	public void setUp() throws Exception {
		setUpDatabase();
	}

	@Test
	public void testAddCategory() throws Exception {
		_addCategory();

		List<Category> categories = CategoryUtil.getParentCategories();

		Assert.assertEquals(1, categories.size());

		Category category = categories.get(0);

		Assert.assertEquals(_CATEGORY_ID, category.getCategoryId());
		Assert.assertEquals(_CATEGORY_NAME, category.getCategoryName());
		Assert.assertEquals(_CATEGORY_PARENT_ID, category.getCategoryParentId());
		Assert.assertEquals(_CATEGORY_LEVEL, category.getCategoryLevel());
	}

	@Test
	public void testCreateGetCategoriesCall() throws Exception {
		Method method = _clazz.getDeclaredMethod("_createGetCategoriesCall");

		method.setAccessible(true);

		GetCategoriesCall getCategoriesCall = (GetCategoriesCall)method.invoke(
			_classInstance);

		ApiContext apiContext = getCategoriesCall.getApiContext();

		DetailLevelCodeType[] detailLevelCodeTypes = {
			DetailLevelCodeType.RETURN_ALL
		};

		Assert.assertNotNull(apiContext);
		Assert.assertEquals(
			SiteCodeType.US, getCategoriesCall.getCategorySiteID());
		Assert.assertArrayEquals(
			detailLevelCodeTypes, getCategoriesCall.getDetailLevel());
		Assert.assertEquals(
			_SUB_CATEGORY_LEVEL_LIMIT, getCategoriesCall.getLevelLimit());
		Assert.assertTrue(getCategoriesCall.getViewAllNodes());
	}

	@Test
	public void testDeleteCategories() throws Exception {
		_addCategory(
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5), 1);
		_addCategory(
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5), 1);

		CategoryUtil.deleteCategories();

		List<Category> categories = CategoryUtil.getParentCategories();

		Assert.assertEquals(0, categories.size());
	}

	@Test
	public void testGetParentCategories() throws Exception {
		_addCategory(
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5), 1);
		_addCategory(
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5),
			RandomStringUtils.randomAlphanumeric(5), 1);

		List<Category> categories = CategoryUtil.getParentCategories();

		Assert.assertEquals(2, categories.size());
	}

	@Test
	public void testGetSubcategories() throws Exception {
		_addCategory("1", "parentCategory", "1", 1);
		_addCategory("2", "subcategory", "1", 2);

		List<Category> categories = CategoryUtil.getSubcategories("1");

		Assert.assertEquals(1, categories.size());
	}

	@Test
	public void testIsNewerCategoryVersion() throws Exception {
		ReleaseUtil.addRelease(_CATEGORY_RELEASE_NAME, "100");

		Method method = _clazz.getDeclaredMethod(
			"_isNewerCategoryVersion", String.class);

		method.setAccessible(true);

		Assert.assertFalse((boolean)method.invoke(_classInstance, ""));
		Assert.assertFalse((boolean)method.invoke(_classInstance, "1"));
		Assert.assertFalse((boolean) method.invoke(_classInstance, "100"));
		Assert.assertTrue((boolean) method.invoke(_classInstance, "200"));
	}

	private static final int _SUB_CATEGORY_LEVEL_LIMIT = 2;

	private static Object _classInstance;
	private static Class _clazz;

}