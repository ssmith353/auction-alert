/**
 * Copyright (c) 2015-present Jonathan McCann
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

package com.app.model;

/**
 * @author Jonathan McCann
 */
public class CategoryModel {

	public CategoryModel() {
	}

	public CategoryModel(String categoryId, String categoryName) {
		_categoryId = categoryId;
		_categoryName = categoryName;
	}

	public String getCategoryId() {
		return _categoryId;
	}

	public String getCategoryName() {
		return _categoryName;
	}

	public void setCategoryId(String categoryId) {
		_categoryId = categoryId;
	}

	public void setCategoryName(String categoryName) {
		_categoryName = categoryName;
	}

	private String _categoryId;
	private String _categoryName;

}