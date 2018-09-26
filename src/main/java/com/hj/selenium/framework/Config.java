package com.hj.selenium.framework;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by David.Zheng on 22/04/2014.
 */
@Component
@Scope("prototype")
public class Config
{
	private boolean gridMode = false;

	private String browser = "firefox";

	private String hub = "";

	private int timeout = 30;

	private boolean proxyEnable = false;

	private boolean acceptSSLCerts = true;

	private String proxyURL = "";

	private String screenshotsDir = "";

	private boolean screenRecorderEnable = false;

	private String screenRecorderDir = "";

	public boolean isGridMode()
	{
		return gridMode;
	}

	public void setGridMode(boolean gridMode)
	{
		this.gridMode = gridMode;
	}

	public String getBrowser()
	{
		return browser;
	}

	public String getHub()
	{
		return hub;
	}

	public int getTimeout()
	{
		return timeout;
	}

	public Boolean isProxyEnable()
	{
		return proxyEnable;
	}

	public boolean isAcceptSSLCerts()
	{
		return acceptSSLCerts;
	}

	public String getProxyURL()
	{
		return proxyURL;
	}

	public String getScreenshotsDir()
	{
		return screenshotsDir;
	}

	public boolean isScreenRecorderEnable()
	{
		return screenRecorderEnable;
	}

	public String getScreenRecorderDir()
	{
		return screenRecorderDir;
	}

	void setBrowser(String browser)
	{
		this.browser = browser;
	}

	void setHub(String hub)
	{
		this.hub = hub;
	}

	void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	void setProxyEnable(boolean proxyEnable)
	{
		this.proxyEnable = proxyEnable;
	}

	void setAcceptSSLCerts(boolean acceptSSLCerts)
	{
		this.acceptSSLCerts = acceptSSLCerts;
	}

	void setProxyURL(String proxyURL)
	{
		this.proxyURL = proxyURL;
	}

	void setScreenshotsDir(String screenshotsDir)
	{
		this.screenshotsDir = screenshotsDir;
	}

	void setScreenRecorderEnable(boolean screenRecorderEnable)
	{
		this.screenRecorderEnable = screenRecorderEnable;
	}

	void setScreenRecorderDir(String screenRecorderDir)
	{
		this.screenRecorderDir = screenRecorderDir;
	}
}
