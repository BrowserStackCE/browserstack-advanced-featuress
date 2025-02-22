package com.utils;

import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static io.restassured.RestAssured.*;

public final class AppUtils {

    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");

    private AppUtils() {
    }

    public static void uploadApp(String customId, String appUrl) {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api-cloud.browserstack.com")
                .setBasePath("app-automate")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        List<String> customIds = get("recent_apps/" + customId).jsonPath().getList("custom_id");
        if (customIds == null) {
            System.out.println("Uploading app...");
            given()
                    .header("Content-Type", "multipart/form-data")
                    .multiPart("url", appUrl)
                    .param("custom_id", customId)
                    .post("upload");
        } else {
            System.out.println("Using previously uploaded app");
        }
    }

    public static void uploadApp(String customId, File appFile) {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api-cloud.browserstack.com")
                .setBasePath("app-automate")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        List<String> customIds = get("recent_apps/" + customId).jsonPath().getList("custom_id");
        if (customIds == null) {
            System.out.println("Uploading app...");
            given()
                    .header("Content-Type", "multipart/form-data")
                    .multiPart("file", appFile, "application/vnd.android.package-archive")
                    .param("custom_id", customId)
                    .post("upload");
        } else {
            System.out.println("Using previously uploaded app");
        }
    }

    public static String uploadMedia(File mediaFile) {
        return given()
                .header("Content-Type", "multipart/form-data")
                .multiPart("file", mediaFile, "image/jpg")
//                .param("custom_id", "SampleMedia")
                .post("upload-media")
                .jsonPath()
                .get("media_url");
    }

    public static void uploadMedia(String customId, File mediaFile) {
        given()
            .header("Content-Type", "multipart/form-data")
            .multiPart("file", mediaFile, "image/jpg")
            .param("custom_id", customId)
            .post("upload-media");
    }

    public static void setNetworkCondition(String sessionId, String networkCondition) {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://api-cloud.browserstack.com")
                .setBasePath("app-automate")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        given()
                .header("Content-Type", "application/json")
                .body("{\"networkProfile\":\"" + networkCondition + "\"}")
                .put("sessions/" + sessionId + "/update_network.json");
    }

    public static List<String> getAllContexts(String sessionId) {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://hub.browserstack.com")
                .setBasePath("wd/hub")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        return given()
                .get("session/" + sessionId + "/contexts")
                .jsonPath()
                .getList("value");
    }

    public static void switchContect(String sessionId, String context) {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://hub.browserstack.com")
                .setBasePath("wd/hub")
                .setAuth(authenticationScheme)
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        given()
                .header("Content-Type", "application/json")
                .body("{\"name\":\"" + context + "\"}")
                .post("session/" + sessionId + "/context");
    }

    public static byte[] pullFile(String sessionId, String filePath) {
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://hub.browserstack.com")
                .setBasePath("wd/hub")
                .setAuth(authenticationScheme)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        String base64String =  given()
                .header("Content-Type", "application/json")
                .body("{\"path\":\"" + filePath + "\"}")
                .post("session/" + sessionId + "/appium/device/pull_file")
                .jsonPath()
                .getString("value");
        return Base64.getDecoder().decode(base64String);
    }

    public static void pushFile(String sessionId, String sourceFilePath, String destinationFilePath) {
        String data = "";
        File file = new File(sourceFilePath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] fileBytes = new byte[(int) file.length()];
            fileInputStream.read(fileBytes); // Read file data
            data = Base64.getEncoder().encodeToString(fileBytes); // Encode to Base64
        } catch (IOException e) {
            e.printStackTrace();
        }
        PreemptiveBasicAuthScheme authenticationScheme = new PreemptiveBasicAuthScheme();
        authenticationScheme.setUserName(USERNAME);
        authenticationScheme.setPassword(ACCESS_KEY);
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://hub.browserstack.com")
                .setBasePath("wd/hub")
                .setAuth(authenticationScheme)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        given()
                .header("Content-Type", "application/json")
                .body("{\"path\":\"" + destinationFilePath + "\", \"data\":\"" + data + "\"}")
                .post("session/" + sessionId + "/appium/device/push_file");
    }

}
