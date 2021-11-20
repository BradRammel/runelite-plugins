package com.inventorysetups;

import net.runelite.client.ui.ColorScheme;

import javax.swing.*;
import java.awt.*;

public class ItemCell extends JPanel {

    private final int HEIGHT = 40;
    private final int WIDTH = 40;

    private int row;
    private int column;

    ItemCell(int row, int column, JLabel itemImage, String itemName)
    {
        this.row = row;
        this.column = column;
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setToolTipText(itemName);
        add(itemImage);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }

    public int getRow()
    {
        return this.row;
    }

    public int getColumn()
    {
        return this.column;
    }

}
