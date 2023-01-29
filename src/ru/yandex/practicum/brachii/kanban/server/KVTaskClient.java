package ru.yandex.practicum.brachii.kanban.server;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class KVTaskClient {

    private String apiToken;
    private final URL urlKVServer;

    public KVTaskClient(URL url) throws IOException {
        this.urlKVServer = url;
        this.apiToken = register(url);
    }

    private String register(URL url) throws IOException {
        String token = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = url.toURI();
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            token = response.body();
        } catch (URISyntaxException | InterruptedException e) {
            e.getMessage();
        }
        return token;
    }

    public void put(String key, String json) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
    }

    public String load(String key) {
        String response = "";
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + apiToken);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> responseHttp = client.send(request, HttpResponse.BodyHandlers.ofString());
            response = responseHttp.body();
        } catch (IOException | InterruptedException e) {
            e.getMessage();
        }
        return response;
    }


}
