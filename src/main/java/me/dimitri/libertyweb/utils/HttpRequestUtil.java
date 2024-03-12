package me.dimitri.libertyweb.utils;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Singleton
public class HttpRequestUtil {

    private final HttpClient client;

    @Inject
    public HttpRequestUtil() {
        client = HttpClient.newHttpClient();
    }

    public String get(String url) {
        try {
            URI uri = URI.create(url);
            HttpRequest request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofMinutes(2))
                    .setHeader("User-Agent", "LibertyWeb Data")
                    .GET()
                    .uri(uri)
                    .build();

            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.thenApply(HttpResponse::statusCode).get(5, TimeUnit.SECONDS);
            String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);

            if (statusCode == 200) {
                return result;
            }

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
