package io.github.TSEcho.PokeTeams.Commands;

import java.util.ArrayList;
import java.util.Map;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.serializer.TextSerializers;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Info implements CommandExecutor {

	private RoleRequirement role;
	private String members;
	private String player;
	private CommandSource source;
	private ArrayList<String> list = new ArrayList<String>();
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		list.clear();
		members = "";
		source = src;
		role = new RoleRequirement((Player) src);
		
		if(src instanceof Player) {
			
			//if the user is in a team
			if(role.inTeam()) {
				
				sendWinLoss();
				sendKills();
				sendCaught();
				sendBase();
				sendList();
				
			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
			
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}

		return CommandResult.success();
	}
	
	private void sendWinLoss() {
		source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("\n&e&lTeam " + role.getTeam() + "\n\n&c&lWin/Loss: &r&e" + 
				role.getWins() + "/" + (role.getLosses() + role.getWins()) + " ["  + role.getRatio() + "%]"));
	}
	
	private void sendBase() {
		if(role.hasBase()) {
			source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c&lBase: &r&e" + 
					(int) role.getX() + ", " + (int) role.getY() + ", " + (int) role.getZ()));
		} else {
			source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c&lBase: &r&eNot Set"));
		}
	}
	
	private void sendKills() {
		source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c&lWild Pokemon Defeated: &r&e" + role.getKills()));
	}
	
	private void sendCaught() {
		source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c&lPokemon Caught: &r&e" + role.getCaught()));
	}
	
	private void sendList() {
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> players : ConfigurationManager
				.storNode.getNode("Teams", role.getTeam(), "Members").getChildrenMap().entrySet()) {
			
			player = players.getKey().toString();
			list.add(player);
		}
		
		for(String i : list) {
			members += i + ", ";
		}
		
		members = members.substring(0, members.length() - 2);
		
		source.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c&lMember List: &e" + members + "\n"));
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.INFO)
				.executor(new Info())
				.build();
	}

}
