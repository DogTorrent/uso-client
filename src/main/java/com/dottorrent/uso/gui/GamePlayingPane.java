package com.dottorrent.uso.gui;

import com.dottorrent.uso.service.GameConfig;
import com.dottorrent.uso.service.HitObject;
import com.dottorrent.uso.service.Music;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/28
 */
public class GamePlayingPane extends JLayeredPane {
    private ImageIcon bgImageIcon;
    private ImageIcon lineImageIcon;
    private ImageIcon keyImageIcon;
    private double scalingFactor;
    private QualityLabel bgImageLabel;
    private QualityLabel[] lineImageLabels;
    private ExecutorService lineExecutorService;
    private LineThread[] lineThreads;
    private Music music;
    private long startTime;
    private GamePlayingPane gamePlayingPane;
    private int hitAreaY;
    private int lineBoldWidth;

    public GamePlayingPane(Music music) {
        this(music, GameConfig.getScalingFactor());
    }

    public GamePlayingPane(Music music, double scalingFactor) {
        this.scalingFactor = scalingFactor;
        this.music = music;
        this.gamePlayingPane = this;

        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                Image.SCALE_SMOOTH));
        bgImageLabel = new QualityLabel();

        lineImageIcon = new ImageIcon(getClass().getResource("/pictures/line.png"));
        lineImageIcon.setImage(lineImageIcon.getImage().getScaledInstance(
                (int) (lineImageIcon.getIconWidth() * scalingFactor),
                (int) (lineImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        lineImageLabels = new QualityLabel[4];
        Arrays.setAll(lineImageLabels, i -> new QualityLabel());
        hitAreaY = (int) (1000 * scalingFactor);
        lineBoldWidth = (int) (10 * scalingFactor);

        keyImageIcon = new ImageIcon(getClass().getResource("/pictures/key.png"));
        keyImageIcon.setImage(keyImageIcon.getImage().getScaledInstance(
                (int) (keyImageIcon.getIconWidth() * scalingFactor),
                (int) (keyImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        try {
            music.initHitObjects();
            music.initAudio();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        initComponents();
        initThreadPool();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        GamePlayingPane gamePlayingPane = new GamePlayingPane(new MusicList().getSpecifiedMusic(0));
        testFrame.setContentPane(gamePlayingPane);
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.getContentPane().setVisible(true);
        testFrame.setLocationRelativeTo(null);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        //======== this ========
        this.setPreferredSize(new Dimension((int) (1920 * scalingFactor), (int) (1080 * scalingFactor)));
        if (screenWidth <= getPreferredSize().width ||
                screenHeight <= getPreferredSize().height) {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                    (int) (getPreferredSize().width + 96),
                    (int) (getPreferredSize().height + 54),
                    Image.SCALE_SMOOTH)
            );
        }

        //---- lineImageLabels ----
        for (int i = 0; i < lineImageLabels.length; i++) {
            lineImageLabels[i].setIcon(lineImageIcon);
            lineImageLabels[i].setSize(lineImageIcon.getIconWidth(), lineImageIcon.getIconHeight());
            if (i == 0) {
                lineImageLabels[i].setLocation(getPreferredSize().width / 3, 0);
                this.add(lineImageLabels[i], JLayeredPane.DEFAULT_LAYER);
            } else {
                lineImageLabels[i].setLocation(lineImageLabels[i - 1].getX() + lineImageIcon.getIconWidth() - (int) (lineBoldWidth * scalingFactor)
                        , 0);
                this.add(lineImageLabels[i], JLayeredPane.getLayer(lineImageLabels[0]));
            }
        }

        //---- bgImageLabel ----
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
        bgImageLabel.setLocation(0, 0);
        this.add(bgImageLabel, JLayeredPane.DEFAULT_LAYER);
    }

    private void initThreadPool() {
        lineExecutorService = Executors.newFixedThreadPool(4);
        lineThreads = new LineThread[4];
        for (int i = 0; i < lineThreads.length; i++) {
            lineThreads[i] = new LineThread(i);
        }

        List<HitObject> hitObjects = music.getHitObjects();
        for (HitObject hitObject : hitObjects) {
            lineThreads[hitObject.getIndexX()].addHitObject(hitObject);
        }

    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            Executors.newSingleThreadExecutor().execute(() -> {
                startTime = GameConfig.getStartDelay() + System.currentTimeMillis();
                for (LineThread lineThread : lineThreads) {
                    lineExecutorService.execute(lineThread);
                }
                Executors.newSingleThreadScheduledExecutor().schedule(() -> music.play(), startTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            });
        }
    }

    private class LineThread extends Thread {
        public int index;
        ArrayList<QualityLabel> keyImageLabels;
        ArrayList<HitObject> hitObjects;
        ScheduledThreadPoolExecutor hitObjectExecutor;

        LineThread(int index) {
            this.index = index;
            keyImageLabels = new ArrayList<>();
            hitObjects = new ArrayList<>();
            hitObjectExecutor = new ScheduledThreadPoolExecutor(20);
        }

        public void addHitObject(HitObject hitObject) {
            hitObjects.add(hitObject);
            QualityLabel keyImageLabel = new QualityLabel();
            keyImageLabel.setIcon(keyImageIcon);
            keyImageLabels.add(keyImageLabel);
        }

        @Override
        public void run() {
            for (int i = 0; i < hitObjects.size(); i++) {
                HitObject hitObject = hitObjects.get(i);
                QualityLabel keyImageLabel = keyImageLabels.get(i);
                HitObjectThread hitObjectThread = new HitObjectThread(hitObject, keyImageLabel);
                long subtraction =
                        startTime + GameConfig.getHitBoxShowDelay()+hitObject.getStartTime() - (long) (hitAreaY)/ GameConfig.getPixelsPerTick() * GameConfig.getMillisPerTick();
                hitObjectExecutor.schedule(hitObjectThread, subtraction - System.currentTimeMillis(),
                        TimeUnit.MILLISECONDS);
            }
        }
    }

    private class HitObjectThread implements Runnable {
        boolean isShortKey;
        HitObject hitObject;
        QualityLabel keyImageLabel;
        QualityLabel currentLineLabel;
        ScheduledExecutorService scheduledThreadPoolExecutor;

        public HitObjectThread(HitObject hitObject, QualityLabel keyImageLabel) {
            this.hitObject = hitObject;
            this.keyImageLabel = keyImageLabel;
            this.isShortKey = (hitObject.getEndTime() == 0);
            this.currentLineLabel = lineImageLabels[hitObject.getIndexX()];
            scheduledThreadPoolExecutor = Executors.newSingleThreadScheduledExecutor();

            gamePlayingPane.add(keyImageLabel, JLayeredPane.getLayer(currentLineLabel));
            keyImageLabel.setBounds((currentLineLabel.getLocation().x + lineBoldWidth), -keyImageIcon.getIconHeight(),
                    keyImageIcon.getIconWidth(),
                    keyImageIcon.getIconHeight());
        }

        @Override
        public void run() {


            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                if (keyImageLabel.getY() <= hitAreaY - keyImageIcon.getIconHeight()) {
                    keyImageLabel.setLocation(keyImageLabel.getX(),
                            keyImageLabel.getY() + GameConfig.getPixelsPerTick());
                }else {
                    gamePlayingPane.remove(keyImageLabel);
                    gamePlayingPane.repaint();
                    scheduledThreadPoolExecutor.shutdown();
                }
            }, GameConfig.getMillisPerTick(), GameConfig.getMillisPerTick(), TimeUnit.MILLISECONDS);
        }
    }
}
