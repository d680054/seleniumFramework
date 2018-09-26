package com.hj.selenium.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by David.Zheng on 15/04/2014.
 *
 * Example:
 *
 * read properties file
 * @SeleniumConfig("selenium.properties")
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface SeleniumConfig
{
	String value() default "";
}
