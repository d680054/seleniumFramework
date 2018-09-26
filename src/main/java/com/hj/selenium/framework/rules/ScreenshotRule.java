/**
 *
 */
package com.hj.selenium.framework.rules;

import com.hj.selenium.framework.constant.Constants;
import com.hj.selenium.framework.util.PropertyUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author David
 */
@Component
public class ScreenshotRule implements TestRule
{
	private static final Logger logger = LoggerFactory.getLogger(ScreenshotRule.class);

	private WebDriver driver;

	private String dir;

	public void setWebDriver(WebDriver driver)
	{
		this.driver = driver;
	}

	@Override
	public Statement apply(final Statement base, final Description description)
	{
		return new Statement()
		{
			@Override
			public void evaluate() throws Throwable
			{
				try
				{
					base.evaluate();
				}
				catch (Throwable t)
				{
					onException(t, driver, description.getMethodName());
					logger.debug(t.getMessage());
					throw t;
				}
				finally
				{
					if (driver != null)
					{
						driver.quit();
					}
				}
			}
		};
	}

	/**
	 * @param ex
	 * @param driver
	 * @param methodName
	 */
	public void onException(Throwable ex, WebDriver driver, String methodName)
	{
		String fileName = generateScreenShotFilename(methodName);
		logger.debug("Create the sreenshot file name:" + fileName);

		try
		{
			takeScreenShot(driver, fileName);
			createFailureRef(fileName, ex.getMessage());
		}
		catch (Exception e)
		{
			logger.debug("Exception occurs while taking screenshots:" + e.getMessage());
		}

	}

	/**
	 * @param methodName
	 * @return
	 */
	private String generateScreenShotFilename(String methodName)
	{
		Calendar c = Calendar.getInstance();
		int month = c.get(Calendar.MONTH) + 1;
		String folder =
		  "" + c.get(Calendar.YEAR) + "_" + month + "_" + c.get(Calendar.DATE) + "_"
			+ c.get(Calendar.HOUR_OF_DAY);
		if (dir != null && dir != "" && dir.length() > 0)
		{
			return dir + "/" + folder + "/" + methodName;
		}
		else
		{
			return folder + "/" + methodName;
		}

	}

	/**
	 * @param driver
	 * @return
	 * @throws Exception
	 */
	private void takeScreenShot(WebDriver driver, String fileName) throws Exception
	{
		WebDriver screenshotDriver = driver;

		String value = PropertyUtil.getProperty(Constants.GRID_MODE);
		if (BooleanUtils.toBoolean(value))
		{
			screenshotDriver = new Augmenter().augment(driver);
		}

		File screenshot = ((TakesScreenshot) screenshotDriver).getScreenshotAs(OutputType.FILE);

		FileUtils.copyFile(screenshot, new File(fileName + ".png"));
	}

	/**
	 * @param fileName
	 * @param content
	 * @throws java.io.IOException
	 */
	private void createFailureRef(String fileName, String content) throws IOException
	{
		File f = new File(fileName + ".txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		bw.write(content);
		bw.close();
	}
}
