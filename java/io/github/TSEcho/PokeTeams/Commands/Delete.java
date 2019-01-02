package io.github.TSEcho.PokeTeams.Commands;

import java.util.Map;

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

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import io.github.TSEcho.PokeTeams.Utilities.Utils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Delete implements CommandExecutor {

	private RoleRequirement role;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {
			
			role = new RoleRequirement((Player) src);
			
			//if the user is in a team
			if(role.inTeam()) {
				
				//if the user has the permissions to delete a group
				if(role.canDelete()) {
					
					sendMessages();
					ConfigurationManager.storNode.getNode("Teams", role.getTeam()).setValue(null);
					Utils.removeChat(src.getName());
					ConfigurationManager.save();
					
				} else {
					src.sendMessage(Texts.INSUFFICIENT_RANK);
				}
				
			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
			
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}


		return CommandResult.success();
	}
	
	private void sendMessages() { 
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigurationManager
				.storNode.getNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet()) {
			
			String member = teams.getKey().toString();
			
			if(Sponge.getServer().getPlayer(member).isPresent()) {
				Sponge.getServer().getPlayer(member).get().sendMessage(Text.of(TextColors.RED, role.getTeam() + " has been disbanded!"));
			}
		}
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.DELETE)
				.executor(new Delete())
				.build();
	}
}
