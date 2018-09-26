package com.hj.selenium.framework.util;

import com.hj.selenium.framework.AbstractSelenium;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by David.Zheng on 29/04/2014.
 */
public class PropertyUtil
{
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static Map<String, Properties> propertyMap = new HashMap();

	public static String getProperty(String file, String key) throws IOException
	{
		if (!StringUtils.isBlank(AbstractSelenium.profile))
		{
			file = AbstractSelenium.profile + "/" + file;
		}
		Properties propertyConfigurer;

		if (propertyMap.get(file) != null)
		{
			logger.debug("loading the [" + file + "] from Cache.");
			propertyConfigurer = propertyMap.get(file);
		}
		else
		{
			propertyConfigurer = PropertiesLoaderUtils.loadProperties(new ClassPathResource(file));
			propertyMap.put(file, propertyConfigurer);
			logger.debug("putting the [" + file + "] into Cache.");
		}

		String value = propertyConfigurer.getProperty(key);
		logger.debug("Property key:value [" + key + ":" + value + "]");

		return value;
	}

	public static String getProperty(String key) throws IOException
	{
		for (Properties property : propertyMap.values())
		{
			if (!StringUtils.isBlank(property.getProperty(key)))
			{
				return property.getProperty(key);
			}
		}

		return "";
	}

	static
	{
		try
		{
			String path = PropertyUtil.class.getClassLoader().getResource("").getPath();
			findFiles(".properties", path + AbstractSelenium.profile, propertyMap);
		}
		catch (Exception e)
		{
			logger.debug("Initializing all the property files");
			e.printStackTrace();
		}

	}

	private static void findFiles(String filenameSuffix, String currentDirUsed, Map<String, Properties> propertyMap) throws IOException
	{
		File dir = new File(currentDirUsed);
		if (!dir.exists() || !dir.isDirectory())
		{
			return;
		}

		for (File file : dir.listFiles())
		{
			if (file.getAbsolutePath().endsWith(filenameSuffix))
			{
				String fileName = "";
				if (!StringUtils.isBlank(AbstractSelenium.profile))
				{
					fileName = AbstractSelenium.profile + "/" + file.getName();
				}
				Properties propertyConfigurer = PropertiesLoaderUtils.loadProperties(new ClassPathResource(fileName));
				propertyMap.put(fileName, propertyConfigurer);
			}
		}

	}
}
