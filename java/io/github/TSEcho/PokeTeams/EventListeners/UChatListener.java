package io.github.TSEcho.PokeTeams.EventListeners;

import java.util.HashSet;
import java.util.Map;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import br.net.fabiozumbi12.UltimateChat.Sponge.API.SendChannelMessageEvent;
import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import io.github.TSEcho.PokeTeams.Utilities.Utils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class UChatListener implements EventListener<SendChannelMessageEvent>{

	private SendChannelMessageEvent event;
	private Player player;
	private String user;
	private String message, prefix, chatColor;
	private Text newMessage, staffMessage;
	private RoleRequirement role;
	private HashSet<String> sendPlayers = new HashSet<String>();
	
	@Override
	public void handle(SendChannelMessageEvent e) throws Exception {
		
		event = e;
		
		if(isPlayer()) {
			if(inGroupChat()) {
				modifyMessage();
			}
		}
	}
	
	private void modifyMessage() {
		
		prefix = ConfigurationManager.confNode.getNode("Chat-Settings", "Prefix").getString();
		prefix = prefix.replaceAll("%team%", role.getTeam());
		prefix = prefix.replaceAll("%player%", player.getName());
		
		chatColor = ConfigurationManager.confNode.getNode("Chat-Settings", "Chat-Color").getString();
		message = TextSerializers.FORMATTING_CODE.serialize(event.getMessage());
		
		newMessage = TextSerializers.FORMATTING_CODE.deserialize(prefix + chatColor + message);
		staffMessage = TextSerializers.FORMATTING_CODE.deserialize("&7" + message);
	
		//Modify message to include tag
		sendMessages();
		event.setCancelled(true);
	}
	
	//sends new message to all members of a team
	private void sendMessages() {
	
		//loop through members
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> members : ConfigurationManager
				.storNode.getNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet()) {
			
			//adds user to the list of people to send the message to
			user = members.getKey().toString();
			sendPlayers.add(user);
		}
		
		//if social spy is true, send messages to staff
		if(ConfigurationManager.confNode.getNode("Chat-Settings", "Players-SocialSpy").getBoolean()) {
			
			for(Player staff : Sponge.getServer().getOnlinePlayers()) {
				
				if(staff.hasPermission(Permissions.SOCIAL_SPY)) {
					
					staff.sendMessage(staffMessage);
				}
			}
		}
		
		//sends the message if the player is present
		if(Sponge.getServer().getPlayer(user).isPresent()) {
			Sponge.getServer().getPlayer(user).get().sendMessage(newMessage);
		}
		
		//if console social spy is true, messages console 
		if(ConfigurationManager.confNode.getNode("Chat-Settings", "Console-SocialSpy").getBoolean()) {
			Sponge.getServer().getConsole().sendMessage(newMessage);
		}
	}
	
	private boolean isPlayer() {
		
		//returns true if the source is a player and are in a chat
		if(event.getSender() instanceof Player) {
			
			player = (Player) event.getSource();
			role = new RoleRequirement(player);
			
			return true;
			
		} else {
			return false;
		}
	}
	
	private boolean inGroupChat() {
		return role.inTeam() && Utils.inChat(player.getName());
	}
}
