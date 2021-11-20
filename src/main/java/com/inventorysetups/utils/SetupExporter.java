package com.inventorysetups.utils;

import com.inventorysetups.InventorySetupsPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SetupExporter {

    private final InventorySetupsPlugin plugin;

    private Client client;

    private ItemManager itemManager;

    private ClientThread clientThread;

    public SetupExporter(final InventorySetupsPlugin plugin, final Client client, final ItemManager itemManager,
                         final ClientThread clientThread)
    {
        this.plugin = plugin;
        this.client = client;
        this.itemManager = itemManager;
        this.clientThread = clientThread;
    }

    public void exportToFile(String setupPath)
    {
        List<Integer> itemIds = getItemIds();
        if (itemIds.size() == 0)
        {
            log.info("A setup was not exported. You may not be logged in.");
            return;
        }

        clientThread.invokeLater(() ->
        {
            try
            {
                File file = new File(setupPath);
                if (file.createNewFile())
                {
                    log.info("File was created");
                }

                FileWriter fileWriter = new FileWriter(file);
                for (int itemId : itemIds)
                {
                    ItemComposition itemComposition = itemManager.getItemComposition(itemId);
                    String itemName = itemComposition.getName();
                    fileWriter.write(itemId + "," + itemName + "\n");
                }
                fileWriter.close();
            }
            catch (IOException e)
            {
                log.info("An inventory setup was not exported.");
            }
        });
    }

    private List<Integer> getItemIds()
    {
        List<Integer> itemIds = new ArrayList<>();
        ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        if (itemContainer == null)
        {
            return new ArrayList<>();
        }

        for (Item item : itemContainer.getItems())
        {
            int itemId = item.getId();
            itemIds.add(itemId);
        }
        return itemIds;
    }

}
