package io.github.TSEcho.PokeTeams.Configuration;

import java.util.ArrayList;

public class Defaults {
	
	private static ArrayList<String> words = new ArrayList<String>(); 
	private static ArrayList<String> roles = new ArrayList<String>();
	private static String[] swears = {"anal", "arse", "ass", "bastard", "bitch", "cock", "cunt", "damn", "dick", "dildo",
			"dyke", "fag", "fuck" ,"goddamn", "damn", "hell", "homo", "jizz", "nigger", "nigga", "penis", "pussy", "queer",
			"shit", "slut", "tit", "twat", "vagina", "wank", "whore"};
	
	public static void configSetup() {
		
		roles.add("Officer");
		roles.add("Member");
		roles.add("Grunt");
		roles.add("Owner");
		
		ConfigurationManager.confNode.getNode("Team-Settings", "Name", "Max-Length").setValue(12);
		ConfigurationManager.confNode.getNode("Team-Settings", "Name", "Use-Censor").setValue(true);
		ConfigurationManager.confNode.getNode("Team-Settings", "Max-Members").setValue(8);
		
		ConfigurationManager.confNode.getNode("Battle-Settings", "Message-Winners").setValue(true);
		ConfigurationManager.confNode.getNode("Battle-Settings", "Message-Losers").setValue(true);
		ConfigurationManager.confNode.getNode("Battle-Settings", "Record-All-Battles").setValue(false)
			.setComment("If false, only battles between two team members will be recorded");
		
		ConfigurationManager.confNode.getNode("Chat-Settings", "Prefix").setValue("&f[%team%&f] &b%player% &f-> ");
		ConfigurationManager.confNode.getNode("Chat-Settings", "Chat-Color").setValue("&7");
		ConfigurationManager.confNode.getNode("Chat-Settings", "Console-SocialSpy").setValue(true);
		ConfigurationManager.confNode.getNode("Chat-Settings", "Players-SocialSpy").setValue(true)
			.setComment("Note: To see messages, players will need the social-spy permission");
		
		for(String role : roles) {
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Invite").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Kick").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Promote").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Demote").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Delete").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Base-Set").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Chat").setValue(true);
			ConfigurationManager.confNode.getNode("Team-Settings", "Roles", role, "Base-Teleport").setValue(true);
		}
	}
	
	public static void censorSetup() {

		ConfigurationManager.censorNode.getNode("Partial-Censor").setValue(true)
			.setComment("If true, censor will deny team names if they contain any of the words");
		
		ConfigurationManager.censorNode.getNode("Censored-Words").setValue(addWords());
	}
	
	private static ArrayList<String> addWords() {
		for(String word : swears) {
			words.add(word);
		}
		return words;
	}
}
