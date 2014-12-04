package com.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static final String APPLICATION_ID = "application.id";

	public static Properties getConfigurationProperties() {
		return _properties;
	}

	public static void loadConfigurationProperties() throws IOException {
		String propertiesFilePath =
			System.getProperty("catalina.base") + "/" + "config.properties";

		loadConfigurationProperties(propertiesFilePath);
	}

	public static void loadConfigurationProperties(String propertiesFilePath) throws IOException {
		_log.debug("Reading properties from {}", propertiesFilePath);

		Properties properties = new Properties();

		try {
			InputStream inputStream = new FileInputStream(propertiesFilePath);

			if (inputStream == null) {
				throw new FileNotFoundException();
			}

			properties.load(inputStream);
		}
		catch (IOException ioe) {
			_log.error(
				"Cannot find or load properties file: {}", propertiesFilePath);

			throw new IOException();
		}

		_properties = properties;
	}

	public static void setConfigurationProperties(Properties properties) {
		_properties = properties;
	}

	private static Properties _properties;

	private static Logger _log = LoggerFactory.getLogger(PropertiesUtil.class);
}