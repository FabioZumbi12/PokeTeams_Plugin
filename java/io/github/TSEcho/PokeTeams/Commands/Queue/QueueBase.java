package io.github.TSEcho.PokeTeams.Commands.Queue;

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

public class QueueBase implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		PaginationList.builder()
		.title(Text.of(TextColors.YELLOW, "PokeTeams Queue"))
		.contents(
				Text.of(TextColors.YELLOW, "/teams queue"),
				Text.of(TextColors.YELLOW, "/teams queue join"),
				Text.of(TextColors.YELLOW, "/teams queue leave"),
				Text.of(TextColors.YELLOW, "/teams queue list"))
		.padding(Text.of(TextColors.GREEN, "="))
		.sendTo(src);
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.executor(new QueueBase())
				.permission(Permissions.QUEUE_BASE)
				.child(Join.build(), "join")
				.child(Leave.build(), "leave")
				.child(List.build(), "list")
				.build();
	}
}