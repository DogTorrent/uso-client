/*
 * Created by JFormDesigner on Tue Dec 15 06:48:12 CST 2020
 */

package com.dottorrent.uso.client.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import javax.swing.*;
import javax.swing.border.*;
import com.dottorrent.uso.client.gui.component.*;
import com.dottorrent.uso.client.service.*;

/**
 * @author .torrent
 */
public class ConfigSettingFrame extends JFrame {
    public ConfigSettingFrame() {
        initComponents();
    }

    private void confirmButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JLayeredPane();
        highQualityCheckBox = new JCheckBox();
        fullScreenCheckBox = new JCheckBox();
        hitDelayLabel = new JLabel();
        hitDelayTextField = new JFormattedTextField();
        hitBoxShowDelayLabel = new JLabel();
        hitBoxShowDelayTextField = new JFormattedTextField();
        millisPerTickLabel = new JLabel();
        millisPerTickTextField = new JFormattedTextField();
        pixelsPerTickLabel = new JLabel();
        pixelsPerTickTextField = new JFormattedTextField();
        userServerUriLabel = new JLabel();
        userServerUriTextField = new JFormattedTextField();
        scoreServerUriLabel = new JLabel();
        scoreServerUriTextField = new JFormattedTextField();
        musicServerUriLabel = new JLabel();
        musicServerUriTextField = new JFormattedTextField();
        cancelButton = new JButton();
        confirmButton = new JButton();
        bgLabel = new QualityLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

            //---- highQualityCheckBox ----
            highQualityCheckBox.setText("\u9ad8\u753b\u8d28 ");
            highQualityCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
            highQualityCheckBox.setContentAreaFilled(false);
            highQualityCheckBox.setForeground(Color.white);
            highQualityCheckBox.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(highQualityCheckBox, JLayeredPane.DEFAULT_LAYER);
            highQualityCheckBox.setBounds(15, 15, 80, 30);
            highQualityCheckBox.setSelected(GameConfig.getHighQuality());

            //---- fullScreenCheckBox ----
            fullScreenCheckBox.setText("\u5168\u5c4f\u6a21\u5f0f ");
            fullScreenCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
            fullScreenCheckBox.setContentAreaFilled(false);
            fullScreenCheckBox.setForeground(Color.white);
            fullScreenCheckBox.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(fullScreenCheckBox, JLayeredPane.DEFAULT_LAYER);
            fullScreenCheckBox.setBounds(100, 15, 80, 30);
            fullScreenCheckBox.setSelected(Toolkit.getDefaultToolkit().getScreenSize().width/1920.0==GameConfig.getScalingFactor());

            //---- hitDelayLabel ----
            hitDelayLabel.setText("\u6309\u952e\u6ede\u540e\u6beb\u79d2");
            hitDelayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            hitDelayLabel.setForeground(Color.white);
            hitDelayLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(hitDelayLabel, JLayeredPane.DEFAULT_LAYER);
            hitDelayLabel.setBounds(15, 50, 80, 30);

            //---- hitDelayTextField ----
            hitDelayTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            hitDelayTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(hitDelayTextField, JLayeredPane.DEFAULT_LAYER);
            hitDelayTextField.setBounds(100, 50, 80, 30);
            hitDelayTextField.setText(String.valueOf(GameConfig.getHitDelay()));

            //---- hitBoxShowDelayLabel ----
            hitBoxShowDelayLabel.setText("\u663e\u793a\u6ede\u540e\u6beb\u79d2");
            hitBoxShowDelayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            hitBoxShowDelayLabel.setForeground(Color.white);
            hitBoxShowDelayLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(hitBoxShowDelayLabel, JLayeredPane.DEFAULT_LAYER);
            hitBoxShowDelayLabel.setBounds(15, 85, 80, 30);

            //---- hitBoxShowDelayTextField ----
            hitBoxShowDelayTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            hitBoxShowDelayTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(hitBoxShowDelayTextField, JLayeredPane.DEFAULT_LAYER);
            hitBoxShowDelayTextField.setBounds(100, 85, 80, 30);
            hitBoxShowDelayTextField.setText(String.valueOf(GameConfig.getHitBoxShowDelay()));

