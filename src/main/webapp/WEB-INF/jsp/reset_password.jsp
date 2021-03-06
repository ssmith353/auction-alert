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

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>Reset Password</title>

		<link href="<c:url value="/resources/css/tooltipster.css" />" rel="stylesheet">

		<script src="/resources/js/jquery-2.1.3.min.js" type="text/javascript"></script>
		<script src="/resources/js/jquery-tooltipster-3.0.min.js" type="text/javascript"></script>
		<script src="/resources/js/jquery-validate-1.14.0.min.js" type="text/javascript"></script>

		<script src="/resources/js/skel.min.js" type="text/javascript"></script>
		<script src="/resources/js/skel-layers.min.js" type="text/javascript"></script>
		<script src="/resources/js/init.js" type="text/javascript"></script>

		<script src="/resources/js/validate-user.js" type="text/javascript"></script>

		<noscript>
			<link href="/resources/css/skel.css" rel="stylesheet" />
			<link href="/resources/css/style.css" rel="stylesheet" />
			<link href="/resources/css/style-xlarge.css" rel="stylesheet" />
		</noscript>
	</head>

	<body>
		<header class="skel-layers-fixed" id="header">
			<h1><a href="/home">Auction Alert</a></h1>
			<nav id="nav">
				<ul>
					<li><a href="log_in">Log In</a></li>
					<li><a href="create_account" class="button special">Sign Up</a></li>
				</ul>
			</nav>
		</header>

		<section class="minor narrow">
			<h1>Reset Password</h1>
		</section>

		<div id="user-details">
			<c:if test="${not empty error}">
				<div id="error">
					<i class="fa-times-circle icon"></i>
					${error}
				</div>
			</c:if>

			<form:form commandName="resetPassword" id="resetPasswordForm">
				<div>
					<b>Email Address: </b><input id="emailAddress" name="emailAddress" type="email" />
				</div>

				<div>
					<b>Password: </b><input id="password" name="password" type="password" />
				</div>

				<div>
					<b>Password Reset Token: </b><input id="passwordResetToken" name="passwordResetToken" type="text" />
				</div>

				<div class="padding-top">
					<input class="button special" formaction="reset_password" formmethod="post" id="resetPasswordSubmit" type="submit" value="Reset Password" />
				</div>
			</form:form>
		</div>

		<%@ include file="footer.jspf" %>
	</body>
</html>