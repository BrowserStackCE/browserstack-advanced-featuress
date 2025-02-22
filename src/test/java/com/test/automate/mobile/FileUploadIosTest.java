package com.test.automate.mobile;

import com.test.base.WebBaseTest;
import com.utils.AppUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FileUploadIosTest extends WebBaseTest {

    @Test(description = "Not working")
    public void fileUpload() {
        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        AppUtils.pushFile(sessionId, "src/test/resources/app/data/turtlerock.jpg", "@com.google.chrome.ios/turtlerock.jpg");

        driver.get("https://the-internet.herokuapp.com/upload");
        driver.findElement(By.id("file-upload")).sendKeys("@com.google.chrome.ios/turtlerock.jpg");
        driver.findElement(By.id("file-submit")).click();
        Assert.assertEquals(driver.findElement(By.id("uploaded-files")).getText(), "turtlerock.jpg", "File upload failed");
    }

}
