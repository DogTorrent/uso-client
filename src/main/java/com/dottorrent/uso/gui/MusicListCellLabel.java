package com.dottorrent.uso.gui;

import com.dottorrent.uso.service.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/25
 */
public class MusicListCellLabel extends JLabel {
    private int textOffsetX, textOffsetY;
    private int iconOffsetX, iconOffsetY;
    private int iconLeftProtectWidth, iconRightProtectWidth;
    private Color textColor;
    private boolean isHighQuality;

    public MusicListCellLabel(){
        this(GameConfig.getHighQuality());
    }

    public MusicListCellLabel(boolean isHighQuality) {
        super();
        textColor = getForeground();
        this.isHighQuality=isHighQuality;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setTextOffsetX(int textOffsetX) {
        this.textOffsetX = textOffsetX;
    }

    public void setTextOffsetY(int textOffsetY) {
        this.textOffsetY = textOffsetY;
    }

    public void setIconOffsetX(int iconOffsetX) {
        this.iconOffsetX = iconOffsetX;
    }

    public void setIconOffsetY(int iconOffsetY) {
        this.iconOffsetY = iconOffsetY;
    }

    public void setIconProtectWidth(int iconLeftProtectWidth, int iconRightProtectWidth) {
        this.iconLeftProtectWidth = iconLeftProtectWidth;
        this.iconRightProtectWidth = iconRightProtectWidth;
    }

    public void setHighQuality(boolean highQuality) {
        isHighQuality = highQuality;
    }

    @Override
    protected void paintComponent(Graphics g) {

        // Paint icon
        if(isHighQuality) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        Icon icon = getIcon();
        int iconPositionX = (getWidth() - icon.getIconWidth());
        int iconPositionY = (getHeight() - icon.getIconHeight());
        int iconPaintTextAreaWidth=icon.getIconWidth()- iconLeftProtectWidth - iconRightProtectWidth;
        icon.paintIcon(null, g, iconPositionX + iconOffsetX, iconPositionY + iconOffsetY);

        // Paint text
        if(isHighQuality) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        FontMetrics fm = getFontMetrics(getFont());

        String text = getText();
        if (iconPaintTextAreaWidth < fm.stringWidth(text)) {
            while (iconPaintTextAreaWidth < fm.stringWidth(text)) {
                text = text.substring(0, text.length() - 1);
            }
            text = text.substring(0, text.length() - 3);
            text += "...";
        }

        int textPositionX = iconLeftProtectWidth;
        int textPositionY = fm.getAscent();
        if (getHorizontalTextPosition() == SwingConstants.CENTER) {
            textPositionX =
                    iconPositionX+iconLeftProtectWidth+(iconPaintTextAreaWidth-fm.stringWidth(text))/2;
            textPositionY = (getHeight() + textPositionY) / 2;
        }

        g.setFont(getFont());
        g.drawString(text, textPositionX + textOffsetX, textPositionY + textOffsetY);
    }
}
