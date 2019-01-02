package io.github.TSEcho.PokeTeams.Commands.Admin;

import java.util.ArrayList;
import java.util.Map;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class List implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		PaginationList.builder()
		.title(Text.of(TextColors.YELLOW, "Teams List"))
		.contents(addTeams())
		.padding(Text.of(TextColors.AQUA, "="))
		.sendTo(src);
		
		return CommandResult.success();
	}
	
	private ArrayList<Text> addTeams() {
		
		ArrayList<Text> teams = new ArrayList<Text>();
		int i = 1;
		
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teamLoop : ConfigurationManager
				.storNode.getNode("Teams").getChildrenMap().entrySet()) {
			
			Text team = Text.of(TextColors.YELLOW, i + ".) " + teamLoop.getKey().toString());
			teams.add(team);
			i++;
		}
		
		return teams;
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_LIST)
				.executor(new List())
				.build();
	}
}