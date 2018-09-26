package com.hj.selenium.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by David.Zheng on 15/04/2014.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CSVReader
{
	String source();

	int start() default 1;

	int end() default 2147483647;
}
