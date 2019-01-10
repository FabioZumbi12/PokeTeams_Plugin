package io.github.TSEcho.PokeTeams.Commands.Admin;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;

public class Reset implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		String team = args.<String>getOne(Text.of("team")).get();
		
		//if the team exists set all values to 0 
		if(!ConfigurationManager.storNode.getNode("Teams", team).isVirtual()) {
			
			ConfigurationManager.storNode.getNode("Teams", team, "Record", "Wins").setValue(0);
			ConfigurationManager.storNode.getNode("Teams", team, "Record", "Losses").setValue(0);
			ConfigurationManager.storNode.getNode("Teams", team, "Stats", "Kills").setValue(0);
			ConfigurationManager.storNode.getNode("Teams", team, "Stats", "Caught").setValue(0);
			
			src.sendMessage(Texts.RESET_TEAM);
			
		} else {
			src.sendMessage(Text.of(TextColors.RED, team + " does not exist!"));
		}
		
		return CommandResult.success();
	}

	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_RESET)
				.arguments(GenericArguments.string(Text.of("team")))
				.executor(new Reset())
				.build();
	}
}
