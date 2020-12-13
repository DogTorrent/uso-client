package com.dottorrent.uso.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/12/11
 */
public class PlayingResult {
    private long score;
    private long totalScore;
    private ArrayList<HitObject> hitObjects;
    private ArrayList<Integer> hitObjectsStatus;
    private Music music;
    public static int MISS=0;
    public static int GREAT=1;
    public static int EARLY=2;
    public static int LATE=3;

    public PlayingResult(ArrayList<HitObject> hitObjects,Music music) {
        this.hitObjects=hitObjects;
        this.hitObjectsStatus=new ArrayList<>();
        for(HitObject hitObject:hitObjects){
            this.hitObjectsStatus.add(MISS);
            if(hitObject.getEndTime()!=0){
                totalScore+=4;
            } else {
                totalScore+=2;
            }
        }
        this.music=music;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public ArrayList<HitObject> getHitObjects() {
        return hitObjects;
    }

    public int getHitObjectsStatus(HitObject hitObject) {
        return hitObjectsStatus.get(hitObjects.indexOf(hitObject));
    }

    public int getMissNumber(){
        return (int) hitObjectsStatus.stream().filter(integer -> integer == MISS).count();
    }

    public int getGreatNumber(){
        return (int) hitObjectsStatus.stream().filter(integer -> integer == GREAT).count();
    }

    public int getLateNumber(){
        return (int) hitObjectsStatus.stream().filter(integer -> integer == LATE).count();
    }

    public int getEarlyNumber(){
        return (int) hitObjectsStatus.stream().filter(integer -> integer == EARLY).count();
    }

    public void setHitObjectsStatus(HitObject hitObject,int status) {
        hitObjectsStatus.set(hitObjects.indexOf(hitObject),status);
        if(status==GREAT){
            score+=2;
        }else if(status!=MISS){
            score+=1;
        }
    }

    public boolean saveLocalResult(User user){
        Path localDataDirPath=GameConfig.getLocalDataDirPath();
        //如果data文件夹不存在，就创建该文件夹
        if(!localDataDirPath.toFile().isDirectory()){
            try {
                Files.createDirectories(localDataDirPath);
            } catch (IOException e) {
                return false;
            }
        }

        Path localSaveFilePath=Path.of(localDataDirPath.toString(),GameConfig.getLocalSaveFilename().toString());
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+localSaveFilePath.toString());
            Statement statement= connection.createStatement();
            //如果表不存在，就创造表
            statement.execute("CREATE TABLE IF NOT EXISTS \"user_"+user.getUserID()+"\" (" +
                    " \"song_identifier\" TEXT NOT NULL," +
                    " \"time_cn\" LONG NOT NULL," +
                    " \"score\" INTEGER NOT NULL," +
                    " \"great_num\" INTEGER NOT NULL," +
                    " \"early_num\" INTEGER NOT NULL," +
                    " \"late_num\" INTEGER NOT NULL," +
                    " \"miss_num\" INTEGER NOT NULL" +
                    ");");
            statement.execute("INSERT INTO user_" + user.getUserID() +
                    " (song_identifier,time_cn,score,great_num,early_num,late_num,miss_num)" +
                    " VALUES (" +
                    " '" + music.getIdentification() + "'," +
                    " "+ Long.valueOf(new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA).format(new Date()))+ " ," +
                    " "+getScore()+ " ," +
                    " "+getGreatNumber()+ " ," +
                    " "+getEarlyNumber()+ " ," +
                    " "+getLateNumber()+ " ," +
                    " "+getMissNumber()+ " );");
        }catch (SQLException | ClassNotFoundException e){
            return false;
        }
        return true;
    }

}


