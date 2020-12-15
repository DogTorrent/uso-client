/*
 * Created by JFormDesigner on Sun Dec 13 14:50:10 CST 2020
 */

package com.dottorrent.uso.client.gui.component;

import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.UserManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author .torrent
 */
public class RegisterDialog extends JDialog {
    double scalingFactor;
    private boolean ifRegSuccess=false;
    private JLayeredPane dialogPane;
    private JLabel userIDLabel;
    private JTextField userIDField;
    private JLabel userNameLabel;
    private JTextField userNameField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private QualityButton loginButton;
    private JPanel notifyPanel;
    private QualityLabel notifyLabel;
    private QualityButton cancelButton;
    private QualityButton registerButton;
    private ImageIcon bgImageIcon;
    private QualityLabel bgImageLabel;

    public RegisterDialog(Window owner) {
        this(owner, GameConfig.getScalingFactor());
    }

    public RegisterDialog(Window owner, double scalingFactor) {
        this.scalingFactor = scalingFactor;
        initComponents();
        initListeners();
    }

    public static void main(String[] args) {
        JFrame tempFrame = new JFrame();
        tempFrame.setPreferredSize(new Dimension(500, 500));
        tempFrame.setSize(tempFrame.getPreferredSize());
        tempFrame.setVisible(true);
        System.out.println(RegisterDialog.showRegisterDialog(tempFrame));

    }

    private void initComponents() {

        dialogPane = new JLayeredPane();
        userIDLabel = new JLabel();
        userIDField = new JTextField();
        userNameLabel = new JLabel();
        userNameField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        buttonBar = new JPanel();
        loginButton = new QualityButton();
        cancelButton = new QualityButton();
        registerButton = new QualityButton();
        bgImageLabel = new QualityLabel();
        notifyPanel = new JPanel();
        notifyLabel=new QualityLabel();

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
            userIDLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
            userIDLabel.setForeground(Color.WHITE);
            userIDLabel.setText("请输入用户ID");
            userIDLabel.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (32 * scalingFactor)));
            userIDLabel.setSize(userIDLabel.getPreferredSize());
            userIDLabel.setLocation((int) (20 * scalingFactor), (int) (20 * scalingFactor));
            dialogPane.add(userIDLabel, JLayeredPane.DEFAULT_LAYER);

            //---- userIDField ----
            userIDField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
            userIDField.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (48 * scalingFactor)));
            userIDField.setSize(userIDField.getPreferredSize());
            userIDField.setLocation((int) (20 * scalingFactor),
                    userIDLabel.getY() + userIDLabel.getHeight());
            dialogPane.add(userIDField, JLayeredPane.DEFAULT_LAYER);

            //---- userNameLabel ----
            userNameLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
            userNameLabel.setForeground(Color.WHITE);
            userNameLabel.setText("请输入用户名");
            userNameLabel.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (32 * scalingFactor)));
            userNameLabel.setSize(userNameLabel.getPreferredSize());
            userNameLabel.setLocation((int) (20 * scalingFactor),
                    dialogPane.getPreferredSize().height / 4 + (int) (20 * scalingFactor));
            dialogPane.add(userNameLabel, JLayeredPane.DEFAULT_LAYER);

            //---- userNameField ----
            userNameField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
            userNameField.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (48 * scalingFactor)));
            userNameField.setSize(userNameField.getPreferredSize());
            userNameField.setLocation((int) (20 * scalingFactor),
                    userNameLabel.getY() + userNameLabel.getHeight());
            dialogPane.add(userNameField, JLayeredPane.DEFAULT_LAYER);

            //---- passwordLabel ----
            passwordLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
            passwordLabel.setForeground(Color.WHITE);
            passwordLabel.setText("请输入密码");
            passwordLabel.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (32 * scalingFactor)));
            passwordLabel.setSize(passwordLabel.getPreferredSize());
            passwordLabel.setLocation((int) (20 * scalingFactor),
                    dialogPane.getPreferredSize().height / 2 + (int) (20 * scalingFactor));
            dialogPane.add(passwordLabel, JLayeredPane.DEFAULT_LAYER);

            //---- passwordField ----
            passwordField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
            passwordField.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (48 * scalingFactor)));
            passwordField.setSize(passwordField.getPreferredSize());
            passwordField.setLocation((int) (20 * scalingFactor),
                    passwordLabel.getY() + passwordLabel.getHeight());
            dialogPane.add(passwordField, JLayeredPane.DEFAULT_LAYER);

            //======== buttonBar ========
            {
                buttonBar.setLayout(new BoxLayout(buttonBar, BoxLayout.X_AXIS));

                //---- loginButton ----
                loginButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
                loginButton.setText("别点我啊！");
                buttonBar.add(loginButton);

                //---- notifyPanel && notifyLabel ----
                notifyPanel.setOpaque(false);
                notifyPanel.add(notifyLabel);
                notifyLabel.setForeground(Color.WHITE);
                buttonBar.add(notifyPanel);

                //---- cancelButton ----
                cancelButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
                cancelButton.setText("取消");
                buttonBar.add(cancelButton);

                //---- registerButton ----
                registerButton.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (24 * scalingFactor)));
                registerButton.setText("注册");
                buttonBar.add(registerButton);
            }
            buttonBar.setOpaque(false);
            buttonBar.setPreferredSize(new Dimension((int) (dialogPane.getPreferredSize().width - 20 * scalingFactor * 2),
                    (int) (48 * scalingFactor)));
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
        registerButton.addActionListener(e -> {
            String userID= userIDField.getText();
            String userName=userNameField.getText();
            String password= String.valueOf(passwordField.getPassword());
            if(userID.length()<1||userID.contains(" ")||(!userID.matches("([0-9])+"))||
                    password.length()<1||password.contains(" ")||
                    userName.length()<1){
                notifyLabel.setForeground(Color.RED);
                notifyLabel.setText("输入有误");
            }else {
                if (UserManager.register(Long.parseLong(userID), userName,password)) {
                    ifRegSuccess=true;
                    notifyLabel.setForeground(Color.WHITE);
                    notifyLabel.setText("注册成功");
                    RegisterDialog.this.dispose();
                }else {
                    notifyLabel.setForeground(Color.RED);
                    notifyLabel.setText("注册失败");
                }
            }
        });
        cancelButton.addActionListener(e -> RegisterDialog.this.dispose());
    }
    public static boolean showRegisterDialog(Window owner){
        RegisterDialog registerDialog=new RegisterDialog(owner,0.5);
        registerDialog.setVisible(true);
        return registerDialog.ifRegSuccess;
    }
}
