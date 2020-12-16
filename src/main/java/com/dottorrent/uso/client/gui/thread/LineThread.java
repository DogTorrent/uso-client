package com.dottorrent.uso.client.gui.thread;

import com.dottorrent.uso.client.gui.component.QualityLabel;
import com.dottorrent.uso.client.gui.pane.GamePlayingPane;
import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.HitObject;
import com.dottorrent.uso.client.service.LineHitObjKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 初始化某一滑道内所有绘制滑块和长条的线程 {@link HitObjectThread} 的线程，继承自 {@link Thread}
 *
 * @author .torrent
 * @version 1.0.0 2020/12/8
 */
public class LineThread extends Thread {
    public int index;
    private final GamePlayingPane gamePlayingPane;
    private final ArrayList<HitObjectThread> lineHitObjectThreads;
    private final ScheduledExecutorService hitObjectExecutorService;
    private final LineHitObjKeyListener lineHitObjKeyListener;
    private final ImageIcon keyImageIcon;

    public LineThread(GamePlayingPane gamePlayingPane, ImageIcon keyImageIcon, int index) {
        this.gamePlayingPane = gamePlayingPane;
        this.index = index;
        lineHitObjectThreads = new ArrayList<>();
        hitObjectExecutorService = new ScheduledThreadPoolExecutor(10);
        lineHitObjKeyListener = new LineHitObjKeyListener(gamePlayingPane, GameConfig.getLineKeyCode(index));
        this.keyImageIcon = keyImageIcon;
        gamePlayingPane.keyboardFocusManager.addKeyEventPostProcessor(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                lineHitObjKeyListener.keyPressed(e);
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                lineHitObjKeyListener.keyReleased(e);
            }
            return false;
        });
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
        lineHitObjectThreads.add(new HitObjectThread(gamePlayingPane, hitObject, keyImageLabel));
        lineHitObjKeyListener.addHitObjects(hitObject);
    }

    @Override
    public void run() {
        lineHitObjKeyListener.setMusicStartTime(gamePlayingPane.getStartTime());
        for (HitObjectThread hitObjectThread : lineHitObjectThreads) {
            long subtraction =
                    gamePlayingPane.getStartTime() + GameConfig.getHitBoxShowDelay() + hitObjectThread.getHitObject().getStartTime() - gamePlayingPane.getKeyShowAdvancedMillis();
                /*
                在subtraction-System.currentTimeMillis()的基础上再-1000，也就是提前1000毫秒将1000毫秒后要执行的hitObjectThread启动
                但是不用担心提前了1000毫秒会导致时间错误，因为在hitObjectThread的run方法内还有一个检测的流程，真正的执行任务的时间由hitObjectThread决定
                因此，这就类似于一个缓冲区，提前1000毫秒就启动线程，以降低从启动到真正执行线程之间的延迟对游戏的影响。
                当然，默认1000写死不太好，后面可以考虑加入GameConfig的设置项里。
                @TODO 把提前缓冲时间加入GameConfig的设置项
                */
            hitObjectExecutorService.schedule(hitObjectThread, subtraction - System.currentTimeMillis() - 1000,
                    TimeUnit.MILLISECONDS);
        }
    }
}
