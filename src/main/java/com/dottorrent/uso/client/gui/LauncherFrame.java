/*
 * Created by JFormDesigner on Fri Nov 20 23:14:42 CST 2020
 */

package com.dottorrent.uso.client.gui;

import com.dottorrent.uso.client.gui.component.LoginDialog;
import com.dottorrent.uso.client.gui.component.QualityButton;
import com.dottorrent.uso.client.gui.component.QualityLabel;
import com.dottorrent.uso.client.service.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 启动器界面，继承自 {@link JFrame}
 *
 * @author .torrent
 */
public class LauncherFrame extends JFrame {
    private int mouseClickX;
    private int mouseClickY;
    private final ImageIcon bgImageIcon;
    private final ImageIcon mainLogoImageIcon;
    private final ImageIcon subLogoImageIcon;
    private ImageIcon buttonImageIcon;
    private ImageIcon buttonPressedImageIcon;
    private ImageIcon exitButtonImageIcon;
    private ImageIcon exitButtonOnMovedImageIcon;
    private ImageIcon exitButtonPressedImageIcon;
    private ImageIcon settingsButtonImageIcon;
    private ImageIcon settingsButtonOnMovedImageIcon;
    private ImageIcon settingsButtonPressedImageIcon;
    private final double scalingFactor;
    private final JLayeredPane welcomeLayeredPane;
    private final QualityButton exitButton;
    private final QualityButton settingsButton;
    private final QualityButton localModeButton;
    private final QualityButton onlineModeButton;
    private final QualityLabel mainLogoImageLabel;
    private final QualityLabel subLogoImageLabel;
    private final QualityLabel bgImageLabel;

    public LauncherFrame() {
        this(0.5);
    }

