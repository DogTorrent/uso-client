package com.dottorrent.uso.client.gui;

import com.dottorrent.uso.client.gui.pane.GamePlayingPane;
import com.dottorrent.uso.client.gui.pane.MusicSelectingPane;
import com.dottorrent.uso.client.service.Music;
import com.dottorrent.uso.client.service.User;

import javax.swing.*;
import java.awt.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/12/12
 */
public class OnlineGameFrame extends JFrame {
    private Container oldPane;
    private Container defaultPane;
    private LauncherFrame launcherFrame;

    public OnlineGameFrame(LauncherFrame launcherFrame){
        super();
        this.setUndecorated(true);
        this.defaultPane=this.getContentPane();
        this.launcherFrame=launcherFrame;
    }
    public void enterMusicSelectingPane(){
        if(this.getContentPane()!=defaultPane&&this.getContentPane()!=null) {
            oldPane=this.getContentPane();
            this.setVisible(false);
        }
        this.setContentPane(new MusicSelectingPane(new User(0,null,null)));
        if(!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    public void enterGamePlayingPane(Music music, User user){
        if(this.getContentPane()!=defaultPane&&this.getContentPane()!=null) {
            oldPane=this.getContentPane();
            oldPane.setVisible(false);
        }
        this.setContentPane(new GamePlayingPane(music,user));
        if(!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    public void exitFrame(){
        launcherFrame.pack();
        this.dispose();
    }

    public Container getOldPane() {
        return oldPane;
    }
}
