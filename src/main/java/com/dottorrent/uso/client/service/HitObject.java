package com.dottorrent.uso.client.service;

/**
 * 单个滑块/长条
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class HitObject {
    private final int indexX;
    private final int startTime;
    /**
     * 结束时间，若为 0，则为短key，若不为 0，则为长条
     */
    private final int endTime;

    public HitObject(int indexX, int startTime, int endTime) {
        this.indexX = indexX;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getIndexX() {
        return indexX;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
}
