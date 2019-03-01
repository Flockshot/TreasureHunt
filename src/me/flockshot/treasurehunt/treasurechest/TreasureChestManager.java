package me.flockshot.treasurehunt.treasurechest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.flockshot.treasurehunt.TreasureHuntPlugin;
import me.flockshot.treasurehunt.treasuretypes.TreasureType;
import me.flockshot.treasurehunt.utils.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;




public class TreasureChestManager
{

	TreasureHuntPlugin plugin;
	HashMap<Location, String> treasureChestsNameByLoc = new HashMap<Location, String>();
	HashMap<String, TreasureChest> treasureChestsByName = new HashMap<String, TreasureChest>();
	List<String> names = new ArrayList<String>();
	FileManager fileManager;
	
	public TreasureChestManager(TreasureHuntPlugin plugin)
	{
		this.plugin = plugin;
		fileManager = new FileManager(plugin);
		setTreasureChests();
	}
	
	
	private void setTreasureChests()
	{
		File folder = plugin.getChestsDir();
		File[] listOfFiles = folder.listFiles();
		FileConfiguration tChest;
		String fileName;

		if(listOfFiles==null) return;
		for (int i = 0; i < listOfFiles.length; i++)
		{
			if (listOfFiles[i].isFile())
		    {
				tChest = YamlConfiguration.loadConfiguration(listOfFiles[i]);
				//Location loc = new Location();
		    	fileName = listOfFiles[i].getName();
		    	fileName = fileName.replace(".yml", "");
		    	
		    	Double x = tChest.contains("Chest.Location.X") ? tChest.getDouble("Chest.Location.X") : null;
		    	Double y = tChest.contains("Chest.Location.Y") ? tChest.getDouble("Chest.Location.Y") : null;
		    	Double z = tChest.contains("Chest.Location.Z") ? tChest.getDouble("Chest.Location.Z") : null;
		    	World world = tChest.contains("Chest.Location.World") ? Bukkit.getWorld(tChest.getString("Chest.Location.World")) : null;
		    	boolean looted = tChest.contains("Chest.Looted") ? tChest.getBoolean("Chest.Looted") : false;
		    	TreasureType type = tChest.contains("Chest.Type") ? plugin.getTTypeManager().getTreasureType(tChest.getString("Chest.Type")) : null;
		    	
		    	
		    	if(x==null || y==null || z==null || world==null || type==null)
		    	{
		    		plugin.getLogger().log(Level.SEVERE, ChatColor.DARK_RED+"Invalid Config values of "+fileName);
		    		continue;
		    	}
		    	TreasureChest chest = new TreasureChest(fileName, new Location(world, x, y, z), looted, type);
		    	chest.generateLoot();
		    	addChest(chest);
		    	
		    }
		}
	}
	
	public void addChest(TreasureChest treasureChest)
	{
		treasureChestsNameByLoc.put(treasureChest.getLocation(), treasureChest.getName());
		treasureChestsByName.put(treasureChest.getName(), treasureChest);
	}
	
	public void createTreasureChest(TreasureChest treasureChest)
	{
		fileManager.saveChestFile(fileManager.setConfigurationForTreasureChest(fileManager.createFile(treasureChest.getName(), plugin.getChestsDir()), treasureChest), treasureChest.getName());
		addChest(treasureChest);
	}	
	
	public void saveAllChests()
	{
		fileManager.saveAllChestFiles(treasureChestsByName);
	}
	
	
	public boolean exists(String name)
	{
		if(treasureChestsByName.containsKey(name))
		{
			return true;
		}
		else return false;
	}
	public boolean exists(Location loc)
	{
		if(treasureChestsNameByLoc.containsKey(loc))
		{
			return true;
		}
		else return false;

	}
	
	public TreasureChest getTreasureChest(String name)
	{
		if(treasureChestsByName.containsKey(name))
		{
			return treasureChestsByName.get(name);
		}
		else return null;
	}
	
	public TreasureChest getTreasureChest(Location location)
	{

		for(double x=-0.5; x<=0.5; x+=0.5)
		{
			for(double z=-0.5; z<=0.5; z+=0.5)
			{
				Location loc = location;
				loc.setX(loc.getX()+x);
				loc.setZ(loc.getZ()+z);
				
				if(treasureChestsNameByLoc.containsKey(loc))
				{
					return getTreasureChest(treasureChestsNameByLoc.get(loc));
				}								
			}
		}				
		return null;		
	}


	public boolean removeTreasureChest(String name)
	{
		treasureChestsNameByLoc.remove(treasureChestsByName.get(name).getLocation());
		treasureChestsByName.remove(name);
		
		return fileManager.deleteFile(name, plugin.getChestsDir());		
	}


	public void changeType(String name, String type)
	{
		// TODO Auto-generated method stub
		getTreasureChest(name).setType(plugin.getTTypeManager().getTreasureType(type));
		
	}

	public void resetAll()
	{
		for(String name : treasureChestsByName.keySet())
		{
			reset(name);
		}
	}

	public void reset(String name)
	{
		// TODO Auto-generated method stub
		treasureChestsByName.get(name).setLooted(false);
		treasureChestsByName.get(name).generateLoot();
	}


	




}
