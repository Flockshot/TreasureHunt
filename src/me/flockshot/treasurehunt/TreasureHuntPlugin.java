package me.flockshot.treasurehunt;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.flockshot.treasurehunt.commands.TreasureHuntCommand;
import me.flockshot.treasurehunt.listeners.InteractEvent;
import me.flockshot.treasurehunt.treasurechest.TreasureChestManager;
import me.flockshot.treasurehunt.treasuretypes.TreasureTypeManager;


public class TreasureHuntPlugin extends JavaPlugin
{
	//File chestFile;
	//FileConfiguration chestConfig;
	TreasureChestManager tcManager;
	TreasureTypeManager ttManager;
	File chestsDir;
	File typesDir;
	
	public void onEnable()
	{
		
		File treasureChestDir = new File(this.getDataFolder()+ File.separator+"TreasureChests");		
		treasureChestDir.mkdir();		
		chestsDir = treasureChestDir;
		File treasureTypeDir = new File(this.getDataFolder()+ File.separator+"TreasureTypes");		
		treasureTypeDir.mkdir();
		typesDir = treasureTypeDir;
		
		//createFiles();
		
		
		getConfig().options().copyDefaults(true);
		saveConfig();		
		
		setTCManager(new TreasureChestManager(this));
		setTTypeManager(new TreasureTypeManager(this));
		
		int min = 20*60*10;
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{			 
			public void run()
			{
				getTCManager().saveAllChests();
				//getTTypeManager().saveAllTypes();
			}
		}, 0L, min);
		
		registerEvents();
		registerCommands();
    }
	
	
		
	private void setTCManager(TreasureChestManager treasureChestManager)
	{
		tcManager = treasureChestManager;		
	}
	
	public TreasureChestManager getTCManager()
	{
		return tcManager;
	}
	
	public File getChestsDir()
	{
		return chestsDir;
	}

	private void setTTypeManager(TreasureTypeManager treasureTypeManager)
	{
		ttManager = treasureTypeManager;		
	}
	
	public TreasureTypeManager getTTypeManager()
	{
		return ttManager;
	}
	
	public File getTypesDir()
	{
		return typesDir;
	}


	private void registerCommands()
	{
		// TODO Auto-generated method stub
		this.getCommand("treasurehunt").setExecutor(new TreasureHuntCommand(this));
		//this.getCommand("wands").setExecutor(new TreasureHunt(this));
		//this.getCommand("wands").setExecutor(new TreasureHunt(this));
	}

	/*
	private void createFiles()
	{
		// TODO Auto-generated method stub
		File chests = new File(this.getDataFolder(), "TreasureChests.yml");
		if(!chests.exists())
		{
			try
			{
				chests.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//chestFile = chests;
		setChestFile(chests);
		setChestConfig(YamlConfiguration.loadConfiguration(chests));
		
		File types = new File(this.getDataFolder(), "TreasureChests.yml");
		if(!chests.exists())
		{
			try
			{
				chests.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		//chestFile = chests;
		setChestFile(chests);
		setChestConfig(YamlConfiguration.loadConfiguration(chests));
		
	}
	


	private void setChestConfig(YamlConfiguration config)
	{
		// TODO Auto-generated method stub
		chestConfig = config;
		
	}



	private void setChestFile(File chests)
	{
		chestFile = chests;
	}
	*/


	public void onDisable()
	{	
		//TODO SAVE
		getTCManager().saveAllChests();
		getTTypeManager().saveAllTypes();
		setTCManager(null);
		setTTypeManager(null);
		
		
	}
	
	
	private void registerEvents()
	{
		// TODO Auto-generated method stub

		getServer().getPluginManager().registerEvents(new InteractEvent(this), this);
		
		
	}
	
	
}
