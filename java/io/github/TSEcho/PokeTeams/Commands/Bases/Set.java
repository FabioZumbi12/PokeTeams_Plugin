package io.github.TSEcho.PokeTeams.Commands.Bases;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class Set implements CommandExecutor {

	private RoleRequirement role;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {
			
			role = new RoleRequirement((Player) src);
			
			//check if they have a team, if they are able to teleport, and if a base is set
			if(role.inTeam()) {
				
				//if the user is able to set a base
				if(role.canSetBase()) {
					
					setBase((Player) src);
					
				} else {
					src.sendMessage(Texts.INSUFFICIENT_RANK);
				}
				
			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
		}
		return CommandResult.success();
	}
	
	private void setBase(Player player) {
		ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Location", "X").setValue(player.getLocation().getX());
		ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Location", "Y").setValue(player.getLocation().getY());
		ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Location", "Z").setValue(player.getLocation().getZ());
		ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Location", "World").setValue(player.getWorldUniqueId().get().toString());
		ConfigurationManager.save();
		player.sendMessage(Text.of(TextColors.GREEN, "Your base location is now " + (int) role.getX() 
							+ ", " + (int) role.getY() + ", " + (int) role.getZ()));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.BASE_SET)
				.executor(new Set())
				.build();
	}
}
