
package com.arun.cucumber.hello.bdd.stepdefs;

import com.arun.cucumber.hello.bdd.CucumberTestContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

/**
 * Class that abstract test context management and REST API invocation.
 */
public class AbstractSteps {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSteps.class);

    private CucumberTestContext CONTEXT = CucumberTestContext.CONTEXT;

    @LocalServerPort

    private int port;

    protected String baseUrl() {
        return "http://localhost:" + this.port + "/api/";
    }

    protected CucumberTestContext testContext() {
        return this.CONTEXT;
    }

    protected void executePost(String apiPath) {
        executePost(apiPath, null, null);
    }

    protected void executePost(String apiPath, Map<String, String> pathParams) {
        executePost(apiPath, pathParams, null);
    }

    protected void executePost(String apiPath, Map<String, String> pathParams, Map<String, String> queryParamas) {
        final RequestSpecification request = this.CONTEXT.getRequest();
        final Object payload = this.CONTEXT.getPayload();
        final String url = baseUrl() + apiPath;

        setPayload(request, payload);
        setQueryParams(pathParams, request);
        setPathParams(queryParamas, request);

        Response response = request.accept(ContentType.JSON).log().all().post(url);

        logResponse(response);

        this.CONTEXT.setResponse(response);
        if (response.getStatusCode() == HttpStatus.CREATED.value()) {

            this.CONTEXT.setLastSuccessResponse(response);

        }
    }

    protected void executeMultiPartPost(String apiPath) {
        final RequestSpecification request = this.CONTEXT.getRequest();
        final Object payload = this.CONTEXT.getPayload();
        final String url = baseUrl() + apiPath;

        Response response = request.multiPart("fuelTransfer", payload, "application/json").log().all().post(url);

        logResponse(response);
        this.CONTEXT.setResponse(response);
    }

    protected void executeDelete(String apiPath) {
        executeDelete(apiPath, null, null);
    }

    protected void executeDelete(String apiPath, Map<String, String> pathParams) {
        executeDelete(apiPath, pathParams, null);
    }

    protected void executeDelete(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = this.CONTEXT.getRequest();
        final Object payload = this.CONTEXT.getPayload();
        final String url = baseUrl() + apiPath;

        setPayload(request, payload);
        setPathParams(pathParams, request);
        setQueryParams(queryParams, request);

        Response response = request.accept(ContentType.JSON).log().all().delete(url);

        logResponse(response);
        this.CONTEXT.setResponse(response);
    }

    protected void executePut(String apiPath) {
        executePut(apiPath, null, null);
    }

    protected void executePut(String apiPath, Map<String, String> pathParams) {
        executePut(apiPath, pathParams, null);
    }

    protected void executePut(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = this.CONTEXT.getRequest();
        final Object payload = this.CONTEXT.getPayload();
        final String url = baseUrl() + apiPath;

        setPayload(request, payload);
        setQueryParams(queryParams, request);
        setPathParams(pathParams, request);

        Response response = request.accept(ContentType.JSON).log().all().put(url);

        logResponse(response);
        this.CONTEXT.setResponse(response);
    }

    protected void executePatch(String apiPath) {
        executePatch(apiPath, null, null);
    }

    protected void executePatch(String apiPath, Map<String, String> pathParams) {
        executePatch(apiPath, pathParams, null);
    }

    protected void executePatch(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = this.CONTEXT.getRequest();
        final Object payload = this.CONTEXT.getPayload();
        final String url = baseUrl() + apiPath;

        setPayload(request, payload);
        setQueryParams(queryParams, request);
        setPathParams(pathParams, request);

        Response response = request.accept(ContentType.JSON).log().all().patch(url);

        logResponse(response);
        this.CONTEXT.setResponse(response);
    }

    protected void executeGet(String apiPath) {
        executeGet(apiPath, null, null);
    }

    protected void executeGet(String apiPath, Map<String, String> pathParams) {
        executeGet(apiPath, pathParams, null);
    }

    protected void executeGet(String apiPath, Map<String, String> pathParams, Map<String, String> queryParams) {
        final RequestSpecification request = this.CONTEXT.getRequest();
        final String url = baseUrl() + apiPath;

        setPathParams(pathParams, request);
        setQueryParams(queryParams, request);

        Response response = request.accept(ContentType.JSON).log().all().get(url);

        logResponse(response);
        this.CONTEXT.setResponse(response);
    }

    private void logResponse(Response response) {
        response.then().log().all();
    }

    private void setPathParams(Map<String, String> pathParams, RequestSpecification request) {
        if (null != pathParams) {
            request.pathParams(pathParams);
        }
    }

    private void setQueryParams(Map<String, String> queryParamas, RequestSpecification request) {
        if (null != queryParamas) {
            request.queryParams(queryParamas);
        }
    }

    private void setPayload(RequestSpecification request, Object payload) {
        if (null != payload) {
            request.contentType(ContentType.JSON).body(payload);
        }
    }

}	