    public LauncherFrame(double scalingFactor) {
        this.scalingFactor = scalingFactor;
        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/bg.png"));
        mainLogoImageIcon = new ImageIcon(getClass().getResource("/pictures/logo_main.png"));
        subLogoImageIcon = new ImageIcon(getClass().getResource("/pictures/logo_sub.png"));
        try {
            BufferedImage buttonImage = ImageIO.read(getClass().getResource("/pictures/button.png"));
            int width = buttonImage.getWidth() / 2;
            int height = buttonImage.getHeight();
            buttonImageIcon = new ImageIcon(buttonImage.getSubimage(0, 0, width, height));
            buttonPressedImageIcon = new ImageIcon(buttonImage.getSubimage(width, 0, width, height));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            BufferedImage exitButtonImage = ImageIO.read(getClass().getResource("/pictures/exit.png"));
            int width = exitButtonImage.getWidth() / 3;
            int height = exitButtonImage.getHeight();
            exitButtonImageIcon = new ImageIcon(exitButtonImage.getSubimage(0, 0, width, height));
            exitButtonOnMovedImageIcon = new ImageIcon(exitButtonImage.getSubimage(width, 0, width, height));
            exitButtonPressedImageIcon = new ImageIcon(exitButtonImage.getSubimage(width * 2, 0, width, height));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            BufferedImage settingsButtonImage = ImageIO.read(getClass().getResource("/pictures/settings.png"));
            int width = settingsButtonImage.getWidth() / 3;
            int height = settingsButtonImage.getHeight();
            settingsButtonImageIcon = new ImageIcon(settingsButtonImage.getSubimage(0, 0, width, height));
            settingsButtonImageIcon = new ImageIcon(settingsButtonImage.getSubimage(0, 0, width, height));
            settingsButtonOnMovedImageIcon = new ImageIcon(settingsButtonImage.getSubimage(width, 0, width, height));
            settingsButtonPressedImageIcon = new ImageIcon(settingsButtonImage.getSubimage(width * 2, 0, width, height));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor + 96),
                (int) (bgImageIcon.getIconHeight() * scalingFactor + 54),
                Image.SCALE_SMOOTH));
        mainLogoImageIcon.setImage(mainLogoImageIcon.getImage().getScaledInstance(
                mainLogoImageIcon.getIconWidth(),
                mainLogoImageIcon.getIconHeight(),
                Image.SCALE_SMOOTH));
        subLogoImageIcon.setImage(subLogoImageIcon.getImage().getScaledInstance(
                subLogoImageIcon.getIconWidth(),
                subLogoImageIcon.getIconHeight(),
                Image.SCALE_SMOOTH));
        initImageIconSize(buttonImageIcon);
        initImageIconSize(buttonPressedImageIcon);
        initImageIconSize(exitButtonImageIcon);
        initImageIconSize(exitButtonOnMovedImageIcon);
        initImageIconSize(exitButtonPressedImageIcon);
        initImageIconSize(settingsButtonImageIcon);
        initImageIconSize(settingsButtonOnMovedImageIcon);
        initImageIconSize(settingsButtonPressedImageIcon);
        welcomeLayeredPane = new JLayeredPane();
        exitButton = new QualityButton();
        settingsButton = new QualityButton();
        localModeButton = new QualityButton();
        onlineModeButton = new QualityButton();
        mainLogoImageLabel = new QualityLabel();
        subLogoImageLabel = new QualityLabel();
        bgImageLabel = new QualityLabel();
        initComponents();
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            new LauncherFrame(Double.parseDouble(args[0])).setVisible(true);
        } else {
            new LauncherFrame().setVisible(true);
        }
    }

    /**
     * 初始化 ImageIcon 的大小为图片原尺寸 * scalingFactor
     *
     * @param imageIcon 要被初始化的 ImageIcon
     */
    private void initImageIconSize(ImageIcon imageIcon) {
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
                (int) (imageIcon.getIconWidth() * scalingFactor),
                (int) (imageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
    }

    private void exitButtonActionPerformed(ActionEvent e) {
        System.exit(0);
    }


    /**
     * 拖动窗口时修改窗口位置。由于窗口设置了undecorated，所以需要自行实现拖动窗口的功能。
     *
     * @param e 按键拖动事件
     */
    private void thisMouseDragged(MouseEvent e) {
        int dragX = e.getXOnScreen();
        int dragY = e.getYOnScreen();
        this.setLocation(dragX - mouseClickX, dragY - mouseClickY);
    }

    /**
     * 点击窗口时获取窗口位置，配合 {@link #thisMouseDragged} 使用
     *
     * @param e 按键点击事件
     */
    private void thisMousePressed(MouseEvent e) {
        mouseClickX = e.getXOnScreen() - this.getLocation().x;
        mouseClickY = e.getYOnScreen() - this.getLocation().y;
    }

    /**
     * 背景图片位置随着鼠标移动而移动
     *
     * @param e 鼠标移动事件
     */
    private void bgImageMovedWithMouse(MouseEvent e) {
        try {
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
        } catch (IllegalComponentStateException illegalComponentStateException) {
            System.out.println("Launcher is hidden, stop moving background");
        }
    }

    private void componentMouseEntered(MouseEvent e) {
        e.getComponent().setLocation(e.getComponent().getX(), e.getComponent().getY() - 3);
    }

    private void componentMouseExited(MouseEvent e) {
        e.getComponent().setLocation(e.getComponent().getX(), e.getComponent().getY() + 3);
    }

    private void localModeButtonMouseClicked(MouseEvent e) {
        new GameFrame(this).enterMusicSelectingPane();
        this.setVisible(false);
    }

    /**
     * 进入联机模式的 GameFrame，需要事先登陆
     *
     * @param e 点击 {@link #onlineModeButton} 事件
     */
    private void onlineModeButtonMouseClicked(MouseEvent e) {
        User user = LoginDialog.showLoginDialog(this);
        if (user != null && user.getUserID() != 0) {
            new GameFrame(this, user).enterMusicSelectingPane();
            this.setVisible(false);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        //======== this ========
        this.setPreferredSize(new Dimension((int) (1920 * scalingFactor), (int) (1080 * scalingFactor)));

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (screenWidth <= getPreferredSize().width ||
                screenHeight <= getPreferredSize().height) {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            gd.setFullScreenWindow(this);
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                    getPreferredSize().width + 96,
                    getPreferredSize().height + 54,
                    Image.SCALE_SMOOTH)
            );
        } else {
            setUndecorated(true);
        }
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

        //======== welcomeLayeredPane ========
        {

            //---- exitButton ----
            exitButton.setIcon(exitButtonImageIcon);
            exitButton.setRolloverIcon(exitButtonOnMovedImageIcon);
            exitButton.setPressedIcon(exitButtonPressedImageIcon);
            exitButton.setContentAreaFilled(false);
            exitButton.setBorderPainted(false);
            exitButton.addActionListener(this::exitButtonActionPerformed);
            exitButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            exitButton.setBounds(10, 10, 32, 32);
            welcomeLayeredPane.add(exitButton, JLayeredPane.DEFAULT_LAYER);

            //---- settingsButton ----
            settingsButton.setIcon(settingsButtonImageIcon);
            settingsButton.setRolloverIcon(settingsButtonOnMovedImageIcon);
            settingsButton.setPressedIcon(settingsButtonPressedImageIcon);
            settingsButton.setContentAreaFilled(false);
            settingsButton.setBorderPainted(false);
            settingsButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            settingsButton.addActionListener(e -> {
                new ConfigSettingFrame().setVisible(true);
            });
            settingsButton.setBounds(getPreferredSize().width - 10 - 32, 10, 32, 32);
            welcomeLayeredPane.add(settingsButton, JLayeredPane.DEFAULT_LAYER);

            //---- localModeButton ----
            localModeButton.setText("\u672c\u5730\u6a21\u5f0f");
            localModeButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
            localModeButton.setIcon(buttonImageIcon);
            localModeButton.setBorderPainted(false);
            localModeButton.setContentAreaFilled(false);
            localModeButton.setHorizontalTextPosition(SwingConstants.CENTER);
            localModeButton.setForeground(new Color(219, 219, 219));
            localModeButton.setPressedIcon(buttonPressedImageIcon);
            localModeButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            localModeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    localModeButtonMouseClicked(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    componentMouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    componentMouseExited(e);
                }
            });
            localModeButton.setBounds(getPreferredSize().width / 5,
                    getPreferredSize().height / 5 * 4,
                    180,
                    60);
            welcomeLayeredPane.add(localModeButton, JLayeredPane.DEFAULT_LAYER);

            //---- internetModeButton ----
            onlineModeButton.setText("\u7ebf\u4e0a\u6a21\u5f0f");
            onlineModeButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 22));
            onlineModeButton.setHorizontalTextPosition(SwingConstants.CENTER);
            onlineModeButton.setContentAreaFilled(false);
            onlineModeButton.setBorderPainted(false);
            onlineModeButton.setIcon(buttonImageIcon);
            onlineModeButton.setForeground(new Color(219, 219, 219));
            onlineModeButton.setPressedIcon(buttonPressedImageIcon);
            onlineModeButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            onlineModeButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onlineModeButtonMouseClicked(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    componentMouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    componentMouseExited(e);
                }
            });
            onlineModeButton.setBounds(getPreferredSize().width - getPreferredSize().width / 5 - 180,
                    getPreferredSize().height / 5 * 4,
                    180,
                    60);
            welcomeLayeredPane.add(onlineModeButton, JLayeredPane.DEFAULT_LAYER);

            //---- mainLogoImageLabel ----
            mainLogoImageLabel.setIcon(mainLogoImageIcon);
            mainLogoImageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    componentMouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    componentMouseExited(e);
                }
            });
            mainLogoImageLabel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            mainLogoImageLabel.setSize(mainLogoImageIcon.getIconWidth(), mainLogoImageIcon.getIconHeight());
            mainLogoImageLabel.setLocation((getPreferredSize().width - mainLogoImageLabel.getWidth()) / 2,
                    getPreferredSize().height / 3 - mainLogoImageLabel.getHeight() / 2);
            welcomeLayeredPane.add(mainLogoImageLabel, JLayeredPane.DEFAULT_LAYER);

            //---- subLogoImageLabel ----
            subLogoImageLabel.setIcon(subLogoImageIcon);
            subLogoImageLabel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            subLogoImageLabel.setSize(subLogoImageIcon.getIconWidth(), subLogoImageIcon.getIconHeight());
            subLogoImageLabel.setLocation((getPreferredSize().width - subLogoImageLabel.getWidth()) / 2,
                    getPreferredSize().height / 3 * 2 - subLogoImageLabel.getHeight());
            welcomeLayeredPane.add(subLogoImageLabel, JLayeredPane.DEFAULT_LAYER);

            //---- bgImageLabel ----
            bgImageLabel.setIcon(bgImageIcon);
            bgImageLabel.setSize(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight());
            bgImageLabel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bgImageMovedWithMouse(e);
                }
            });
            if (getWidth() != screenWidth &&
                    getHeight() != screenHeight) {
                bgImageLabel.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        thisMouseDragged(e);
                    }
                });
                bgImageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        thisMousePressed(e);
                    }
                });
            }
            bgImageLabel.setLocation(0, 0);
            welcomeLayeredPane.add(bgImageLabel, JLayeredPane.DEFAULT_LAYER);
        }
        contentPane.add(welcomeLayeredPane);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

}
