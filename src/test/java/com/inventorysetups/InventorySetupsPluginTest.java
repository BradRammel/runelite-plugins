package com.inventorysetups;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class InventorySetupsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(InventorySetupsPlugin.class);
		RuneLite.main(args);
	}
}