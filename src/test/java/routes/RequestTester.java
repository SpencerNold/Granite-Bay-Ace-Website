package routes;

import com.google.gson.JsonObject;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.http.HttpClient;
import me.spencernold.kwaf.http.HttpRequest;
import me.spencernold.kwaf.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

interface RequestTester {
    default HttpResponse sendRequest(String path, Http.Method method, Map<String, String> parameters, Map<String, String> headers, JsonObject body) {
        HttpRequest request = new HttpRequest
                .Builder(method, "http://localhost:80" + path, new HashMap<>(), new HashMap<>(), new byte[0])
                .parameters(parameters)
                .headers(headers)
                .body(body.toString().getBytes(StandardCharsets.UTF_8))
                .build();
        return HttpClient.Companion.send(request);
    }
}