            //---- millisPerTickLabel ----
            millisPerTickLabel.setText("\u79fb\u52a8\u95f4\u9694\u6beb\u79d2");
            millisPerTickLabel.setHorizontalAlignment(SwingConstants.CENTER);
            millisPerTickLabel.setForeground(Color.white);
            millisPerTickLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(millisPerTickLabel, JLayeredPane.DEFAULT_LAYER);
            millisPerTickLabel.setBounds(15, 120, 80, 30);

            //---- millisPerTickTextField ----
            millisPerTickTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            millisPerTickTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(millisPerTickTextField, JLayeredPane.DEFAULT_LAYER);
            millisPerTickTextField.setBounds(100, 120, 80, 30);
            millisPerTickTextField.setText(String.valueOf(GameConfig.getMillisPerTick()));

            //---- pixelsPerTickLabel ----
            pixelsPerTickLabel.setText("\u79fb\u52a8\u95f4\u9694\u50cf\u7d20");
            pixelsPerTickLabel.setHorizontalAlignment(SwingConstants.CENTER);
            pixelsPerTickLabel.setForeground(Color.white);
            pixelsPerTickLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(pixelsPerTickLabel, JLayeredPane.DEFAULT_LAYER);
            pixelsPerTickLabel.setBounds(15, 155, 80, 30);

            //---- pixelsPerTickTextField ----
            pixelsPerTickTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            pixelsPerTickTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(pixelsPerTickTextField, JLayeredPane.DEFAULT_LAYER);
            pixelsPerTickTextField.setBounds(100, 155, 80, 30);
            pixelsPerTickTextField.setText(String.valueOf(GameConfig.getPixelsPerTick()));

            //---- userServerUriLabel ----
            userServerUriLabel.setText("\u8d26\u6237\u670d\u52a1\u5730\u5740");
            userServerUriLabel.setHorizontalAlignment(SwingConstants.CENTER);
            userServerUriLabel.setForeground(Color.white);
            userServerUriLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(userServerUriLabel, JLayeredPane.DEFAULT_LAYER);
            userServerUriLabel.setBounds(200, 50, 80, 30);

            //---- userServerUriTextField ----
            userServerUriTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            userServerUriTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(userServerUriTextField, JLayeredPane.DEFAULT_LAYER);
            userServerUriTextField.setBounds(285, 50, 175, 30);
            userServerUriTextField.setText(String.valueOf(GameConfig.getUserServerUri()));

            //---- scoreServerUriLabel ----
            scoreServerUriLabel.setText("\u5206\u6570\u670d\u52a1\u5730\u5740");
            scoreServerUriLabel.setHorizontalAlignment(SwingConstants.CENTER);
            scoreServerUriLabel.setForeground(Color.white);
            scoreServerUriLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(scoreServerUriLabel, JLayeredPane.DEFAULT_LAYER);
            scoreServerUriLabel.setBounds(200, 85, 80, 30);

            //---- scoreServerUriTextField ----
            scoreServerUriTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            scoreServerUriTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(scoreServerUriTextField, JLayeredPane.DEFAULT_LAYER);
            scoreServerUriTextField.setBounds(285, 85, 175, 30);
            scoreServerUriTextField.setText(String.valueOf(GameConfig.getResultServerUri()));

            //---- musicServerUriLabel ----
            musicServerUriLabel.setText("\u8c31\u9762\u670d\u52a1\u5730\u5740");
            musicServerUriLabel.setHorizontalAlignment(SwingConstants.CENTER);
            musicServerUriLabel.setForeground(Color.white);
            musicServerUriLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(musicServerUriLabel, JLayeredPane.DEFAULT_LAYER);
            musicServerUriLabel.setBounds(200, 120, 80, 30);

            //---- musicServerUriTextField ----
            musicServerUriTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            musicServerUriTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(musicServerUriTextField, JLayeredPane.DEFAULT_LAYER);
            musicServerUriTextField.setBounds(285, 120, 175, 30);
            musicServerUriTextField.setText(String.valueOf(GameConfig.getMusicServerUri()));

