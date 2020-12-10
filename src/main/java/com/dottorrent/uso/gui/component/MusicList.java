package com.dottorrent.uso.gui.component;

import com.dottorrent.uso.service.GameConfig;
import com.dottorrent.uso.service.Music;
import com.dottorrent.uso.service.MusicLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Description here
 *
 * @author .torrent
 * @version 1.0.0 2020/11/23
 */
public class MusicList extends JList {
    private LinkedList<Music> songsList;
    private ImageIcon listCellBgImageIcon;
    private ImageIcon listCellBgImageSelectedIcon;
    private int cellHoveredIndex=-1;
    private Dimension cellSize;
    private double scalingFactor;

    public int getCellHoveredIndex(){
        return cellHoveredIndex;
    }

    public MusicList(){
        this(GameConfig.getScalingFactor());
    }

    @Override
    public Object getSelectedValue() {
        return songsList.get(getSelectedIndex());
    }

    public Music getSpecifiedMusic(int index){
        return songsList.get(index);
    }

    public void setCellSize(int width,int height) {
        if(width!=0&&height==0) {
            listCellBgImageIcon.setImage(listCellBgImageIcon.getImage().getScaledInstance(
                    (int) (width-40*scalingFactor),
                    (int)((width-40*scalingFactor) / listCellBgImageIcon.getIconWidth() * listCellBgImageIcon.getIconHeight()),
                    Image.SCALE_SMOOTH));
            listCellBgImageSelectedIcon.setImage(listCellBgImageSelectedIcon.getImage().getScaledInstance(
                    (int) (width-40*scalingFactor),
                    (int)((width-40*scalingFactor) / listCellBgImageSelectedIcon.getIconWidth() * listCellBgImageSelectedIcon.getIconHeight()),
                    Image.SCALE_SMOOTH));
        }
    }

    public MusicList(double scalingFactor){
        this.scalingFactor=scalingFactor;
        songsList=new LinkedList<>();
        setOpaque(false);
        try {
            BufferedImage listCellImage = ImageIO.read(getClass().getResource("/pictures/list_cell.png"));
            int width=listCellImage.getWidth()/2;
            int height=listCellImage.getHeight();
            listCellBgImageIcon=new ImageIcon(listCellImage.getSubimage(0,0,width,height));
            listCellBgImageSelectedIcon=new ImageIcon(listCellImage.getSubimage(width,0,width,height));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        listCellBgImageIcon.setImage(listCellBgImageIcon.getImage().getScaledInstance(
                (int) (listCellBgImageIcon.getIconWidth()*scalingFactor),
                (int) (listCellBgImageIcon.getIconHeight()*scalingFactor),
                Image.SCALE_SMOOTH));
        listCellBgImageSelectedIcon.setImage(listCellBgImageSelectedIcon.getImage().getScaledInstance(
                (int) (listCellBgImageSelectedIcon.getIconWidth()*scalingFactor),
                (int) (listCellBgImageSelectedIcon.getIconHeight()*scalingFactor),
                Image.SCALE_SMOOTH));

        setModel(new AbstractListModel(){
            @Override
            public int getSize() {
                return songsList.size();
            }

            @Override
            public Object getElementAt(int index) {
                return songsList.get(index);
            }
        });

        setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            Music music=(Music) value;
            MusicListCellLabel cellLabel=new MusicListCellLabel();
            if(isSelected){
                cellLabel.setIcon(listCellBgImageSelectedIcon);
                cellLabel.setIconOffsetX((int) (-40*scalingFactor));
                cellLabel.setTextOffsetX((int) (-40*scalingFactor));
            }else{
                cellLabel.setIcon(listCellBgImageIcon);
                if(index==cellHoveredIndex){
                    cellLabel.setIconOffsetX((int) (-20*scalingFactor));
                    cellLabel.setTextOffsetX((int) (-20*scalingFactor));
                }
            }
            cellLabel.setText(music.getTitle()+" ["+music.getVersion()+"]");
            cellLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32*scalingFactor)));
            cellLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            cellLabel.setIconProtectWidth((int)(70*scalingFactor),(int)(20*scalingFactor));
            return cellLabel;
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index=locationToIndex(e.getPoint());
                if(index==-1){
                    return;
                }
                if(getCellBounds(index,index).contains(e.getPoint())){
                    if(getCellHoveredIndex()!=index){
                        cellHoveredIndex=index;
                        updateUI();
                    }
                }
            }
        });

        freshSongsList();
    }

    public void freshSongsList() {
        songsList.clear();
        MusicLoader.loadSongsList(songsList);
    }

    public static void main(String[] args) {
        JFrame testFrame=new JFrame();
        testFrame.add(new MusicList());
        testFrame.pack();
        testFrame.setVisible(true);
    }

}
