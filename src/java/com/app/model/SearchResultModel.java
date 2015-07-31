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

package com.app.model;

import java.util.Date;

/**
 * @author Jonathan McCann
 */
public class SearchResultModel {

	public SearchResultModel() {
	}

	public SearchResultModel(
		int searchQueryId, String itemId, String itemTitle, double auctionPrice,
		double fixedPrice, String itemURL, String galleryURL, Date endingTime,
		String typeOfAuction) {

		_searchQueryId = searchQueryId;
		_itemId = itemId;
		_itemTitle = itemTitle;
		_auctionPrice = auctionPrice;
		_fixedPrice = fixedPrice;
		_itemURL = itemURL;
		_galleryURL = galleryURL;
		_endingTime = endingTime;
		_typeOfAuction = typeOfAuction;
	}

	public boolean equals(Object obj) {
		if ((obj == null) || !(obj instanceof SearchResultModel)) {
			return false;
		}

		return (_itemId.equals(((SearchResultModel)obj).getItemId()));
	}

	public double getAuctionPrice() {
		return _auctionPrice;
	}

	public Date getEndingTime() {
		return _endingTime;
	}

	public double getFixedPrice() {
		return _fixedPrice;
	}

	public String getGalleryURL() {
		return _galleryURL;
	}

	public String getItemId() {
		return _itemId;
	}

	public String getItemTitle() {
		return _itemTitle;
	}

	public String getItemURL() {
		return _itemURL;
	}

	public int getSearchQueryId() {
		return _searchQueryId;
	}

	public int getSearchResultId() {
		return _searchResultId;
	}

	public String getTypeOfAuction() {
		return _typeOfAuction;
	}

	public int hashCode() {
		return Integer.parseInt(_itemId);
	}

	public void setAuctionPrice(double auctionPrice) {
		_auctionPrice = auctionPrice;
	}

	public void setEndingTime(Date itemEndingTime) {
		_endingTime = itemEndingTime;
	}

	public void setFixedPrice(double fixedPrice) {
		_fixedPrice = fixedPrice;
	}

	public void setGalleryURL(String galleryURL) {
		_galleryURL = galleryURL;
	}

	public void setItemId(String itemId) {
		_itemId = itemId;
	}

	public void setItemTitle(String itemTitle) {
		_itemTitle = itemTitle;
	}

	public void setItemURL(String itemURL) {
		_itemURL = itemURL;
	}

	public void setSearchQueryId(int searchQueryId) {
		_searchQueryId = searchQueryId;
	}

	public void setSearchResultId(int searchResultId) {
		_searchResultId = searchResultId;
	}

	public void setTypeOfAuction(String typeOfAuction) {
		_typeOfAuction = typeOfAuction;
	}

	private double _auctionPrice;
	private Date _endingTime;
	private double _fixedPrice;
	private String _galleryURL;
	private String _itemId;
	private String _itemTitle;
	private String _itemURL;
	private int _searchQueryId;
	private int _searchResultId;
	private String _typeOfAuction;

}