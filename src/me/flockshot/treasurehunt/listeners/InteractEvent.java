package me.flockshot.treasurehunt.listeners;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.flockshot.treasurehunt.TreasureHuntPlugin;

public class InteractEvent implements Listener
{
	private TreasureHuntPlugin plugin;
	
	public InteractEvent(TreasureHuntPlugin plug)
	{
		plugin = plug;
	}
	
	@EventHandler
	public void onAction(PlayerInteractEvent event)
	{
		
		Player player = event.getPlayer();		
			
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(event.getClickedBlock() instanceof Chest)
			{
				Inventory inv = ((Chest) event.getClickedBlock()).getInventory();
				if(plugin.getTCManager().getTreasureChest(inv.getLocation())!=null)
				{
					plugin.getTCManager().getTreasureChest(inv.getLocation()).loot(player);
				}
			}
		}

	}
}
