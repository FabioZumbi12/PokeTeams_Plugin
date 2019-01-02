package io.github.TSEcho.PokeTeams.Commands;

import java.util.ArrayList;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Settings.Permissions;

public class Help implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		ArrayList<Text> help = new ArrayList<Text>();
		help.add(Text.of(TextColors.RED, "/teams leaderboard"));
		help.add(Text.of(TextColors.RED, "/teams create <team>"));
		help.add(Text.of(TextColors.RED, "/teams delete"));
		help.add(Text.of(TextColors.RED, "/teams info"));
		help.add(Text.of(TextColors.RED, "/teams invite"));
		help.add(Text.of(TextColors.RED, "/teams kick <player>"));
		
		if(Sponge.getPluginManager().getPlugin("ultimatechat").isPresent() ||
				Sponge.getPluginManager().getPlugin("nucleus").isPresent()) {
			
			help.add(Text.of(TextColors.RED, "/teams chat"));
		}
		
		help.add(Text.of(TextColors.RED, "/teams base"));
		help.add(Text.of(TextColors.RED, "/teams base set"));
		help.add(Text.of(TextColors.RED, "/teams admin"));
		
		
		PaginationList.builder()
		.title(Text.of(TextColors.RED, "PokeTeams"))
		.contents(help)
		.padding(Text.of(TextColors.AQUA, "="))
		.sendTo(src);
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.BASE)
				.executor(new Help())
				.build();
	}
}
