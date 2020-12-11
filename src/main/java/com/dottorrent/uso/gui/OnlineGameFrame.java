package com.dottorrent.uso.gui;

import com.dottorrent.uso.gui.pane.GamePlayingPane;
import com.dottorrent.uso.gui.pane.MusicSelectingPane;
import com.dottorrent.uso.service.Music;

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
        this.setContentPane(new MusicSelectingPane());
        if(!this.isVisible()) {
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        this.getContentPane().setVisible(true);
        this.repaint();
    }

    public void enterGamePlayingPane(Music music){
        if(this.getContentPane()!=defaultPane&&this.getContentPane()!=null) {
            oldPane=this.getContentPane();
            oldPane.setVisible(false);
        }
        this.setContentPane(new GamePlayingPane(music));
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
