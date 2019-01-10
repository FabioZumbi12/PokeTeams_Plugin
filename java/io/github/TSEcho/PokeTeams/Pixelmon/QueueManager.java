package io.github.TSEcho.PokeTeams.Pixelmon;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

import io.github.TSEcho.PokeTeams.PokeTeams;
import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import net.minecraft.entity.player.EntityPlayerMP;

public class QueueManager {

	private static Player player, player1, player2;
	private static int i, j;
	private static String playerName1, playerName2;
	public static ArrayList<String> queue = new ArrayList<String>();
	
	//is used when a player executes the add command
	public static void addMember(Player user) {
		
		player = user;
		
		//add player to the queue then execute choosing of opponents
		if(!queue.contains(player.getName())) {
			queue.add(player.getName());
			player.sendMessage(Texts.ADDED_QUEUE);
		} else {
			player.sendMessage(Texts.ALREADY_IN_QUEUE);
		}
	}
	
	public static void choosePlayers() {
		
		//looping through map and matching up players
		for(i = 0; i < queue.size(); i++) {
			
			for(j = 0; j < queue.size(); j++) {
				
				//quick check to make sure that slot is valid with a player's information
				if(!queue.get(i).isEmpty() && !queue.get(j).isEmpty()) {
					
					//only if the match isn't the same person
					if(!queue.get(i).equals(queue.get(j))) {
						
						playerName1 = queue.get(i);
						playerName2 = queue.get(j);
						
						startBattle();
					}
				}
			}
		}
	}
	
	private static void startBattle() {
		
		//optional checking grabs player's by name
		if(Sponge.getServer().getPlayer(playerName1).isPresent() && Sponge.getServer().getPlayer(playerName2).isPresent()) {
			
			player1 = Sponge.getServer().getPlayer(playerName1).get();
			player2 = Sponge.getServer().getPlayer(playerName2).get();
			
			if(player1.isOnline() && player2.isOnline()) {
				
				RoleRequirement role1 = new RoleRequirement(player1);
				RoleRequirement role2 = new RoleRequirement(player2);

				//making sure they are still in a team and they aren't in the same team
				if((role1.inTeam() && role2.inTeam()) && (!role1.getTeam().equals(role2.getTeam()))) {
					
					player1.sendMessage(Texts.START_BATTLE);
					player2.sendMessage(Texts.START_BATTLE);
	
					removeQueue();
				
					Task.builder()
						.name(PokeTeams.ID + "-Battle")
						.delay(5, TimeUnit.SECONDS)
						.execute(() -> forceBattle())
						.submit(PokeTeams.getInstance());
				}
			}
		}
	}
	
	private static void forceBattle() {
		
		PokeTeams.getInstance().getLogger().info("DEBUG: " + player1.getName() + " is in battle with " + player2.getName());
		
		Optional<PlayerStorage> opt1 = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player1);
		Optional<PlayerStorage> opt2 = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) player2);

		if(opt1.isPresent()) {
			
		    PlayerStorage storage1 = opt1.get();
			EntityPixelmon pokemon1 = storage1.getFirstAblePokemon((net.minecraft.world.World) player1.getWorld());
		
			if(opt2.isPresent()) {
				
			    PlayerStorage storage2 = opt2.get();
				EntityPixelmon pokemon2 = storage2.getFirstAblePokemon((net.minecraft.world.World) player2.getWorld());
				
				PlayerParticipant battler1 = new PlayerParticipant((EntityPlayerMP) player1, pokemon1);
				PlayerParticipant battler2 = new PlayerParticipant((EntityPlayerMP) player2, pokemon2);
				
				battler1.startedBattle = true;
				battler2.startedBattle = true;
				
				new BattleControllerBase(battler1, battler2);

			}
		}
	}
	
	private static void removeQueue() {
		
		//removing the two from the queue
		queue.remove(playerName1);
		queue.remove(playerName2);
		
		//Loops through the arrayList, replaces any blank spots with the value above its index
		for(int l = 0; l < queue.size(); l++) {
			
			if(queue.get(l).isEmpty()) {
				if(!queue.get(l + 1).isEmpty()) {
					queue.set(l, queue.get(l + 1));
				}
			}
		}
		ConfigurationManager.save();
	}
}
