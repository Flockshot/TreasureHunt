package me.flockshot.treasurehunt.treasurecommand;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.ezprestige.PrestigeManager;
import me.clip.ezrankspro.multipliers.CostHandler;
import me.clip.ezrankspro.rankdata.Rankup;
import me.flockshot.treasurehunt.utils.NumberUtility;
import me.flockshot.treasurehunt.utils.PlaceholderTranslator;

public class TreasureCommand
{
	private double chance;
	private String command;
	
	public TreasureCommand(String value, double chance)
	{
		setCommand(value);
		setChance(chance);
	}
	
	public String toString() {
		return "["+chance+"]"+command;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String value) {		
		this.command = value;
	}
	
	public double getChance() {
		return chance;
	}
	public void setChance(double chance) {
		this.chance = chance;
	}
	
	private String translate(String value)
	{
		if(value.contains("Rnd(") && value.contains(")"))
		{
			String random = StringUtils.substringBetween(value, "Rnd(", ")");
			if(random.contains(","))
			{
				String[] bounds = random.split(",");
				if(NumberUtility.isNum(bounds[0]) &&  NumberUtility.isNum(bounds[1]))
				{
					Random rand = new Random();
					int randomInt = rand.nextInt(Integer.valueOf(bounds[1])-Integer.valueOf(bounds[0]));
					randomInt+=Integer.valueOf(bounds[0]);
					value.replace("Rnd("+value+")", randomInt+"");
				}
			}			
		}
		return value;
	}
	
	public void execute(Player player)
	{
		String cmd = translate(getCommand());
		if(cmd.contains("RankPercent(") && cmd.contains(")"))
		{
			String rankPercent = StringUtils.substringBetween(cmd, "RankPercent(", ")");
			if(NumberUtility.isNum(rankPercent))
			{
				int percent = Integer.valueOf(rankPercent);
				int value = 0;				
				if(Bukkit.getPluginManager().getPlugin("EZRanksPro") != null)	
				{
					Rankup rankup = Rankup.getRankup(player);
					if(rankup==null)
					{
						if(Bukkit.getPluginManager().getPlugin("EZPrestige") != null)	
						{
							//if(PrestigeManager.isLastPrestige(player))
							//{
						    if(PrestigeManager.getNextPrestige(player)!=null)
                            {
                                value = (int) ((percent/100) * PrestigeManager.getNextPrestige(player).getCost());
                            }	
								
							//}
						}						
					}
					else
					{
						if(!Rankup.isLastRank(player))
						{
							double cost = rankup.getCost();
							cost = CostHandler.getMultiplier(player, cost);
							cost = CostHandler.getDiscount(player, cost);
							
							value = (int) ((percent/100)*cost);
						}
					}
				}
				
				cmd.replace("RankPercent("+rankPercent+")", value+"");
			}
		}
		
		cmd = new PlaceholderTranslator().getTranslatedString(player, cmd); 
	    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
	}
	
}
