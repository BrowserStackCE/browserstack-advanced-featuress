package com.test.automate.mobile;

import com.test.base.WebBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FileDownloadIosTest extends WebBaseTest {

    @Test(description = "Not working")
    public void fileDownloadTest() {
        driver.get("https://the-internet.herokuapp.com/download");
        String fileName = driver.findElement(By.cssSelector("div.example > a")).getText();
        System.out.println("File name: " + fileName);
        driver.findElement(By.cssSelector("div.example > a")).click();

        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        List<String> contexts = AppUtils.getAllContexts(sessionId);
        AppUtils.switchContect(sessionId, contexts.get(0));
        driver.findElement(AppiumBy.accessibilityId("Download")).click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) { }

        byte[] fileBase64 = AppUtils.pullFile(sessionId, "./On My iPhone/Downloads/" + fileName);
        try (OutputStream stream = new FileOutputStream("target/" + fileName)) {
            stream.write(fileBase64);
        } catch (IOException e) {
            Assert.fail("Unable to download file", e);
        }
    }

}
