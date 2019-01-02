package io.github.TSEcho.PokeTeams.Utilities;

import java.util.ArrayList;

import com.google.common.reflect.TypeToken;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class CensorCheck {

	private static boolean swearFound;
	private static String swear, word;
	private static ArrayList<String> words = new ArrayList<String>();
	
	public static boolean containsSwear(String myWord) {
		
		word = myWord;
		swearFound = false;
		words.clear();
		
		//adding all words from censor
		try {
			words.addAll(ConfigurationManager.censorNode.getNode("Censored-Words").getList(TypeToken.of(String.class)));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		
		//censor parting system
		if(ConfigurationManager.confNode.getNode("Team-Settings", "Name", "Use-Censor").getBoolean()) {
			
			if(ConfigurationManager.censorNode.getNode("Partial-Censor").getBoolean()) {
				partialCensor();
			} else {
				fullWord();
			}
		}
		
		//the max length is less than the words length
		if(ConfigurationManager.confNode.getNode("Team-Settings", "Name", "Max-Length").getInt() < word.length()) {
			swearFound = true;
		}	
		
		//the word contains an ampersand 
		if(word.contains("&")) {
			swearFound = true;
		}
		
		return swearFound;
	}
	
	private static void partialCensor() {
		for(String swear : words) {
			if(word.toLowerCase().contains(swear.toLowerCase())) {
				swearFound = true;
			}
		}
	}
	
	private static void fullWord() {
		for(String word : words) {
			if(word.equalsIgnoreCase(swear)) {
				swearFound = true;
			}
		}
	}
	
}
