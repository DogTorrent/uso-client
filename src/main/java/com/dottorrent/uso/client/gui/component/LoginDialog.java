/*
 * Created by JFormDesigner on Sun Dec 13 14:50:10 CST 2020
 */

package com.dottorrent.uso.client.gui.component;

import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.User;
import com.dottorrent.uso.client.service.manager.UserManager;

import javax.swing.*;
import java.awt.*;

/**
 * 登录弹窗，继承了 {@link JDialog}，提供跳转到注册弹窗 {@link RegisterDialog} 的按钮
 *
 * @author .torrent
 * @see java.awt.Dialog
 */
public class LoginDialog extends JDialog {
    double scalingFactor;
    private User user = null;
    private JLayeredPane dialogPane;
    private JLabel userIDLabel;
    private JTextField userIDField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JPanel buttonBar;
    private QualityButton registerButton;
    private JPanel notifyPanel;
    private QualityLabel notifyLabel;
    private QualityButton cancelButton;
    private QualityButton loginButton;
    private ImageIcon bgImageIcon;
    private QualityLabel bgImageLabel;

    /**
     * 初始化登录界面，只初始化不会显示，需要进一步调用 {@link #setVisible(boolean)} 方法才能显示并阻塞.
     * <p>
     * 不带缩放比例参数，会默认调用 {@link GameConfig#getScalingFactor()} 方法
     *
     * @param owner 所属窗口
     * @see #LoginDialog(Window, double)
     */
    public LoginDialog(Window owner) {
        this(owner, GameConfig.getScalingFactor());
    }

    /**
     * 初始化登录界面，只初始化不会显示，需要进一步调用 {@link #setVisible(boolean)} 方法才能显示并阻塞.
     *
     * @param owner         所属窗口
     * @param scalingFactor 缩放参数
     */
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

    /**
     * 显示一个登录窗口并通过 {@link #setModal(boolean)} 方法阻塞
     *
     * @param owner 所属窗口
     * @return 返回获取到的用户类，如果登陆失败则会返回 null
     */
    public static User showLoginDialog(Window owner) {
        LoginDialog loginDialog = new LoginDialog(owner, 0.5);
        loginDialog.setVisible(true);
        return loginDialog.user;
    }

    /**
     * 初始化组件
     */
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
        notifyPanel = new JPanel();
        notifyLabel = new QualityLabel();

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
        setBackground(new Color(0, 0, 0, 0));
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
                registerButton.setText("注册");
                buttonBar.add(registerButton);

                //---- notifyPanel && notifyLabel ----
                notifyPanel.setOpaque(false);
                notifyPanel.add(notifyLabel);
                notifyLabel.setForeground(Color.WHITE);
                buttonBar.add(notifyPanel);

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
                    dialogPane.getPreferredSize().height - buttonBar.getPreferredSize().height - (int) (20 * scalingFactor));
            dialogPane.add(buttonBar, JLayeredPane.DEFAULT_LAYER);
        }
        this.setContentPane(dialogPane);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * 初始化 listener
     */
    private void initListeners() {
        loginButton.addActionListener(e -> {
            String userID = userIDField.getText();
            String password = String.valueOf(passwordField.getPassword());
            //目前只支持数字ID，在这里做一个正则判断
            //@TODO 修改数据库代码、服务端代码和网络通信相关的代码以支持其他形式的ID
            //@TODO 文字字号略小，后期做一个自适应字号
            if (userID.length() < 1 || userID.contains(" ") || (!userID.matches("([0-9])+")) ||
                    password.length() < 1 || password.contains(" ")) {
                notifyLabel.setForeground(Color.RED);
                notifyLabel.setText("输入有误");
            } else {
                //调用UserManager的login方法，尝试网络登录
                //@TODO 超时判断和登陆中的动画
                user = UserManager.login(userIDField.getText(), String.valueOf(passwordField.getPassword()));
                if (user != null) {
                    notifyLabel.setForeground(Color.WHITE);
                    notifyLabel.setText("登陆成功");
                    LoginDialog.this.dispose();
                } else {
                    notifyLabel.setForeground(Color.RED);
                    notifyLabel.setText("登陆失败");
                }
            }
        });
        cancelButton.addActionListener(e -> LoginDialog.this.dispose());
        //点击跳转到注册弹窗
        registerButton.addActionListener(e -> {
            this.dispose();
            RegisterDialog.showRegisterDialog(this.getOwner());
        });
    }
}
