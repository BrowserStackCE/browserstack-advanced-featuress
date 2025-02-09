package com.utils;

import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;

import java.io.File;
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

}
