package com.test.automate.mobile;

import com.test.base.WebBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextSwitchingTest extends WebBaseTest {

    @Test
    public void contextSwitching() {
        driver.get("https://bstackdemo.com");
        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        List<String> contexts = AppUtils.getAllContexts(sessionId);
        System.out.println("### Contexts: " + contexts);
        AppUtils.switchContect(sessionId, contexts.get(0));
        driver.findElement(AppiumBy.accessibilityId("ShareButton")).click();
        driver.findElement(AppiumBy.accessibilityId("Close")).click();
        AppUtils.switchContect(sessionId, contexts.get(1));
        driver.findElement(By.id("signin")).click();
    }

}
