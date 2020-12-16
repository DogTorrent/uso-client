package com.dottorrent.uso.client.service;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 谱面类，初始化时不会加载任何信息，需要手动调用方法加载。
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class Music {
    private String identifier;
    private String title;
    private String artist;
    private String creator;
    private String version;
    private double difficulty;
    private ArrayList<HitObject> hitObjects;
    private Path cfgFilePath;
    private Path audioPath;
    private Path bgImagePath;
    private Player player;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public double getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }

    public List<HitObject> getHitObjects() {
        return hitObjects;
    }

    public void setHitObjects(List<HitObject> hitObjects) {
        if (hitObjects instanceof ArrayList) {
            this.hitObjects = (ArrayList<HitObject>) hitObjects;
        } else {
            this.hitObjects = new ArrayList<>(hitObjects);
        }
    }

    public Path getCfgFilePath() {
        return cfgFilePath;
    }

    public void setCfgFilePath(Path cfgFilePath) {
        this.cfgFilePath = cfgFilePath;
    }

    public Path getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(Path audioPath) {
        this.audioPath = audioPath;
    }

    public Path getBgImagePath() {
        return bgImagePath;
    }

    public void setBgImagePath(Path bgImagePath) {
        this.bgImagePath = bgImagePath;
    }

    public void initAudio() throws FileNotFoundException, JavaLayerException {
        player = new Player(new BufferedInputStream(new FileInputStream(audioPath.toFile())));
    }

    public void initHitObjects() throws FileNotFoundException {
        MusicLoader.loadSongHitObjects(getCfgFilePath(), this);
    }

    public void play() {
        try {
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        player.close();
    }

}
