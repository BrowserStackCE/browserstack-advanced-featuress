package com.test.appautomate;

import com.test.base.AndroidBaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class BiometricAuthenticationTest extends AndroidBaseTest {

    @Test
    public void biometricAuthentication() {

        WebElement authenticate = wait.until(d -> d.findElement(AppiumBy.className("android.widget.Button")));
        authenticate.click();
        driver.executeScript("browserstack_executor: {\"action\": \"biometric\", \"arguments\": {\"biometricMatch\": \"fail\"}}");
    }

}