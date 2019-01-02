package io.github.TSEcho.PokeTeams.Commands;

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

	private RoleRequirement role, roleOther;
	private CommandSource source;
	private Player kickPlayer;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		kickPlayer = args.<Player>getOne(Text.of("player")).get();
		source = src;
		role = new RoleRequirement((Player) source);
		roleOther = new RoleRequirement(kickPlayer);
		
		if(src instanceof Player) {
			
			//if they are both in teams
			if((role.inTeam() && roleOther.inTeam()) || (role.getTeam().equals(roleOther.getTeam()))) {
							
				//if the user has the permissions to kick
				if(role.canKick()) {
					
					kick();
					
				} else {
					src.sendMessage(Texts.INSUFFICIENT_RANK);
				}
				
			} else {
				src.sendMessage(Texts.BOTH_NOT_IN_TEAM);
			}
			
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}

		
		return CommandResult.success();
	}
	
	private void kick() {
		
		//if the user is not kicking themselves
		if(!source.getName().equals(kickPlayer.getName())) {
			
			//if the user is below the player
			if(role.getPlace() > roleOther.getPlace()) {
					
				ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Members", kickPlayer.getName()).setValue(null);
				ConfigurationManager.save();
				source.sendMessage(Text.of(TextColors.GREEN, "You have kicked " + kickPlayer.getName()));
				Utils.removeChat(kickPlayer.getName());
				
				if(kickPlayer.isOnline()) {
					kickPlayer.sendMessage(Text.of(TextColors.RED, "You have kicked from " + role.getTeam()));
				}
				
			} else {
				source.sendMessage(Texts.INSUFFICIENT_RANK);
			}	
		} else {
			source.sendMessage(Texts.KICK_YOURSELF);
		}

	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.KICK)
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
				.executor(new Kick())
				.build();
	}
}
