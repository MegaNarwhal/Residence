package com.bekvon.bukkit.residence.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.cmd;
import com.bekvon.bukkit.residence.itemlist.WorldItemManager;
import com.bekvon.bukkit.residence.permissions.PermissionManager;
import com.bekvon.bukkit.residence.protection.WorldFlagManager;
import com.bekvon.bukkit.residence.text.help.HelpEntry;

public class reload implements cmd {

    @Override
    public boolean perform(String[] args, boolean resadmin, Command command, CommandSender sender) {
	if (!resadmin && !sender.isOp()) {
	    sender.sendMessage(Residence.getLM().getMessage("General.NoPermission"));
	    return true;
	}

	if (args.length != 2) {
	    return false;
	}

	if (args[1].equalsIgnoreCase("lang")) {
	    Residence.getLM().LanguageReload();
	    File langFile = new File(new File(Residence.dataFolder, "Language"), Residence.getConfigManager().getLanguage() + ".yml");
	    BufferedReader in = null;
	    try {
		in = new BufferedReader(new InputStreamReader(new FileInputStream(langFile), "UTF8"));
	    } catch (UnsupportedEncodingException e1) {
		e1.printStackTrace();
	    } catch (FileNotFoundException e1) {
		e1.printStackTrace();
	    }
	    if (langFile.isFile()) {
		FileConfiguration langconfig = new YamlConfiguration();
		try {
		    langconfig.load(in);
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (InvalidConfigurationException e) {
		    e.printStackTrace();
		}
		Residence.helppages = HelpEntry.parseHelp(langconfig, "CommandHelp");
	    } else {
		System.out.println(Residence.prefix + " Language file does not exist...");
	    }
	    sender.sendMessage(Residence.prefix + " Reloaded language file.");
	    return true;
	} else if (args[1].equalsIgnoreCase("config")) {
	    Residence.getConfigManager().UpdateConfigFile();
	    sender.sendMessage(Residence.prefix + " Reloaded config file.");
	    return true;
	} else if (args[1].equalsIgnoreCase("groups")) {
	    Residence.getConfigManager().loadGroups();
	    Residence.gmanager = new PermissionManager();
	    Residence.wmanager = new WorldFlagManager();
	    sender.sendMessage(Residence.prefix + " Reloaded groups file.");	 
	    return true;   
	} else if (args[1].equalsIgnoreCase("flags")) {
	    Residence.getConfigManager().loadFlags();
	    Residence.gmanager = new PermissionManager();
	    Residence.imanager = new WorldItemManager();
	    Residence.wmanager = new WorldFlagManager();
	    sender.sendMessage(Residence.prefix + " Reloaded flags file.");	
	    return true;
	}
	return false;
    }
}
