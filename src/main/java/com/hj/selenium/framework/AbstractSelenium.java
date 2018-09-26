package com.hj.selenium.framework;

import com.hj.selenium.framework.annotation.Page;
import com.hj.selenium.framework.constant.Constants;
import com.hj.selenium.framework.rules.RepeatRule;
import com.hj.selenium.framework.rules.ScreenshotRule;
import com.hj.selenium.framework.rules.SeleniumConfigRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Created by david on 14/04/2014.
 * <p/>
 * The derived class should implement its own configure file
 *
 * @ContextConfiguration(locations = { "/testContext.xml" })
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Selenium.xml" })
public abstract class AbstractSelenium
{
	private static final Logger logger = LoggerFactory.getLogger(AbstractSelenium.class);

	private static ThreadLocal<WebDriver> thread = new ThreadLocal<WebDriver>();

	public final static String profile = System.getProperty(Constants.PROFILE);

	@Value("classpath:geckodriver")
	private Resource res;

	@Rule
	@Autowired
	public RepeatRule repeatRule;

	@Rule
	@Autowired
	public TestRule screenShotRule;

	@Rule
	@Autowired
	public SeleniumConfigRule seleniumConfigRule;

	@Autowired
	protected Config config;

	private String targetURL;

	@Before
	public void config() throws Exception
	{
		System.setProperty("webdriver.gecko.driver", res.getURI().getPath());
		WebDriver driver = initDriver();
		thread.set(driver);
		setTimeout(config.getTimeout());
		((ScreenshotRule) screenShotRule).setWebDriver(getDriver());
		initPageInstance(getDriver());
		getDriver().get(targetURL);
		getDriver().manage().window().maximize();
	}

	/**
	 *
	 * @param driver
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 */
	private void initPageInstance(final WebDriver driver) throws IllegalAccessException, NoSuchFieldException
	{
		final Object o = this;

		ReflectionUtils.doWithFields(this.getClass(), new ReflectionUtils.FieldCallback()
		{
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException
			{
				Page page = field.getAnnotation(Page.class);
				if (page != null)
				{
					Class clazz = field.getType();
					PageContext p = (PageContext) PageFactory.initElements(driver, clazz);
					p.setDriver(driver);
					p.setWait(new WebDriverWait(driver, config.getTimeout()));
					field.setAccessible(true);
					field.set(o, p);
					logger.debug("initialize the page instance");
				}
			}
		});
	}

	/**
	 * Sets the selenium timeout, the time unit is second
	 *
	 * @param seconds
	 */

	protected void setTimeout(int seconds)
	{
		getDriver().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
		getDriver().manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
	}


	/**
	 * config the capabilities for specified browser type.
	 *
	 * @param browser
	 * @return
	 */
	private DesiredCapabilities configBrowserCapabilities(String browser)
	{
		DesiredCapabilities desiredBrowser;

		if (browser.toLowerCase().startsWith(Constants.CHROME))
		{
			desiredBrowser = DesiredCapabilities.chrome();
		}
		else if (browser.toLowerCase().startsWith(Constants.IE))
		{
			desiredBrowser = DesiredCapabilities.internetExplorer();
		}
		else
		{
			desiredBrowser = DesiredCapabilities.firefox();
			//desiredBrowser.setCapability(CapabilityType.ACCEPT_SSL_CERTS, config.isAcceptSSLCerts());
		}

		buildProxy(desiredBrowser);

		return desiredBrowser;
	}

	/**
	 * @param browser
	 * @param capabilities
	 * @return
	 */
	private WebDriver getWebDriver(String browser, DesiredCapabilities capabilities)
	{
		WebDriver driver;

		if (browser.toLowerCase().startsWith(Constants.CHROME))
		{
			driver = new ChromeDriver(capabilities);
		}
		else if (browser.toLowerCase().startsWith(Constants.IE))
		{
			driver = new InternetExplorerDriver(capabilities);
		}
		else
		{
			driver = new FirefoxDriver(capabilities);
		}

		return driver;
	}


	private WebDriver initDriver() throws MalformedURLException
	{
		WebDriver driver;
		DesiredCapabilities capabilities = configBrowserCapabilities(config.getBrowser());

		if (config.isGridMode())
		{
			driver = new RemoteWebDriver(new URL(config.getHub()), capabilities);
		}
		else
		{
			driver = getWebDriver(config.getBrowser(), capabilities);
		}

		return driver;
	}

	/**
	 * build the proxy according to the configuration file
	 *
	 * @return
	 */
	private void buildProxy(DesiredCapabilities desiredBrowser)
	{

		if (config.isProxyEnable())
		{
			Proxy proxy = new Proxy();

			if (config.getProxyURL().endsWith(".pac"))
			{
				proxy.setProxyAutoconfigUrl(config.getProxyURL());
			}
			else
			{
				proxy.setHttpProxy(config.getProxyURL()).setSslProxy(config.getProxyURL());
			}

			desiredBrowser.setCapability(CapabilityType.PROXY, proxy);
		}
	}

	/**
	 * Derived class should implement this method to set its own target url.
	 */
	public void setWebsite(String targetURL)
	{
		if (!targetURL.startsWith("http"))
		{
			targetURL = "http://" + targetURL;
		}
		this.targetURL = targetURL;
	}

	protected String getTargetURL()
	{
		return targetURL;
	}

	/**
	 * Return the webdriver
	 *
	 * @return
	 */
	private WebDriver getDriver()
	{
		return thread.get();
	}

	/**
	 * Return the Selenium Config instance.
	 *
	 * @return
	 */
	private Config getSeleniumConfig()
	{
		return config;
	}

}
