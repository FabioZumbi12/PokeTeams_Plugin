package io.github.TSEcho.PokeTeams.Settings;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Texts {

	//technical messages
	public static final Text RELOADED = TextSerializers.FORMATTING_CODE.deserialize("&bPokeTeams has been reloaded!");
	public static final Text NOT_PLAYER = TextSerializers.FORMATTING_CODE.deserialize("&cOnly a player can enter this command");
	
	//problem messages
	public static final Text NOT_IN_TEAM = TextSerializers.FORMATTING_CODE.deserialize("&cYou are not in a team!");
	public static final Text AlREADY_EXISTS = TextSerializers.FORMATTING_CODE.deserialize("&cThis team already exists!");
	public static final Text ALREADY_IN_TEAM = TextSerializers.FORMATTING_CODE.deserialize("&cYou are already in a team!");
	public static final Text INSUFFICIENT_RANK = TextSerializers.FORMATTING_CODE.deserialize("&cYour rank is insufficient for that!");
	public static final Text INNAPROPRIATE = TextSerializers.FORMATTING_CODE.deserialize("&cThat name is not allowed!");
	public static final Text BOTH_NOT_IN_TEAM = TextSerializers.FORMATTING_CODE.deserialize("&cBoth of you need to be in the same team!");
	public static final Text CANT_PROMOTE = TextSerializers.FORMATTING_CODE.deserialize("&cThis user cannot be promoted!");
	public static final Text CANT_DEMOTE = TextSerializers.FORMATTING_CODE.deserialize("&cThis user cannot be demoted!");
	public static final Text MAX_MEMBERS = TextSerializers.FORMATTING_CODE.deserialize("&cYou have reached the member limit!");
	public static final Text KICK_YOURSELF = TextSerializers.FORMATTING_CODE.deserialize("&cYou cannot kick yourself!");
	public static final Text NO_BASE = TextSerializers.FORMATTING_CODE.deserialize("&cYour team does not have a base!");
	public static final Text NEEDS_LEADER = TextSerializers.FORMATTING_CODE.deserialize("&cTeam needs to have an owner!");
	public static final Text NOT_EXISTS = TextSerializers.FORMATTING_CODE.deserialize("&cThis team does not exist!");
	
	//success messages
	public static final Text CREATED_TEAM = TextSerializers.FORMATTING_CODE.deserialize("&aYou are not in a team");
	public static final Text DELETED_TEAM = TextSerializers.FORMATTING_CODE.deserialize("&aThis team has been disbanded");
	public static final Text TELEPORTED = TextSerializers.FORMATTING_CODE.deserialize("&aYou have been warped successfully");
	public static final Text SET_BASE = TextSerializers.FORMATTING_CODE.deserialize("&aYou have set your base");
	public static final Text SEND_INVITE = TextSerializers.FORMATTING_CODE.deserialize("&aYou have sent an invite!");
	public static final Text DEMOTED = TextSerializers.FORMATTING_CODE.deserialize("&cYou have been demoted");
	public static final Text LEFT = TextSerializers.FORMATTING_CODE.deserialize("&aYou have left your team!");
	public static final Text ADDED_CHAT = TextSerializers.FORMATTING_CODE.deserialize("&bYou are now in your team chat!");
	public static final Text REMOVED_CHAT = TextSerializers.FORMATTING_CODE.deserialize("&bYou have left your team chat!");

}
