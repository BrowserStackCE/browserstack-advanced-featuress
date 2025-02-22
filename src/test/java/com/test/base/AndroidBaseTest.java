package com.test.base;

import com.utils.AppUtils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidBaseTest {

    protected AndroidDriver driver;
    protected Wait<AndroidDriver> wait;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        String appUrl = "https://www.browserstack.com/app-automate/sample-apps/android/WikipediaSample.apk";
        AppUtils.uploadApp("AndroidDemoApp", appUrl);
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NotFoundException.class);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

}
