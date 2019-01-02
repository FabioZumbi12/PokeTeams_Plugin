package io.github.TSEcho.PokeTeams.Commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import io.github.TSEcho.PokeTeams.Configuration.ConfigurationManager;
import io.github.TSEcho.PokeTeams.Settings.Permissions;
import io.github.TSEcho.PokeTeams.Settings.Texts;
import io.github.TSEcho.PokeTeams.Utilities.RoleRequirement;

public class Invite implements CommandExecutor {

	private Player sender, receiver;
	private RoleRequirement role, roleOther;
	private Text invite;
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		
		if(src instanceof Player) {

			sender = (Player) src;
			receiver = args.<Player>getOne(Text.of("player")).get();
			role = new RoleRequirement(sender);
			roleOther = new RoleRequirement(receiver);
			
			//if the user is in a team
			if(role.inTeam()) {
				
				//if the user has the permissions to invite a player
				if(role.canInvite()) {
					
					//if the other player is not in a team
					if(!roleOther.inTeam()) {
						
						//if the member total for this group is less than the max players
						if(role.getMemberTotal() < role.getMaxPlayers()) {
							
							invite = Text.builder("You have been invited by " + sender.getName() + " to join team " + role.getTeam() + "\n")
									.color(TextColors.YELLOW)
									.append(Text.builder("Click me to accept!")
											.style(TextStyles.ITALIC)
											.color(TextColors.GRAY)
											.build())
									.onClick(TextActions.executeCallback(callback -> joinTeam()))
									.build();
							receiver.sendMessage(invite);
							
							src.sendMessage(Texts.SEND_INVITE);
							
						} else {
							src.sendMessage(Texts.MAX_MEMBERS);
						}

					} else {
						src.sendMessage(Text.of(TextColors.RED, receiver.getName() + " is already in a team!"));
					}
					
				} else {
					src.sendMessage(Texts.INSUFFICIENT_RANK);
				}
			} else {
				src.sendMessage(Texts.NOT_IN_TEAM);
			}
			
		} else {
			src.sendMessage(Texts.NOT_PLAYER);
		}
		
		return CommandResult.success();
	}
	
	private void joinTeam() {
		ConfigurationManager.storNode.getNode("Teams", role.getTeam(), "Members", receiver.getName()).setValue("Grunt");
		receiver.sendMessage(Text.of(TextColors.GREEN, "You have joined " + role.getTeam() + "!"));
		sender.sendMessage(Text.of(TextColors.GREEN, receiver.getName() + " has accepted your invite!"));
		ConfigurationManager.save();
	}
	
	public static CommandSpec build() {
		return CommandSpec.builder()
				.permission(Permissions.INVITE)
				.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
				.executor(new Invite())
				.build();
	}
}

