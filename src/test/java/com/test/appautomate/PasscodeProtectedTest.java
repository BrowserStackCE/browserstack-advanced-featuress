package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertTrue;

public class PasscodeProtectedTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://hub-cloud.browserstack.com/wd/hub";
    private AndroidDriver<MobileElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        String appUrl = "https://www.browserstack.com/app-automate/sample-apps/android/WikipediaSample.apk";
        AppUtils.uploadApp("AndroidDemoApp", appUrl);
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method m) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Advanced Features");
        caps.setCapability("build", "Advanced Features");
        caps.setCapability("name", m.getName() + " - Google Pixel 3");

        caps.setCapability("device", "Samsung Galaxy S22 Ultra");
        caps.setCapability("os_version", "12.0");
        caps.setCapability("app", "AndroidDemoApp");

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        caps.setCapability("browserstack.enablePasscode", true);

        driver = new AndroidDriver<>(new URL(URL), caps);
    }

    @Test
    public void searchWikipedia() {
        driver.lockDevice();
        driver.unlockDevice();
        Wait<AndroidDriver<MobileElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);
        driver.findElementByAccessibilityId("Search Wikipedia").click();
        MobileElement insertTextElement = wait.until(d -> d.findElementById("org.wikipedia.alpha:id/search_src_text"));
        insertTextElement.sendKeys("BrowserStack");
        wait.until(d -> d.findElementByClassName("android.widget.ListView").isDisplayed());
        List<String> companyNames = driver.findElementsByClassName("android.widget.TextView")
                .stream().map(MobileElement::getText).collect(toList());
        driver.hideKeyboard();
        assertTrue(companyNames.contains("BrowserStack"), "Company is not present in the list");
        driver.lockDevice();
        driver.unlockDevice();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driver.quit();
    }

}
