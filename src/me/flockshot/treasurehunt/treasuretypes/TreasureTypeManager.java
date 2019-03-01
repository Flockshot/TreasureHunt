package me.flockshot.treasurehunt.treasuretypes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.flockshot.treasurehunt.TreasureHuntPlugin;
import me.flockshot.treasurehunt.treasurecommand.TreasureCommand;
import me.flockshot.treasurehunt.treasurecommand.TreasureCommandManager;
import me.flockshot.treasurehunt.treasureitem.TreasureItem;
import me.flockshot.treasurehunt.treasureitem.TreasureItemManager;
import me.flockshot.treasurehunt.utils.FileManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;




public class TreasureTypeManager
{

	TreasureHuntPlugin plugin;
	
	HashMap<String, TreasureType> treasureTypes = new HashMap<String, TreasureType>();
	FileManager fileManager;
	
	
	public TreasureTypeManager(TreasureHuntPlugin plugin)
	{
		this.plugin = plugin;
		fileManager = new FileManager(plugin);
		setTreasureTypes();
	}
	
	
	private void setTreasureTypes()
	{
		File folder = plugin.getTypesDir();
		File[] listOfFiles = folder.listFiles();
		FileConfiguration typeFile;
		String fileName;

		for (int i = 0; i < listOfFiles.length; i++)
		{
			if(listOfFiles[i].isFile())
		    {
				typeFile = YamlConfiguration.loadConfiguration(listOfFiles[i]);
				//Location loc = new Location();
		    	fileName = listOfFiles[i].getName();
		    	fileName = fileName.replace(".yml", "");
		    	

		    	List<TreasureCommand> commands = new TreasureCommandManager(plugin).getTreasureCommands(typeFile, "Commands");
		    	List<TreasureItem> items = new TreasureItemManager(plugin).getItems(typeFile, "Items");
		    	
		    	
		    	if(commands == null || items==null)
		    	{
		    		plugin.getLogger().log(Level.SEVERE, ChatColor.DARK_RED+"Invalid Config values of "+fileName+" in Types folder");
		    		continue;
		    	}		    	
		    	treasureTypes.put(fileName, new TreasureType(fileName, items, commands));		    	
		    }
		}		
	}
	
	public TreasureType getTreasureType(String name)
	{
		if(treasureTypes.containsKey(name))
		{
			return treasureTypes.get(name);
		}
		else return null;
	}
	
	public boolean exists(String name)
	{
		if(treasureTypes.containsKey(name))
		{
			return true;
		}
		else return false;
	}
	
	public void saveAllTypes()
	{
		fileManager.saveAllTypeFiles(treasureTypes);
	}


	public void addType(String type, ItemStack item)
	{
		// TODO Auto-generated method stub
		List<TreasureItem> items = new ArrayList<TreasureItem>();
		if(item==null) item = new ItemStack(Material.IRON_INGOT, 1);
		
		items.add(new TreasureItem(item.getItemMeta()!=null ? item.getItemMeta().getDisplayName() : item.getType()+"", item, 0, 100.0));
		TreasureType tType = new TreasureType(type, items, new ArrayList<TreasureCommand>());
		
		treasureTypes.put(type, tType);
		fileManager.saveTypeFile(fileManager.setConfigurationForTreasureType(fileManager.createFile(type, plugin.getTypesDir()), tType), type);
		
	}


	public boolean delType(String type)
	{
		// TODO Auto-generated method stub

		treasureTypes.remove(type);			
		return fileManager.deleteFile(type, plugin.getTypesDir());		
		
		
	}

}
