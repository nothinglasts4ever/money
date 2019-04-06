package com.github.nl4.money.helper

import com.jayway.restassured.RestAssured
import com.jayway.restassured.response.Response
import com.jayway.restassured.specification.RequestSpecification

class RequestSpecificationTemplate {

    static String SERVER_URL = "http://localhost:4567" // TODO: put to .properties file

    static RequestSpecification prepareRequestSpecification() {
        def specification = RestAssured.given()
                .contentType("application/json")
                .header("Accept-Language", "en-US")
        specification
    }

    static Response processGetRequest(String url) {
        prepareRequestSpecification()
                .get(SERVER_URL + url)
    }

    static Response processPostRequest(String url, def requestBody) {
        prepareRequestSpecification()
                .body(requestBody)
                .post(SERVER_URL + url)
    }

    static Response processPutRequest(String url) {
        prepareRequestSpecification()
                .put(SERVER_URL + url)
    }

    static Response processPutRequest(String url, def requestBody) {
        prepareRequestSpecification()
                .body(requestBody)
                .put(SERVER_URL + url)
    }

}