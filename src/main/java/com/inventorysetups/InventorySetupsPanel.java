package com.inventorysetups;

import com.inventorysetups.utils.SetupExporter;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class InventorySetupsPanel extends PluginPanel implements ActionListener {

    private static final String RUNELITE_SETTINGS = "C:/Users/bmram/Documents/runelite-settings";
    private static final String SETUPS_DIR = "/inventory-presets/setups";

    private final InventorySetupsPlugin plugin;
    private Client client;
    private ItemManager itemManager;
    private ClientThread clientThread;

    private JComboBox<String> setupComboBox;
    private ItemPanel itemPanel;

    InventorySetupsPanel(final InventorySetupsPlugin plugin, Client client, ItemManager itemManager,
                         ClientThread clientThread)
    {
        this.plugin = plugin;
        this.client = client;
        this.itemManager = itemManager;
        this.clientThread = clientThread;

        setBorder(new EmptyBorder(6, 6, 6, 6));
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setLayout(new BorderLayout());

        // Create layout panel for wrapping
        final JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
        add(layoutPanel, BorderLayout.NORTH);

        setupComboBox = new JComboBox<>();
        updateSetups();
        layoutPanel.add(setupComboBox);

        JPanel spacing = new JPanel();
        spacing.setPreferredSize(new Dimension(0, 10));
        itemPanel = new ItemPanel(itemManager, "None");
        layoutPanel.add(itemPanel);

        setupComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String setup = setupComboBox.getSelectedItem().toString();
                if (setup == null)
                {
                    return;
                }
                itemPanel.updateDisplayedSetup(setup);
            }
        });

        JButton exportButton = new JButton("Export Setup");
        exportButton.setActionCommand("export");
        exportButton.addActionListener(this);
        layoutPanel.add(exportButton);
        layoutPanel.add(spacing);

        JButton deleteSetupButton = new JButton("Delete Setup");
        deleteSetupButton.setActionCommand("delete");
        deleteSetupButton.addActionListener(this);
        layoutPanel.add(deleteSetupButton);
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        if ("export".equals(event.getActionCommand()))
        {
            String setupName = JOptionPane.showInputDialog("Please enter a name for the setup.");
            SetupExporter exporter = new SetupExporter(plugin, client, itemManager, clientThread);
            exporter.exportToFile(RUNELITE_SETTINGS + SETUPS_DIR + "/" + setupName + ".txt");

            // figure out how to update the dropdown with new values since
            // exporter.exportToFile() is running on the client thread making
            // this not work. To update the UI after exporting a new setup turn
            // the inventory presets plugin off then back on.
            // updateSetups();
        }
        else if ("delete".equals(event.getActionCommand()))
        {
            deleteSetup();
        }
    }

    private void updateSetups()
    {
        setupComboBox.removeAllItems();
        setupComboBox.addItem("None");
        File setupDir = new File(RUNELITE_SETTINGS + SETUPS_DIR);
        File[] setupFileList = setupDir.listFiles();
        if (setupFileList == null)
        {
            return;
        }

        for (File setupFile : setupFileList)
        {
            String setupName = setupFile.getName().split("\\.")[0];
            setupComboBox.addItem(setupName);
        }
    }

    private void deleteSetup()
    {
        String currentSetup = setupComboBox.getSelectedItem().toString();
        if (currentSetup == null || currentSetup.equals("None"))
        {
            return;
        }

        File setupDir = new File(RUNELITE_SETTINGS + SETUPS_DIR);
        File[] setupFileList = setupDir.listFiles();
        if (setupFileList == null)
        {
            return;
        }

        for (File setup : setupFileList)
        {
            String fileName = setup.getName().split("\\.")[0];
            if (currentSetup.equals(fileName))
            {
                setupComboBox.removeItem(currentSetup);
                setup.delete();
            }
        }
    }

    private void updateCurrentSetup()
    {
        String currentSetup = setupComboBox.getSelectedItem().toString();
        if (currentSetup == null || currentSetup == "None")
        {
            return;
        }

        File setupDir = new File(RUNELITE_SETTINGS + SETUPS_DIR);
        File[] setupFileList = setupDir.listFiles();
        if (setupFileList == null)
        {
            return;
        }

        for (File setup : setupFileList)
        {
            String fileName = setup.getName().split("\\.")[0];
            if (currentSetup.equals(fileName))
            {

            }
        }
    }
}
