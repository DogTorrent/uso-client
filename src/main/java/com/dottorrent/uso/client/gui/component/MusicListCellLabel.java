package com.dottorrent.uso.client.gui.component;

import com.dottorrent.uso.client.service.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * 继承了 {@link JLabel}, 提供更好看的谱面列表 cell 和更多的自定义选项, 谱面列表组件见 {@link MusicList}
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

    public MusicListCellLabel() {
        this(GameConfig.getHighQuality());
    }

    public MusicListCellLabel(boolean isHighQuality) {
        super();
        textColor = getForeground();
        this.isHighQuality = isHighQuality;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * 设置文字渲染时的横向偏移量
     *
     * @param textOffsetX 文字 X 方向偏移量
     */
    public void setTextOffsetX(int textOffsetX) {
        this.textOffsetX = textOffsetX;
    }

    /**
     * 设置文字渲染时的纵向偏移量
     *
     * @param textOffsetY 文字 Y 方向偏移量
     */
    public void setTextOffsetY(int textOffsetY) {
        this.textOffsetY = textOffsetY;
    }

    /**
     * 设置图片渲染时的横向偏移量
     *
     * @param iconOffsetX 图片渲染时 X 方向的偏移量
     */
    public void setIconOffsetX(int iconOffsetX) {
        this.iconOffsetX = iconOffsetX;
    }

    /**
     * 设置图片渲染时的横向偏移量
     *
     * @param iconOffsetY 图片渲染时 Y 方向的偏移量
     */
    public void setIconOffsetY(int iconOffsetY) {
        this.iconOffsetY = iconOffsetY;
    }


    /**
     * 设置文字在图片上方时的左右保护距离，文字将不会被渲染到图片左右保护范围以外的区域，如果文字长度溢出，则会将多出部分渲染为三个点
     *
     * @param iconLeftProtectWidth  文字在图片上方时的左侧保护距离
     * @param iconRightProtectWidth 文字在图片上方时的右侧保护距离
     */
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
        if (isHighQuality) {
            // 高质量
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        Icon icon = getIcon();
        int iconPositionX = (getWidth() - icon.getIconWidth());
        int iconPositionY = (getHeight() - icon.getIconHeight());
        int iconPaintTextAreaWidth = icon.getIconWidth() - iconLeftProtectWidth - iconRightProtectWidth;
        icon.paintIcon(null, g, iconPositionX + iconOffsetX, iconPositionY + iconOffsetY);

        // Paint text
        if (isHighQuality) {
            // 高质量
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
        FontMetrics fm = getFontMetrics(getFont());

        String text = getText();
        //检测文字长度，如果文字长度溢出，则会将多出部分渲染为三个点
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
                    iconPositionX + iconLeftProtectWidth + (iconPaintTextAreaWidth - fm.stringWidth(text)) / 2;
            textPositionY = (getHeight() + textPositionY) / 2;
        }

        g.setFont(getFont());
        g.drawString(text, textPositionX + textOffsetX, textPositionY + textOffsetY);
    }
}
