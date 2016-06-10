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

package com.app.listener;

import com.app.util.CategoryUtil;
import com.app.util.DatabaseUtil;
import com.app.util.PropertiesUtil;
import com.app.util.PropertiesValues;
import com.app.util.eBayAPIUtil;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

import com.stripe.Stripe;

import java.sql.Driver;
import java.sql.DriverManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jonathan McCann
 */
public class eBayServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		_log.info("Destroying servlet context");

		try {
			AbandonedConnectionCleanupThread.shutdown();

			Driver driver = DriverManager.getDriver(
				PropertiesValues.JDBC_DEFAULT_URL);

			DriverManager.deregisterDriver(driver);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		_log.info("Initializing servlet context");

		try {
			_log.info("Loading configuration properties");

			PropertiesUtil.loadConfigurationProperties();

			_log.info("Loading eBay service client");

			eBayAPIUtil.loadeBayServiceClient();

			_log.info("Initializing eBay API context");

			eBayAPIUtil.loadApiContext();

			_log.info("Loading database properties");

			Class.forName("com.mysql.jdbc.Driver");

			DatabaseUtil.loadDatabaseProperties();

			_log.info("Initializing database");

			DatabaseUtil.initializeDatabase();

			_log.info("Initializing categories");

			CategoryUtil.initializeCategories();

			_log.info("Setting up Stripe");

			Stripe.apiKey = PropertiesValues.STRIPE_SECRET_KEY;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static final Logger _log = LoggerFactory.getLogger(
		eBayServletContextListener.class);

}