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

package com.app.util;

import com.app.dao.impl.SearchQueryDAOImpl;
import com.app.dao.impl.SearchQueryPreviousResultDAOImpl;
import com.app.dao.impl.SearchResultDAOImpl;
import com.app.exception.DatabaseConnectionException;
import com.app.model.SearchQueryModel;
import com.app.model.SearchResultModel;
import com.app.model.eBaySearchResult;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jonathan McCann
 */
public class SearchResultUtil {

	public static void performSearch()
		throws DatabaseConnectionException, SQLException {

		List<SearchQueryModel> searchQueryModels =
			_searchQueryDAOImpl.getSearchQueries();

		_log.info(
			"Getting eBay search results for {} search queries",
			searchQueryModels.size());

		Map<SearchQueryModel, List<SearchResultModel>> searchQueryResultMap =
			new HashMap<>();

		for (SearchQueryModel searchQueryModel : searchQueryModels) {
			List<SearchResultModel> searchResultModels =
				eBaySearchResult.geteBaySearchResults(searchQueryModel);

			searchResultModels = _filterSearchResults(
				searchQueryModel, searchResultModels);

			if (!searchResultModels.isEmpty()) {
				searchQueryResultMap.put(searchQueryModel, searchResultModels);
			}
		}

		if (!searchQueryResultMap.isEmpty()) {
			MailUtil.sendSearchResultsToRecipients(searchQueryResultMap);
		}
	}

	private static List<SearchResultModel> _filterSearchResults(
			SearchQueryModel searchQueryModel,
			List<SearchResultModel> newSearchResultModels)
		throws DatabaseConnectionException, SQLException {

		List<String> searchQueryPreviousResults =
			_searchQueryPreviousResultDAOImpl.getSearchQueryPreviousResults(
				searchQueryModel.getSearchQueryId());

		Iterator iterator = newSearchResultModels.iterator();

		while (iterator.hasNext()) {
			SearchResultModel searchResultModel =
				(SearchResultModel)iterator.next();

			if (searchQueryPreviousResults.contains(
					searchResultModel.getItemId())) {

				iterator.remove();
			}
		}

		if (!newSearchResultModels.isEmpty()) {
			_log.debug(
				"Found {} new search results for search query: {}",
				newSearchResultModels.size(),
				searchQueryModel.getSearchQuery());

			List<SearchResultModel> existingSearchResultModels =
				_searchResultDAOImpl.getSearchQueryResults(
					searchQueryModel.getSearchQueryId());

			_saveNewResultsAndRemoveOldResults(
				existingSearchResultModels, newSearchResultModels);
		}

		return newSearchResultModels;
	}

	private static void _saveNewResultsAndRemoveOldResults(
			List<SearchResultModel> existingSearchResultModels,
			List<SearchResultModel> newSearchResultModels)
		throws DatabaseConnectionException, SQLException {

		int numberOfSearchResultsToRemove =
			existingSearchResultModels.size() + newSearchResultModels.size() - 5;

		if (numberOfSearchResultsToRemove > 0) {
			for (int i = 0; i < numberOfSearchResultsToRemove; i++) {
				SearchResultModel searchResult = existingSearchResultModels.get(
					i);

				_searchResultDAOImpl.deleteSearchResult(
					searchResult.getSearchResultId());
			}
		}

		for (SearchResultModel searchResultModel : newSearchResultModels) {
			_searchResultDAOImpl.addSearchResult(searchResultModel);

			int searchQueryPreviousResultsCount =
				_searchQueryPreviousResultDAOImpl.
					getSearchQueryPreviousResultsCount(
						searchResultModel.getSearchQueryId());

			if (searchQueryPreviousResultsCount ==
					PropertiesValues.
						TOTAL_NUMBER_OF_PREVIOUS_SEARCH_RESULT_IDS) {

				_searchQueryPreviousResultDAOImpl.
					deleteSearchQueryPreviousResult(
						searchResultModel.getSearchQueryId());
			}

			_searchQueryPreviousResultDAOImpl.addSearchQueryPreviousResult(
				searchResultModel.getSearchQueryId(),
				searchResultModel.getItemId());
		}
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SearchResultUtil.class);

	private static final SearchQueryDAOImpl _searchQueryDAOImpl =
		new SearchQueryDAOImpl();
	private static final
		SearchQueryPreviousResultDAOImpl _searchQueryPreviousResultDAOImpl =
			new SearchQueryPreviousResultDAOImpl();
	private static final SearchResultDAOImpl _searchResultDAOImpl =
		new SearchResultDAOImpl();

}