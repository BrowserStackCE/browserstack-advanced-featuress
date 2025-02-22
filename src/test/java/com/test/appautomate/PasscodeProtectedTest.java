package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertTrue;

public class PasscodeProtectedTest extends AndroidBaseTest {

    @Test
    public void searchWikipedia() {
        driver.findElement(AppiumBy.accessibilityId("Search Wikipedia")).click();
        driver.lockDevice();
        driver.unlockDevice();
        driver.pressKey(new KeyEvent(AndroidKey.DIGIT_1));
        driver.pressKey(new KeyEvent(AndroidKey.DIGIT_2));
        driver.pressKey(new KeyEvent(AndroidKey.DIGIT_3));
        driver.pressKey(new KeyEvent(AndroidKey.DIGIT_4));
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        WebElement insertTextElement = wait.until(d -> d.findElement(AppiumBy.id("org.wikipedia.alpha:id/search_src_text")));
        insertTextElement.sendKeys("BrowserStack");
        wait.until(d -> d.findElement(AppiumBy.className("android.widget.ListView")));
        List<String> companyNames = driver.findElements(AppiumBy.className("android.widget.TextView"))
                .stream().map(WebElement::getText).collect(toList());
        assertTrue(companyNames.contains("BrowserStack"), "Company is present in the list");
    }

}
