package com.dottorrent.uso.service;


/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class GameConfig {
    private int millisPerTick =17;
    private int hitDelay=0;
    private int judgeOffset=30;
    private int millisPerPixel=34;
    private double scalingFactor=0.5;
    private boolean isHighQuality=true;
    private static GameConfig gameConfigInstance =new GameConfig();

    public static GameConfig getInstance(){
        return gameConfigInstance;
    }

    public int getMillisPerTick() {
        return millisPerTick;
    }

    public void setMillisPerTick(int freshRate) {
        this.millisPerTick = 1000/freshRate;
    }

    public int getHitDelay() {
        return hitDelay;
    }

    public void setHitDelay(int hitDelay) {
        this.hitDelay = hitDelay;
    }

    public int getMillisPerPixel() {
        return millisPerPixel;
    }

    public void setMillisPerPixel(int millisPerPixel) {
        this.millisPerPixel = millisPerPixel;
    }

    public int getJudgeOffset() {
        return judgeOffset;
    }

    public void setJudgeOffset(int judgeOffset) {
        this.judgeOffset = judgeOffset;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public void setHighQuality(boolean highQuality) {
        isHighQuality = highQuality;
    }
    public boolean getHighQuality() {
        return isHighQuality;
    }
}
