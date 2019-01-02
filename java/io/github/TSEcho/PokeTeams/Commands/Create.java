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
import org.spongepowered.api.text.serializer.TextSerializers;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.CensorCheck;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class Create implements CommandExecutor {

	private RoleRequirement role;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		String team = args.<String>getOne(Text.of("team")).get();
		
		if(src instanceof Player) {
			
			role = new RoleRequirement((Player) src);
			
			//if the user is in a team
			if(!role.inTeam()) {
				
				//if a user has the permissions to create a team 
				if(role.canCreate()) {
					
					//if the name doesn't pass the filter
					if(!CensorCheck.containsSwear(team)) {
						
						ConfigurationManager.storNode.getNode("Teams", team, "Members", src.getName()).setValue("Owner");
						ConfigurationManager.storNode.getNode("Teams", team, "Record", "Wins").setValue(0);
						ConfigurationManager.storNode.getNode("Teams", team, "Record", "Losses").setValue(0);
						ConfigurationManager.storNode.getNode("Teams", team, "Stats", "Kills").setValue(0);
						ConfigurationManager.storNode.getNode("Teams", team, "Stats", "Caught").setValue(0);
						ConfigurationManager.save();
						src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&aYou have created the " + team + " &ateam!"));
						
					} else {
						src.sendMessage(Texts.INNAPROPRIATE);
					}

				} else {
					src.sendMessage(Texts.AlREADY_EXISTS);
				}
			} else {
				src.sendMessage(Texts.ALREADY_IN_TEAM);
			}
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.CREATE)
				.arguments(GenericArguments.remainingJoinedStrings(Text.of("team")))
				.executor(new Create())
				.build();
	}
}
