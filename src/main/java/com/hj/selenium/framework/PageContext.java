package com.hj.selenium.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

/**
 * Created by David.Zheng on 1/05/2014.
 */
public abstract class PageContext
{
	private WebDriver driver;

	private Wait wait;

	public void navigateToPage(String url)
	{
		driver.get(url);
	}

	protected WebDriver getDriver()
	{
		return driver;
	}

	void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}

	void setWait(Wait wait)
	{
		this.wait = wait;
	}

	protected WebElement cssSelector(String css)
	{
		By by = By.cssSelector(css);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));

		return getDriver().findElement(by);
	}

	protected WebElement xpathSelector(String xpath)
	{
		By by = By.xpath(xpath);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));

		return getDriver().findElement(by);
	}

	protected void scrollIntoView(By by)
	{
		waitForElement(by);
		((JavascriptExecutor) getDriver())
				.executeScript("arguments[0].scrollIntoView(true);", getDriver().findElement(by));
	}

	protected void waitForAjax()
	{
		wait.until(new ExpectedCondition<Boolean>()
		{
			@Override
			public Boolean apply(WebDriver webDriver)
			{
				return (Boolean) ((JavascriptExecutor) driver).executeScript("return (jQuery.active == 0)");
			}
		});

	}

	protected void waitForElement(By by)
	{
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	protected void hardWait(int second)
	{
		try
		{
			Thread.sleep(second * 1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public String getTitle()
	{
		return getDriver().getTitle();
	}

}
