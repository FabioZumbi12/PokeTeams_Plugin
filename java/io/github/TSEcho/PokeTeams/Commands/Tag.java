package io.github.TSEcho.PokeTeams.Commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.CensorCheck;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class Tag implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		String tag = args.<String>getOne(Text.of("tag")).get();
		
		//if the user is a player
		if(src instanceof Player) {
			
			RoleRequirement role = new RoleRequirement((Player) src);
			
			//if the user is in a team
			if(role.inTeam()) {
				
				//if the user has the permissions to set a tag
				if(role.canSetTag()) {
					
					String team = role.getTeam();
					
					//if the name doesnt clash with filter
					if(!CensorCheck.containsSwear(tag)) {
						
						ConfigurationManager.storNode.getNode("Teams", team, "Tag").setValue(tag);
						ConfigurationManager.save();
						
						src.sendMessage(Text.of(TextColors.GREEN, "Team " + team + "'s tag is now " + tag));
						
					} else {
						src.sendMessage(Texts.INNAPROPRIATE);
					}
					
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

	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.ADMIN_RENAME)
				.arguments(GenericArguments.string(Text.of("tag")))
				.executor(new Tag())
				.build();
	}
}
