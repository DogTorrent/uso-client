/*
 * Created by JFormDesigner on Sun Dec 13 14:50:10 CST 2020
 */

package com.dottorrent.uso.client.gui.component;

import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Brainrain
 */
public class LoginDialog extends JDialog {
    double scalingFactor;
    private User user=null;
    private JLayeredPane dialogPane;
    private JLabel userIDLabel;
    private JTextField userIDField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private QualityButton registerButton;
    private QualityButton cancelButton;
    private QualityButton loginButton;
    private ImageIcon bgImageIcon;
    private QualityLabel bgImageLabel;

    public LoginDialog(Window owner) {
        this(owner, GameConfig.getScalingFactor());
    }

    public LoginDialog(Window owner, double scalingFactor) {
        this.scalingFactor = scalingFactor;
        initComponents();
        initListeners();
    }

    public static void main(String[] args) {
        JFrame tempFrame = new JFrame();
        tempFrame.setPreferredSize(new Dimension(500, 500));
        tempFrame.setSize(tempFrame.getPreferredSize());
        tempFrame.setVisible(true);
        System.out.println(LoginDialog.showLoginDialog(tempFrame));

    }

    private void initComponents() {

        dialogPane = new JLayeredPane();
        userIDLabel = new JLabel();
        userIDField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        registerButton = new QualityButton();
        cancelButton = new QualityButton();
        loginButton = new QualityButton();
        bgImageLabel = new QualityLabel();

        //---- bgImageIcon && bgImageLabel ----
        bgImageIcon = new ImageIcon(getClass().getResource("/pictures/popup_label_bg.png"));
        bgImageIcon.setImage(bgImageIcon.getImage().getScaledInstance(
                (int) (bgImageIcon.getIconWidth() * scalingFactor),
                (int) (bgImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        bgImageLabel.setIcon(bgImageIcon);
        bgImageLabel.setPreferredSize(new Dimension(bgImageIcon.getIconWidth(), bgImageIcon.getIconHeight()));
        bgImageLabel.setSize(bgImageLabel.getPreferredSize());

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        setResizable(false);
        setBackground(new Color(0,0,0,0));
        setPreferredSize(bgImageLabel.getPreferredSize());
        setSize(getPreferredSize());

        //======== dialogPane ========
        {
            dialogPane.setPreferredSize(getPreferredSize());
            dialogPane.setSize(getPreferredSize());
            dialogPane.add(bgImageLabel, JLayeredPane.FRAME_CONTENT_LAYER);

            //---- userIDLabel ----
            userIDLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
            userIDLabel.setForeground(Color.WHITE);
            userIDLabel.setText("请输入用户ID");
            userIDLabel.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (42 * scalingFactor)));
            userIDLabel.setSize(userIDLabel.getPreferredSize());
            userIDLabel.setLocation((int) (20 * scalingFactor), (int) (20 * scalingFactor));
            dialogPane.add(userIDLabel, JLayeredPane.DEFAULT_LAYER);

            //---- userIDField ----
            userIDField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
            userIDField.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (64 * scalingFactor)));
            userIDField.setSize(userIDField.getPreferredSize());
            userIDField.setLocation((int) (20 * scalingFactor),
                    userIDLabel.getY() + userIDLabel.getHeight());
            dialogPane.add(userIDField, JLayeredPane.DEFAULT_LAYER);

            //---- passwordLabel ----
            passwordLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
            passwordLabel.setForeground(Color.WHITE);
            passwordLabel.setText("请输入密码");
            passwordLabel.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (42 * scalingFactor)));
            passwordLabel.setSize(passwordLabel.getPreferredSize());
            passwordLabel.setLocation((int) (20 * scalingFactor),
                    dialogPane.getPreferredSize().height / 3 + (int) (20 * scalingFactor));
            dialogPane.add(passwordLabel, JLayeredPane.DEFAULT_LAYER);

            //---- passwordField ----
            passwordField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
            passwordField.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (64 * scalingFactor)));
            passwordField.setSize(passwordField.getPreferredSize());
            passwordField.setLocation((int) (20 * scalingFactor),
                    passwordLabel.getY() + passwordLabel.getHeight());
            dialogPane.add(passwordField, JLayeredPane.DEFAULT_LAYER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));

                //---- registerButton ----
                registerButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
                registerButton.setText("没有账号？注册");
                buttonBar.add(registerButton);

                //---- fillings ----
                JPanel fillings = new JPanel();
                fillings.setOpaque(false);
                buttonBar.add(fillings);

                //---- cancelButton ----
                cancelButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
                cancelButton.setText("取消");
                buttonBar.add(cancelButton);

                //---- okButton ----
                loginButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
                loginButton.setText("登陆");
                buttonBar.add(loginButton);
            }
            buttonBar.setOpaque(false);
            buttonBar.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (64 * scalingFactor)));
            buttonBar.setSize(buttonBar.getPreferredSize());
            buttonBar.setLocation((int) (20 * scalingFactor),
                    dialogPane.getPreferredSize().height -buttonBar.getPreferredSize().height-(int) (20 * scalingFactor));
            dialogPane.add(buttonBar, JLayeredPane.DEFAULT_LAYER);
        }
        this.setContentPane(dialogPane);
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void initListeners(){
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDialog.this.dispose();
            }
        });
    }
    public static User showLoginDialog(Window owner){
        LoginDialog loginDialog=new LoginDialog(owner);
        loginDialog.setVisible(true);
        return loginDialog.user;
    }
}
