package com.dottorrent.uso.gui;

import com.dottorrent.uso.gui.pane.GamePlayingPane;
import com.dottorrent.uso.gui.pane.MusicSelectingPane;
import com.dottorrent.uso.service.Music;

import javax.swing.*;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/28
 */
public class LocalGameFrame extends JFrame {
    public LocalGameFrame(){
        super();
        this.setUndecorated(true);
    }
    public void enterMusicSelectingPane(){
        if(this.getContentPane()!=null) {
            this.getContentPane().setVisible(false);
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
        if(this.getContentPane()!=null) {
            this.getContentPane().setVisible(false);
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
}
