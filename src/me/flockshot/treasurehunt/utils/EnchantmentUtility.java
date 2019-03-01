package me.flockshot.treasurehunt.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class EnchantmentUtility
{

	public Map<Enchantment, Integer> convertEnchants(List<String> ench)
	{
		
		
		Map<Enchantment, Integer> enchs = new HashMap<Enchantment, Integer>();
		if(ench.isEmpty())
		{
			return enchs;
		}
		Iterator<String> it = ench.iterator();
		while(it.hasNext())
		{
			String current = (String) it.next();
			
			Enchantment enchant = getEnchant(current);
			int lvl = getLevel(current);
			
			
			enchs.put(enchant, lvl);
			
		}
		
		return enchs;
	}
	
	@SuppressWarnings("deprecation")
	public Enchantment getEnchant(String ench)
	{
		Enchantment enchant = null;
		
		if(ench.contains(":"))
		{
			String[] split = ench.split(":");	
			enchant = NumberUtility.isNum(split[0]) ? Enchantment.getById(Integer.parseInt(split[0])) : Enchantment.getByName(split[0]);		
		}
		else Bukkit.broadcastMessage(ChatColor.DARK_RED+"Invalid enchant format of in file.");
		
		return enchant;
	}
	
	public int getLevel(String ench)
	{
		int lvl = 0;
		
		if(ench.contains(":"))
		{
			String[] split = ench.split(":");
			lvl = NumberUtility.isNum(split[1]) ? Integer.parseInt(split[1]) : 0;
		}
		else Bukkit.broadcastMessage(ChatColor.DARK_RED+"Invalid enchant format of in file.");
		
		return lvl;
	}
	
	public boolean compareEnchant(ItemStack item, String ench)
	{
		 
		 Enchantment enchant = getEnchant(ench);
		 int lvl = getLevel(ench);
		 
		 if(item.containsEnchantment(enchant))
		 {
			 if(item.getEnchantmentLevel(enchant) >= lvl)
			 {
				 return true;
			 }
		 }

		return false;
	}
	
}
