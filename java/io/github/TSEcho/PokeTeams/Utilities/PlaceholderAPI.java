package io.github.TSEcho.PokeTeams.Utilities;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;

public class PlaceholderAPI {
	
	public static String getTeam(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) {
			return role.getTeam();
		} else {
			return ConfigurationManager.confNode.getNode("Placeholder-Settings", "Default-TeamName").getString();
		}
	}
	
	public static String getTag(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) { 
			return role.getTag();
		} else {
			return ConfigurationManager.confNode.getNode("Placeholder-Settings", "Default-TeamTag").getString();
		}
	}
	
	public static String getWins(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) {
			return String.valueOf(role.getWins());
		} else {
			return String.valueOf(0);
		}
	}
	
	public static String getLosses(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) {
			return String.valueOf(role.getLosses());
		} else {
			return String.valueOf(0);
		}
	}
	
	public static String getRatio(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) {
			return String.valueOf(role.getRatio() + "%");
		} else {
			return String.valueOf(0 + "%");
		}
	}
	
	public static String getKills(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) {
			return String.valueOf(role.getKills());
		} else {
			return String.valueOf(0);
		}
	}
	
	public static String getCaught(CommandSource src) {
		RoleRequirement role = new RoleRequirement((Player) src);
		
		if(role.inTeam()) {
			return String.valueOf(role.getCaught());
		} else {
			return String.valueOf(0);
		}
	}

}
