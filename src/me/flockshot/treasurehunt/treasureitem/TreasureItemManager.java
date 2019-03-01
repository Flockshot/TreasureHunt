package me.flockshot.treasurehunt.treasureitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import me.flockshot.treasurehunt.TreasureHuntPlugin;
import me.flockshot.treasurehunt.utils.ColorTranslator;
import me.flockshot.treasurehunt.utils.EnchantmentUtility;
import me.flockshot.treasurehunt.utils.ItemBuilder;

public class TreasureItemManager
{
	TreasureHuntPlugin plugin;
	
	public TreasureItemManager(TreasureHuntPlugin plug)
	{
		plugin = plug;
	}
	
	
	public List<TreasureItem> getItems(FileConfiguration file, String path)
	{
		List<TreasureItem> items = new ArrayList<TreasureItem>();
		
		if(file.contains(path))
		{
			//Set<String> pathNames = file.getConfigurationSection(path).getKeys(false);
			
			final ConfigurationSection pathNames = file.getConfigurationSection(path);
			
			if(pathNames!=null)
			{
				String fullPath;
				String itemName;
				for(final String key : pathNames.getKeys(false))
				{
					// key will now be either '1' or '2'. Keep in mind, if you were to put true as a param in #getKeys, it would return more than '1' or '2'.
					// For example, it'd also retrun '1.material', '1.slot' etc. You can debug to see yourself.
					
					itemName = key;
					fullPath = path+"."+itemName;
					
					int slot = file.contains(fullPath+".Slot") ? file.getInt(fullPath+".Slot") : -1;
					double chance = file.contains(fullPath+".Chance") ?  file.getDouble(fullPath+".Chance") : -1;
					
					Material mat = file.contains(fullPath+".Material") ? Material.valueOf(file.getString(fullPath+".Material")) : null;
					int amount =  file.contains(fullPath+".Amount") ? file.getInt(fullPath+".Amount") : -1;
					short data = (short) (file.contains(fullPath+".Data") ? file.getInt(fullPath+".Data") : -1);					
					Map<Enchantment, Integer> enchants =  file.contains(fullPath+".Enchants") ? new EnchantmentUtility().convertEnchants(file.getStringList(fullPath+".Enchants")) : null;
					String name = file.contains(fullPath+".Name") ? new ColorTranslator().getTranslatedString(file.getString(fullPath+".Name")) : null; 
					List<String> lore = file.contains(fullPath+".Lore") ? new ColorTranslator().getTranslatedLore(file.getStringList(fullPath+".Name")) : null; 
					List<ItemFlag> flags = file.contains(fullPath+".Flags") ? convertToFlags(file.getStringList(fullPath+".Flags")) : null;
					
					
					if(mat == null || data == -1 || amount == -1 || name == null || lore == null || flags == null || slot == -1 || chance>100 || chance<0)
					{
						plugin.getLogger().log(Level.SEVERE, ChatColor.DARK_RED+"Invalid config values of "+file.getName()+" in Types folder");
						continue;
					}
					ItemBuilder item;
					if(file.contains(fullPath+".Skull")) item = new ItemBuilder(mat, amount, data, enchants, name, lore, flags, file.getString(fullPath+".Skull"));
					else if(file.contains(fullPath+".R") && file.contains(fullPath+".G") && file.contains(fullPath+".B"))
					{
						item = new ItemBuilder(mat, amount, data, enchants, name, lore, flags, file.getInt(fullPath+".R"), file.getInt(fullPath+".G"), file.getInt(fullPath+".B"));
					}
					
					item = new ItemBuilder(mat, amount, data, enchants, name, lore, flags);
					 
					
					//TODO DENY MESSAGE TO BE AN EVENT TYPE????
					items.add(new TreasureItem(itemName, item.createItem(), slot, chance));					
				}
			}
		}		
		return items;
	}


	private List<ItemFlag> convertToFlags(List<String> stringFlags)
	{
		List<ItemFlag> flags = new ArrayList<ItemFlag>();
		for( String key : stringFlags)
		{
			flags.add(ItemFlag.valueOf(key));
		}
		
		return flags;
	}
}
