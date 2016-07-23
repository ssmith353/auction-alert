<%--
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
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>Monitor</title>
		<link href="<c:url value="/resources/css/tooltipster.css" />" rel="stylesheet">
		<script src="<c:url value="/resources/js/jquery-2.1.3.min.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/jquery-tooltipster-3.0.min.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/jquery-validate-1.14.0.min.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/validate-search-query.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/skel.min.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/skel-layers.min.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/init.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/rss.js" />" type="text/javascript"></script>
		<script src="<c:url value="/resources/js/subcategory.js" />" type="text/javascript"></script>
		<noscript>
			<link rel="stylesheet" href="/resources/css/skel.css" />
			<link rel="stylesheet" href="/resources/css/style.css" />
			<link rel="stylesheet" href="/resources/css/style-xlarge.css" />
		</noscript>
	</head>
	<body>
		<%@ include file="header.jspf" %>

		<section id="banner" class="minor">
			<div class="inner">
				<h2>Monitor</h2>
			</div>
		</section>

		<div class="container padding-top">
			<div id="search" class="icon fa-angle-down">
				<span class="monitor-header">Search Query</span>
			</div>

			<div id="searchQuery" class="container padding-top">
				<input id="campaignId" type="hidden" value="${campaignId}">

				<form:form commandName="searchQuery" id="searchQueryForm">
					<form:input path="searchQueryId" type="hidden" value="${searchQuery.searchQueryId}" />

					<input id="initialSubcategoryId" type="hidden" value="${searchQuery.subcategoryId}" />

					<fmt:formatNumber pattern="0.00" value="${searchQuery.minPrice}" var="minPrice" />
					<fmt:formatNumber pattern="0.00" value="${searchQuery.maxPrice}" var="maxPrice" />

					<ul class="alt">
						<li>
							<b>Keywords:</b> <form:input maxlength="300" path="keywords" value="${searchQuery.keywords}" />
							<form:select path="categoryId">
								<form:option value="All Categories"></form:option>
								<form:options items="${searchQueryCategories}" />
							</form:select>
							<form:select disabled="true" id="subcategoryId" path="subcategoryId">
								<form:option value="All Subcategories"></form:option>
							</form:select>
						</li>
						<li>
							<b>Search Options:</b>

							<div>
								<form:checkbox label="Search Description" path="searchDescription" value="${searchQuery.searchDescription}" /> <br>
								<form:checkbox label="Free Shipping" path="freeShippingOnly" value="${searchQuery.freeShippingOnly}" />
							</div>
						</li>
						<li>
							<b>Listing Type:</b>

							<div>
								<form:checkbox label="Auction" path="auctionListing" value="${searchQuery.auctionListing}"/> <br>
								<form:checkbox label="Buy It Now" path="fixedPriceListing" value="${searchQuery.fixedPriceListing}" />
							</div>
						</li>
						<li>
							<b>Condition:</b>

							<div>
								<form:checkbox label="New" path="newCondition" value="${searchQuery.newCondition}"/> <br>
								<form:checkbox label="Used" path="usedCondition" value="${searchQuery.usedCondition}"/> <br>
								<form:checkbox label="Unspecified" path="unspecifiedCondition" value="${searchQuery.unspecifiedCondition}" />
							</div>
						</li>
						<li>
							<b>Price:</b>

							<div>
								Show items priced from <form:input path="minPrice" value="${minPrice}" /> to <form:input path="maxPrice" value="${maxPrice}" />
							</div>
						</li>
					</ul>
				</form:form>
			</div>

			<div>
				<button class="button special" id="startMonitoring">Start Monitoring</button>
				<button class="button special" id="stopMonitoring">Stop Monitoring</button>
				<button class="button special" id="clearResults">Clear Results</button>

				<div class="padding-top">
					<input id="desktopNotifications" type="checkbox" />
					<label for="desktopNotifications">Receive Desktop Notifications</label>
				</div>
			</div>

			<section class="special box">
				<h2 class="align-left">Results</h2>
				<div id="content">
					<h5>Please start monitoring in order to display results.</h5>
				</div>
			</section>
		</div>

		<%@ include file="footer.jspf" %>
	</body>
</html>