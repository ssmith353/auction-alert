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
import com.app.util.EbayAPIUtil;
import com.app.util.ReleaseUtil;

import com.ebay.sdk.ApiContext;
import com.ebay.sdk.call.GetCategoriesCall;
import com.ebay.soap.eBLBaseComponents.CategoryType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.SiteCodeType;

import java.lang.reflect.Method;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
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

		//setUpApiContext();
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
		EbayAPIUtil.loadApiContext("ebay.token");

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
	public void testInitializeCategories() throws Exception {
		ReleaseUtil.addRelease(_CATEGORY_RELEASE_NAME, "100");

		CategoryUtil.initializeCategories();
	}

	@Test
	public void testIsNewerCategoryVersion() throws Exception {
		Method method = _clazz.getDeclaredMethod(
			"_isNewerCategoryVersion", String.class);

		method.setAccessible(true);

		Assert.assertTrue((boolean)method.invoke(_classInstance, ""));
		Assert.assertTrue((boolean)method.invoke(_classInstance, "1"));
		Assert.assertTrue((boolean)method.invoke(_classInstance, "100"));
		Assert.assertTrue((boolean)method.invoke(_classInstance, "200"));

		ReleaseUtil.addRelease(_CATEGORY_RELEASE_NAME, "100");

		Assert.assertFalse((boolean)method.invoke(_classInstance, ""));
		Assert.assertFalse((boolean)method.invoke(_classInstance, "1"));
		Assert.assertFalse((boolean)method.invoke(_classInstance, "100"));
		Assert.assertTrue((boolean)method.invoke(_classInstance, "200"));
	}

	@Test
	public void testPopulateCategoriesWithNewerVersion() throws Exception {
		GetCategoriesCall getCategoriesCall = Mockito.mock(
			GetCategoriesCall.class);

		Mockito.doReturn(
			"101"
		).when(
			getCategoriesCall
		).getReturnedCategoryVersion();

		CategoryType[] ebayCategories = new CategoryType[1];

		ebayCategories[0] = new CategoryType();

		ebayCategories[0].setCategoryID("2");
		ebayCategories[0].setCategoryName("newParentCategory");
		ebayCategories[0].setCategoryParentID(new String[1]);
		ebayCategories[0].setCategoryParentID(0, "2");
		ebayCategories[0].setCategoryLevel(1);

		Mockito.doReturn(
			ebayCategories
		).when(
			getCategoriesCall
		).getCategories();

		Method populateCategories = _clazz.getDeclaredMethod(
			"_populateCategories", GetCategoriesCall.class);

		populateCategories.setAccessible(true);

		ReleaseUtil.addRelease(_CATEGORY_RELEASE_NAME, "100");

		_addCategory("1", "parentCategory", "1", 1);

		populateCategories.invoke(_classInstance, getCategoriesCall);

		List<Category> categories = CategoryUtil.getParentCategories();

		Assert.assertEquals(1, categories.size());

		Category category = categories.get(0);

		Assert.assertEquals("2", category.getCategoryId());
		Assert.assertEquals("newParentCategory", category.getCategoryName());
		Assert.assertEquals("2", category.getCategoryParentId());
		Assert.assertEquals(1, category.getCategoryLevel());

		Assert.assertEquals(
			"101", ReleaseUtil.getReleaseVersion(_CATEGORY_RELEASE_NAME));
	}

	@Test
	public void testPopulateCategoriesWithOlderVersion() throws Exception {
		Method populateCategories = _clazz.getDeclaredMethod(
			"_populateCategories", GetCategoriesCall.class);

		populateCategories.setAccessible(true);

		ReleaseUtil.addRelease(_CATEGORY_RELEASE_NAME, "100");

		_addCategory("1", "parentCategory", "1", 1);

		GetCategoriesCall getCategoriesCall = new GetCategoriesCall();

		populateCategories.invoke(_classInstance, getCategoriesCall);

		List<Category> categories = CategoryUtil.getParentCategories();

		Assert.assertEquals(1, categories.size());

		Category category = categories.get(0);

		Assert.assertEquals("1", category.getCategoryId());
		Assert.assertEquals("parentCategory", category.getCategoryName());
		Assert.assertEquals("1", category.getCategoryParentId());
		Assert.assertEquals(1, category.getCategoryLevel());
	}

	private static final int _SUB_CATEGORY_LEVEL_LIMIT = 2;

	private static Object _classInstance;
	private static Class _clazz;

}