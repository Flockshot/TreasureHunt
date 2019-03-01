package me.flockshot.treasurehunt.treasuretypes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.flockshot.treasurehunt.treasurecommand.TreasureCommand;
import me.flockshot.treasurehunt.treasureitem.TreasureItem;

public class TreasureType
{

	private String name;
	private List<TreasureItem> items;
	private List<TreasureCommand> commands;
	private int freeSlot = -2;
	
	
	public TreasureType(String name, List<TreasureItem> items, List<TreasureCommand> commands)
	{		
		setName(name);
		setItems(items);
		setCommands(commands);
	}

	private int calculateFreeSlot()
	{
		// TODO Auto-generated method stub
		List<Integer> slots = new ArrayList<Integer>();
		for(int i=0; i<=53; i++)
		{
			slots.add(i);
		}
		
		for(TreasureItem item : getItems())
		{
			if(item.getSlot()>=0 && item.getSlot()<=53)
			{
				slots.remove(slots.indexOf(item.getSlot()));
				
			}
		}
		if(slots.isEmpty())
		{
			return -1;
		}
		
		return slots.get(0);
	}

	public void executeAllCommands(Player player)
	{
		for(TreasureCommand cmd : getCommands())
		{
			cmd.execute(player);
		}
	}

	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}






	public List<TreasureItem> getItems() {
		return items;
	}





	public void setItems(List<TreasureItem> items) {
		this.items = items;
	}





	public List<TreasureCommand> getCommands() {
		return commands;
	}





	public void setCommands(List<TreasureCommand> commands) {
		this.commands = commands;
	}



	public int getFreeSlot() {
		if(freeSlot==-2)
		{
			setFreeSlot(calculateFreeSlot());
		}
		return freeSlot;
	}



	public void setFreeSlot(int freeSlot) {
		this.freeSlot = freeSlot;
	}

	public List<String> getCommandsList()
	{
		List<String> cmds = new ArrayList<String>();
		// TODO Auto-generated method stub
		if(!(getCommands().isEmpty() || getCommands() == null))
		{
			for(TreasureCommand cmd : getCommands())
			{
				cmds.add(cmd.toString());
			}
		}
		
		return cmds;
	}


}
