package org.example.webCommunication;

import org.example.model.EvaluatedSheets;
import org.example.model.SheetsResponse;
import org.example.webCommunication.utils.Mapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class WixCommunicationClient {
    private static final URL url;

    static {
        try {
            url = new URL("https://www.wix.com/_serverless/hiring-task-spreadsheet-evaluator/sheets?tag=is_equal_strings");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static SheetsResponse getSheets() {;
        return new Mapper().toObject(url, SheetsResponse.class);
    }

    public static String postSheets(String uri, EvaluatedSheets evaluatedSheets) {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = buildRequest(uri, evaluatedSheets);
        try{
            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest buildRequest(String uri, EvaluatedSheets evaluatedSheets) {
        return HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new Mapper().toJson(evaluatedSheets), StandardCharsets.UTF_8))
                .build();
    }
}
