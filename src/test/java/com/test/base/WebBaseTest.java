package com.test.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

public class WebBaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        driver.quit();
    }

}
