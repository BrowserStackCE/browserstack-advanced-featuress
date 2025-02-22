package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import com.utils.AppUtils;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class OfflineAndroidTest extends AndroidBaseTest {

    @Test
    public void testOfflineAndroid() {
        driver.activateApp("com.android.chrome");
        driver.rotate(ScreenOrientation.PORTRAIT);
        wait.until(d -> d.getContextHandles().contains("WEBVIEW_chrome"));
        driver.context("WEBVIEW_chrome");

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
