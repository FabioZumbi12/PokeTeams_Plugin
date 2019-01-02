package io.github.TSEcho.PokeTeams.Utilities;

import java.util.ArrayList;
import java.util.Map;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Utils {

	private static ArrayList<String> inChat = new ArrayList<String>();
		
	//add a user to the chat saying they are in one currently
	public static void addChat(String name) {
		inChat.add(name);
	}
	
	//removes a user from a chat so their messages send normally
	public static void removeChat(String name) {
		if(inChat(name)) {
			inChat.remove(name);
		}
	}
	
	//gets if a user is in a chat
	public static boolean inChat(String name) {
		return inChat.contains(name);
	}
	
	//returns all teams as a list
	public static ArrayList<String> teamList() {
		
		ArrayList<String> teams = new ArrayList<String>();
		
		for (Map.Entry<Object, ? extends CommentedConfigurationNode> teamLoop : ConfigurationManager
				.storNode.getNode("Teams").getChildrenMap().entrySet()) {
			
			String team = teamLoop.getKey().toString();
			teams.add(team);
		}
		
		return teams;
	}
}
