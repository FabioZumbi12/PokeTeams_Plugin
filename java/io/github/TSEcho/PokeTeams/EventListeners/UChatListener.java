package io.github.TSEcho.PokeTeams.EventListeners;

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

public class UChatListener implements EventListener<SendChannelMessageEvent>{

	private SendChannelMessageEvent event;
	private Player player;
	private String message, prefix, chatColor;
	private Text newMessage, staffMessage;
	private RoleRequirement role;
	
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
		prefix = prefix.replaceAll("%teamtag%", role.getTag());
		
		chatColor = ConfigurationManager.confNode.getNode("Chat-Settings", "Chat-Color").getString();
		message = TextSerializers.FORMATTING_CODE.serialize(event.getMessage());
		
		newMessage = TextSerializers.FORMATTING_CODE.deserialize(prefix + chatColor + message);
		staffMessage = TextSerializers.FORMATTING_CODE.deserialize("&7" + message);
	
		//Modify message to include tag
		sendMessages();
		event.setCancelled(true);
	}
	
	//loops through online players and sends messages dependant on teams and social spy
	private void sendMessages() {		
		for(Player members : Sponge.getServer().getOnlinePlayers()) {
			
			if(inTeam(members)) {
				members.sendMessage(newMessage);
			} else if(isStaff(members)) {
				members.sendMessage(staffMessage);
			}
		}

		//if console social spy is true, messages console 
		if(ConfigurationManager.confNode.getNode("Chat-Settings", "Console-SocialSpy").getBoolean()) {
			Sponge.getServer().getConsole().sendMessage(newMessage);
		}
	}
	
	private boolean inTeam(Player members) {
		return !ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Members", members.getName()).isVirtual();
	}
	
	private boolean isStaff(Player members) {
		return ConfigurationManager.confNode.getNode("Chat-Settings", "Players-SocialSpy").getBoolean() && members.hasPermission(Permissions.SOCIAL_SPY);
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
