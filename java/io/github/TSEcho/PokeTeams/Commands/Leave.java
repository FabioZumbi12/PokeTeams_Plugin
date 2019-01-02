package io.github.TSEcho.PokeTeams.Commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import io.github.TSEcho.PokeTeams.Utilities.Utils;

public class Leave implements CommandExecutor {

	private RoleRequirement role;
	private Player player;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {
			
			player = (Player) src;
			role = new RoleRequirement(player);
			
			//if the user is in a team
			if(role.inTeam()) {
				
				//if the user is the owner and the total is just them
				if(canOwnerDelete()) {
					
					ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Members", src.getName()).setValue(null);
					ConfigurationManager.save();
					Utils.removeChat(player.getName());
					src.sendMessage(Texts.LEFT);
				
					//if the team is now empty, delete it
					if(ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet().isEmpty()) {
						
						ConfigurationManager.storNode.getNode("Teams", role.getTeam()).setValue(null);
						src.sendMessage(Texts.DELETED_TEAM);
						ConfigurationManager.save();
					} 	
					
				} else {
					
					//if the user is not the owner
					if(role.getPlace() != 3) {
						
						ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Members", player.getName()).setValue(null);
						ConfigurationManager.save();
						Utils.removeChat(player.getName());
						src.sendMessage(Texts.LEFT);
						
					} else {
						
						src.sendMessage(Texts.NEEDS_LEADER);
					}
				}
		
			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
			
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}
		
		return CommandResult.success();
	}

	private boolean canOwnerDelete() {
		return role.getRole().equals("Owner") && role.getMemberTotal() == 1;
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.LEAVE)
				.executor(new Leave())
				.build();
	}
}
