package io.github.TSEcho.PokeTeams.Utilities;

import java.util.LinkedHashMap;
import java.util.Map;

import org.spongepowered.api.entity.living.player.Player;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class RoleRequirement {
	
	private boolean inTeam = false;
	private String playerName, role, teamTemp, team;
	private Player player;
	private int amountPlayers = 0;
	private LinkedHashMap <String, Integer> roles = new LinkedHashMap<String, Integer>();
	private LinkedHashMap <Integer, String> ranks = new LinkedHashMap<Integer, String>();
	
	public RoleRequirement(Player user) {
		player = user;
		playerName = player.getName();
		fillRoles();
	}
	
	private void fillRoles() {
		roles.put("Grunt", 0);
		roles.put("Member", 1);
		roles.put("Officer", 2);
		roles.put("Owner", 3);
		
		ranks.put(0, "Grunt");
		ranks.put(1, "Member");
		ranks.put(2, "Officer");
		ranks.put(3, "Owner");
	}
	
	//returns if a place is in a team
	public boolean inTeam() {
		
		//loop through teams
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigurationManager
				.storNode.getNode("Teams").getChildrenMap().entrySet()) {
			
			teamTemp = teams.getKey().toString();
			
			//loops through members of the team
			for (Map.Entry<Object, ? extends CommentedConfigurationNode> members : ConfigurationManager
					.storNode.getNode("Teams", teamTemp, "Members").getChildrenMap().entrySet()) {
				
				amountPlayers++;
				
				//if a member matches the user trying to use home, return true
				if(members.getKey().toString().equals(playerName)) {
					inTeam = true;
					
					//set the team and the role of the user
					team = teamTemp;
					role = members.getValue().getString();
				}
			}
		}
		return inTeam;
	}
	
	//sets the user with a role based on integer input
	public void setRole(int newRole) {
		role = ranks.get(newRole);
		ConfigurationManager.storNode.getNode("Teams", team, "Members", playerName).setValue(role);
		ConfigurationManager.save();
	}
	
	//returns the place as in integer in the heirarchy
	public int getPlace() {
		return roles.get(role);
	}
	
	//returns the role of the player
	public String getRole() {
		return role;
	}
	
	//player does not have a team
	public boolean canCreate() {
		return ConfigurationManager.storNode.getNode("Teams", team).isVirtual();
	}
	
	//player can delete a team
	public boolean canDelete() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Delete").getBoolean();
	}
	
	//player can kick a member from a team
	public boolean canKick() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Kick").getBoolean();
	}
	
	//player can promote another player
	public boolean canPromote() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Promote").getBoolean();
	}
	
	//player can demote a player
	public boolean canDemote() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Demote").getBoolean();
	}
	
	//player can invite others to the team
	public boolean canInvite() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Invite").getBoolean();
	}
	
	//player can set a base
	public boolean canSetBase() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Base-Set").getBoolean();
	}
		
	//if the group the user is in can teleport
	public boolean canTeleport() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Base-Teleport").getBoolean();
	}
	
	//if the user has a base set in their team
	public boolean hasBase() {
		return !ConfigurationManager.storNode.getNode("Teams", team, "Location").isVirtual();
	}

	//X location in base
	public double getX() {
		return ConfigurationManager.storNode.getNode("Teams", team, "Location", "X").getDouble();
	}
	
	//Y Location in base
	public double getY() {
		return ConfigurationManager.storNode.getNode("Teams", team, "Location", "Y").getDouble();
	}

	//Z Location in base
	public double getZ() {
		return ConfigurationManager.storNode.getNode("Teams", team, "Location", "Z").getDouble();
	}
	
	//returns the amount of wins per this team
	public double getWins() {
		return Double.valueOf(ConfigurationManager.storNode.getNode("Teams", team, "Record", "Wins").getInt());
	}
	
	//returns the amount of losses per this team
	public double getLosses() {
		return Double.valueOf(ConfigurationManager.storNode.getNode("Teams", team, "Record", "Losses").getInt());
	}
	
	//returns the w/l ratio if losses are not zero
	public int getRatio() {
		if(ConfigurationManager.storNode.getNode("Teams", team, "Record", "Losses").getInt() != 0) {
			return (int) ((getWins() / (getWins() + getLosses())) * 100.0);
		} else {
			return 0;
		}
	}
	
	//returns the caught pokemon per team
	public int getCaught() {
		return ConfigurationManager.storNode.getNode("Teams", team, "Stats", "Caught").getInt();
	}
	
	//returns the kills per team
	public int getKills() {
		return ConfigurationManager.storNode.getNode("Teams", team, "Stats", "Kills").getInt();
	}
	
	//returns the amount of players in a team
	public int getMemberTotal() {
		return amountPlayers;
	}
	
	//returns the max amount of players in a team
	public int getMaxPlayers() {
		return ConfigurationManager.confNode.getNode("Team-Settings", "Max-Members").getInt();
	}
	
	//team name
	public String getTeam() {
		return team;
	}
}
