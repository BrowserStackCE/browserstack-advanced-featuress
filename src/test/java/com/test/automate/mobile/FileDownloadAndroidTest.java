package com.test.automate.mobile;

import com.test.base.WebBaseTest;
import com.utils.AppUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileDownloadAndroidTest extends WebBaseTest {

    @Test
    public void fileDownloadTest() {
        driver.get("https://the-internet.herokuapp.com/download");
        String fileName = driver.findElement(By.cssSelector("div.example > a")).getText();
        System.out.println("File name: " + fileName);
        driver.findElement(By.cssSelector("div.example > a")).click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        byte[] fileBase64 = AppUtils.pullFile(sessionId, "/sdcard/Download/" + fileName);
        try (OutputStream stream = new FileOutputStream("target/" + fileName)) {
            stream.write(fileBase64);
        } catch (IOException e) {
            Assert.fail("Unable to download file", e);
        }
    }

}
