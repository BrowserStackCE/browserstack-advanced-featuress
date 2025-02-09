package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
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
import java.util.HashMap;

public class PlayStoreTest {

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
        caps.setCapability("name", m.getName());

        caps.setCapability("device", "Google Pixel 6 Pro");
        caps.setCapability("os_version", "12.0");
        caps.setCapability("app", "AndroidDemoApp");

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        caps.setCapability("browserstack.appStoreConfiguration", new HashMap<String, String>() {{
            put("username", "insvsawant@gmail.com");
            put("password", "aezakmi321$");
        }});

        driver = new AndroidDriver<>(new URL(URL), caps);
    }

    @Test
    public void searchWikipedia() {
        String application = "WhatsApp";

        Wait<AndroidDriver<MobileElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);
        driver.findElementByAccessibilityId("Search Wikipedia").click();

        Activity activity = new Activity("com.android.vending", "AssetBrowserActivity");
        activity.setStopApp(false);
        driver.startActivity(activity);

        wait.until(d -> d.findElementByXPath("//android.widget.TextView[@text='Search apps & games']")).click();
        driver.findElementByClassName("android.widget.EditText").sendKeys(application);
        wait.until(d -> d.findElementByXPath("//android.widget.TextView[@text='" + application.toLowerCase() + "']")).click();
        wait.until(d -> d.findElementByXPath("//android.widget.TextView[@text='Install']")).click();
        Wait<AndroidDriver<MobileElement>> installWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMinutes(2))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);
        installWait.until(d -> d.findElementByXPath("//android.widget.TextView[@text='Open']")).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driver.quit();
    }

}
