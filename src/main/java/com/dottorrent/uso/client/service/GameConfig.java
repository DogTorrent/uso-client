package com.dottorrent.uso.client.service;


import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class GameConfig {
    private static int millisPerTick =10;
    private static int hitDelay=-250;
    private static int judgeOffset=80;
    private static int pixelsPerTick =7;
    private static int startDelay=5000;
    private static int hitBoxShowDelay=250;
    private static double scalingFactor=0.5;
    private static boolean isHighQuality=true;
    private static Path localDataDirPath = Path.of("data");
    private static File localSaveFilename = new File("saves.db");
    private static int[] lineKeyCodes ={KeyEvent.VK_D,KeyEvent.VK_F,KeyEvent.VK_J,KeyEvent.VK_K};
    private static URI userServerUri = URI.create("http://127.0.0.1:14514/uso-server/user");
    private static URI resultServerUri = URI.create("http://127.0.0.1:14514/uso-server/result");

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

    public static Path getLocalDataDirPath() {
        return localDataDirPath;
    }

    public static File getLocalSaveFilename() {
        return localSaveFilename;
    }

    public static int getLineKeyCode(int index) {
        return lineKeyCodes[index];
    }

    public static URI getUserServerUri() {
        return userServerUri;
    }

    public static void setUserServerUri(URI userServerUri) {
        GameConfig.userServerUri = userServerUri;
    }

    public static URI getResultServerUri() {
        return resultServerUri;
    }

    public static void setResultServerUri(URI resultServerUri) {
        GameConfig.resultServerUri = resultServerUri;
    }
}
