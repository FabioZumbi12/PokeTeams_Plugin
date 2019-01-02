package io.github.TSEcho.PokeTeams.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

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

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Leaderboard implements CommandExecutor {

	private double wins, losses;
	private double ratio;
	private String team;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		TreeMap<Double, String> tree = new TreeMap<Double, String>(Collections.reverseOrder());
		
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teams : ConfigurationManager.storNode
				.getNode("Teams").getChildrenMap().entrySet()) {
			
			team = teams.getKey().toString();
			wins = Double.valueOf(ConfigurationManager.storNode.getNode("Teams", team, "Record", "Wins").getInt());
			losses = Double.valueOf(ConfigurationManager.storNode.getNode("Teams", team, "Record", "Losses").getInt());
			
			if(losses != 0) {
				ratio = ((wins / (wins + losses)) * 100.0);
			} else {
				ratio = 0.0;
			}
			
			//setting those values into an HashMap
			tree.put(ratio, team);
		}
		
			ArrayList<Text> teams = new ArrayList<Text>();
			int num = 1;
			
			//sorting list and adding to final output message
			for(Double i : tree.keySet()) {
				teams.add(TextSerializers.FORMATTING_CODE.deserialize("&a" + num + ".) " + tree.get(i) + "  -  [W/L] " + i.intValue() + "%"));
				num++;
			}
			
			PaginationList.builder()
				.title(Text.of(TextColors.GREEN, "Top Teams"))
				.padding(Text.of(TextColors.YELLOW, "="))
				.linesPerPage(12)
				.contents(teams)
				.sendTo(src);
			
			
			tree.clear();
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.LEADERBOARD)
				.executor(new Leaderboard())
				.build();
	}

}
