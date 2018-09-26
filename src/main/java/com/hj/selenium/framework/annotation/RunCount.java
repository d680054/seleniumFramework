package com.hj.selenium.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by David.Zheng on 15/04/2014.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RunCount
{
	int value() default 1;
}
