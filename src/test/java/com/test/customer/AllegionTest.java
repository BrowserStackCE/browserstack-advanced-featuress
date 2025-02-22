package com.test.customer;

import com.browserstack.BrowserStackSdk;
import com.browserstack.PercySDK;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static org.openqa.selenium.Keys.TAB;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.assertEquals;

public class AllegionTest {

    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
    }

    @Test
    public void bStackDemoLogin2() {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(90))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NotFoundException.class, StaleElementReferenceException.class);
        driver.get("https://ip-api.com/");
        By ipLocator = By.cssSelector("#codeOutput span:nth-child(2)");
        wait.until(d -> !d.findElement(ipLocator).getText().equals("query"));
        String ip = driver.findElement(ipLocator).getText();
        System.out.println("IP is " + ip);
//        Assert.assertFalse(ip.contains("74.50.105"), "Whitelisting still exists");
//        Assert.assertEquals(ip, "\"74.50.105.78\"", "Whitelisting not working");
        for (int i = 0; i < 20; i++) {
            driver.get("https://overtur-qa.allegion.com");
            wait.until(elementToBeClickable(By.id("welcome")));
            driver.manage().deleteAllCookies();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void closeDriver() {
        driver.quit();
    }

}
