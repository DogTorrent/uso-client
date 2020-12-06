package com.dottorrent.uso.service;


/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class GameConfig {
    private static int millisPerTick =10;
    private static int hitDelay=0;
    private static int judgeOffset=60;
    private static int pixelsPerTick =5;
    private static int startDelay=5000;
    private static int hitBoxShowDelay=-20;
    private static double scalingFactor=0.5;
    private static boolean isHighQuality=true;

    public static int getMillisPerTick() {
        return millisPerTick;
    }

    public static void setMillisPerTick(int freshRate) {
        GameConfig.millisPerTick = 1000/freshRate;
    }

    public static int getHitDelay() {
        return hitDelay;
    }

    public static void setHitDelay(int hitDelay) {
        GameConfig.hitDelay = hitDelay;
    }

    public static int getPixelsPerTick() {
        return pixelsPerTick;
    }

    public static void setPixelsPerTick(int pixelsPerTick) {
        GameConfig.pixelsPerTick = pixelsPerTick;
    }

    public static int getJudgeOffset() {
        return judgeOffset;
    }

    public static void setJudgeOffset(int judgeOffset) {
        GameConfig.judgeOffset = judgeOffset;
    }

    public static double getScalingFactor() {
        return scalingFactor;
    }

    public static void setScalingFactor(double scalingFactor) {
        GameConfig.scalingFactor = scalingFactor;
    }

    public static void setHighQuality(boolean highQuality) {
        GameConfig.isHighQuality = highQuality;
    }
    public static boolean getHighQuality() {
        return isHighQuality;
    }

    public static int getStartDelay() {
        return startDelay;
    }

    public static void setStartDelay(int startDelay) {
        GameConfig.startDelay = startDelay;
    }

    public static int getHitBoxShowDelay() {
        return hitBoxShowDelay;
    }

    public static void setHitBoxShowDelay(int hitBoxShowDelay) {
        GameConfig.hitBoxShowDelay = hitBoxShowDelay;
    }
}
