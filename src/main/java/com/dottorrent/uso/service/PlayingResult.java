package com.dottorrent.uso.service;

import java.util.ArrayList;
import java.util.Collections;

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
    public static int MISS=0;
    public static int GREAT=1;
    public static int EARLY=2;
    public static int LATE=3;

    public PlayingResult(ArrayList<HitObject> hitObjects) {
        this.hitObjects=hitObjects;
        this.hitObjectsStatus=new ArrayList<>();
        for(HitObject hitObject:hitObjects){
            this.hitObjectsStatus.add(MISS);
        }
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

    public void setHitObjectsStatus(HitObject hitObject,int status) {
        hitObjectsStatus.set(hitObjects.indexOf(hitObject),status);
    }
}
