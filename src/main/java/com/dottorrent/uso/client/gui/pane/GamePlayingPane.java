package com.dottorrent.uso.client.gui.pane;

import com.dottorrent.uso.client.gui.LocalGameFrame;
import com.dottorrent.uso.client.gui.component.MusicList;
import com.dottorrent.uso.client.gui.component.QualityButton;
import com.dottorrent.uso.client.gui.component.QualityLabel;
import com.dottorrent.uso.client.gui.thread.LineThread;
import com.dottorrent.uso.client.service.*;
import javazoom.jl.decoder.JavaLayerException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private User user;
    public KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    private ImageIcon bgImageIcon;
    private ImageIcon lineImageIcon;
    private ImageIcon keyImageIcon;
    private ImageIcon finalResultImageIcon;
    private ImageIcon backRoundButtonImageIcon;
    private ImageIcon backRoundButtonOnMovedImageIcon;
    private ImageIcon saveRoundButtonImageIcon;
    private ImageIcon saveRoundButtonOnMovedImageIcon;
    private double scalingFactor;
    private QualityLabel bgImageLabel;
    private QualityLabel finalResultImageLabel;
    private QualityButton backRoundButton;
    private QualityButton saveRoundButton;
    private QualityLabel[] lineImageLabels;
    private QualityLabel highLightHitResultLabel;
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

    public GamePlayingPane(Music music,User user) {
        this(music, user, GameConfig.getScalingFactor());
    }

    public GamePlayingPane(Music music, User user, double scalingFactor) {
        this.scalingFactor = scalingFactor;
        this.music = music;
        this.user=user;
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

        finalResultImageIcon = new ImageIcon(getClass().getResource("/pictures/popup_label_bg.png"));
        finalResultImageIcon.setImage(finalResultImageIcon.getImage().getScaledInstance(
                (int) (finalResultImageIcon.getIconWidth() * scalingFactor),
                (int) (finalResultImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        finalResultImageLabel = new QualityLabel();

        //---- backRoundButtonImageIcon && backRoundButtonOnMovedImageIcon ----
        try {
            BufferedImage backRoundButtonImage = ImageIO.read(getClass().getResource("/pictures/back_round.png"));
            int width=backRoundButtonImage.getWidth()/2;
            int height=backRoundButtonImage.getHeight();
            backRoundButtonImageIcon=new ImageIcon(backRoundButtonImage.getSubimage(0,0,width,height));
            backRoundButtonImageIcon.setImage(backRoundButtonImageIcon.getImage().getScaledInstance(
                    (int) (backRoundButtonImageIcon.getIconWidth() * scalingFactor),
                    (int) (backRoundButtonImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
            backRoundButtonOnMovedImageIcon=new ImageIcon(backRoundButtonImage.getSubimage(width,0,width,height));
            backRoundButtonOnMovedImageIcon.setImage(backRoundButtonOnMovedImageIcon.getImage().getScaledInstance(
                    (int) (backRoundButtonOnMovedImageIcon.getIconWidth() * scalingFactor),
                    (int) (backRoundButtonOnMovedImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        backRoundButton=new QualityButton();

        //---- saveRoundButtonImageIcon && saveRoundButtonOnMovedImageIcon ----
        try {
            BufferedImage saveRoundButtonImage = ImageIO.read(getClass().getResource("/pictures/save_round.png"));
            int width=saveRoundButtonImage.getWidth()/2;
            int height=saveRoundButtonImage.getHeight();
            saveRoundButtonImageIcon=new ImageIcon(saveRoundButtonImage.getSubimage(0,0,width,height));
            saveRoundButtonImageIcon.setImage(saveRoundButtonImageIcon.getImage().getScaledInstance(
                    (int) (saveRoundButtonImageIcon.getIconWidth() * scalingFactor),
                    (int) (saveRoundButtonImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
            saveRoundButtonOnMovedImageIcon=new ImageIcon(saveRoundButtonImage.getSubimage(width,0,width,height));
            saveRoundButtonOnMovedImageIcon.setImage(saveRoundButtonOnMovedImageIcon.getImage().getScaledInstance(
                    (int) (saveRoundButtonOnMovedImageIcon.getIconWidth() * scalingFactor),
                    (int) (saveRoundButtonOnMovedImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        saveRoundButton=new QualityButton();

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
        playingResult = new PlayingResult(hitObjects,music);
        scoreBoardLabel = new QualityLabel();
        highLightHitResultLabel = new QualityLabel();
        initComponents();
        initThreadPool();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.setUndecorated(true);
        GamePlayingPane gamePlayingPane = new GamePlayingPane(new MusicList().getSpecifiedMusic(0),new User(0,null,
                null));
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
            this.add(lineImageLabels[i], JLayeredPane.DEFAULT_LAYER);
        }

        //---- scoreBoardLabel ----
        scoreBoardLabel.setText(0 + " / " + playingResult.getTotalScore());
        scoreBoardLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, (int) (48 * scalingFactor)));
        scoreBoardLabel.setSize(this.getPreferredSize().width / 2, (int) (48 * scalingFactor));
        scoreBoardLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        scoreBoardLabel.setVerticalAlignment(SwingConstants.CENTER);
        scoreBoardLabel.setLocation(this.getPreferredSize().width - scoreBoardLabel.getWidth() - (int) (20 * scalingFactor),
                (int) (20 * scalingFactor));
        this.add(scoreBoardLabel,JLayeredPane.MODAL_LAYER);

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

        //---- backRoundButton ----
        backRoundButton.setIcon(backRoundButtonImageIcon);
        backRoundButton.setRolloverIcon(backRoundButtonOnMovedImageIcon);
        backRoundButton.setBounds((int) (finalResultImageLabel.getX()+finalResultImageIcon.getIconWidth()-backRoundButtonImageIcon.getIconWidth()/2-10*scalingFactor),
                (int) (finalResultImageLabel.getY()+finalResultImageIcon.getIconHeight()-backRoundButtonImageIcon.getIconHeight()/2-10*scalingFactor),
                backRoundButtonImageIcon.getIconWidth(),
                backRoundButtonImageIcon.getIconHeight());
        backRoundButton.setContentAreaFilled(false);
        backRoundButton.setBorderPainted(false);
        backRoundButton.setVisible(false);
        backRoundButton.addActionListener(e -> {
            ((LocalGameFrame)(this.getRootPane().getParent())).enterMusicSelectingPane();
        });

        //---- saveRoundButton ----
        saveRoundButton.setIcon(saveRoundButtonImageIcon);
        saveRoundButton.setRolloverIcon(saveRoundButtonOnMovedImageIcon);
        saveRoundButton.setBounds((int) (finalResultImageLabel.getX()+finalResultImageIcon.getIconWidth()-saveRoundButtonImageIcon.getIconWidth()/2*3-10*scalingFactor*2),
                (int) (finalResultImageLabel.getY()+finalResultImageIcon.getIconHeight()-saveRoundButtonImageIcon.getIconHeight()/2-10*scalingFactor),
                saveRoundButtonImageIcon.getIconWidth(),
                saveRoundButtonImageIcon.getIconHeight());
        saveRoundButton.setContentAreaFilled(false);
        saveRoundButton.setBorderPainted(false);
        saveRoundButton.setVisible(false);
        saveRoundButton.addActionListener(e -> {
            playingResult.saveLocalResult(user);
            if(user.getUserID()!=0){
                //@TODO 保存至网络
            }
        });
        finalResultImageLabel.add(saveRoundButton);

        //---- bgImageLabel ----
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
        bgImageLabel.setLocation(0, 0);
        this.add(bgImageLabel, JLayeredPane.FRAME_CONTENT_LAYER);
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
        this.add(finalResultImageLabel, JLayeredPane.MODAL_LAYER,-1);
        finalResultImageLabel.setVisible(true);
        this.add(backRoundButton, JLayeredPane.MODAL_LAYER,0);
        backRoundButton.setVisible(true);
        this.add(saveRoundButton, JLayeredPane.MODAL_LAYER,0);
        saveRoundButton.setVisible(true);
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
