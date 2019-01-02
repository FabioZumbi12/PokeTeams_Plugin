package io.github.TSEcho.PokeTeams.Commands.Admin;

import java.util.Map;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.Utils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Delete implements CommandExecutor {

	private String team;
	private CommandSource source;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
				
		source = src;
		team = args.<String>getOne(Text.of("team")).get();
		
		//if the team exists in the config
		if(!ConfigurationManager.storNode.getNode("Teams", team).isVirtual()) {
			
			//send messages to all players and delete the team
			sendMessages();
			ConfigurationManager.storNode.getNode("Teams", team).setValue(null);
			ConfigurationManager.save();
			
		} else {
			src.sendMessage(Texts.NOT_EXISTS);
		}

		return CommandResult.success();
	}
	
	private void sendMessages() { 
		
		boolean sentDelete = false;
		
		//looping through team members that are online to alert them
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigurationManager
				.storNode.getNode("Teams", team, "Members").getChildrenMap().entrySet()) {
			
			String member = teams.getKey().toString();
			Utils.removeChat(member);
			
			//send message to all team members + the source 
			if(Sponge.getServer().getPlayer(member).isPresent()) {
				Sponge.getServer().getPlayer(member).get().sendMessage(Text.of(TextColors.RED, team + " has been disbanded!"));
				
				//if the source is a player and the source is in the team make sure the extra message isn't set
				if(source instanceof Player && source.getName().equals(member)) {
					sentDelete = true;
				}
			}
			
			if(!sentDelete) {
				source.sendMessage(Text.of(TextColors.RED, team + " has been disbanded!"));
			}
		}
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_DELETE)
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("team"))))
				.executor(new Delete())
				.build();
	}
	
}
