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

public class Promote implements CommandExecutor {

	private RoleRequirement role, roleOther;
	private CommandSource source;
	private Player promoPlayer;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		promoPlayer = args.<Player>getOne(Text.of("player")).get();
		source = src;
		
		if(src instanceof Player) {
			
			role = new RoleRequirement((Player) source);
			roleOther = new RoleRequirement(promoPlayer);
			
			//if the players are both in a team
			if(role.inTeam() && roleOther.inTeam()) {
				
				//if the players are in the same team
				if(role.getTeam().equals(roleOther.getTeam())) {
					
					//if the user can promote people
					if(role.canPromote()) {
						
						promote();
						
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
			src.sendMessage(Texts.NOT_PLAYER);
		}

		return CommandResult.success();
	}
	
	private void promote() {
		
		//if the role is a valid rank within the list
		if(roleOther.getPlace() + 1 < 4) {
			
			//if the user is 2 ranks above or is the owner
			if(role.getPlace() > roleOther.getPlace() + 1 || role.getPlace() == 3) {
				
				//if the user is the owner and the promotion is to an officer
				if(roleOther.getPlace() == 2 && role.getPlace() == 3) {
					
					roleOther.setRole(roleOther.getPlace() + 1);
					role.setRole(role.getPlace() - 1);
					
					source.sendMessage(Text.of(TextColors.GREEN, "You have promoted " + promoPlayer.getName() + " to " + roleOther.getRole()));
					source.sendMessage(Text.of(TextColors.RED, "You have demoted to " + role.getRole()));
					promoPlayer.sendMessage(Text.of(TextColors.GREEN, "You have promoted to " + roleOther.getRole()));
					
				} else {
					
					roleOther.setRole(roleOther.getPlace() + 1);
					
					source.sendMessage(Text.of(TextColors.GREEN, "You have promoted " + promoPlayer.getName() + " to " + roleOther.getRole()));
					promoPlayer.sendMessage(Text.of(TextColors.GREEN, "You have been promoted to " + roleOther.getRole()));
				}
				
			} else {
				source.sendMessage(Texts.INSUFFICIENT_RANK);
			}
		} else {
			source.sendMessage(Texts.CANT_PROMOTE);
		}
	}
	
	public static CommandSpec build() {
		
		return CommandSpec.builder()
				.permission(Permissions.PROMOTE)
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
				.executor(new Promote())
				.build();
	}
}
