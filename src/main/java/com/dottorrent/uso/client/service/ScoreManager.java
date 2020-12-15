package com.dottorrent.uso.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/12/14
 */
public class ScoreManager {
    public static int getHighScore(String userId,String musicIdentifier,Duration duration){
        if(!userId.equals("0")){
            Map<String, String> map = new HashMap<>(2);
            map.put("user_id", userId);
            map.put("music_identifier", musicIdentifier);
            try {
                String bodyJson = new ObjectMapper().writeValueAsString(map);
                HttpClient httpClient = HttpClient.newHttpClient();
                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(GameConfig.getResultServerUri())
                        .timeout(duration)
                        .header("Content-Type", "application/json")
                        .header("operation-command", "get-highscore")
                        .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                        .build();
                HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                return Integer.parseInt(httpResponse.headers().firstValue("High-Score").get());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            try {
                Connection connection = DbManager.dbMgr.dbConnection;
                Statement statement = connection.createStatement();
                return statement.executeQuery("SELECT MAX(\"score\") FROM \"user_"+userId+"\" WHERE \"music_identifier\" = \"" + musicIdentifier + "\"").getInt(1);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static boolean checkMusicExist(String musicIdentifier,Duration  duration){
        try {
            HttpClient httpClient= HttpClient.newHttpClient();
            HttpRequest httpRequest=HttpRequest.newBuilder()
                    .uri(GameConfig.getMusicServerUri())
                    .timeout(duration)
                    .header("Content-Type", "application/json")
                    .header("operation-command","check-music-exist")
                    .header("music_identifier",musicIdentifier)
                    .GET()
                    .build();
            HttpResponse<String> httpResponse=httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            Optional<String> isExist=httpResponse.headers().firstValue("Is-Exist");
            if(isExist.isPresent()){
                return Boolean.parseBoolean(isExist.get());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean uploadScore(PlayingResult playingResult,User user){
        if(user.getUserID()==0){
            return false;
        }
        Map<String,String> map=new HashMap<>(2);
        map.put("user_id", String.valueOf(user.getUserID()));
        map.put("password_hash",user.getPasswordHash());
        map.put("music_identifier", playingResult.getMusicIdentifier());
        map.put("score", String.valueOf(playingResult.getScore()));
        map.put("great_number", String.valueOf(playingResult.getGreatNumber()));
        map.put("early_number", String.valueOf(playingResult.getEarlyNumber()));
        map.put("late_number", String.valueOf(playingResult.getLateNumber()));
        map.put("miss_number", String.valueOf(playingResult.getMissNumber()));
        map.put("time_cn", String.valueOf(playingResult.getTimeInChina()));
        try {
            String bodyJson=new ObjectMapper().writeValueAsString(map);
            HttpClient httpClient= HttpClient.newHttpClient();
            HttpRequest httpRequest=HttpRequest.newBuilder()
                    .uri(GameConfig.getResultServerUri())
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .header("operation-command","upload-score")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();
            HttpResponse<String> httpResponse=httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(httpResponse.headers().firstValue("Is-Op-Successful").get());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveLocalResult(PlayingResult playingResult,User user){
        try {
            Connection connection = DbManager.dbMgr.dbConnection;
            Statement statement= connection.createStatement();
            String userId=DbManager.sqlWordPreprocessing(String.valueOf(user.getUserID()));
            //如果表不存在，就创造表
            statement.execute("CREATE TABLE IF NOT EXISTS \"user_"+userId+"\" (" +
                    " \"music_identifier\" TEXT NOT NULL," +
                    " \"time_cn\" LONG NOT NULL," +
                    " \"score\" INTEGER NOT NULL," +
                    " \"great_num\" INTEGER NOT NULL," +
                    " \"early_num\" INTEGER NOT NULL," +
                    " \"late_num\" INTEGER NOT NULL," +
                    " \"miss_num\" INTEGER NOT NULL" +
                    ");");
            statement.execute("INSERT INTO user_" + userId +
                    " (music_identifier,time_cn,score,great_num,early_num,late_num,miss_num)" +
                    " VALUES (" +
                    " '" + playingResult.getMusicIdentifier() + "'," +
                    " "+ Long.valueOf(new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA).format(new Date()))+ " ," +
                    " "+playingResult.getScore()+ " ," +
                    " "+playingResult.getGreatNumber()+ " ," +
                    " "+playingResult.getEarlyNumber()+ " ," +
                    " "+playingResult.getLateNumber()+ " ," +
                    " "+playingResult.getMissNumber()+ " );");
        }catch (SQLException e){
            return false;
        }
        return true;
    }

}
