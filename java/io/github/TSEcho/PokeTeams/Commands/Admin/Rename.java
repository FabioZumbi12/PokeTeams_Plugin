package io.github.TSEcho.PokeTeams.Commands.Admin;

import java.util.Map;

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
import io.github.TSEcho.PokeTeams.Utilities.CensorCheck;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Rename implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		String team = args.<String>getOne(Text.of("team")).get();
		String name = args.<String>getOne(Text.of("name")).get();
		
		//if the team exists, change the key and put all values for the old copy in its place
		if(!ConfigurationManager.storNode.getNode("Teams", team).isVirtual()) {
			
			//if the name doesnt clash with filter
			if(!CensorCheck.containsSwear(name)) {
				
				Map<Object, ? extends CommentedConfigurationNode> teamCopy = ConfigurationManager.storNode.getNode("Teams", team).getChildrenMap();
				ConfigurationManager.storNode.getNode("Teams", name).setValue(teamCopy);
				ConfigurationManager.storNode.getNode("Teams", team).setValue(null);
				ConfigurationManager.save();
				
				src.sendMessage(Text.of(TextColors.GREEN, team + " is now called " + name));
				
			} else {
				src.sendMessage(Texts.INNAPROPRIATE);
			}
			
		} else {
			src.sendMessage(Text.of(TextColors.RED, team + " does not exist!"));
		}
		
		return CommandResult.success();
	}

	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_RENAME)
				.arguments(GenericArguments.seq(
						(GenericArguments.string(Text.of("team")))),
						GenericArguments.remainingJoinedStrings(Text.of("name")))
				.executor(new Rename())
				.build();
	}
}
