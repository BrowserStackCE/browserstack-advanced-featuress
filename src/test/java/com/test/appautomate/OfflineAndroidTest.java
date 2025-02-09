package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class OfflineAndroidTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://hub-cloud.browserstack.com/wd/hub";
    private AndroidDriver<AndroidElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        AppUtils.uploadApp("AndroidDemoApp", "android/WikipediaSample.apk");
    }

    @BeforeTest(alwaysRun = true)
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Advanced Features");
        caps.setCapability("build", "Advanced Features");
        caps.setCapability("name", "Offline Android");

        caps.setCapability("os_version", "13.0");
        caps.setCapability("device", "Samsung Galaxy S23");
        caps.setCapability("app", "AndroidDemoApp");

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        driver = new AndroidDriver<>(new URL(URL), caps);
    }

    @Test
    public void testOfflineAndroid() {
        Wait<AndroidDriver<AndroidElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);

        Activity activity = new Activity("com.android.chrome", "com.google.android.apps.chrome.Main");
        activity.setStopApp(false);
        driver.startActivity(activity);
        wait.until(d -> d.getContextHandles().contains("WEBVIEW_chrome"));
        driver.context("WEBVIEW_chrome");

        String sessionId = driver.getSessionId().toString();
        driver.get("https://bstackdemo.com");

        AppUtils.setNetworkCondition(sessionId, "no-network");
        try {
            driver.get("https://bstackdemo.com");
        } catch (WebDriverException e) {
            System.out.println("Unable to open BStackDemo website");
        }

        AppUtils.setNetworkCondition(sessionId, "reset");
        driver.get("https://bstackdemo.com");
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driver.quit();
    }

}
