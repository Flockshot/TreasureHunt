package me.flockshot.treasurehunt;

import java.io.File;

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
		
		
	private void setTCManager(TreasureChestManager treasureChestManager) {
		tcManager = treasureChestManager;		
	}	
	public TreasureChestManager getTCManager() {
		return tcManager;
	}

	private void setTTypeManager(TreasureTypeManager treasureTypeManager) {
		ttManager = treasureTypeManager;		
	}	
	public TreasureTypeManager getTTypeManager() {
		return ttManager;
	}
	
	public File getTypesDir() {
		return typesDir;
	}
    public File getChestsDir() {
        return chestsDir;
    }

	private void registerCommands()
	{
		this.getCommand("treasurehunt").setExecutor(new TreasureHuntCommand(this));
	}

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