            //---- cancelButton ----
            cancelButton.setText("\u53d6\u6d88");
            cancelButton.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            dialogPane.add(cancelButton, JLayeredPane.DEFAULT_LAYER);
            cancelButton.setBounds(new Rectangle(new Point(315, 220), cancelButton.getPreferredSize()));
            cancelButton.addActionListener(e -> {
                dispose();
            });

            //---- confirmButton ----
            confirmButton.setText("\u786e\u8ba4");
            confirmButton.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            confirmButton.addActionListener(e -> confirmButtonActionPerformed(e));
            dialogPane.add(confirmButton, JLayeredPane.DEFAULT_LAYER);
            confirmButton.setBounds(new Rectangle(new Point(400, 220), confirmButton.getPreferredSize()));
            confirmButton.addActionListener(e -> {
                boolean isHighQuality=highQualityCheckBox.isSelected();
                boolean fullScreen=fullScreenCheckBox.isSelected();
                double scalingFactor=0.5;
                if(fullScreen){
                    scalingFactor=Toolkit.getDefaultToolkit().getScreenSize().width/1920.0;
                }
                int millisPerTick = Integer.parseInt(millisPerTickTextField.getText());
                int hitDelay= Integer.parseInt(hitDelayTextField.getText());
                int pixelsPerTick = Integer.parseInt(pixelsPerTickTextField.getText());
                int hitBoxShowDelay= Integer.parseInt(hitBoxShowDelayTextField.getText());
                URI userServerUri = URI.create(userServerUriTextField.getText());
                URI resultServerUri = URI.create(scoreServerUriTextField.getText());
                URI musicServerUri = URI.create(musicServerUriTextField.getText());
                GameConfig.setHighQuality(isHighQuality);
                GameConfig.setScalingFactor(scalingFactor);
                GameConfig.setMillisPerTick(millisPerTick);
                GameConfig.setHitDelay(hitDelay);
                GameConfig.setPixelsPerTick(pixelsPerTick);
                GameConfig.setHitBoxShowDelay(hitBoxShowDelay);
                GameConfig.setUserServerUri(userServerUri);
                GameConfig.setResultServerUri(resultServerUri);
                GameConfig.setMusicServerUri(musicServerUri);
                dispose();
            });

            //---- bgLabel ----
            ImageIcon bgImageIcon=new ImageIcon(getClass().getResource("/pictures/popup_label_bg.png"));
            bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(480,270,Image.SCALE_SMOOTH));
            bgLabel.setIcon(bgImageIcon);
            dialogPane.add(bgLabel, JLayeredPane.DEFAULT_LAYER);
            bgLabel.setBounds(0, 0, 480, 270);
        }
        setUndecorated(true);
        setContentPane(dialogPane);
        setBackground(new Color(0,0,0,0));
        setSize(480, 270);

        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLayeredPane dialogPane;
    private JCheckBox highQualityCheckBox;
    private JCheckBox fullScreenCheckBox;
    private JLabel hitDelayLabel;
    private JFormattedTextField hitDelayTextField;
    private JLabel hitBoxShowDelayLabel;
    private JFormattedTextField hitBoxShowDelayTextField;
    private JLabel millisPerTickLabel;
    private JFormattedTextField millisPerTickTextField;
    private JLabel pixelsPerTickLabel;
    private JFormattedTextField pixelsPerTickTextField;
    private JLabel userServerUriLabel;
    private JFormattedTextField userServerUriTextField;
    private JLabel scoreServerUriLabel;
    private JFormattedTextField scoreServerUriTextField;
    private JLabel musicServerUriLabel;
    private JFormattedTextField musicServerUriTextField;
    private JButton cancelButton;
    private JButton confirmButton;
    private QualityLabel bgLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public static void main(String[] args) {
        ConfigSettingFrame configSettingFrame=new ConfigSettingFrame();
        configSettingFrame.setVisible(true);
    }
}
