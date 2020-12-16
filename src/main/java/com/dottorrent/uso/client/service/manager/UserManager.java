package com.dottorrent.uso.client.service.manager;

import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户总管理类，包含{@linkplain #login(String, String) 注册}和{@linkplain #register(long, String, String) 登陆}方法
 *
 * @author .torrent
 * @version 1.0.0 2020/12/14
 */
public class UserManager {
    public static User login(String userId, String password) {
        Map<String, String> map = new HashMap<>(2);
        map.put("user_id", userId);
        map.put("password_hash", DigestUtils.sha1Hex(password));
        try {
            // json化
            String bodyJson = new ObjectMapper().writeValueAsString(map);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(GameConfig.getUserServerUri())
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .header("operation-command", "login")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.body().contains("Login success")) {
                String userName = httpResponse.headers().firstValue("user-name").get();
                return new User(Long.parseLong(userId), password, userName);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean register(long userId, String userName, String password) {
        Map<String, String> map = new HashMap<>(3);
        map.put("user_id", String.valueOf(userId));
        map.put("user_name", userName);
        map.put("password_hash", DigestUtils.sha1Hex(password));
        try {
            // json化
            String bodyJson = new ObjectMapper().writeValueAsString(map);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(GameConfig.getUserServerUri())
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .header("operation-command", "register")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.body().contains("Register success")) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
