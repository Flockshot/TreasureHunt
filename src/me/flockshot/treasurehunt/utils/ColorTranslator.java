package me.flockshot.treasurehunt.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;

public class ColorTranslator
{	
	public List<String> getTranslatedLore(List<String> lore)
	{
		List<String> clore = new ArrayList<String>();
		
		if(lore!=null && !lore.isEmpty())
		{			
			Iterator<?> it = lore.iterator();
			String cur;
			
			while(it.hasNext())
			{
				cur = getTranslatedString((String) it.next());
				clore.add(cur);
			}
		}
		return clore;
	}
	
	public String getTranslatedString(String st)
	{
		return ChatColor.translateAlternateColorCodes('&', st);
	}
}
