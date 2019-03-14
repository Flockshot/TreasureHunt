package me.flockshot.treasurehunt.commands;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import me.flockshot.treasurehunt.TreasureHuntPlugin;
import me.flockshot.treasurehunt.treasurechest.TreasureChest;
import net.md_5.bungee.api.ChatColor;

public class TreasureHuntCommand implements CommandExecutor {
	TreasureHuntPlugin plugin;

	public TreasureHuntCommand(TreasureHuntPlugin treasureHuntPlugin) {
		plugin = treasureHuntPlugin;
	}

	String command = "/treasurehunt ";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(args.length >= 1)		
			if(sender.hasPermission("treasurehunt.admin." + args[0].toLowerCase()))			
				switch (args[0].toLowerCase())
				{
				    case "addchest":
					    addChest(sender, args.length >= 3 ? args[1] : "", args.length >= 3 ? args[2] : "");
					    break;
				    case "removechest":
					    removeChest(sender, args.length >= 2 ? args[1] : "");
					    break;
				    case "changetype":
					    changeType(sender, args.length >= 2 ? args[1] : "", args.length >= 3 ? args[2] : "");
					    break;
				    case "reset":
					    resetChest(sender, args.length >= 2 ? args[1] : "");
					    break;
				    case "createtype":
				        createType(sender, args.length >= 2 ? args[1] : "");
					    break;
				    case "deltype":
				        delType(sender, args.length >= 2 ? args[1] : "");
					    break;
					// case "additem":
					// addItems(sender);
					// case "additems":
					// setItems(sender, args.length>=2 ? NumberUtility.isNum(args[1]) ?
					// Double.valueOf(args[1]) : 100.0 : 100.0);
					// case "addcommand":
					// addCommand(sender, args.length>=2 ?
					// Arrays.toString(args).toLowerCase().replace("addcommand ", "") : "");
				    case "reload":
				        reload(sender);
				        break;
				    default:
				        commandInfo(sender);
				}			
			else
			    pluginInfo(sender);
		else
		    pluginInfo(sender);

