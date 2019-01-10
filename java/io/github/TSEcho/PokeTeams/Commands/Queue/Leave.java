package io.github.TSEcho.PokeTeams.Commands.Queue;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import io.github.TSEcho.PokeTeams.Pixelmon.QueueManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;

public class Leave implements CommandExecutor {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {
			
			if(QueueManager.queue.contains(src.getName())) {
				
				QueueManager.queue.remove(src.getName());
				src.sendMessage(Texts.LEAVE_QUEUE);
			
			} else {
				src.sendMessage(Texts.NOT_IN_QUEUE);
			}
		}
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.QUEUE_LEAVE)
				.executor(new Leave())
				.build();
	}
}