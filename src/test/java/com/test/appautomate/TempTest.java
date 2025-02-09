package com.test.appautomate;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

public class TempTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @Test
    public void fileUpload() throws MalformedURLException, InterruptedException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("device", "iPhone 12 Pro Max");
        caps.setCapability("os_version", "14");
        caps.setCapability("real_mobile", "true");
        caps.setCapability("project", "My First Project");
        caps.setCapability("build", "My First Build");
        caps.setCapability("name", "Bstack-[Java] Sample Test");
        caps.setCapability("nativeWebTap", "true");
        caps.setCapability("browserstack.uploadMedia", new String[]{"media://4601a241be90a5475593be4013929c20a69f0a55"});

        IOSDriver<IOSElement> driver = new IOSDriver<>(new URL(URL), caps);
        driver.get("https://the-internet.herokuapp.com/upload");
        Thread.sleep(5000);
        driver.findElement(By.id("file-upload")).click();
        driver.context("NATIVE_APP");
        driver.findElement(By.name("Photo Library")).click();
        Thread.sleep(5000);
        List<IOSElement> list = driver.findElements(By.className("XCUIElementTypeImage"));
        list.get(0).click();
        Thread.sleep(5000);
        driver.findElement(By.name("Choose")).click();
        Set<String> contextName = driver.getContextHandles();
        driver.context(contextName.toArray()[1].toString());
        driver.findElement(By.id("file-submit")).click();
        driver.quit();
    }

}
