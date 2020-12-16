package com.dottorrent.uso.client.gui.component;

import com.dottorrent.uso.client.service.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * 提供一个渲染画质选项的按钮，继承自 {@link JPanel}
 *
 * @author .torrent
 * @version 1.0.0 2020/11/26
 * @see JPanel
 */
public class QualityLabel extends JLabel {
    boolean isHighQuality;

    public QualityLabel() {
        this(GameConfig.getHighQuality());
    }

    public QualityLabel(boolean isHighQuality) {
        this.isHighQuality = isHighQuality;
    }

    /**
     * 设置是否以高画质渲染组件
     *
     * @param isHighQuality 是否以高画质渲染组件
     */
    public void setHighQuality(boolean isHighQuality) {
        this.isHighQuality = isHighQuality;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (isHighQuality) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        super.paintComponent(g);
    }
}
