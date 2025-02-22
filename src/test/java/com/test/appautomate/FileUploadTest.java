package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import com.utils.AppUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class FileUploadTest extends AndroidBaseTest {

    @Test
    public void searchWikipedia() throws IOException {
        driver.activateApp("com.android.chrome");
        driver.rotate(ScreenOrientation.PORTRAIT);
        wait.until(d -> d.getContextHandles().contains("WEBVIEW_chrome"));
        driver.context("WEBVIEW_chrome");

        File imageFile = new File("src/test/resources/app/data/turtlerock.jpg");
        driver.pushFile("/sdcard/Download/turtlerock.jpg", imageFile);

        driver.get("https://the-internet.herokuapp.com/upload");
        driver.findElement(By.id("file-upload")).sendKeys("/sdcard/Download/turtlerock.jpg");
        driver.findElement(By.id("file-submit")).click();
        Assert.assertEquals(driver.findElement(By.id("uploaded-files")).getText(), "turtlerock.jpg", "File upload failed");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driver.quit();
    }

}
