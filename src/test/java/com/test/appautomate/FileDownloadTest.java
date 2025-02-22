package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.Keys.TAB;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class FileDownloadTest extends AndroidBaseTest {

    @Test
    public void searchWikipedia() {
        driver.activateApp("com.android.chrome");
        driver.rotate(ScreenOrientation.PORTRAIT);
        wait.until(d -> d.getContextHandles().contains("WEBVIEW_chrome"));
        driver.context("WEBVIEW_chrome");

        driver.get("https://the-internet.herokuapp.com/download");
        String fileName = driver.findElement(By.cssSelector("div.example > a")).getText();
        driver.findElement(By.cssSelector("div.example > a")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) { }

        byte[] fileBase64 = driver.pullFile("/sdcard/Download/" + fileName);
        try (OutputStream stream = new FileOutputStream("target/" + fileName)) {
            stream.write(fileBase64);
        } catch (IOException e) {
            Assert.fail("Unable to download file", e);
        }
    }

}
