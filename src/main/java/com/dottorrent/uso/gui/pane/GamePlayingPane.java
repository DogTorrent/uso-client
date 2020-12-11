package com.dottorrent.uso.gui.pane;

import com.dottorrent.uso.gui.component.MusicList;
import com.dottorrent.uso.gui.component.QualityLabel;
import com.dottorrent.uso.gui.thread.LineThread;
import com.dottorrent.uso.service.GameConfig;
import com.dottorrent.uso.service.HitObject;
import com.dottorrent.uso.service.Music;
import com.dottorrent.uso.service.PlayingResult;
import javazoom.jl.decoder.JavaLayerException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/28
 */
public class GamePlayingPane extends JLayeredPane {
    public KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    private ImageIcon bgImageIcon;
    private ImageIcon lineImageIcon;
    private ImageIcon keyImageIcon;
    private ImageIcon finalResultImageIcon;
    private double scalingFactor;
    private QualityLabel bgImageLabel;
    private QualityLabel finalResultImageLabel;
    private QualityLabel[] lineImageLabels;
    private JLabel highLightHitResultLabel;
    private ExecutorService lineExecutorService;
    private ScheduledThreadPoolExecutor showHitResultExecutor;
    private PlayingResult playingResult;
    private LineThread[] lineThreads;
    private Music music;
    private long startTime;
    private GamePlayingPane gamePlayingPane;
    private QualityLabel scoreBoardLabel;
    private int hitAreaY;
    private int lineBoldWidth;
    private ArrayList<HitObject> hitObjects;
    /**
     * 音符滑块提前显示的毫秒数，也就是音符滑块从顶部下落到判定线所需要的时间，我们需要提前这么久开始让滑块显示在画面上
     */
    private long keyShowAdvancedMillis;

    public GamePlayingPane(Music music) {
        this(music, GameConfig.getScalingFactor());
    }

