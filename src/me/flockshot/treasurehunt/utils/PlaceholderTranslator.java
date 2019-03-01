package me.flockshot.treasurehunt.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderTranslator
{
	private boolean isUsingAPI = false;
	public PlaceholderTranslator()
	{
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) isUsingAPI = true;			
	}
	
	public String getTranslatedString(Player player, String string)
	{
		if(isUsingAPI)
		{
			return PlaceholderAPI.setPlaceholders(player, string);
		}
		else return string;
	}
}
