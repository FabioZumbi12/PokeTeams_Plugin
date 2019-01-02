package io.github.TSEcho.PokeTeams.Settings;

import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

public class WorldInfo {
	
	public static String WORLD_NAME;
	public static World WORLD;
	public static UUID WORLD_UUID;
	
	public static void init() {
		 WORLD_NAME = Sponge.getServer().getDefaultWorldName();
		 WORLD = Sponge.getServer().getWorld(WORLD_NAME).get();
		 WORLD_UUID = WORLD.getUniqueId();
	}
}
