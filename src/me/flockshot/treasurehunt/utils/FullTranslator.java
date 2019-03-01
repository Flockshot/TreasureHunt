package me.flockshot.treasurehunt.utils;

import org.bukkit.entity.Player;

public class FullTranslator
{
	public String getTranslatedString(Player player, String string)
	{
		ColorTranslator ct = new ColorTranslator();
		PlaceholderTranslator pt = new PlaceholderTranslator();
		
		return ct.getTranslatedString(pt.getTranslatedString(player, string));		
	}
}
