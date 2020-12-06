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
        keyShowAdvancedMillis = (long) (hitAreaY) / GameConfig.getPixelsPerTick() * GameConfig.getMillisPerTick();

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
        GamePlayingPane gamePlayingPane = new GamePlayingPane(new MusicList().getSpecifiedMusic(13));
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
        System.out.println(music.getTitle() + " " + music.getVersion());
    }

    private class LineThread extends Thread {
        public int index;
        ArrayList<HitObjectThread> hitObjectThreads;
        ScheduledThreadPoolExecutor hitObjectExecutor;

        LineThread(int index) {
            this.index = index;
            hitObjectThreads = new ArrayList<>();
            hitObjectExecutor = new ScheduledThreadPoolExecutor(20);
        }

        public void addHitObject(HitObject hitObject) {
            QualityLabel keyImageLabel = new QualityLabel();
            if (hitObject.getEndTime() == 0) {
                keyImageLabel.setIcon(keyImageIcon);
            } else {
                int height =
                        (hitObject.getEndTime() - hitObject.getStartTime()) / GameConfig.getMillisPerTick() * GameConfig.getPixelsPerTick();
                keyImageLabel.setIcon(
                        new ImageIcon(
                                keyImageIcon.getImage().getScaledInstance(
                                        keyImageIcon.getIconWidth(), height, Image.SCALE_SMOOTH
                                )
                        )
                );
            }
            hitObjectThreads.add(new HitObjectThread(hitObject, keyImageLabel));
        }

        @Override
        public void run() {
            for (int i = 0; i < hitObjectThreads.size(); i++) {
                HitObjectThread hitObjectThread = hitObjectThreads.get(i);
                long subtraction =
                        startTime + GameConfig.getHitBoxShowDelay() + hitObjectThread.getHitObject().getStartTime() - keyShowAdvancedMillis;
                /*
                在subtraction-System.currentTimeMillis()的基础上再-1000，也就是提前1000毫秒将1000毫秒后要执行的hitObjectThread启动
                但是不用担心提前了1000毫秒会导致时间错误，因为在hitObjectThread的run方法内还有一个检测的流程，真正的执行任务的时间由hitObjectThread决定
                因此，这就类似于一个缓冲区，提前1000毫秒就启动线程，以降低从启动到真正执行线程之间的延迟对游戏的影响。
                当然，默认1000写死不太好，后面可以考虑加入GameConfig的设置项里。
                @TODO 把提前缓冲时间加入GameConfig的设置项
                */
                hitObjectExecutor.schedule(hitObjectThread, subtraction - System.currentTimeMillis() - 1000,
                        TimeUnit.MILLISECONDS);
            }
        }
    }

    @Deprecated
    private class HitObjectThread implements Runnable {
        public HitObject hitObject;
        QualityLabel keyImageLabel;
        QualityLabel currentLineLabel;
        ScheduledExecutorService scheduledThreadPoolExecutor;

        public HitObjectThread(HitObject hitObject, QualityLabel keyImageLabel) {
            this.hitObject = hitObject;
            this.keyImageLabel = keyImageLabel;
            this.currentLineLabel = lineImageLabels[hitObject.getIndexX()];
        }

        public HitObject getHitObject() {
            return hitObject;
        }

        @Override
        public void run() {
            //提前在画面上初始化key，位置正好在可视区域的上方隐藏区域
            scheduledThreadPoolExecutor = Executors.newSingleThreadScheduledExecutor();
            gamePlayingPane.add(keyImageLabel, JLayeredPane.getLayer(currentLineLabel));
            keyImageLabel.setBounds((currentLineLabel.getLocation().x + lineBoldWidth),
                    -keyImageLabel.getIcon().getIconHeight(),
                    keyImageLabel.getIcon().getIconWidth(),
                    keyImageLabel.getIcon().getIconHeight());
            /*
            计算真正的开始动画的时间
            公式含义：
             startTime (音乐启动时间)
             + GameConfig.getHitBoxShowDelay() (Config中设置的画面相对音乐延迟的时间，一般为负数，也就是提前)
             + hitObject.getStartTime() (音符滑块到达判定线的相对时间，相对的是真正的音乐开始播放的时间，也就是前两项之和；在谱面文件中写死)
             - keyShowAdvancedMillis (音符滑块提前显示的毫秒数，也就是音符滑块从顶部下落到判定线所需要的时间，我们需要提前这么久开始让滑块显示在画面上)
             + GameConfig.getMillisPerTick() 每tick相隔的毫秒数，因为已经在可视区域的正上方隐藏区域提前初始化好了音符滑块，这算做第一次tick，所以实际第一次tick
             的时候是理论上第二次tick，我们需要在理论上开始动画的时间点的后一次tick真正开始动画
             - System.currentTimeMillis() 当前的时间，一般都比真正开始绘制动画的时间要前，所以这里要校准一下，算出延迟启动的时间。
             为什么一般都比真正开始绘制动画的时间要前呢？因为在start和真正执行此线程run方法之间可能有延迟，所以一般会提前start。
             当然，不提前start也是可以的，在后面initialDelay作为参数的时候，会有个三元运算，如果initialDelay小于0，则设置延迟为0，以确保不会出现延迟为负数而线程无法启动的情况
            */
            long initialDelay =
                    startTime + GameConfig.getHitBoxShowDelay() + hitObject.getStartTime() - keyShowAdvancedMillis + GameConfig.getMillisPerTick() - System.currentTimeMillis();

            scheduledThreadPoolExecutor.scheduleAtFixedRate(
                    () -> {
                        if (keyImageLabel.getY() <= gamePlayingPane.getHeight()) {
                            keyImageLabel.setLocation(keyImageLabel.getX(),
                                    keyImageLabel.getY() + GameConfig.getPixelsPerTick());
                            if(keyImageLabel.getY()==hitAreaY-keyImageLabel.getHeight()){
                                System.out.println(System.currentTimeMillis()-startTime-hitObject.getStartTime());
                            }
                        } else {
                            gamePlayingPane.remove(keyImageLabel);
                            scheduledThreadPoolExecutor.shutdown();
                        }
                    },
                    initialDelay >= 0 ? initialDelay : 0, /*如果initialDelay小于0，则设置延迟为0，以确保不会出现延迟为负数而线程无法启动的情况*/
                    GameConfig.getMillisPerTick(),
                    TimeUnit.MILLISECONDS
            );
        }
    }
}
