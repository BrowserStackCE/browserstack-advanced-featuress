package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
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
import java.util.Map;

public class OfflineIosTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String HUB_URL = "https://hub.browserstack.com/wd/hub";
    private IOSDriver<IOSElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        AppUtils.uploadApp("iOSDemoApp", "ios/BStackSampleApp.ipa");
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method m) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Advanced Features");
        caps.setCapability("build", "Advanced Features");
        caps.setCapability("name", "Offline iOS");

        caps.setCapability("device", "iPhone 15");
        caps.setCapability("os_version", "17");
        caps.setCapability("app", "iOSDemoApp");
        caps.setCapability("fullContextList", true);

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        driver = new IOSDriver<>(new URL(HUB_URL), caps);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOfflineIos() {
        Wait<IOSDriver<IOSElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);

        driver.activateApp("com.google.chrome.ios");
        wait.until(d -> d.getContextHandles().size() > 1);
        for (Object context : driver.getContextHandles()) {
            Map<String, String> contextMap = (Map<String, String>) context;
            if (contextMap.getOrDefault("url", "").equals("about://newtab/")) {
                driver.context(contextMap.get("id"));
            }
        }

        String sessionId = driver.getSessionId().toString();
        driver.get("https://bstackdemo.com");

        AppUtils.setNetworkCondition(sessionId, "no-network");
        driver.get("https://bstackdemo.com");

        AppUtils.setNetworkCondition(sessionId, "reset");
        driver.get("https://bstackdemo.com");

    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driver.quit();
    }

}
