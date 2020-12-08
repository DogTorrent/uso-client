/*
 * Created by JFormDesigner on Thu Nov 26 15:25:41 CST 2020
 */

package com.dottorrent.uso.gui.pane;

import com.dottorrent.uso.gui.component.MusicList;
import com.dottorrent.uso.gui.component.QualityLabel;
import com.dottorrent.uso.service.GameConfig;
import com.dottorrent.uso.service.Music;

import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;

/**
 * @author Brainrain
 */
public class MusicSelectingPane extends JLayeredPane {
    private ImageIcon bgImageIcon;
    private double scalingFactor;
    private JScrollPane musicListScrollPane;
    private MusicList musicList;
    private QualityLabel bgImageLabel;

    public MusicSelectingPane() {
        this(GameConfig.getScalingFactor());
    }
    public MusicSelectingPane(double scalingFactor) {
        this.scalingFactor = scalingFactor;
        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                Image.SCALE_SMOOTH));
        musicListScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        musicList = new MusicList(scalingFactor);
        bgImageLabel = new QualityLabel();
        initComponents();
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
                    (int) (getPreferredSize().width +96),
                    (int) (getPreferredSize().height +54),
                    Image.SCALE_SMOOTH)
            );
        }

        //======== musicListScrollPane ========
        musicListScrollPane.setViewportView(musicList);
        musicListScrollPane.setBounds(getPreferredSize().width/2,0,
                getPreferredSize().width/2,getPreferredSize().height);
        musicListScrollPane.setOpaque(false);
        musicListScrollPane.getViewport().setOpaque(false);
        musicListScrollPane.setBorder(BorderFactory.createEmptyBorder());
        musicListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        musicListScrollPane.getVerticalScrollBar().setUI(new ScrollBarUI() {
            @Override
            public Dimension getPreferredSize(JComponent c) {
                c.setPreferredSize(new Dimension(0,0));
                return c.getPreferredSize();
            }
        });
        {
            musicList.addListSelectionListener(e -> {
                Path imgPath=((Music)(musicList.getSelectedValue())).getBgImagePath();
                if(imgPath.toFile().isFile()){
                    ImageIcon imgIcon=new ImageIcon(imgPath.toString());
                    if (screenWidth <= getPreferredSize().width ||
                            screenHeight <= getPreferredSize().height) {
                        imgIcon.setImage(imgIcon.getImage().getScaledInstance(
                                (int) (getPreferredSize().width +96),
                                (int) (getPreferredSize().height +54),
                                Image.SCALE_SMOOTH)
                        );
                    }else {
                        imgIcon.setImage(imgIcon.getImage().getScaledInstance(
                                (int) (1920 * scalingFactor + 96),
                                (int) (1080 * scalingFactor + 54),
                                Image.SCALE_SMOOTH));
                    }
                    bgImageLabel.setIcon(imgIcon);
                    bgImageLabel.repaint();
                }else{
                    if(bgImageLabel.getIcon()!=bgImageIcon) {
                        bgImageLabel.setIcon(bgImageIcon);
                        bgImageLabel.repaint();
                    }
                }
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

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.getContentPane().add(new MusicSelectingPane());
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.setLocationRelativeTo(null);
    }
}
