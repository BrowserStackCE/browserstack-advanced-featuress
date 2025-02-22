package com.test.appautomate;

import com.test.base.IosBaseTest;
import com.utils.AppUtils;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.Map;

public class OfflineIosTest extends IosBaseTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testOfflineIos() {
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
        Assert.assertEquals(driver.getTitle(), "StackDemo", "Unable to open website");

        AppUtils.setNetworkCondition(sessionId, "no-network");
        try {
            driver.get("https://bstackdemo.com");
            Assert.fail("Website loaded");
        } catch (WebDriverException e) {
            Assert.assertEquals(driver.getTitle(), "bstackdemo.com", "Offline scenario not working 1");
        } catch (NullPointerException e) {
            Assert.assertEquals(driver.getTitle(), "bstackdemo.com", "Offline scenario not working 2");
        }

        AppUtils.setNetworkCondition(sessionId, "reset");
        driver.get("https://bstackdemo.com");
        Assert.assertEquals(driver.getTitle(), "StackDemo", "Unable to open website");
    }

}
