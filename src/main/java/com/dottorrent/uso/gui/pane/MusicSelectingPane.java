/*
 * Created by JFormDesigner on Thu Nov 26 15:25:41 CST 2020
 */

package com.dottorrent.uso.gui.pane;

import com.dottorrent.uso.gui.LocalGameFrame;
import com.dottorrent.uso.gui.component.MusicList;
import com.dottorrent.uso.gui.component.QualityButton;
import com.dottorrent.uso.gui.component.QualityLabel;
import com.dottorrent.uso.service.GameConfig;
import com.dottorrent.uso.service.Music;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Brainrain
 */
public class MusicSelectingPane extends JLayeredPane {
    private ImageIcon bgImageIcon;
    private ImageIcon backImageIcon;
    private ImageIcon topBarImageIcon;
    private ImageIcon bottomBarImageIcon;
    private ImageIcon musicInfoImageIcon;
    private ImageIcon playButtonImageIcon;
    private ImageIcon playButtonOnMovedIcon;
    private double scalingFactor;
    private JScrollPane musicListScrollPane;
    private MusicList musicList;
    private QualityLabel bgImageLabel;
    private QualityLabel topBarImageLabel;
    private QualityLabel bottomBarImageLabel;
    private QualityLabel musicInfoImageLabel;
    private QualityButton playButton;

