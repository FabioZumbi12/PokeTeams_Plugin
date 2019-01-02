package io.github.TSEcho.PokeTeams.Commands.Admin;

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

public class Set implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		Player player = args.<Player>getOne(Text.of("player")).get();
		String team = args.<String>getOne(Text.of("team")).get();
		RoleRequirement role = new RoleRequirement(player);
		
		//checks to see if the player isn't in a team
		if(!role.inTeam()) {
			
			//check to see if team is valid
			if(!ConfigurationManager.storNode.getNode("Teams", team).isVirtual()) {
				
				ConfigurationManager.storNode.getNode("Teams", team, "Members", player.getName()).setValue("Grunt");
				ConfigurationManager.save();
				player.sendMessage(Text.of(TextColors.GREEN, "You have been placed in team " + team));
				src.sendMessage(Text.of(TextColors.GREEN, player.getName() + " has been placed in team " + team));
				
			} else {
				src.sendMessage(Texts.NOT_EXISTS);
			}
			
		} else {
			src.sendMessage(Texts.ALREADY_IN_TEAM);
		}
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_SET)
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
						GenericArguments.string(Text.of("team")))
				.executor(new Set())
				.build();
	}
}
	