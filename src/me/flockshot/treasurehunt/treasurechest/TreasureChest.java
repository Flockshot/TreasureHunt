package me.flockshot.treasurehunt.treasurechest;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.flockshot.treasurehunt.treasurecommand.TreasureCommand;
import me.flockshot.treasurehunt.treasureitem.TreasureItem;
import me.flockshot.treasurehunt.treasuretypes.TreasureType;

public class TreasureChest
{
	private String name;
	private Location loc;
	private boolean looted;
	private TreasureType type;
		
	public TreasureChest(String name, Location location, boolean looted, TreasureType type)
	{		
		setName(name);
		setLocation(location);
		setLooted(looted);
		setType(type);
	}

	public void loot(Player player)
	{
		if(type.getCommands()!=null)
			if(!type.getCommands().isEmpty())
				for(TreasureCommand command : type.getCommands())
					command.execute(player);			

		setLooted(true);
	}

	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Location getLocation() {
		return loc;
	}
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	

	public boolean isLooted() {
		return looted;
	}
	public void setLooted(boolean looted) {
		this.looted = looted;
	}

	
	public TreasureType getType() {
		return type;
	}
	public void setType(TreasureType type) {
		this.type = type;
	}


	public void generateLoot()
	{
		if(!isLooted())
		{
			Chest chest = (Chest) getLocation().getBlock().getState();
			Inventory inv = chest.getInventory();
			inv.clear();
			
			if(inv.getViewers()!=null)
				for(HumanEntity human : inv.getViewers())
					human.closeInventory();

			
			for(TreasureItem tItem : getType().getItems())
			{
				double chance = tItem.getChance();
				if(chance>0)
				{
					Random rand = new Random();
					double randomDouble = rand.nextDouble();
					if(randomDouble<=chance)
						inv.setItem(tItem.getSlot(), tItem.getItem());
				}
			}
		}
	}


}
