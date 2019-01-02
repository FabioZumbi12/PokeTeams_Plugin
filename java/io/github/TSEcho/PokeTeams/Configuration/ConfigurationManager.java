package io.github.TSEcho.PokeTeams.Configuration;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigurationManager {

	private static File dir, config, storage, censor;
	private static ConfigurationLoader<CommentedConfigurationNode> storLoad, confLoad, censorLoad;
	public static CommentedConfigurationNode confNode, storNode, censorNode;
	
	public static void setup(File folder) {
		dir = folder;
	}
	
	public static void load() {
		
		if(!dir.exists()) {
			dir.mkdir();
		}
		
		try {
			
			config = new File(dir, "Configuration.conf");
			storage = new File(dir, "Teams.conf");
			censor = new File(dir, "Censor.conf");
			confLoad = HoconConfigurationLoader.builder().setFile(config).build();
			storLoad = HoconConfigurationLoader.builder().setFile(storage).build();
			censorLoad = HoconConfigurationLoader.builder().setFile(censor).build();
			
				if(!config.exists()) {
					config.createNewFile();
					confNode = confLoad.load();
					Defaults.configSetup();
					confLoad.save(confNode);
				}
				
				if(!storage.exists()) {
					storage.createNewFile();
					storNode = storLoad.load();
					storLoad.save(storNode);
				}
				
				if(!censor.exists()) {
					censor.createNewFile();
					censorNode = censorLoad.load();
					Defaults.censorSetup();
					censorLoad.save(censorNode);
				}
				
				confNode = confLoad.load();
				storNode = storLoad.load();
				censorNode = censorLoad.load();
				
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save() {
		try {
			confLoad.save(confNode);
			storLoad.save(storNode);
			censorLoad.save(censorNode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
