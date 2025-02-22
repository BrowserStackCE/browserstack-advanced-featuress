package com.test.customer;

import com.test.base.IosBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.time.Duration;

public class JLPTest extends IosBaseTest {

    @Test
    public void checkout() throws InterruptedException {
        click("primaryButtonOptional(\"Allow all\")");
        click("Allow");
        for (int i = 0; i < 10; i++) {
            searchFurniture();
        }
    }

    private void click(String accessibilityId) {
        WebElement ele = wait.until(d -> d.findElement(AppiumBy.accessibilityId(accessibilityId)));
        ele.click();
    }

    private void sendKeys(String accessibilityId, String text) {
        WebElement ele = wait.until(d -> d.findElement(AppiumBy.accessibilityId(accessibilityId)));
        ele.sendKeys(text + Keys.ENTER);
    }

    private void searchFurniture() throws InterruptedException {
        click("Search product or brand");
        Thread.sleep(1000);
        sendKeys("Search product or brand", "office furniture");
        click("John Lewis ANYDAY");
        click("productDetailUI.AddToBasket.Button");
        click("OK");
        click("Home");
    }

}