    public MusicSelectingPane() {
        this(GameConfig.getScalingFactor());
    }
    public MusicSelectingPane(double scalingFactor) {
        this.scalingFactor = scalingFactor;

        //---- bgImageIcon ----
        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                Image.SCALE_SMOOTH));

        //---- musicInfoImageIcon ----
        musicInfoImageIcon=new ImageIcon(getClass().getResource("/pictures/music_info.png"));
        musicInfoImageIcon.setImage(musicInfoImageIcon.getImage().getScaledInstance(
                (int) (musicInfoImageIcon.getIconWidth() * scalingFactor),
                (int) (musicInfoImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));

        //---- playButtonImageIcon && playButtonOnMovedIcon ----
        try {
            BufferedImage playButtonImage = ImageIO.read(getClass().getResource("/pictures/play.png"));
            int width=playButtonImage.getWidth()/2;
            int height=playButtonImage.getHeight();
            playButtonImageIcon=new ImageIcon(playButtonImage.getSubimage(0,0,width,height));
            playButtonImageIcon.setImage(playButtonImageIcon.getImage().getScaledInstance(
                    (int) (playButtonImageIcon.getIconWidth() * scalingFactor),
                    (int) (playButtonImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
            playButtonOnMovedIcon=new ImageIcon(playButtonImage.getSubimage(width,0,width,height));
            playButtonOnMovedIcon.setImage(playButtonOnMovedIcon.getImage().getScaledInstance(
                    (int) (playButtonOnMovedIcon.getIconWidth() * scalingFactor),
                    (int) (playButtonOnMovedIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        //---- topBarImageIcon && bottomBarImageIcon ----
        try {
            BufferedImage barImage = ImageIO.read(getClass().getResource("/pictures/bar.png"));
            int width=barImage.getWidth();
            int height=barImage.getHeight()/2;
            topBarImageIcon=new ImageIcon(barImage.getSubimage(0,0,width,height));
            topBarImageIcon.setImage(topBarImageIcon.getImage().getScaledInstance(
                    (int) (topBarImageIcon.getIconWidth() * scalingFactor),
                    (int) (topBarImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
            bottomBarImageIcon=new ImageIcon(barImage.getSubimage(0,height,width,height));
            bottomBarImageIcon.setImage(bottomBarImageIcon.getImage().getScaledInstance(
                    (int) (bottomBarImageIcon.getIconWidth() * scalingFactor),
                    (int) (bottomBarImageIcon.getIconHeight() * scalingFactor),
                    Image.SCALE_SMOOTH));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        topBarImageLabel=new QualityLabel();
        bottomBarImageLabel=new QualityLabel();
        musicInfoImageLabel=new QualityLabel();
        musicListScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        musicList = new MusicList(scalingFactor);
        bgImageLabel = new QualityLabel();
        playButton=new QualityButton();
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

        //======== this ========
        this.setPreferredSize(new Dimension((int) (1920 * scalingFactor), (int) (1080 * scalingFactor)));

        //---- musicInfoImageLabel ----
        musicInfoImageLabel.setVisible(false);
        musicInfoImageLabel.setIcon(musicInfoImageIcon);
        musicInfoImageLabel.setSize(musicInfoImageIcon.getIconWidth(), musicInfoImageIcon.getIconHeight());
        musicInfoImageLabel.setPreferredSize(new Dimension(musicInfoImageIcon.getIconWidth(),
            musicInfoImageIcon.getIconHeight()));
        musicInfoImageLabel.setLocation(getPreferredSize().width/4,
                (getPreferredSize().height-musicInfoImageIcon.getIconHeight())/2);
        musicInfoImageLabel.setHorizontalTextPosition(SwingConstants.LEFT);
        musicInfoImageLabel.setVerticalTextPosition(SwingConstants.TOP);
        musicInfoImageLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32*scalingFactor)));
        musicInfoImageLabel.setForeground(new Color(200, 200, 200));

        //---- playButton ----
        playButton.setIcon(playButtonImageIcon);
        playButton.setRolloverIcon(playButtonOnMovedIcon);
        playButton.setBounds((int) (musicInfoImageLabel.getX()+musicInfoImageIcon.getIconWidth()-playButtonImageIcon.getIconWidth()/2-10*scalingFactor),
                (int) (musicInfoImageLabel.getY()+musicInfoImageIcon.getIconHeight()-playButtonImageIcon.getIconHeight()/2-10*scalingFactor),
                playButtonImageIcon.getIconWidth(),
                playButtonImageIcon.getIconHeight());
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setVisible(false);
        playButton.addActionListener(e -> {
            ((LocalGameFrame)(this.getRootPane().getParent())).enterGamePlayingPane((Music) (musicList.getSelectedValue()));
        });

        //---- barImageLabel ----
        topBarImageLabel.setIcon(topBarImageIcon);
        bottomBarImageLabel.setIcon(bottomBarImageIcon);
        topBarImageLabel.setBounds(0,0, topBarImageIcon.getIconWidth(), topBarImageIcon.getIconHeight());
        bottomBarImageLabel.setBounds(0,getPreferredSize().height-bottomBarImageIcon.getIconHeight(),
                bottomBarImageIcon.getIconWidth(), bottomBarImageIcon.getIconHeight());
        this.add(topBarImageLabel);
        this.add(bottomBarImageLabel);

        //======== musicListScrollPane ========
        musicListScrollPane.setViewportView(musicList);
        musicListScrollPane.setBounds(getPreferredSize().width/3*2,topBarImageLabel.getHeight(),
                getPreferredSize().width/3, (int) (getPreferredSize().height-topBarImageLabel.getHeight()-95*scalingFactor));
        musicListScrollPane.setOpaque(false);
        musicListScrollPane.getViewport().setOpaque(false);
        musicListScrollPane.setBorder(BorderFactory.createEmptyBorder());
        musicListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        musicListScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                c.setPreferredSize(new Dimension(1,0));
                return c.getPreferredSize();
            }
        });
        {
            musicList.setCellSize(musicListScrollPane.getPreferredSize().width,0);
            musicList.addListSelectionListener(e -> {
                showMusicInfo((Music)musicList.getSelectedValue());
                changeBgImage((Music)musicList.getSelectedValue());
            });
        }
        musicList.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                bgImageMovedWithMouse(e);
            }
        });
        this.add(musicListScrollPane, JLayeredPane.DEFAULT_LAYER);

        //---- bgImageLabel ----
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
        bgImageLabel.setLocation(0,0);
        bgImageLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                bgImageMovedWithMouse(e);
            }
        });
        this.add(bgImageLabel, JLayeredPane.DEFAULT_LAYER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void bgImageMovedWithMouse(MouseEvent e) {
        int moveX = e.getXOnScreen() - this.getLocationOnScreen().x;
        int moveY = e.getYOnScreen() - this.getLocationOnScreen().y;
        int redundantX = bgImageIcon.getIconWidth() - this.getWidth();
        int redundantY = bgImageIcon.getIconHeight() - this.getHeight();
        float proportionX = (float) redundantX / this.getWidth();
        float proportionY = (float) redundantY / this.getHeight();
        int destX = (int) (proportionX * moveX) - redundantX;
        int destY = (int) (proportionY * moveY) - redundantY;
        if (destX > 0) {
            destX = 0;
        } else if (destX < -redundantX) {
            destX = -redundantX;
        }
        if (destY > 0) {
            destY = 0;
        } else if (destY < -redundantY) {
            destY = -redundantY;
        }
        bgImageLabel.setLocation(-redundantX - destX, -redundantY - destY);
    }

    private void showMusicInfo(Music music){
        musicInfoImageLabel.setText("<html>" +
                "<head>" +
                "<meta charset=\"utf-8\"/>" +
                "<style type=\"text/css\">" +
                ".body { " +
                "width:" + (int) (musicInfoImageLabel.getWidth() - 20 * scalingFactor) + "px;" +
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
                "font-size: " + (int) (28*scalingFactor) + "px;" +
                "}" +
                ".headLine { " +
                "display:block;" +
                "overflow:hidden;" +
                "text-overflow:ellipsis;" +
                "white-space:nowrap; " +
                "font-size: " + (int) (38*scalingFactor) + "px;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body class=\"body\">" +
                "<h1 class=\"headLine\">" + music.getTitle() + "</h1>" +
                "<p class=\"textLine\">" + "<b>Version : </b>" + music.getVersion() + "</p>" +
                "<p class=\"textLine\">" + "<b>Artist : </b>" + music.getArtist() + "</p>" +
                "<p class=\"textLine\">" + "<b>Creator : </b>" + music.getCreator() + "</p>" +
                "<p class=\"textLine\">" + "<b>Difficulty : </b>" + music.getDifficulty() + "</p>" +
                "</body>" +
                "</html>");
        MusicSelectingPane.this.add(musicInfoImageLabel, JLayeredPane.POPUP_LAYER);
        musicInfoImageLabel.setVisible(true);
        MusicSelectingPane.this.add(playButton, JLayeredPane.POPUP_LAYER);
        playButton.setVisible(true);
    }

    private void changeBgImage(Music music){
        if(music!=null) {
            Path imgPath = music.getBgImagePath();
            if (imgPath.toFile().isFile()) {
                ImageIcon imgIcon = new ImageIcon(imgPath.toString());
                imgIcon.setImage(imgIcon.getImage().getScaledInstance(
                        (int) (1920 * scalingFactor + 96),
                        (int) (1080 * scalingFactor + 54),
                        Image.SCALE_SMOOTH));
                bgImageLabel.setIcon(imgIcon);
                bgImageLabel.repaint();
                return;
            }
        }
        if (!bgImageLabel.getIcon().equals(bgImageIcon)) {
            bgImageLabel.setIcon(bgImageIcon);
            bgImageLabel.repaint();
        }
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.getContentPane().add(new MusicSelectingPane(1));
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.setLocationRelativeTo(null);
    }
}
