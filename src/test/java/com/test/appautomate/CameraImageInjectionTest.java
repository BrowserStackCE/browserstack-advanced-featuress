package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class CameraImageInjectionTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    private MobileDriver<MobileElement> driver;
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

    @BeforeTest(alwaysRun = true)
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Advanced Features");
        caps.setCapability("build", "Advanced Features");
        caps.setCapability("name", "Camera Injection - iPhone 11");

        caps.setCapability("device", "iPhone 11");
        caps.setCapability("os_version", "14");
        caps.setCapability("app", "CameraInjectionApp");

        caps.setCapability("autoAcceptAlerts", true);

        caps.setCapability("browserstack.enableCameraImageInjection", true);
//        caps.setCapability("browserstack.uploadMedia", new String[]{mediaUrl});

        driver = new IOSDriver<>(new URL(URL), caps);
    }

    @Test
    public void cameraInjection() {
        Wait<MobileDriver<MobileElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NotFoundException.class);
        MobileElement takePhoto = wait.until(d -> d.findElementByName("Take Photo"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("browserstack_executor: {\"action\": \"cameraImageInjection\", \"arguments\": {\"imageUrl\": \"" + mediaUrl + "\"}}");
        takePhoto.click();
        MobileElement capturePhoto = wait.until(d -> d.findElementByName("capture photo"));
        capturePhoto.click();
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Camera Injection passed\"}}");
        driver.quit();
    }
}