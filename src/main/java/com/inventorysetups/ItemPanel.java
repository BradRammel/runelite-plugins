package com.inventorysetups;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.game.ItemManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

@Slf4j
public class ItemPanel extends JPanel  {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 300;
    private static final int GRID_ROWS = 7;
    private static final int GRID_COLUMNS = 4;
    private static final String RUNELITE_SETTINGS = "C:/Users/bmram/Documents/runelite-settings";
    private static final String SETUPS_DIR = "/inventory-presets/setups";

    private ItemManager itemManager;

    private JPanel gridPanel;

    ItemPanel(ItemManager itemManager, String setup)
    {
        this.itemManager = itemManager;
        gridPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLUMNS));
        updateDisplayedSetup(setup);
        setLayout(new GridBagLayout());
        add(gridPanel);
    }

    public void updateDisplayedSetup(String setup)
    {
        gridPanel.removeAll();
        revalidate();
        repaint();
        Map<String, Object> itemIdAndNames = readItemIds(setup);
        if (itemIdAndNames == null)
        {
            return;
        }

        java.util.List<Integer> itemIds = (java.util.List<Integer>) itemIdAndNames.get("itemIds");
        java.util.List<String> itemNames = (java.util.List<String>) itemIdAndNames.get("itemNames");
        for (int row = 0; row < GRID_ROWS; row++)
        {
            for (int column = 0; column < GRID_COLUMNS; column++)
            {
                JLabel itemImage = new JLabel();
                int itemIndex = (row*4) + column;
                String itemName = "";
                if (itemIds.get(itemIndex) != -1)
                {
                    itemName = itemNames.get(itemIndex);
                    itemManager.getImage(itemIds.get(itemIndex)).addTo(itemImage);
                }
                gridPanel.add(new ItemCell(row, column, itemImage, itemName));
            }
        }
    }

    public Map<String, Object> readItemIds(String setup)
    {
        if (setup.equals("None"))
        {
            return null;
        }

        Map<String, Object> itemIdsAndNames = new LinkedHashMap<>();
        java.util.List<Integer> itemIds = new ArrayList<>();
        java.util.List<String> itemNames = new ArrayList<>();
        try
        {
            String item = "";
            StringBuffer buffer = new StringBuffer();
            File setupFile = new File(RUNELITE_SETTINGS + SETUPS_DIR + "/" + setup + ".txt");
            InputStream inputStream = new FileInputStream(setupFile);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((item = bufferedReader.readLine()) != null)
            {
                List<String> itemInformation = new ArrayList<>(Arrays.asList(item.split(",")));
                itemIds.add(Integer.parseInt(itemInformation.get(0)));
                itemNames.add(itemInformation.get(1));
            }
            itemIdsAndNames.put("itemIds", itemIds);
            itemIdsAndNames.put("itemNames", itemNames);
        }
        catch (IOException e)
        {
            log.info("Something happened while trying to get the item ID's.");
        }

        return itemIdsAndNames;
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(WIDTH, HEIGHT);
    }

}
