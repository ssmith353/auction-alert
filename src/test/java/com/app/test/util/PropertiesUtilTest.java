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

import com.app.test.BaseTestCase;
import com.app.util.PropertiesUtil;
import com.app.util.PropertiesValues;

import java.io.IOException;

import java.net.URL;

import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jonathan McCann
 */
public class PropertiesUtilTest extends BaseTestCase {

	@BeforeClass
	public static void setUpClass() throws Exception {
		setUpProperties();
	}

	@Test
	public void testLoadConfigurationProperties() throws IOException {
		Class<?> clazz = getClass();

		URL resource = clazz.getResource("/test-config.properties");

		PropertiesUtil.loadConfigurationProperties(resource.getPath());

		Assert.assertEquals("Application ID", PropertiesValues.APPLICATION_ID);
		Assert.assertEquals("eBay Token", PropertiesValues.EBAY_TOKEN);
		Assert.assertEquals(
			"eBay Campaign Id", PropertiesValues.EBAY_CAMPAIGN_ID);
		Assert.assertEquals(
			"eBay Publisher Id", PropertiesValues.EBAY_PUBLISHER_ID);
		Assert.assertEquals(
			"JDBC Default Password", PropertiesValues.JDBC_DEFAULT_PASSWORD);
		Assert.assertEquals(
			"JDBC Default URL", PropertiesValues.JDBC_DEFAULT_URL);
		Assert.assertEquals(
			"JDBC Default Username", PropertiesValues.JDBC_DEFAULT_USERNAME);
		Assert.assertEquals(5, PropertiesValues.LOGIN_ATTEMPT_LIMIT);
		Assert.assertEquals(5, PropertiesValues.NUMBER_OF_SEARCH_RESULTS);
		Assert.assertEquals(30, PropertiesValues.NUMBER_OF_EMAILS_PER_DAY);
		Assert.assertEquals(
			"test@test.com", PropertiesValues.OUTBOUND_EMAIL_ADDRESS);
		Assert.assertEquals(
			"test", PropertiesValues.OUTBOUND_EMAIL_ADDRESS_PASSWORD);
		Assert.assertEquals(
			5, PropertiesValues.MAXIMUM_NUMBER_OF_SEARCH_RESULTS);
		Assert.assertEquals(
			2, PropertiesValues.MAXIMUM_NUMBER_OF_SEARCH_QUERIES);
		Assert.assertEquals(2, PropertiesValues.MAXIMUM_NUMBER_OF_USERS);
		Assert.assertEquals(
			"Recaptcha Site Key", PropertiesValues.RECAPTCHA_SITE_KEY);
		Assert.assertEquals(
			"Recaptcha Secret Key", PropertiesValues.RECAPTCHA_SECRET_KEY);
		Assert.assertEquals(
			"http://www.test.com", PropertiesValues.ROOT_DOMAIN_NAME);
		Assert.assertEquals(
			"SendGrid API Key", PropertiesValues.SENDGRID_API_KEY);
		Assert.assertEquals(
			"Stripe Publishable Key", PropertiesValues.STRIPE_PUBLISHABLE_KEY);
		Assert.assertEquals(
			"Stripe Secret Key", PropertiesValues.STRIPE_SECRET_KEY);
		Assert.assertEquals(
			"Stripe Signing Secret", PropertiesValues.STRIPE_SIGNING_SECRET);
		Assert.assertEquals(
			"Stripe Subscription Plan ID",
			PropertiesValues.STRIPE_SUBSCRIPTION_PLAN_ID);
		Assert.assertEquals(
			"Currency Converter API Key",
			PropertiesValues.CURRENCY_CONVERTER_API_KEY);
	}

	@Test
	public void testSetConfigurationProperties() throws IOException {
		Properties properties = new Properties();

		properties.setProperty("application.id", "Updated Application ID");
		properties.setProperty(
			"jdbc.default.password", "Updated JDBC Default Password");
		properties.setProperty(
			"jdbc.default.url", "Updated JDBC Default URL");
		properties.setProperty(
			"jdbc.default.username",
			"Updated JDBC Default Username");

		PropertiesUtil.setConfigurationProperties(properties);

		properties = PropertiesUtil.getConfigurationProperties();

		Assert.assertEquals(
			"Updated Application ID", properties.getProperty("application.id"));
		Assert.assertEquals(
			"Updated JDBC Default Password",
			properties.getProperty("jdbc.default.password"));
		Assert.assertEquals(
			"Updated JDBC Default URL",
			properties.getProperty("jdbc.default.url"));
		Assert.assertEquals(
			"Updated JDBC Default Username",
			properties.getProperty("jdbc.default.username"));
	}

}