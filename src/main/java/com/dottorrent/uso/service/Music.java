package com.dottorrent.uso.service;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/21
 */
public class Music {
    private String identification;
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

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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
        if(hitObjects instanceof ArrayList){
            this.hitObjects = (ArrayList<HitObject>) hitObjects;
        }else{
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
        player=new Player(new BufferedInputStream(new FileInputStream(audioPath.toFile())));
    }

    public void initHitObjects() throws FileNotFoundException{
        MusicLoader.loadSongHitObjects(getCfgFilePath(),this);
    }

    public void play() throws JavaLayerException{
        player.play();
    }
}
