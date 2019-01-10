package io.github.TSEcho.PokeTeams.Commands.Queue;

import java.util.ArrayList;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import io.github.TSEcho.PokeTeams.Pixelmon.QueueManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;

public class List implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		ArrayList<Text> contents = new ArrayList<Text>();
		
		int num = 1;
		for(String i : QueueManager.queue) {
			contents.add(TextSerializers.FORMATTING_CODE.deserialize("&b" + num + ".) " + i));
			num++;
		}
		
		PaginationList.builder()
			.title(Text.of(TextColors.YELLOW, "Queue List"))
			.padding(Text.of(TextColors.GREEN, "="))
			.linesPerPage(12)
			.contents(contents)
			.sendTo(src);
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.QUEUE_LIST)
				.executor(new List())
				.build();
	}
}
