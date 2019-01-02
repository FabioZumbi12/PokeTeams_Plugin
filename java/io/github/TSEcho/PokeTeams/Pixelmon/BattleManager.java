package io.github.TSEcho.PokeTeams.Pixelmon;

import java.util.Map;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class BattleManager {

	private BattleEndEvent event;
	private Player winner, loser;
	private boolean winnerBool, loserBool;
	private RoleRequirement role, roleOther;
	
	public BattleManager(BattleEndEvent e) {
		
		event = e;
		
		if(isHuman()) {
	
			role = new RoleRequirement(winner);
			roleOther = new RoleRequirement(loser);
			
			if(areBattleTeams() || recordBattles()) {
				addStats();
				messagePlayers();	
			}
			
		} else if(isWildWin()) {
			
			role = new RoleRequirement(winner);
			addWildStats();
		}
	}
	
	//adding stats after battle
	private void addWildStats() {
		
		if(role.inTeam()) {
			ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Stats", "Kills").setValue(role.getKills() + 1);
			ConfigurationManager.save();
		}
	}
	
	//adding stats after battle
	private void addStats() {
		
		if(role.inTeam()) {
			ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Record", "Wins").setValue(role.getWins() + 1);
		}
		
		if(roleOther.inTeam()) {
			ConfigurationManager.storNode.getNode("Teams", roleOther.getTeam(), "Record", "Losses").setValue(roleOther.getLosses() + 1);
		}
		
		ConfigurationManager.save();
	}
	
	//sending message results
	private void messagePlayers() {
		
		if(role.inTeam()) {
			if(ConfigurationManager.storNode.getNode("Battle-Settings", "Message-Winners").getBoolean()) {
				winner.sendMessage(Text.of("You beat " + loser.getName() +  " - New Record: " + role.getWins() + "/" + role.getLosses()));
			}
		}

		if(roleOther.inTeam()) {
			if(ConfigurationManager.storNode.getNode("Battle-Settings", "Message-Losers").getBoolean()) {
				loser.sendMessage(Text.of("You lost against " + winner.getName() +  " - New Record: " + role.getWins() + "/" + role.getLosses()));
			}
		}
	}
	
	//checking if they are players
	private boolean isHuman() {
		
		//looping through battle participants to get the loser and winner
        for (Map.Entry<BattleParticipant, BattleResults> entry : event.results.entrySet()) {
            
        	if (entry.getValue() == BattleResults.VICTORY) {

	        	if((entry.getKey().getEntity() instanceof Player)) {
	        		winner = (Player) (Entity) entry.getKey().getEntity();
	            	winnerBool = true;
	        	}
        	}
        	if (entry.getValue() == BattleResults.DEFEAT) {
            
            	if((entry.getKey().getEntity() instanceof Player)) {
                	loser = (Player) (Entity) entry.getKey().getEntity();
                	loserBool = true;
        		}
    		}
        }
        //if there was a loser and winner, set their points
        if(loserBool && winnerBool) {
        	return true;
        } else {
        	winnerBool = loserBool = false;
        	return false;
        }
	}
	
	//checking if there is a winner of a wild battle
	private boolean isWildWin() {
		
		for (Map.Entry<BattleParticipant, BattleResults> entry : event.results.entrySet()) {
            
        	if ((entry.getValue() == BattleResults.VICTORY) && (entry.getKey().getEntity() instanceof Player)) {

        		winner = (Player) (Entity) entry.getKey().getEntity();
        		winnerBool = true;
        	}
        	
        	if((entry.getValue() == BattleResults.DEFEAT) && (entry.getKey().getEntity() instanceof EntityPixelmon)) {
            	loserBool = true;
        	}
        }
		
		if(winnerBool && loserBool) {
			return true;
		} else {
			return false;
		}
	}
	
	//if it should record all battles not just teams
	private boolean recordBattles() {
		return ConfigurationManager.confNode.getNode("Battle-Settings", "Record-All-Battles").getBoolean();
	}
	
	//if both players are on different teams
	private boolean areBattleTeams() {
		return (role.inTeam() && roleOther.inTeam()) && (!role.getTeam().equals(roleOther.getTeam()));
	}
}
