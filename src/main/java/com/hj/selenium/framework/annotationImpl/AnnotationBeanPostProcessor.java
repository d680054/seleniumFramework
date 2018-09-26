package com.hj.selenium.framework.annotationImpl;

import com.hj.selenium.framework.Config;
import com.hj.selenium.framework.annotation.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David.Zheng on 17/04/2014.
 */
@Component
public class AnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
{
	private static final Logger logger = LoggerFactory.getLogger(AnnotationBeanPostProcessor.class);

	private static ThreadLocal<Config> thread = new ThreadLocal<Config>();

	@Autowired
	private SeleniumConfigImpl seleniumConfigImpl;

	@Autowired
	private TargetURLImpl targetURLImpl;

	@Override
	public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException
	{
		parseSeleniumConfig(bean);

		targetURLImpl.processAnnotationByType(bean);

		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback()

		{
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
			{
				CSVReader csvReader = field.getAnnotation(CSVReader.class);
				if (csvReader != null)
				{
					ReflectionUtils.makeAccessible(field);
					String source = csvReader.source();
					int start = csvReader.start();
					int end = csvReader.end();

					List<List<String>> rows = readCSVFile(source, start, end);
					field.set(bean, rows);
					logger.debug("loaded data to field");
				}
			}
		}

		);

		return super.postProcessAfterInitialization(bean, beanName);
	}

	private void parseSeleniumConfig(Object bean)
	{
		Class clazz = bean.getClass();
		if (bean instanceof Config)
		{
			thread.set((Config) bean);
		}

		seleniumConfigImpl.processAnnotationByType(thread.get(), clazz);
	}

	private List<List<String>> readCSVFile(String csvFile, int start, int end)
	{
		logger.debug("read data from external file:" + csvFile + " Starting from:" + start + " Ending to:" + end);
		List<List<String>> rowList = new ArrayList<List<String>>();
		try
		{
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(csvFile);
			Assert.notNull(input, "load " + csvFile + " failed.");
			au.com.bytecode.opencsv.CSVReader reader = new au.com.bytecode.opencsv.CSVReader(new InputStreamReader(input));
			String[] nextLine;
			for (int loop = 1; (nextLine = reader.readNext()) != null && loop <= end; loop++)
			{
				if (loop >= start)
				{
					List<String> row = new ArrayList<String>();
					for (int i = 0; i < nextLine.length; )
					{
						row.add(nextLine[i++]);
					}
					rowList.add(row);
				}

			}
		}
		catch (IOException e)
		{
			logger.debug(e.getMessage());
			throw new BeanCreationException(e.getMessage(), e.getCause());
		}

		return rowList;
	}

	public static Config getConfig()
	{
		return thread.get();
	}

}
