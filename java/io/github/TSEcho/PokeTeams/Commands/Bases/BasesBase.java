package io.github.TSEcho.PokeTeams.Commands.Bases;

import java.util.UUID;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import com.flowpowered.math.vector.Vector3d;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Settings.WorldInfo;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class BasesBase implements CommandExecutor {

	private RoleRequirement role;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {
			
			role = new RoleRequirement((Player) src);
			
			//if the user is in a team
			if(role.inTeam()) {
				
				//if the user has a base set
				if(role.hasBase()) {
					
					//if the user is able to use teleportation
					if(role.canTeleport()) {

						doAction((Player) src);
						
					} else {
						src.sendMessage(Texts.INSUFFICIENT_RANK);
					}
					
				} else {
					src.sendMessage(Texts.NO_BASE);
				}

			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
		}
		return CommandResult.success();
	}
	
	//teleport player to base
	private void doAction(Player player) {
		
		UUID world;
		
		if(!ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Location", "World").isVirtual()) {
			world = UUID.fromString(ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Location", "World").getString());
		} else {
			world = WorldInfo.WORLD_UUID;
		}
		
		player.transferToWorld(world, new Vector3d(role.getX(), role.getY(), role.getZ()));
		player.sendMessage(Texts.TELEPORTED);
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.BASE_TELEPORT)
				.child(Set.build(), "set")
				.executor(new BasesBase())
				.build();
	}
}
