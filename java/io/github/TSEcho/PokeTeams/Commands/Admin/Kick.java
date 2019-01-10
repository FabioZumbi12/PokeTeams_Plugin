package io.github.TSEcho.PokeTeams.Commands.Admin;

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
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import io.github.TSEcho.PokeTeams.Utilities.Utils;

public class Kick implements CommandExecutor {

	private RoleRequirement role;
	private String team;
	private CommandSource source;
	private Player player;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		team = args.<String>getOne(Text.of("team")).get();
		player = args.<Player>getOne(Text.of("player")).get();
		source = src;
		role = new RoleRequirement(player);
		
		
		if(!ConfigurationManager.storNode.getNode("Teams", team).isVirtual()) {
			
			if(role.inTeam()) {
				
				if(role.getTeam().equals(team)) {
					
					kickPlayer();
					
				} else {
					src.sendMessage(Text.of(TextColors.RED, player.getName() + " is not in this team"));
				}
				
			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
			
		} else {
			src.sendMessage(Text.of(Text.of(TextColors.RED, team + " does not exist")));
		}


		
		return CommandResult.success();
	}
	
	private void kickPlayer() {
		
		//if the user being kicked is the owner
		if(role.getPlace() != 3) {
			
			//if there is only 1 player
			if(role.getMemberTotal() == 1) {
				
				ConfigurationManager.storNode.getNode("Teams", team).setValue(null);
				ConfigurationManager.save();
				messagePlayer();
				Utils.removeChat(player.getName());
				
			} else {
				
				ConfigurationManager.storNode.getNode("Teams", team, player.getName()).setValue(null);
				ConfigurationManager.save();
				Utils.removeChat(player.getName());
				messagePlayer();
			}
			
		} else {
			source.sendMessage(Texts.NEEDS_LEADER);
		}
	}
	
	private void messagePlayer() {
		if(Sponge.getServer().getPlayer(player.getName()).isPresent()) {
			Sponge.getServer().getPlayer(player.getName()).get()
				.sendMessage(Text.of(TextColors.RED, "You have been kicked from " + team));
		}
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_KICK)
				.arguments(GenericArguments.seq
						(GenericArguments.string(Text.of("team"))),
						GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
				.executor(new Kick())
				.build();
	}
}