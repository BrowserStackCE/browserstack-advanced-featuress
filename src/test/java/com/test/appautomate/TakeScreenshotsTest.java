package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import com.utils.AppUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertTrue;

public class TakeScreenshotsTest extends AndroidBaseTest {

    @Test
    public void searchWikipedia() throws IOException {
        driver.findElement(AppiumBy.accessibilityId("Search Wikipedia")).click();
        WebElement insertTextElement = wait.until(d -> d.findElement(AppiumBy.id("org.wikipedia.alpha:id/search_src_text")));
        insertTextElement.sendKeys("BrowserStack");
        wait.until(d -> d.findElement(AppiumBy.className("android.widget.ListView")));
        List<String> companyNames = driver.findElements(AppiumBy.className("android.widget.TextView"))
                .stream().map(WebElement::getText).collect(toList());
        File scrFile = driver.getScreenshotAs(OutputType.FILE);
        Files.copy(scrFile.toPath(), Paths.get("target/screenshot.png"), StandardCopyOption.REPLACE_EXISTING);
        assertTrue(companyNames.contains("BrowserStack"), "Company is not present in the list");
    }

}
