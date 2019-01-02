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

import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class Demote implements CommandExecutor {

	private RoleRequirement role, roleOther;
	private CommandSource source;
	private Player demoPlayer;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		demoPlayer = args.<Player>getOne(Text.of("player")).get();
		source = src;
		roleOther = new RoleRequirement(demoPlayer);
		
		if(src instanceof Player) {
			
			role = new RoleRequirement((Player) source);
			
			//if the players are both in a team
			if(role.inTeam() && roleOther.inTeam()) {
				
				//if the players are in the same team
				if(role.getTeam().equals(roleOther.getTeam())) {
					
					//if the user is able to demote a player
					if(role.canDemote()) {
						
						demote();
						
					} else {
						src.sendMessage(Texts.INSUFFICIENT_RANK);
					}
				} else {
					src.sendMessage(Texts.BOTH_NOT_IN_TEAM);
				}
			} else {
				src.sendMessage(Texts.BOTH_NOT_IN_TEAM);
			}
			
		} else {
			doAction();
		}
		
		return CommandResult.success();
	}
	
	private void demote() {
		
		//if the role is a valid rank within the list
		if(roleOther.getPlace() - 1 >= 0) {
			
			//if the user is below the player
			if(role.getPlace() > roleOther.getPlace()) {
					
				doAction();
				
			} else {
				source.sendMessage(Texts.INSUFFICIENT_RANK);
			}
			
		} else {
			source.sendMessage(Texts.CANT_DEMOTE);
		}
	}
	
	private void doAction() {
		
		roleOther.setRole(roleOther.getPlace() - 1);
		
		source.sendMessage(Text.of(TextColors.GREEN, "You have demoted " + demoPlayer.getName() + " to " + roleOther.getRole()));
		demoPlayer.sendMessage(Text.of(TextColors.GREEN, "You have demoted to " + roleOther.getRole()));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.DEMOTE)
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
				.executor(new Demote())
				.build();
	}
}
