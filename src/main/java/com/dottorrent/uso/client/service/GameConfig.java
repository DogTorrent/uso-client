package com.dottorrent.uso.client.service;


import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;

/**
 * 游戏设置，提供一系列静态方法
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class GameConfig {
    /**
     * tick 间隔毫秒数
     */
    private static int millisPerTick = 10;
    /**
     * 按键击打延时毫秒数
     */
    private static int hitDelay = -250;
    /**
     * 进入判定的毫秒范围
     */
    private static int judgeOffset = 80;
    /**
     * 每 tick 滑块/长条经过的像素
     */
    private static int pixelsPerTick = 7;
    /**
     * 游戏启动延时毫秒
     */
    private static int startDelay = 3000;
    /**
     * 滑块显示延时毫秒数
     */
    private static int hitBoxShowDelay = 250;
    /**
     * 画面缩放比例
     */
    private static double scalingFactor = 0.5;
    /**
     * 是否进行高品质渲染
     */
    private static boolean isHighQuality = true;
    /**
     * 本地 data 文件夹
     */
    private static final Path localDataDirPath = Path.of("data");
    /**
     * 本地数据库名，位于 {@linkplain #localDataDirPath 本地 data 文件夹} 内
     */
    private static final File localSaveFilename = new File("saves.db");
    /**
     * 按键绑定，默认为 D,F,J,K 四个键
     */
    private static final int[] lineKeyCodes = {KeyEvent.VK_D, KeyEvent.VK_F, KeyEvent.VK_J, KeyEvent.VK_K};
    /**
     * 线上模式的用户服务器地址
     */
    private static URI userServerUri = URI.create("http://127.0.0.1:14514/uso-server/user");
    /**
     * 线上模式的游戏结果服务器地址
     */
    private static URI resultServerUri = URI.create("http://127.0.0.1:14514/uso-server/result");
    /**
     * 线上模式的谱面服务器地址
     */
    private static URI musicServerUri = URI.create("http://127.0.0.1:14514/uso-server/music");

    public static int getMillisPerTick() {
        return millisPerTick;
    }

    public static void setMillisPerTick(int millisPerTick) {
        GameConfig.millisPerTick = millisPerTick;
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

    public static boolean getHighQuality() {
        return isHighQuality;
    }

    public static void setHighQuality(boolean highQuality) {
        GameConfig.isHighQuality = highQuality;
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

    public static URI getMusicServerUri() {
        return musicServerUri;
    }

    public static void setMusicServerUri(URI musicServerUri) {
        GameConfig.musicServerUri = musicServerUri;
    }
}
