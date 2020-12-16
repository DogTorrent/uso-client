package com.dottorrent.uso.client.gui;

import com.dottorrent.uso.client.gui.pane.GamePlayingPane;
import com.dottorrent.uso.client.gui.pane.MusicSelectingPane;
import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.Music;
import com.dottorrent.uso.client.service.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 游戏窗口，调用不同的初始化方法进入不同的模式
 *
 * @author .torrent
 * @version 1.0.0 2020/11/28
 */
public class GameFrame extends JFrame {
    public ImageIcon exitButtonImageIcon;
    public ImageIcon exitButtonOnMovedImageIcon;
    public ImageIcon exitButtonPressedImageIcon;
    public ImageIcon backButtonImageIcon;
    public ImageIcon backButtonOnMovedImageIcon;
    public ImageIcon backButtonPressedImageIcon;
    private Container oldPane;
    private final Container defaultPane;
    private final LauncherFrame launcherFrame;
    private final User user;

    //local
    public GameFrame(LauncherFrame launcherFrame) {
        this(launcherFrame, new User(0, null, null));
    }

    //online
    public GameFrame(LauncherFrame launcherFrame, User user) {
        super();
        this.setUndecorated(true);
        this.defaultPane = this.getContentPane();
        this.launcherFrame = launcherFrame;
        this.user = user;
        initResource();
    }

    private void initResource() {
        //---- exitButtonImageIcon... ----
        try {
            BufferedImage exitButtonImage = ImageIO.read(getClass().getResource("/pictures/exit.png"));
            int width = exitButtonImage.getWidth() / 3;
            int height = exitButtonImage.getHeight();
            exitButtonImageIcon = new ImageIcon(exitButtonImage.getSubimage(0, 0, width, height));
            exitButtonOnMovedImageIcon = new ImageIcon(exitButtonImage.getSubimage(width, 0, width, height));
            exitButtonPressedImageIcon = new ImageIcon(exitButtonImage.getSubimage(width * 2, 0, width, height));
            initImageIconSize(exitButtonImageIcon);
            initImageIconSize(exitButtonOnMovedImageIcon);
            initImageIconSize(exitButtonPressedImageIcon);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //---- backButtonImageIcon... ----
        try {
            BufferedImage backButtonImage = ImageIO.read(getClass().getResource("/pictures/back.png"));
            int width = backButtonImage.getWidth() / 3;
            int height = backButtonImage.getHeight();
            backButtonImageIcon = new ImageIcon(backButtonImage.getSubimage(0, 0, width, height));
            backButtonOnMovedImageIcon = new ImageIcon(backButtonImage.getSubimage(width, 0, width, height));
            backButtonPressedImageIcon = new ImageIcon(backButtonImage.getSubimage(width * 2, 0, width, height));
            initImageIconSize(backButtonImageIcon);
            initImageIconSize(backButtonOnMovedImageIcon);
            initImageIconSize(backButtonPressedImageIcon);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void initImageIconSize(ImageIcon imageIcon) {
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
                (int) (imageIcon.getIconWidth() * GameConfig.getScalingFactor()),
                (int) (imageIcon.getIconHeight() * GameConfig.getScalingFactor()),
                Image.SCALE_SMOOTH));
    }

    /**
     * 进入 MusicSelectingPane
     */
    public void enterMusicSelectingPane() {
        if (this.getContentPane() != defaultPane && this.getContentPane() != null) {
            oldPane = this.getContentPane();
            oldPane.setVisible(false);
            oldPane.removeAll();
        }
        this.setContentPane(new MusicSelectingPane(user, this));
        if (!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    /**
     * 进入 GamePlayingPane
     */
    public void enterGamePlayingPane(Music music) {
        if (this.getContentPane() != defaultPane && this.getContentPane() != null) {
            oldPane = this.getContentPane();
            oldPane.setVisible(false);
            oldPane.removeAll();
        }
        this.setContentPane(new GamePlayingPane(music, user, this));
        if (!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    public void backToTitle() {
        launcherFrame.setVisible(true);
        this.dispose();
    }

    public Container getOldPane() {
        return oldPane;
    }
}
