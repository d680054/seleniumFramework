/**
 * 
 */
package com.hj.selenium.framework.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author David
 * 
 */
public class WaitUtil
{
	private static final Logger logger = LoggerFactory.getLogger(WaitUtil.class);
	
	private static long DEFAULT_WAIT_PAGE = 30;
	
	/**
	 * Wait for the element to be present in the DOM, and displayed on the page.
	 * And returns the first WebElement using the given method.
	 * 
	 * @param WebDriver The driver object to be used
	 * @param By selector to find the element
	 * @param int The time in seconds to wait until returning a failure
	 * 
	 * @return WebElement the first WebElement using the given method, or null (if the timeout is reached)
	 */
	public static WebElement waitForElement(WebDriver driver, final By by, int timeOutInSeconds)
	{
		WebElement element;
		try
		{
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_PAGE, TimeUnit.SECONDS);
			
			return element;
		}
		catch (Exception e)
		{
			logger.debug("waitForElement got exception:" + e.getMessage());
			
		}
		return null;
	}
	
}
