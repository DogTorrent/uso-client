package com.dottorrent.uso.gui;

import com.dottorrent.uso.service.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/26
 */
public class QualityLabel extends JLabel {
    boolean isHighQuality;

    public QualityLabel(){
        this(GameConfig.getInstance().getHighQuality());
    }

    public QualityLabel(boolean isHighQuality){
        this.isHighQuality = isHighQuality;
    }

    public void setHighQuality(boolean isHighQuality) {
        this.isHighQuality = isHighQuality;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(isHighQuality) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        super.paintComponent(g);
    }
}
