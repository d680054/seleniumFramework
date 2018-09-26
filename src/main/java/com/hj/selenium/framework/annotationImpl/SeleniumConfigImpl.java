package com.hj.selenium.framework.annotationImpl;

import com.hj.selenium.framework.util.PropertyUtil;
import com.hj.selenium.framework.annotation.SeleniumConfig;
import com.hj.selenium.framework.Config;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * Created by David.Zheng on 28/04/2014.
 */
@Service
public class SeleniumConfigImpl
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumConfigImpl.class);

	private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

	public void processAnnotationByMethod(Config config, FrameworkMethod method)
	{
		SeleniumConfig seleniumConfig = method.getAnnotation(SeleniumConfig.class);
		process(config, seleniumConfig);
	}

	public void processAnnotationByType(Config config, Class clazz)
	{
		SeleniumConfig seleniumConfig = (SeleniumConfig) clazz.getAnnotation(SeleniumConfig.class);
		process(config, seleniumConfig);
	}

	private void process(Config config, SeleniumConfig seleniumConfig)
	{
		if (seleniumConfig != null)
		{
			try
			{
				String file = seleniumConfig.value();
				Field[] fields = config.getClass().getDeclaredFields();
				for (Field field : fields)
				{
					String value = PropertyUtil.getProperty(file, field.getName());
					if (value != null)
					{
						ReflectionUtils.makeAccessible(field);
						Object propertyValue = typeConverter.convertIfNecessary(value, field.getType());
						field.set(config, propertyValue);
					}
				}
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage());
				throw new BeanCreationException(e.getMessage(), e.getCause());
			}
		}
	}
}