		return false;
	}

	private void reload(CommandSender sender)
	{
		plugin.reloadConfig();
		plugin.saveConfig();
		plugin.onDisable();
		plugin.onEnable();
		sender.sendMessage(ChatColor.DARK_GREEN + "Plugin reloaded ");
	}

	private void delType(CommandSender sender, String type)
	{
		// TODO Auto-generated method stub
		if(!(type.isEmpty() || type.equals("")))
		{
		    if(plugin.getTTypeManager().exists(type))
            {
                plugin.getTTypeManager().delType(type);
                sender.sendMessage(ChatColor.DARK_GREEN + "Deleted the treasure type with name " + type);
            }
            else
                sender.sendMessage(ChatColor.DARK_RED + "That Treasure type already exists");
		}			
		else
		    sender.sendMessage(ChatColor.YELLOW + command + "deltype [type]");
	}

	private void createType(CommandSender sender, String type)
	{
		if(sender instanceof Player)
		{
			if(!(type.isEmpty() || type.equals("")))
			{
				if(!plugin.getTTypeManager().exists(type))
				{
					ItemStack item = new ItemStack(Material.IRON_INGOT, 1);;
					if(((Player) sender).getInventory().getItemInMainHand() != null)
					    if(!((Player) sender).getInventory().getItemInMainHand().getType().equals(Material.AIR))
                            item = ((Player) sender).getInventory().getItemInMainHand();

					plugin.getTTypeManager().addType(type, item);
					sender.sendMessage(ChatColor.DARK_GREEN + "Created a new treasure type");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "That Treasure type already exists");
			}
			else sender.sendMessage(ChatColor.YELLOW + command + "createtype [type]");
		}
		else sender.sendMessage(ChatColor.DARK_RED + "Only players can use that command");
	}

	private void addChest(CommandSender sender, String name, String type)
	{
		if(sender instanceof Player)
		{
			if(!(name.isEmpty() || name.equals("") || type.isEmpty() || type.equals("")))
			{
				if(!plugin.getTCManager().exists(name))
				{
					if(plugin.getTTypeManager().exists(type))
					{
					    @SuppressWarnings("deprecation")
						Block block = ((Player) sender).getTargetBlock((HashSet<Byte>) null, 50);
						if(block == null)
						{
							sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
							return;
						}
						if(block.getType().equals(Material.CHEST))
						{
							// To get the inventory location, incase the chest is large.
						    
						    
						    Location loc = ((Chest) block.getState()).getInventory().getLocation();						    

						    
							Bukkit.broadcastMessage(loc+"");

							plugin.getTCManager().createTreasureChest(new TreasureChest(name, loc, false, plugin.getTTypeManager().getTreasureType(type)));

							sender.sendMessage(ChatColor.DARK_GREEN + "Successfully created a new Treasure Chest");
						}
						else sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
					}
					else sender.sendMessage(ChatColor.DARK_RED + "Invalid Type: use " + command	+ "createtype [type] to create a type first");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name already exists");
			}
			else sender.sendMessage(ChatColor.YELLOW + command + "addchest [name] [type]");
		}
		else sender.sendMessage(ChatColor.DARK_RED + "Only players can use that command");
	}

	private void removeChest(CommandSender sender, String name)
	{
		if(sender instanceof Player)
		{
			if(name.isEmpty() || name.equals(""))
			{
				@SuppressWarnings("deprecation")
				Block block = ((Player) sender).getTargetBlock((HashSet<Byte>) null, 50);
				
				if(block != null)
				{
					if(block.getType().equals(Material.CHEST))
					{
						// To get the inventory location, incase the chest is large.
						Location loc = ((Chest) block).getBlockInventory().getLocation();

						if(plugin.getTCManager().exists(loc))
						{
							String success = plugin.getTCManager().removeTreasureChest(name) ? "§2Successfully removed"	: "§4Failed to remove";
							sender.sendMessage(success + " the treasure chest you are looking ");
						}
						else sender.sendMessage(ChatColor.DARK_RED + "The chest you are looking at is not a treasure chest");
					}
					else sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");

			}
			else
			{
				if(plugin.getTCManager().exists(name))
				{
					String success = plugin.getTCManager().removeTreasureChest(name) ? "§2Successfully removed"	: "§4Failed to remove";
					sender.sendMessage(success + " the treasure chest with name " + name);
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name does not exist");
			}
		}
		else
		{
			if(!(name.isEmpty() || name.equals("")))
			{
				if(plugin.getTCManager().exists(name))
				{
					String success = plugin.getTCManager().removeTreasureChest(name) ? "§2Successfully removed" : "§4Failed to remove";
					sender.sendMessage(success + " the treasure chest with name " + name);
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name does not exist");
			}
			else sender.sendMessage(ChatColor.YELLOW + command + "removechest [name]");
		}
	}

	private void changeType(CommandSender sender, String type, String name)
	{
		if(sender instanceof Player)
		{
			if(name.isEmpty() || name.equals(""))
			{
				@SuppressWarnings("deprecation")
				Block block = ((Player) sender).getTargetBlock((HashSet<Byte>) null, 50);
				
				if(block == null)
				{
					sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
					return;
				}
				if(block.getType().equals(Material.CHEST))
				{
					// To get the inventory location, incase the chest is large.
					Location loc = ((Chest) block).getBlockInventory().getLocation();

					if(plugin.getTCManager().exists(loc))
					{
						if(plugin.getTTypeManager().exists(type))
						{
							plugin.getTCManager().changeType(name, type);
							sender.sendMessage(ChatColor.DARK_GREEN	+ "Successfully changed type of treasure chest you were looking at to " + type);
						}
						else sender.sendMessage(ChatColor.DARK_RED + "Invalid Type: use " + command	+ "createtype [type] to create a type first");
					}
					else sender.sendMessage(ChatColor.DARK_RED + "The chest you are looking at is not a treasure chest");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
			}
			else
			{
				if(plugin.getTCManager().exists(name))
				{
					plugin.getTCManager().changeType(name, type);
					sender.sendMessage(ChatColor.GREEN + "Successfully changed type of " + name + " to " + type);
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name does not exist");
			}
		}
		else
		{
			if(!(name.isEmpty() || name.equals("")))
			{
				if(plugin.getTTypeManager().exists(type))
				{
					if(plugin.getTCManager().exists(name))
					{
						plugin.getTCManager().changeType(name, type);
						sender.sendMessage(ChatColor.DARK_GREEN + "Successfully changed type of " + name + " to " + type);
					}
					else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name does not exist");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "Invalid Type: use " + command	+ "createtype [type] to create a type first");
			}
			else sender.sendMessage(ChatColor.YELLOW + command + "changetype [type] [name]");
		}
	}

	private void resetChest(CommandSender sender, String name)
	{
		if(sender instanceof Player)
		{
			if(name.isEmpty() || name.equals(""))
			{
				@SuppressWarnings("deprecation")
				Block block = ((Player) sender).getTargetBlock((HashSet<Byte>) null, 50);
				
				if(block == null)
				{
					sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
					return;
				}
				if(block.getType().equals(Material.CHEST))
				{
					// To get the inventory location, incase the chest is large.
					Location loc = ((Chest) block).getBlockInventory().getLocation();

					if(plugin.getTCManager().exists(loc))
					{
						plugin.getTCManager().reset(name);
						sender.sendMessage(ChatColor.DARK_GREEN + "The treasure chest with name " + name + " has been reset");
					}
					else sender.sendMessage(ChatColor.DARK_RED + "The chest you are looking at is not a treasure chest");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The block you are looking at is not a chest");
				
			}
			else
			{
				if(name.equals("all"))
				{
					plugin.getTCManager().resetAll();
					sender.sendMessage(ChatColor.DARK_GREEN + "All treasure chests have been reset");
				}
				else if(plugin.getTCManager().exists(name))
				{
					plugin.getTCManager().reset(name);
					sender.sendMessage(ChatColor.DARK_GREEN + "The treasure chest with name " + name + " has been reset");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name does not exist");
			}
		}
		else
		{
			if(!(name.isEmpty() || name.equals("")))
			{
				if(name.equals("all"))
				{
					plugin.getTCManager().resetAll();
					sender.sendMessage(ChatColor.DARK_GREEN + "All treasure chests have been reset");
				}
				else if (plugin.getTCManager().exists(name))
				{
					plugin.getTCManager().reset(name);
					sender.sendMessage(ChatColor.DARK_GREEN + "The treasure chest with name " + name + " has been reset");
				}
				else sender.sendMessage(ChatColor.DARK_RED + "The treasure chest with that name does not exist");
			}
			else sender.sendMessage(ChatColor.YELLOW + command + "reset [name/all]");
		}
	}

	private void commandInfo(CommandSender sender)
	{
		sender.sendMessage(ChatColor.YELLOW + command + "addchest [name] [type]");
		sender.sendMessage(ChatColor.GRAY + "Makes the chest you are looking at a treasure chest");
		sender.sendMessage(ChatColor.YELLOW + command + "removechest (name)");
		sender.sendMessage(ChatColor.GRAY + "Removes the chest you are looking at , or the by name, as treasure chest");
		sender.sendMessage(ChatColor.YELLOW + command + "changetype [type] (name)");
		sender.sendMessage(ChatColor.GRAY + "Changes the type of loot of the chest you are looking at or by name");
		sender.sendMessage(ChatColor.YELLOW + command + "reset [name/all]");
		sender.sendMessage(ChatColor.GRAY + "All treasure chests loot will get reset or the one you are looking at or by name");
		sender.sendMessage(ChatColor.YELLOW + command + "createtype [type]");
		sender.sendMessage(ChatColor.GRAY + "Makes a loot type with the given type name");
		sender.sendMessage(ChatColor.YELLOW + command + "deltype [type]");
		sender.sendMessage(ChatColor.GRAY + "Delete the loot type with the given type name");
		// sender.sendMessage(ChatColor.YELLOW + command + "additem [type] [chance]
		// [slot]");
		// sender.sendMessage(ChatColor.GRAY + "Add the item you are holding, in the
		// treasure type");
		// sender.sendMessage(ChatColor.YELLOW + command + "additems [type]");
		// sender.sendMessage(ChatColor.GRAY + "Add all the items in your inventory to
		// the treasure type");
		// sender.sendMessage(ChatColor.YELLOW + command + "addcommand [type]");
		// sender.sendMessage(ChatColor.GRAY + "Add a command to the treasure type");
	}

	private void pluginInfo(CommandSender sender)
	{
		PluginDescriptionFile p = plugin.getDescription();
		
		sender.sendMessage(ChatColor.YELLOW + "Plugin" + ChatColor.GRAY + ": " + ChatColor.DARK_GREEN + p.getName());
		sender.sendMessage(ChatColor.YELLOW + "Version" + ChatColor.GRAY + ": " + ChatColor.DARK_GREEN + p.getVersion());
		sender.sendMessage(ChatColor.YELLOW + "Made by" + ChatColor.GRAY + ": " + ChatColor.DARK_GREEN + p.getAuthors());
		sender.sendMessage(ChatColor.YELLOW + "Description" + ChatColor.GRAY + ": " + ChatColor.DARK_GREEN + p.getDescription());
	}

}
