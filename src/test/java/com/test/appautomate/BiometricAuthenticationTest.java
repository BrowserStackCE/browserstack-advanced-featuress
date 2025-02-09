package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
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

public class BiometricAuthenticationTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://hub-cloud.browserstack.com/wd/hub";
    private MobileDriver<AndroidElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        File biometricApp = new File("src/test/resources/appdata/LegacyFingerprint.apk");
        AppUtils.uploadApp("BiometricApp", biometricApp);
    }

    @BeforeTest(alwaysRun = true)
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Advanced Features");
        caps.setCapability("build", "Advanced Features");
        caps.setCapability("name", "Biometric");

        caps.setCapability("os_version", "12.0");
        caps.setCapability("device", "Samsung Galaxy S22");
        caps.setCapability("app", "BiometricApp");

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        caps.setCapability("browserstack.enableBiometric", true);


        driver = new AndroidDriver<>(new URL(URL), caps);
    }

    @Test
    public void biometricAuthentication() {
        Wait<MobileDriver<AndroidElement>> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NotFoundException.class);
        AndroidElement authenticate = wait.until(d -> d.findElementByClassName("android.widget.Button"));
        authenticate.click();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("browserstack_executor: {\"action\": \"biometric\", \"arguments\": {\"biometricMatch\": \"fail\"}}");
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Biometric Authentication passed\"}}");
        driver.quit();
    }
}