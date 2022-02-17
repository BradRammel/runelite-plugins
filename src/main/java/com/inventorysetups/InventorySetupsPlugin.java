package com.inventorysetups;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Inventory Setups"
)
public class InventorySetupsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private InventorySetupsConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ClientThread clientThread;

	private InventorySetupsPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		panel = injector.getInstance(InventorySetupsPanel.class);
		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "ranged.png");

		navButton = NavigationButton.builder()
				.tooltip("Inventory Presets")
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onConfigChanged(final ConfigChanged event) {}

	@Provides
	InventorySetupsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(InventorySetupsConfig.class);
	}
}
