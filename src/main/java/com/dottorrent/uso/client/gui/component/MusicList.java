package com.dottorrent.uso.client.gui.component;

import com.dottorrent.uso.client.service.GameConfig;
import com.dottorrent.uso.client.service.Music;
import com.dottorrent.uso.client.service.MusicLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 继承了 {@link JList}，提供更好看的选歌列表，单个 cell 的 label 见 {@link MusicListCellLabel}
 *
 * @author .torrent
 * @version 1.0.0 2020/11/23
 */
public class MusicList extends JList {

    /**
     * 谱面列表
     */
    private final LinkedList<Music> songsList;
    private ImageIcon listCellBgImageIcon;
    private ImageIcon listCellBgImageSelectedIcon;
    /**
     * 鼠标目前悬浮在的 cell 的 index
     */
    private int cellHoveredIndex = -1;
    private Dimension cellSize;
    private final double scalingFactor;

    public MusicList() {
        this(GameConfig.getScalingFactor());
    }

    public MusicList(double scalingFactor) {
        this.scalingFactor = scalingFactor;
        songsList = new LinkedList<>();
        setOpaque(false);
        try {
            BufferedImage listCellImage = ImageIO.read(getClass().getResource("/pictures/list_cell.png"));
            int width = listCellImage.getWidth() / 2;
            int height = listCellImage.getHeight();
            listCellBgImageIcon = new ImageIcon(listCellImage.getSubimage(0, 0, width, height));
            listCellBgImageSelectedIcon = new ImageIcon(listCellImage.getSubimage(width, 0, width, height));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        listCellBgImageIcon.setImage(listCellBgImageIcon.getImage().getScaledInstance(
                (int) (listCellBgImageIcon.getIconWidth() * scalingFactor),
                (int) (listCellBgImageIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));
        listCellBgImageSelectedIcon.setImage(listCellBgImageSelectedIcon.getImage().getScaledInstance(
                (int) (listCellBgImageSelectedIcon.getIconWidth() * scalingFactor),
                (int) (listCellBgImageSelectedIcon.getIconHeight() * scalingFactor),
                Image.SCALE_SMOOTH));

        //设置Model
        setModel(new AbstractListModel() {
            @Override
            public int getSize() {
                return songsList.size();
            }

            @Override
            public Object getElementAt(int index) {
                return songsList.get(index);
            }
        });

        //设置单个 cell 渲染器
        setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            Music music = (Music) value;
            MusicListCellLabel cellLabel = new MusicListCellLabel();
            if (isSelected) {
                //被选中后，设置新的图标，并向左偏移 40*scalingFactor
                cellLabel.setIcon(listCellBgImageSelectedIcon);
                cellLabel.setIconOffsetX((int) (-40 * scalingFactor));
                cellLabel.setTextOffsetX((int) (-40 * scalingFactor));
            } else {
                cellLabel.setIcon(listCellBgImageIcon);
                if (index == cellHoveredIndex) {
                    //鼠标悬浮时，向左偏移 20*scalingFactor
                    cellLabel.setIconOffsetX((int) (-20 * scalingFactor));
                    cellLabel.setTextOffsetX((int) (-20 * scalingFactor));
                }
            }
            //列表每一项显示的文本
            cellLabel.setText(music.getTitle() + " [" + music.getVersion() + "]");
            cellLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, (int) (32 * scalingFactor)));
            cellLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            //设置左右保护距离
            cellLabel.setIconProtectWidth((int) (70 * scalingFactor), (int) (20 * scalingFactor));
            return cellLabel;
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if (index == -1) {
                    return;
                }
                // 如果鼠标移动到了某个 cell 内就设置 cellHoveredIndex 为鼠标目前悬浮着的 cell 的index，然后刷新画面，cell渲染器会读取
                // cellHoveredIndex 然后进行进一步操作
                if (getCellBounds(index, index).contains(e.getPoint())) {
                    if (getCellHoveredIndex() != index) {
                        cellHoveredIndex = index;
                        updateUI();
                    }
                }
            }
        });

        freshSongsList();
    }

    public static void main(String[] args) {
        JFrame testFrame = new JFrame();
        testFrame.add(new MusicList());
        testFrame.pack();
        testFrame.setVisible(true);
    }

    public int getCellHoveredIndex() {
        return cellHoveredIndex;
    }

    @Override
    public Object getSelectedValue() {
        return songsList.get(getSelectedIndex());
    }

    public Music getSpecifiedMusic(int index) {
        return songsList.get(index);
    }

    /**
     * 设置全体 cell 的大小，实际上是通过缩放 cell 图片实现的。目前只支持设置宽度，高度需要设置为0，否则无效
     *
     * @param width  宽度
     * @param height 高度，需要设置为0
     */
    public void setCellSize(int width, int height) {
        if (width != 0 && height == 0) {
            listCellBgImageIcon.setImage(listCellBgImageIcon.getImage().getScaledInstance(
                    (int) (width - 40 * scalingFactor),
                    (int) ((width - 40 * scalingFactor) / listCellBgImageIcon.getIconWidth() * listCellBgImageIcon.getIconHeight()),
                    Image.SCALE_SMOOTH));
            listCellBgImageSelectedIcon.setImage(listCellBgImageSelectedIcon.getImage().getScaledInstance(
                    (int) (width - 40 * scalingFactor),
                    (int) ((width - 40 * scalingFactor) / listCellBgImageSelectedIcon.getIconWidth() * listCellBgImageSelectedIcon.getIconHeight()),
                    Image.SCALE_SMOOTH));
        }
        // TODO: 添加设置高度的功能
    }

    /**
     * 刷新谱面列表，会清空当前列表然后重新读取本地谱面文件，再重载列表
     */
    public void freshSongsList() {
        songsList.clear();
        MusicLoader.loadSongsList(songsList);
    }

}
