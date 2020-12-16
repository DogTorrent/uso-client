package com.dottorrent.uso.client.gui.thread;

import com.dottorrent.uso.client.gui.component.QualityLabel;
import com.dottorrent.uso.client.gui.pane.GamePlayingPane;
import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.HitObject;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 绘制滑块和长条的 runnable 类，实现了 {@link Runnable}
 *
 * @author .torrent
 * @version 1.0.0 2020/12/8
 */
public class HitObjectThread implements Runnable {
    private final GamePlayingPane gamePlayingPane;
    private final HitObject hitObject;
    private final QualityLabel keyImageLabel;
    private final QualityLabel currentLineLabel;
    private ScheduledExecutorService scheduledThreadPoolExecutor;


    public HitObjectThread(GamePlayingPane gamePlayingPane, HitObject hitObject, QualityLabel keyImageLabel) {
        this.gamePlayingPane = gamePlayingPane;
        this.hitObject = hitObject;
        this.keyImageLabel = keyImageLabel;
        this.currentLineLabel = gamePlayingPane.getLineImageLabels()[hitObject.getIndexX()];
    }

    public HitObject getHitObject() {
        return hitObject;
    }

    @Override
    public void run() {
        //提前在画面上初始化key，位置正好在可视区域的上方隐藏区域
        scheduledThreadPoolExecutor = Executors.newSingleThreadScheduledExecutor();
        keyImageLabel.setBounds((currentLineLabel.getLocation().x + gamePlayingPane.getLineBoldWidth()),
                gamePlayingPane.getY() - keyImageLabel.getIcon().getIconHeight(),
                keyImageLabel.getIcon().getIconWidth(),
                keyImageLabel.getIcon().getIconHeight());
        gamePlayingPane.add(keyImageLabel, JLayeredPane.DEFAULT_LAYER, 0);
            /*
            计算真正的开始动画的时间
            公式含义：
             gamePlayingPane.getStartTime() (音乐启动时间)
             + GameConfig.getHitBoxShowDelay() (Config中设置的画面相对音乐延迟的时间，一般为负数，也就是提前)
             + hitObject.getStartTime() (音符滑块到达判定线的相对时间，相对的是真正的音乐开始播放的时间，也就是前两项之和；在谱面文件中写死)
             - gamePlayingPane.getKeyShowAdvancedMillis() (音符滑块提前显示的毫秒数，也就是音符滑块从顶部下落到判定线所需要的时间，我们需要提前这么久开始让滑块显示在画面上)
             + GameConfig.getMillisPerTick() 每tick相隔的毫秒数，因为已经在可视区域的正上方隐藏区域提前初始化好了音符滑块，这算做第一次tick，所以实际第一次tick
             的时候是理论上第二次tick，我们需要在理论上开始动画的时间点的后一次tick真正开始动画
             - System.currentTimeMillis() 当前的时间，一般都比真正开始绘制动画的时间要前，所以这里要校准一下，算出延迟启动的时间。
             为什么一般都比真正开始绘制动画的时间要前呢？因为在start和真正执行此线程run方法之间可能有延迟，所以一般会提前start。
             当然，不提前start也是可以的，在后面initialDelay作为参数的时候，会有个三元运算，如果initialDelay小于0，则设置延迟为0，以确保不会出现延迟为负数而线程无法启动的情况
            */
        long initialDelay =
                gamePlayingPane.getStartTime() + GameConfig.getHitBoxShowDelay() + hitObject.getStartTime() - gamePlayingPane.getKeyShowAdvancedMillis() + GameConfig.getMillisPerTick() - System.currentTimeMillis();
        scheduledThreadPoolExecutor.scheduleAtFixedRate(
                () -> {
                    if (keyImageLabel.getY() <= gamePlayingPane.getHeight()) {
                        keyImageLabel.setLocation(keyImageLabel.getX(),
                                keyImageLabel.getY() + GameConfig.getPixelsPerTick());
//                            if (keyImageLabel.getY() == hitAreaY - keyImageLabel.getHeight()) {
//                                System.out.println(System.currentTimeMillis() - startTime - hitObject.getStartTime());
//                            }
                    } else {
                        gamePlayingPane.remove(keyImageLabel);
                        scheduledThreadPoolExecutor.shutdown();
                    }
                },
                initialDelay >= 0 ? initialDelay : 0, /*如果initialDelay小于0，则设置延迟为0，以确保不会出现延迟为负数而线程无法启动的情况*/
                GameConfig.getMillisPerTick(),
                TimeUnit.MILLISECONDS
        );
            /*
            @TODO 目前是直接设置keyImageLabel的location，后面可以考虑加入一个缓冲环节，也就是不使用QualityLabel，而是再建立一个新的
              JLabel类，新类中有X、Y坐标属性。每Tick只改变keyImageLabel的X、Y属性，然后外部再做一个统一的刷新画面的线程，每Tick将画面中
              所有的keyImageLabel的坐标设置为其X、Y属性。
            */
    }
}
