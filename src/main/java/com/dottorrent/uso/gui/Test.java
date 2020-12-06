package com.dottorrent.uso.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/30
 */
public class Test {
    BufferedImage buttonImage;
    int a;
    int width;
    int height;
    Object object=new Object();
    private void drawPic(){
        JFrame jFrame=new JFrame();
        jFrame.setSize(500,800);
        jFrame.setVisible(true);
        JLabel jLabel=new JLabel();
        try {
            buttonImage = ImageIO.read(getClass().getResource("/pictures/button.png"));
            width=buttonImage.getWidth()/2;
            height=buttonImage.getHeight();
            buttonImage= buttonImage.getSubimage(0,0,width,height);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        jLabel.setIcon(new ImageIcon(buttonImage));
        jLabel.setSize(width,height);
        jFrame.add(jLabel);
        for(int i=0;i<1;i++) {
            Timer timer = new Timer(5, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (a < 30) {
                        a++;
                    } else {
                        a = 0;
                    }
                    jLabel.setLocation(10,10+a*20);
                }
            });
            timer.start();
        }


    }

    public static void main(String[] args) {
        new Test().drawPic();
    }
}
