# Introduction

The test automation framework currently uses Selenium to create automated web browsing tests.
For more background on Selenium, please check out http://seleniumhq.org/.

# Project Clone：
```
Source: git clone https://davidzheng1022@bitbucket.org/davidzheng1022/automation_framework.git
Wiki: https://bitbucket.org/davidzheng1022/automation_framework/wiki
```

# Features

* Automatically takes screenshot
* Automatically close browser
* Multiple running
------------------------------------
* Spring + JUnit4 based project
* Supports local or Grid Running
* Annotation used, decouple the code
* PageFactory introduced

# Quick Start
1. Download the code and run 'mvn install'

2. Create a maven project

3. Add the dependency to your project pom file
```
    <dependency>
        <groupId>com.hj.selenium.framework</groupId>
        <artifactId>selenium-framework</artifactId>
        <version>1.0.0</version>
    </dependency>
```

4.Create a 'Page' that extends PageContext

```
#!java

public class PacteraPage extends PageContext
{
	@FindBy(id = "s")
	private WebElement input;

	@FindBy(id = "searchsubmit")
	private WebElement button;

	/**
	 * type the text and search
	 *
	 * @param text
	 */
	public void seach(String text)
	{
		input.sendKeys(text);
		button.click();
	}

```


5.Create your test class that extends AbstractSelenium

```
#!java

//@SeleniumConfig("selenium.properties")
@TargetURL("http://www.pactera.com")
public class PacteraPageTest extends AbstractSelenium
{
	@Page
	private PacteraPage pacteraPage;

	@Test
	public void testSearch() {

		pacteraPage.seach("david");
		String result = pacteraPage.getFirstSearchResult();
		Assert.isTrue(StringUtils.containsIgnoreCase(result, "david1"), "The result:'"+ result+ "' does not contain any 'david' key words.");
	}
}
```

# Advanced  
You can put **@SeleniumConfig("selenium.properties")** on the class or method to customised your test scripts.
More options defined in the selenium.properties

```
#running on 'single' mode or 'grid' mode, default value 'single mode'
gridMode=false

#provide the hub url if running on 'grid' mode
hub=http://localhost:4444/wd/hub

#choose your browser among ie/firefox/chrome
browser=firefox

#set the selenium timeout value(seconds), default is 30s
timeout=40

#enable the proxy, default is 'false'
proxyEnable = true

#set the proxy url
#e.g. http://xxx.com/proxy.pac, http://xxx.com:8080
proxyURL = 

#set the directory to store the screen shots, default folder is under your project.
screenshotsDir = /Users/David/Documents/pics

#default is 'false'
screenRecorderEnable = false

#set the directory to store the recording, default folder is under your project.
screenRecorderDir = /Users/David/Documents/recording
```

#Next Relase
* Recording function
* DB(refactor)
* Email function
* PDF function
* .... 

#Reference
```
Selenium: http://seleniumhq.org/
Junit: http://junit.org/
SpringJunit4: http://docs.spring.io/spring-batch/reference/html/testing.html
```

#About Author
```
Email: davidzheng1022@gmail.com
```
