package me.flockshot.treasurehunt.treasurecommand;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import me.flockshot.treasurehunt.TreasureHuntPlugin;
import me.flockshot.treasurehunt.utils.NumberUtility;

public class TreasureCommandManager
{
	TreasureHuntPlugin plugin;
	
	public TreasureCommandManager(TreasureHuntPlugin plug)
	{
		plugin = plug;
	}
	
	public List<TreasureCommand> getTreasureCommands(FileConfiguration file, String path)
	{
		List<TreasureCommand> treasureCommands = new ArrayList<TreasureCommand>();
		
		if(file.contains(path))
		{
			for(final String key : file.getStringList(path))
			{	
				String value = getValue(key);
				double chance =  getChance(key);
				
				treasureCommands.add(new TreasureCommand(value, chance));
			}
		}
		
		return treasureCommands;
	}	

	//TODO TEST IT
	private String getValue(String val)
	{
		if(val.contains("]"))
		{
			val = val.substring(val.indexOf("]")+1);
			while(val.startsWith(" "))
			{
				val = val.substring(1);
			}
		}		
		return val;
	}
	
	private double getChance(String val)
	{
		if(val.contains("[") || val.contains("]"))
		{
			val = StringUtils.substringBetween(val, "[", "]");
			while(val.startsWith(" "))
			{
				val = val.substring(1);
			}
			while(val.endsWith(" "))
			{
				val = val.substring(1);
			}
			if(NumberUtility.isNum(val))
			{
				return Double.valueOf(val);
			}
		}
		
		return 100.0;
	}
}
