package com.hj.selenium.framework.rules;

import com.hj.selenium.framework.annotation.RunCount;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.stereotype.Component;

/**
 * Created by David.Zheng on 16/04/2014.
 */
@Component
public class RepeatRule implements MethodRule
{

	public final Statement apply(final Statement base,
	  final FrameworkMethod method, Object target)
	{

		return new Statement()
		{
			@Override
			public void evaluate() throws Throwable
			{
				int count = 1;//Default
				RunCount rCount = method.getAnnotation(RunCount.class);
				if (rCount != null)
					count = rCount.value();


				while (count-- > 0)
				{
					base.evaluate();
				}
			}
		};
	}
}

