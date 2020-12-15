package com.dottorrent.uso.client.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
    private long timeInChina;
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
        this.timeInChina=0;
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

    public String getMusicIdentifier(){
        return music.getIdentifier();
    }

    /**
     * 设置游戏完成的时间（中国），可以随意重复执行，因为方法内包含判断，实际内容只会执行一次
     */
    public void setTimeInChina(){
        if(timeInChina==0) {
            this.timeInChina = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA).format(new Date()));
        }
    }

    public long getTimeInChina() {
        setTimeInChina();
        return timeInChina;
    }

}


