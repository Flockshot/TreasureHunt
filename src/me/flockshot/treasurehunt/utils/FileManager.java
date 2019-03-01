package me.flockshot.treasurehunt.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.flockshot.treasurehunt.TreasureHuntPlugin;
import me.flockshot.treasurehunt.treasurechest.TreasureChest;
import me.flockshot.treasurehunt.treasureitem.TreasureItem;
import me.flockshot.treasurehunt.treasuretypes.TreasureType;

public class FileManager 
{
	TreasureHuntPlugin plugin;
	public FileManager(TreasureHuntPlugin plug)
	{
		plugin = plug;
	}
	
	public boolean fileExits(String name, File folder)
	{
		File[] file = folder.listFiles();
		String fname;
		for (int i = 0; i < file.length; i++)
		{
			if (file[i].isFile())
		    {
		    	fname = file[i].getName();		    	
		    	fname = fname.replace(".yml", "");		    	

		    	if(fname.equalsIgnoreCase(name))
		    	{
		    		return true;
		    	}
		     }
		}
		return false;
	}
	
	public void saveChestFile(FileConfiguration config, String name) {
		saveFile(config, new File(plugin.getChestsDir(), name+".yml"));
	}
	public void saveTypeFile(FileConfiguration config, String name)	{
		saveFile(config, new File(plugin.getTypesDir(), name+".yml"));
	}
	
	private void saveFile(FileConfiguration config, File file)
	{
	    try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File createFile(String name, File dir)
	{
		File file = new File(dir, name+".yml");
		if(!file.exists())
			try	{
				file.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		
		return file;
	}
	
	public boolean deleteFile(String name, File dir)
	{
		File file = new File(dir, name+".yml");
		return file.delete();
	}
	
	
	public void saveAllChestFiles(HashMap<String, TreasureChest> chests)
	{
		for(String name : chests.keySet())
		{
			File file = new File(plugin.getChestsDir(), name+".yml");
			saveFile(setConfigurationForTreasureChest(file, chests.get(name)), file);
		}
	}
	public void saveAllTypeFiles(HashMap<String, TreasureType> types)
	{
		for(String name : types.keySet())
		{
			File file = new File(plugin.getTypesDir(), name+".yml");
			saveFile(setConfigurationForTreasureType(file, types.get(name)), file);
		}
	}
	
	@SuppressWarnings("deprecation")
	public FileConfiguration setConfigurationForTreasureType(File file, TreasureType treasureType)
	{
		// TODO Auto-generated method stub
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("Commands", treasureType.getCommandsList());
		
		for(TreasureItem item : treasureType.getItems())
		{
			String path = "Items."+item.getName();
			config.set(path+".Slot", item.getSlot());
			config.set(path+".Chance", item.getChance());
			
			ItemStack itemStack = item.getItem();
			ItemMeta meta = itemStack.getItemMeta();
			
			config.set(path+".Material", itemStack.getType()+"");
			config.set(path+".Amount", itemStack.getAmount());
			config.set(path+".Data", itemStack.getDurability());
			
			if(meta.hasEnchants())
			{
				List<String> enchants = new ArrayList<String>();
				for(Enchantment e : meta.getEnchants().keySet())
					enchants.add(e.getName() +":"+meta.getEnchants().get(e));

				config.set(path+".Enchants", enchants);
			}
			
			List<String> flags = new ArrayList<String>();
			for(ItemFlag flag : meta.getItemFlags())
				flags.add(flag.toString());
			
			config.set(path+".Name", meta.hasDisplayName() ? meta.getDisplayName() : "");
			config.set(path+".Lore", meta.hasLore() ? meta.getLore() : new ArrayList<String>());
			config.set(path+".Flags", flags);
			
			
			if((itemStack.getTypeId() == 397) && (itemStack.getData().getData() == 3))
	        {
				SkullMeta skull = (SkullMeta)meta;
				config.set(path+".Skull", skull.getOwner());
	        }
		
			if((meta instanceof LeatherArmorMeta))
			{
	            LeatherArmorMeta lam = (LeatherArmorMeta)meta;
	            config.set(path+"R", lam.getColor().getRed());
	            config.set(path+"G", lam.getColor().getGreen());
	            config.set(path+"B", lam.getColor().getBlue());
	        }
			/*
	        if ((itemStack.getType().equals(Material.WRITTEN_BOOK)) || (itemStack.getType().equals(Material.BOOK_AND_QUILL)))
			{
				BookMeta book = ((BookMeta) meta);
				config.set(path+".Author", book.getAuthor());
				config.set(path+".Title", book.getTitle());
				config.set(path+".Pages", book.getPages());				
			}
			*/
		}
		
		
		return config;
	}

	public FileConfiguration setConfigurationForTreasureChest(File file, TreasureChest chest)
	{
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		// TODO Auto-generated method stub
		config.set("Chest.Location.X", chest.getLocation().getX());
		config.set("Chest.Location.Y", chest.getLocation().getY());
		config.set("Chest.Location.Z", chest.getLocation().getZ());
		config.set("Chest.Location.World", chest.getLocation().getWorld().getName());
		
		config.set("Chest.Looted", chest.isLooted());
		config.set("Chest.Type", chest.getType().getName());
		
		/*
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		return config;
	}
}
