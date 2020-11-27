package com.dottorrent.uso.service;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class HitObject {
    private int indexX;
    private int startTime;
    private int endTime;

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
