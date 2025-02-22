package com.test.automate.desktop;

import com.test.base.WebBaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Base64;

public class FileDownloadTest extends WebBaseTest {

    @Test
    public void fileDownloadTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        driver.get("https://the-internet.herokuapp.com/download");
        String fileName = driver.findElement(By.cssSelector("div.example > a")).getText();
        System.out.println("File name: " + fileName);
        driver.findElement(By.cssSelector("div.example > a")).click();

        String fileExistsScript = "browserstack_executor: {\"action\": \"fileExists\", \"arguments\": {\"fileName\": \""+ fileName + "\"}}";
        String filePropertiesScript = "browserstack_executor: {\"action\": \"getFileProperties\", \"arguments\": {\"fileName\": \"" + fileName + "\"}}";
        String fileContentScript = "browserstack_executor: {\"action\": \"getFileContent\", \"arguments\": {\"fileName\": \"" + fileName + "\"}}";

        wait.until(d -> ((JavascriptExecutor) d).executeScript(fileExistsScript));
        System.out.println("File exists: " + jse.executeScript(fileExistsScript));
        FileProperties properties = new FileProperties(jse.executeScript(filePropertiesScript));
        System.out.println(properties);
        String base64EncodedFile = (String) jse.executeScript(fileContentScript);
        byte[] data = Base64.getDecoder().decode(base64EncodedFile);
        try (OutputStream stream = new FileOutputStream("target/" + fileName)) {
            stream.write(data);
        } catch (IOException e) {
            throw new RuntimeException("Unable to download the file " + fileName, e);
        }
    }

//    @Test
    public void fileDownloadTest1() {
        String fileExistsScript = "browserstack_executor: {\"action\": \"fileExists\", \"arguments\": {\"fileName\": \"BrowserStack - List of devices to test on.csv\"}}";
        String filePropertiesScript = "browserstack_executor: {\"action\": \"getFileProperties\", \"arguments\": {\"fileName\": \"BrowserStack - List of devices to test on.csv\"}}";
        String fileContentScript = "browserstack_executor: {\"action\": \"getFileContent\", \"arguments\": {\"fileName\": \"BrowserStack - List of devices to test on.csv\"}}";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get("https://www.browserstack.com/test-on-the-right-mobile-devices");
        driver.findElement(By.id("accept-cookie-notification")).click();
        driver.findElement(By.className("icon-csv")).click();
        wait.until(d -> ((JavascriptExecutor) d).executeScript(fileExistsScript));
        System.out.println("File exists: " + jse.executeScript(fileExistsScript));
        FileProperties properties = new FileProperties(jse.executeScript(filePropertiesScript));
        System.out.println(properties);
        String base64EncodedFile = (String) jse.executeScript(fileContentScript);
        byte[] data = Base64.getDecoder().decode(base64EncodedFile);
        try (OutputStream stream = new FileOutputStream("target/List of devices.csv")) {
            stream.write(data);
        } catch (IOException e) {
            throw new RuntimeException("Unable to download the file.", e);
        }
    }

}
