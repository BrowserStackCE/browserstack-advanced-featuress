package com.test.appautomate;

import com.test.base.IosBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;

public class CameraImageInjectionTest extends IosBaseTest {

    private String mediaUrl;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        File ImageInjectionApp = new File("src/test/resources/appdata/PhotoCapture.ipa");
        AppUtils.uploadApp("CameraInjectionApp", ImageInjectionApp);
        File mediaFile = new File("src/test/resources/appdata/turtlerock.jpg");
        mediaUrl = AppUtils.uploadMedia(mediaFile);
        System.out.println("Uploaded image...");
        System.out.println("Media URL: " + mediaUrl);
    }

    @Test
    public void cameraInjection() {
        WebElement takePhoto = wait.until(d -> d.findElement(AppiumBy.name("Take Photo")));
        driver.executeScript("browserstack_executor: {\"action\": \"cameraImageInjection\", \"arguments\": {\"imageUrl\": \"" + mediaUrl + "\"}}");
        takePhoto.click();
        WebElement capturePhoto = wait.until(d -> d.findElement(AppiumBy.name("capture photo")));
        capturePhoto.click();
    }

}