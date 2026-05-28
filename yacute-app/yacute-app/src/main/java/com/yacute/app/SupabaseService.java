package com.yacute.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupabaseService {
    private static final HttpClient client = HttpClient.newHttpClient();

    private final String baseUrl;
    private final String apiKey;

    public SupabaseService() {
        Env.load();

        System.out.println(Env.get("SUPABASE_URL"));
        System.out.println(Env.get("SUPABASE_ANON_KEY"));

        this.baseUrl = Env.get("SUPABASE_URL");
        this.apiKey = Env.get("SUPABASE_ANON_KEY");

        if (baseUrl == null || apiKey == null) {
            throw new RuntimeException("Missing SUPABASE_URL or SUPABASE_ANON_KEY in .env");
        }
    }

    public void insertLog(String username) throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("login_time", LocalDateTime.now().toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/rest/v1/login_logs"))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("Supabase insert error: " + response.statusCode() + " " + response.body());
        }
    }

    public List<LogEntry> fetchLogs() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/rest/v1/login_logs?select=login_time&order=login_time.desc"))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new RuntimeException("Supabase fetch error: " + response.statusCode() + " " + response.body());
        }

        List<LogEntry> logs = new ArrayList<>();
        JSONArray array = new JSONArray(response.body());
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String timeStr = obj.optString("login_time", "");
            if (!timeStr.isEmpty()) {
                LocalDateTime ldt = parseTime(timeStr);
                logs.add(new LogEntry(ldt));
            }
        }
        return logs;
    }

    private LocalDateTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) return LocalDateTime.now();
        String cleaned = timeStr;
        if (cleaned.length() > 19) {
            cleaned = cleaned.substring(0, 19);
        }
        return LocalDateTime.parse(cleaned);
    }
}
