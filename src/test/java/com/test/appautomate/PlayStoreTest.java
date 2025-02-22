package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Duration;

public class PlayStoreTest extends AndroidBaseTest {

    @Test
    public void searchWikipedia() {
        String application = "WhatsApp";

        driver.activateApp("com.android.vending");
        driver.rotate(ScreenOrientation.PORTRAIT);
        wait.until(d -> d.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Search apps & games']"))).click();
        driver.findElement(AppiumBy.className("android.widget.EditText")).sendKeys(application);
        wait.until(d -> d.findElement(AppiumBy.xpath("//android.widget.TextView[@text='" + application.toLowerCase() + "']"))).click();
        wait.until(d -> d.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Install']"))).click();
        Wait<AndroidDriver> installWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofMinutes(2))
                .pollingEvery(Duration.ofSeconds(10))
                .ignoring(NotFoundException.class);
        installWait.until(d -> d.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Open']"))).click();
    }

}