    public GamePlayingPane(Music music, double scalingFactor) {
        this.scalingFactor = scalingFactor;
        this.music = music;
        this.gamePlayingPane = this;

        Path imgPath = music.getBgImagePath();
        if (imgPath.toFile().isFile()) {
            bgImageIcon = new ImageIcon(imgPath.toString());
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                    (int) (1920 * scalingFactor),
                    (int) (1080 * scalingFactor),
                    Image.SCALE_SMOOTH));
        } else {
            bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                    (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                    (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                    Image.SCALE_SMOOTH));
        }
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
        keyShowAdvancedMillis = (long) (hitAreaY) / GameConfig.getPixelsPerTick() * GameConfig.getMillisPerTick();

        keyImageIcon = new ImageIcon(getClass().getResource("/pictures/key.png"));
        keyImageIcon.setImage(keyImageIcon.getImage().getScaledInstance(
                (int) (keyImageIcon.getIconWidth() * scalingFactor),
                (int) (keyImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));

        finalResultImageIcon = new ImageIcon(getClass().getResource("/pictures/music_info.png"));
        finalResultImageIcon.setImage(finalResultImageIcon.getImage().getScaledInstance(
                (int) (finalResultImageIcon.getIconWidth() * scalingFactor),
                (int) (finalResultImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        finalResultImageLabel = new QualityLabel();

        try {
            music.initHitObjects();
            music.initAudio();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
        hitObjects = (ArrayList<HitObject>) music.getHitObjects();
        playingResult = new PlayingResult(hitObjects);
        scoreBoardLabel = new QualityLabel();
        highLightHitResultLabel = new JLabel();
        initComponents();
        initThreadPool();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setUndecorated(true);
        GamePlayingPane gamePlayingPane = new GamePlayingPane(new MusicList().getSpecifiedMusic(0));
        testFrame.setContentPane(gamePlayingPane);
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.getContentPane().setVisible(true);
        testFrame.setLocationRelativeTo(null);
    }

    public QualityLabel[] getLineImageLabels() {
        return lineImageLabels;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getLineBoldWidth() {
        return lineBoldWidth;
    }

    public long getKeyShowAdvancedMillis() {
        return keyShowAdvancedMillis;
    }

    public PlayingResult getPlayingResult() {
        return playingResult;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        //======== this ========
        this.setPreferredSize(new Dimension((int) (1920 * scalingFactor), (int) (1080 * scalingFactor)));
        if (screenWidth <= getPreferredSize().width || screenHeight <= getPreferredSize().height) {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                    (int) (getPreferredSize().width + 96),
                    (int) (getPreferredSize().height + 54),
                    Image.SCALE_SMOOTH)
            );
        }

        //---- highLightHitResult ----
        highLightHitResultLabel.setPreferredSize(new Dimension(lineImageIcon.getIconWidth() * 2, (int) (64 * scalingFactor)));
        highLightHitResultLabel.setSize(new Dimension(lineImageIcon.getIconWidth() * 2, (int) (64 * scalingFactor)));
        highLightHitResultLabel.setLocation((getPreferredSize().width - highLightHitResultLabel.getPreferredSize().width) / 2,
                (getPreferredSize().height - highLightHitResultLabel.getPreferredSize().height) / 2);
        highLightHitResultLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, (int) (64 * scalingFactor)));
        highLightHitResultLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        highLightHitResultLabel.setVerticalTextPosition(SwingConstants.CENTER);
        this.add(highLightHitResultLabel, JLayeredPane.POPUP_LAYER);

        //---- lineImageLabels ----
        for (int i = 0; i < lineImageLabels.length; i++) {
            lineImageLabels[i].setIcon(lineImageIcon);
            lineImageLabels[i].setSize(lineImageIcon.getIconWidth(), lineImageIcon.getIconHeight());
            lineImageLabels[i].setLocation((getPreferredSize().width - lineImageIcon.getIconWidth() * lineImageLabels.length) / 2 + lineImageIcon.getIconWidth() * i, 0);
            if (i == 0) {
                this.add(lineImageLabels[i], JLayeredPane.DEFAULT_LAYER);
            } else {
                this.add(lineImageLabels[i], JLayeredPane.getLayer(lineImageLabels[0]));
            }
        }

        //---- scoreBoardLabel ----
        scoreBoardLabel.setText(0 + " / " + playingResult.getTotalScore());
        scoreBoardLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, (int) (48 * scalingFactor)));
        scoreBoardLabel.setSize(this.getPreferredSize().width / 2, (int) (48 * scalingFactor));
        scoreBoardLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreBoardLabel.setVerticalAlignment(SwingConstants.CENTER);
        scoreBoardLabel.setLocation(this.getPreferredSize().width - scoreBoardLabel.getWidth() - (int) (20 * scalingFactor),
                (int) (20 * scalingFactor));
        this.add(scoreBoardLabel);

        //---- finalResultImageLabel ----
        finalResultImageLabel.setVisible(false);
        finalResultImageLabel.setIcon(finalResultImageIcon);
        finalResultImageLabel.setSize(finalResultImageIcon.getIconWidth(), finalResultImageIcon.getIconHeight());
        finalResultImageLabel.setPreferredSize(new Dimension(finalResultImageIcon.getIconWidth(),
                finalResultImageIcon.getIconHeight()));
        finalResultImageLabel.setLocation((getPreferredSize().width - finalResultImageLabel.getWidth()) / 2,
                (getPreferredSize().height - finalResultImageLabel.getHeight()) / 2);
        finalResultImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        finalResultImageLabel.setVerticalTextPosition(SwingConstants.TOP);
        finalResultImageLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
        finalResultImageLabel.setForeground(new Color(200, 200, 200));

        //---- bgImageLabel ----
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
        bgImageLabel.setLocation(0, 0);
        this.add(bgImageLabel, JLayeredPane.DEFAULT_LAYER);
    }

    private void initThreadPool() {
        lineExecutorService = Executors.newFixedThreadPool(4);
        lineThreads = new LineThread[4];
        showHitResultExecutor = new ScheduledThreadPoolExecutor(20);
        for (int i = 0; i < lineThreads.length; i++) {
            lineThreads[i] = new LineThread(gamePlayingPane, keyImageIcon, i);
        }

        for (HitObject hitObject : hitObjects) {
            lineThreads[hitObject.getIndexX()].addHitObject(hitObject);
        }
    }

    public void initHitResultShowThreads() {
        for (HitObject hitObject : hitObjects) {
            showHitResultExecutor.schedule(new ShowHitResultThread(hitObject),
                    GameConfig.getJudgeOffset() + this.startTime - GameConfig.getHitDelay() + hitObject.getStartTime() - System.currentTimeMillis(),
                    TimeUnit.MILLISECONDS);
        }
    }

    private void showFinalResult() {
        finalResultImageLabel.setText("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<style type=\"text/css\">" +
                ".body { " +
                "width:" + (int) (finalResultImageLabel.getWidth() - 20 * scalingFactor) + "px;" +
                "padding-left: " + (int) (30 * scalingFactor) + "px;" +
                "padding-right: " + (int) (30 * scalingFactor) + "px;" +
                "padding-top: " + (int) (20 * scalingFactor) + "px;" +
                "padding-bottom: " + (int) (20 * scalingFactor) + "px;" +
                "}" +
                ".textLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (28 * scalingFactor) + "px;" +
                "}" +
                ".headLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (38 * scalingFactor) + "px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body class=\"body\">" +
                "<h1 class=\"headLine\">" + "Score : " + String.format("%.2f", (double) playingResult.getScore() / (double) playingResult.getTotalScore() * 100) + " %" + "</h1>" +
                "<p class=\"textLine\">" + "<b>Great : </b>" + playingResult.getGreatNumber() + "</p>" +
                "<p class=\"textLine\">" + "<b>Early : </b>" + playingResult.getEarlyNumber() + "</p>" +
                "<p class=\"textLine\">" + "<b>Late : </b>" + playingResult.getLateNumber() + "</p>" +
                "<p class=\"textLine\">" + "<b>Miss : </b>" + playingResult.getMissNumber() + "</p>" +
                "</body>" +
                "</html>");
        this.add(finalResultImageLabel, JLayeredPane.POPUP_LAYER);
        finalResultImageLabel.setVisible(true);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            startTime = GameConfig.getStartDelay() + System.currentTimeMillis();
            for (LineThread lineThread : lineThreads) {
                lineExecutorService.execute(lineThread);
            }
            new ScheduledThreadPoolExecutor(1).schedule(() -> {
                music.play();
                try {
                    Thread.sleep(GameConfig.getStartDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showFinalResult();
            }, startTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            initHitResultShowThreads();
            System.out.println(music.getTitle() + " " + music.getVersion());
        }
    }

    private class ShowHitResultThread extends Thread {
        private HitObject hitObject;
        private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

        public ShowHitResultThread(HitObject hitObject) {
            this.hitObject = hitObject;
            if (hitObject.getEndTime() != 0) {
                scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
            }
        }

        @Override
        public void run() {
            if (hitObject.getEndTime() == 0) {
                draw();
            } else {
                scheduledThreadPoolExecutor.scheduleAtFixedRate(this::draw, 0, 200,
                        TimeUnit.MILLISECONDS);
            }
        }

        private void draw() {
            if (hitObject.getEndTime() != 0) {
                long endTime =
                        GameConfig.getJudgeOffset() + gamePlayingPane.startTime - GameConfig.getHitDelay() + hitObject.getEndTime();
                if (System.currentTimeMillis() > endTime) {
                    scheduledThreadPoolExecutor.shutdown();
                    return;
                }
            }
            int status = playingResult.getHitObjectsStatus(hitObject);
            if (status == PlayingResult.GREAT) {
                highLightHitResultLabel.setForeground(new Color(90, 203, 87, 255));
                highLightHitResultLabel.setText("GREAT");
            } else if (status == PlayingResult.MISS) {
                highLightHitResultLabel.setForeground(new Color(203, 87, 87, 255));
                highLightHitResultLabel.setText("MISS");
            } else {
                highLightHitResultLabel.setForeground(new Color(226, 143, 99, 255));
                highLightHitResultLabel.setText(status == PlayingResult.LATE ? "LATE" : "EARLY");
            }
            highLightHitResultLabel.setVisible(true);
            gamePlayingPane.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            highLightHitResultLabel.setVisible(false);
            scoreBoardLabel.setText(playingResult.getScore() + " / " + playingResult.getTotalScore());
            gamePlayingPane.repaint();
        }
    }

}
