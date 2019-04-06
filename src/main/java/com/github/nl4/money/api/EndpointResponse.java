package com.github.nl4.money.api;

import com.google.gson.Gson;
import spark.Response;

public class EndpointResponse {

    private static final String JSON = "application/json";

    public static String ok(Response response, Object content) {
        response.status(200);
        response.type(JSON);
        return new Gson().toJson(content);
    }

    public static String created(Response response, Object content) {
        response.status(201);
        response.type(JSON);
        return new Gson().toJson(content);
    }

    public static String badRequest(Response response, Object content) {
        response.status(400);
        response.type(JSON);
        return new Gson().toJson(content);
    }

    public static String notFound(Response response, Object content) {
        response.status(404);
        response.type(JSON);
        return new Gson().toJson(content);
    }

}