package com.test.appautomate;

import com.utils.AppUtils;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidTouchAction;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;

public class MobileGesturesTest {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "http://hub-cloud.browserstack.com/wd/hub";
    private AndroidDriver<MobileElement> driver;

    @BeforeSuite(alwaysRun = true)
    public void setupApp() {
        String appUrl = "https://www.browserstack.com/app-automate/sample-apps/android/WikipediaSample.apk";
        AppUtils.uploadApp("AndroidDemoApp", appUrl);
    }

    @BeforeMethod(alwaysRun = true)
    public void setup(Method m) throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("project", "BrowserStack Advanced Features");
        caps.setCapability("build", "Advanced Features");
        caps.setCapability("name", m.getName() + " - Google Pixel 6");

        caps.setCapability("device", "Google Pixel 6");
        caps.setCapability("os_version", "12.0");
        caps.setCapability("app", "AndroidDemoApp");

        caps.setCapability("browserstack.user", USERNAME);
        caps.setCapability("browserstack.key", ACCESS_KEY);

        driver = new AndroidDriver<>(new URL(URL), caps);
    }

    @Test
    public void searchWikipedia() {
        TouchAction<AndroidTouchAction> action = new AndroidTouchAction(driver);
        Rectangle rect;
        int x1, x2, y1, y2;

        rect = driver.findElementById("org.wikipedia.alpha:id/view_list_card_list").getRect();
        x1 = (int) (rect.getX() + rect.getWidth() * 0.8);
        x2 = (int) (rect.getX() + rect.getWidth() * 0.2);
        y1 = rect.getY() + rect.getHeight() / 2;
        action.press(point(x1, y1))
                .waitAction(waitOptions(ofMillis(500)))
                .moveTo(point(x2, y1))
                .release()
                .perform();

        rect = driver.findElementById("org.wikipedia.alpha:id/fragment_feed_feed").getRect();
        y1 = (int) (rect.getY() + rect.getHeight() * 0.8);
        y2 = (int) (rect.getY() + rect.getHeight() * 0.2);
        x1 = rect.getX() + rect.getWidth() / 2;
        action.press(point(x1, y1))
                .waitAction(waitOptions(ofMillis(500)))
                .moveTo(point(x1, y2))
                .release()
                .perform();


    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driver.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\"}}");
        driver.quit();
    }

}
