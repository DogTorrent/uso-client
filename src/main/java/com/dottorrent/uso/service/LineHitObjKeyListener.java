package com.dottorrent.uso.service;

import com.dottorrent.uso.gui.pane.GamePlayingPane;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/12/7
 */
public class LineHitObjKeyListener implements KeyListener {
    private int lockedHitObjIndex = -1;
    private ArrayList<HitObject> hitObjects;
    private long musicStartTime;
    private boolean judgingStarted;
    private int keyCode;
    private int progressIndex=0;
    private GamePlayingPane gamePlayingPane=null;

    public LineHitObjKeyListener(int keyCode) {
        this.keyCode=keyCode;
        hitObjects = new ArrayList<>();
    }

    public LineHitObjKeyListener(GamePlayingPane gamePlayingPane,int keyCode) {
        this.keyCode=keyCode;
        this.gamePlayingPane=gamePlayingPane;
        hitObjects = new ArrayList<>();
    }

    public void setMusicStartTime(long musicStartTime) {
        this.musicStartTime = musicStartTime;
        new ScheduledThreadPoolExecutor(1).schedule(
                () -> setJudgingStarted(true),
                 musicStartTime-System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);
    }

    public void setJudgingStarted(boolean judgingStarted) {
        this.judgingStarted = judgingStarted;
    }

    public void addHitObjects(HitObject hitObject) {
        hitObjects.add(hitObject);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        long currentTime=System.currentTimeMillis();
//        System.out.println("Pressed "+e.getKeyCode()+" "+keyCode+" "+lockedHitObjIndex);
        if(e.getKeyCode()!=keyCode){
            return;
        }
        // 如果还没有启动检测，就忽略事件
        if(!judgingStarted){
            return;
        }
        // 如果这一条轨道被阻塞了(也就是按键已经按下了，且按下的按键没放开)
        if(lockedHitObjIndex!=-1){
            //如果是长条
            if(hitObjects.get(lockedHitObjIndex).getEndTime()!=0){
                if(gamePlayingPane!=null){
                    gamePlayingPane.showHitResult(0);
                }
            }
            return;
        }
        long diff=GameConfig.getJudgeOffset();
        for(int i=progressIndex;i<hitObjects.size();i++){
            HitObject hitObject= hitObjects.get(i);
            long tempDiff=currentTime-musicStartTime+GameConfig.getHitDelay()-hitObject.getStartTime();
            if(Math.abs(tempDiff)<Math.abs(diff)){
                diff=tempDiff;
                lockedHitObjIndex=i;
            }
        }
        if(lockedHitObjIndex!=-1){
            progressIndex=lockedHitObjIndex;
            if(hitObjects.get(lockedHitObjIndex).getEndTime()==0){
                //是普通滑块的情况
                //@TODO
                if(gamePlayingPane!=null){
                    gamePlayingPane.showHitResult(diff);
                }
                System.out.println("Hit-S! "+diff);
            }else{
                //是长条滑块的情况
                //@TODO
                if(gamePlayingPane!=null){
                    gamePlayingPane.showHitResult(diff);
                }
                System.out.println("Hit-L! "+diff);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        long currentTime=System.currentTimeMillis();
        if(e.getKeyCode()!=keyCode){
            return;
        }
        // 如果还没有启动检测，就忽略事件
        if(!judgingStarted){
            return;
        }
        // 如果这一条轨道未被阻塞(也就是按键没有被按下)，就忽略事件
        if(lockedHitObjIndex==-1){
            return;
        }
        if(hitObjects.get(lockedHitObjIndex).getEndTime()!=0){
            long diff=currentTime-musicStartTime+GameConfig.getHitDelay()-hitObjects.get(lockedHitObjIndex).getEndTime();
            if(Math.abs(diff)<GameConfig.getJudgeOffset()){
                //@TODO
                if(gamePlayingPane!=null){
                    gamePlayingPane.showHitResult(diff);
                }
                System.out.println("Hit-L-End! "+diff);
            }else{
                //@TODO
//                System.out.println("Hit-L-Miss! "+diff);
            }
        }
        lockedHitObjIndex=-1;
    }
}
