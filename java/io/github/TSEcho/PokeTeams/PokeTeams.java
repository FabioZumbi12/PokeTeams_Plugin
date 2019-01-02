package io.github.TSEcho.PokeTeams;

import java.io.File;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;

import br.net.fabiozumbi12.UltimateChat.Sponge.API.SendChannelMessageEvent;
import io.github.TSEcho.PokeTeams.Commands.Base;
import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.EventListeners.UChatListener;
import io.github.TSEcho.PokeTeams.Pixelmon.BattleManager;
import io.github.TSEcho.PokeTeams.Pixelmon.CatchingManager;
import io.github.TSEcho.PokeTeams.Settings.WorldInfo;
import io.github.TSEcho.PokeTeams.Utilities.PlaceholderAPI;
import io.github.TSEcho.PokeTeams.Utilities.Utils;
import me.rojo8399.placeholderapi.Placeholder;
import me.rojo8399.placeholderapi.PlaceholderService;
import me.rojo8399.placeholderapi.Source;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Plugin(id = PokeTeams.ID, 
		name = PokeTeams.NAME, 
		authors = PokeTeams.AUTHORS, 
		description = PokeTeams.DESCRIPTION,
		version = PokeTeams.VERSION,
		dependencies = {@Dependency(id = Pixelmon.MODID, version = Pixelmon.VERSION), 
					    @Dependency(id = "ultimatechat", version = "1.8.8", optional = true),
					    @Dependency(id = "placeholderapi", version = "4.4")})

public class PokeTeams {

	public static final String ID = "poketeams";
	public static final String NAME = "PokeTeams";
	public static final String VERSION = "1.3.0";
	public static final String AUTHORS = "TSEcho";
	public static final String DESCRIPTION = "Teams plugin with Pixelmon Reforged Support";
	
	private PlaceholderService placeholders;
	private static PokeTeams instance;
	
	@Inject 
	private Logger logger;

	@Inject 
	@ConfigDir(sharedRoot = false) 
	private File dir;
	
	@Listener
	public void onPreInit(GamePreInitializationEvent e) {
		instance = this;
		ConfigurationManager.setup(dir);
		ConfigurationManager.load();
	}
	
	@Listener
	public void onInit(GameInitializationEvent e) {
		Sponge.getCommandManager().register(instance, Base.build(), "poketeams", "teams", "team");
		Pixelmon.EVENT_BUS.register(instance);

		if(Sponge.getPluginManager().getPlugin("ultimatechat").isPresent()) {
			
			Sponge.getEventManager().registerListener(instance, SendChannelMessageEvent.class, Order.FIRST, new UChatListener());
		} 
	}
	
	@Listener
	public void onStart(GameStartedServerEvent e) {
		WorldInfo.init();
		loadPlaceholders();
	}
	
	@Listener
	public void onReload(GameReloadEvent e) {
		ConfigurationManager.load();
	}
	
	@Listener
	public void onLeave(ClientConnectionEvent.Disconnect e, @Root Player player) {
		Utils.removeChat(player.getName());
	}
	
	@SubscribeEvent
	public void onEndBattle(BattleEndEvent e) {
		new BattleManager(e);
	}
	
	@SubscribeEvent
	public void onCatch(CaptureEvent.SuccessfulCapture e) {
		new CatchingManager(e);
	}
	
	@Placeholder(id = "team")
	public String getTeam(@Source CommandSource src) {
		return PlaceholderAPI.getTeam(src);
	}
	
	@Placeholder(id = "team_wins")
	public String getWins(@Source CommandSource src) {
		return PlaceholderAPI.getWins(src);
	}
	
	@Placeholder(id = "team_losses")
	public String getLosses(@Source CommandSource src) {
		return PlaceholderAPI.getLosses(src);
	}
	
	@Placeholder(id = "team_ratio")
	public String getRatio(@Source CommandSource src) {
		return PlaceholderAPI.getRatio(src);
	}
	
	@Placeholder(id = "team_kills")
	public String getKills(@Source CommandSource src) {
		return PlaceholderAPI.getKills(src);
	}
	
	@Placeholder(id = "team_caught")
	public String getCaught(@Source CommandSource src) {
		return PlaceholderAPI.getCaught(src);
	}
	
	private void loadPlaceholders() {
		placeholders = Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);

		placeholders.loadAll(this, this).stream().map(builder -> { 
			
			switch(builder.getId()) {
				case "team":
					return builder.description("Player's team name");
				case "team_wins":
					return builder.description("Player's team's wins");
				case "team_losses":
					return builder.description("Player's team's losses");
				case "team_kills":
					return builder.description("Player's team's kills");
				case "team_ratio":
					return builder.description("Player's team's win/loss ratio");
				case "team_caught":
					return builder.description("Player's team's total caught pokemon");
			}
			
			return builder;
			
		}).map(builder -> builder.author("TSEcho").version("1.0.0")).forEach(builder -> {
			
			try {
				
				builder.buildAndRegister();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}
	
	public static PokeTeams getInstance() {
		return instance;
	}
	
	public Logger getLogger() {
		return logger; 
	}
}
