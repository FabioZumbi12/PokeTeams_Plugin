package io.github.TSEcho.PokeTeams.Commands.Admin;

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

		PaginationList.builder()
		.title(Text.of(TextColors.YELLOW, "PokeTeams Admin"))
		.contents(
				Text.of(TextColors.YELLOW, "/teams set <player> <team>"),
				Text.of(TextColors.YELLOW, "/teams delete <team>"),
				Text.of(TextColors.YELLOW, "/teams kick <team> <name>"),
				Text.of(TextColors.YELLOW, "/teams rename <team>"),
				Text.of(TextColors.YELLOW, "/teams list"))
		.padding(Text.of(TextColors.RED, "="))
		.sendTo(src);
		
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_BASE)
				.executor(new Help())
				.build();
	}
}