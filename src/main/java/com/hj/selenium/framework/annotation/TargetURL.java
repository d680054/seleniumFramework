package com.hj.selenium.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by David.Zheng on 15/04/2014.
 *
 * Example:
 * 1. use external files:
 * @TargetURL(
 *
 * 2. use links:
 * @TargetURL("http://www.google.com.au")
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface TargetURL
{
	String source() default "";

	String key() default "";

	String value() default "";

}
