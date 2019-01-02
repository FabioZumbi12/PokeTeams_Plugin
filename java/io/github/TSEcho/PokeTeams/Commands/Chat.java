package io.github.TSEcho.PokeTeams.Commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import io.github.TSEcho.PokeTeams.Utilities.Utils;

public class Chat implements CommandExecutor {

	private Player player;
	private RoleRequirement role;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {
		
			//sees if the user has ultimate chat before working with chat features
			if(Sponge.getPluginManager().getPlugin("ultimatechat").isPresent() ||
					Sponge.getPluginManager().getPlugin("nucleus").isPresent()) {
				
				player = (Player) src;
				role = new RoleRequirement(player);
					
				//if the user is in a team
				if(role.inTeam()) {
					
					//if the user is inside of a chat
					if(Utils.inChat(player.getName())) {
						
						Utils.removeChat(player.getName());
						player.sendMessage(Texts.REMOVED_CHAT);
						
					//if the user is not in a chat
					} else {
						
						Utils.addChat(player.getName());
						player.sendMessage(Texts.ADDED_CHAT);
					}
					
				} else {
					src.sendMessage(Texts.NOT_IN_TEAM);
				}
			
			} else {
				src.sendMessage(Text.of(TextColors.RED, "This feature requires UltimateChat!"));
			}
			
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}

		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.CHAT)
				.executor(new Chat())
				.build();
	}
}
