package com.hj.selenium.framework.annotationImpl;

import com.hj.selenium.framework.annotation.TargetURL;
import com.hj.selenium.framework.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * Created by David.Zheng on 28/04/2014.
 */
@Service
public class TargetURLImpl
{
	private static final Logger logger = LoggerFactory.getLogger(TargetURLImpl.class);

	public void processAnnotationByMethod(Object target, FrameworkMethod method)
	{
		TargetURL targetURL = (TargetURL) method.getAnnotation(TargetURL.class);
		process(target, targetURL);
	}

	public void processAnnotationByType(Object target)
	{
		TargetURL targetURL = (TargetURL) target.getClass().getAnnotation(TargetURL.class);
		process(target, targetURL);
	}

	private void process(Object target, TargetURL targetURL)
	{
		if (targetURL != null)
		{
			try
			{
				Class[] cArg = new Class[1];
				cArg[0] = String.class;
				Method method = target.getClass().getMethod("setWebsite", cArg);
				String url = targetURL.value();
				if (StringUtils.isBlank(url))
				{
					String source = targetURL.source();
					String key = targetURL.key();
					url = PropertyUtil.getProperty(source, key);
				}
				method.invoke(target, new String[] { url });
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage());
				throw new BeanCreationException(e.getMessage(), e.getCause());
			}
		}
	}
}
