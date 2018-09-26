package com.hj.selenium.framework.rules;

import com.hj.selenium.framework.annotationImpl.SeleniumConfigImpl;
import com.hj.selenium.framework.annotationImpl.AnnotationBeanPostProcessor;
import com.hj.selenium.framework.annotationImpl.TargetURLImpl;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by David.Zheng on 28/04/2014.
 */
@Component
public class SeleniumConfigRule implements MethodRule
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumConfigRule.class);

	@Autowired
	private SeleniumConfigImpl seleniumConfigImpl;

	@Autowired
	private TargetURLImpl targetURLImpl;

	public final Statement apply(final Statement base,
	  final FrameworkMethod method, final Object target)
	{

		return new Statement()
		{
			@Override
			public void evaluate() throws Throwable
			{
				seleniumConfigImpl.processAnnotationByMethod(AnnotationBeanPostProcessor.getConfig(), method);
				targetURLImpl.processAnnotationByMethod(target, method);

				base.evaluate();
			}
		};
	}
}
