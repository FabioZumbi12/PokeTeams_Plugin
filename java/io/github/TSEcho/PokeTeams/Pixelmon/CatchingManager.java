package io.github.TSEcho.PokeTeams.Pixelmon;

import org.spongepowered.api.entity.living.player.Player;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class CatchingManager {

	private CaptureEvent.SuccessfulCapture e;
	private Player player;
	private RoleRequirement role;
	
	public CatchingManager(CaptureEvent.SuccessfulCapture event) {
		e = event;
		player = (Player) e.player;
		role = new RoleRequirement(player);
		addStats();
	}
	
	private void addStats() {	
		if(role.inTeam()) {
			ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Stats", "Caught").setValue(role.getCaught() + 1);
			ConfigurationManager.save();
		}
	}
}
