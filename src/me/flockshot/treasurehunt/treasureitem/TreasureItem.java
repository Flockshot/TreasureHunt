package me.flockshot.treasurehunt.treasureitem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class TreasureItem
{
	private String name;
	private ItemStack item;
	private int slot;
	private double chance;
	
	
	public TreasureItem(String name, ItemStack item, int slot, double chance)
	{
		setName(name);
		setItem(item);
		setSlot(slot);
		setChance(chance);
	}

	public List<String> getFlagListInString()
	{
		List<String> flags = new ArrayList<String>();
		
		if(getItem().getItemMeta()!=null)
			if(getItem().getItemMeta().getItemFlags()!=null)
				for(ItemFlag flag : getItem().getItemMeta().getItemFlags())
					flags.add(flag.toString());

		return flags;		
	}
	
	public double getChance() {
		return chance;
	}
	public void setChance(double chance) {
		this.chance = chance;
	}

	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}

	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
