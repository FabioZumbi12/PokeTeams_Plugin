package io.github.TSEcho.PokeTeams.Commands;

import java.util.ArrayList;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.TSEcho.PokeTeams.Commands.Admin.AdminBase;
import io.github.TSEcho.PokeTeams.Commands.Bases.BasesBase;
import io.github.TSEcho.PokeTeams.Commands.Queue.QueueBase;
import io.github.TSEcho.PokeTeams.Settings.Permissions;

public class Base implements CommandExecutor{

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		ArrayList<Text> help = new ArrayList<Text>();
		help.add(Text.of(TextColors.RED, "/teams leaderboard"));
		help.add(Text.of(TextColors.RED, "/teams list"));
		help.add(Text.of(TextColors.RED, "/teams create <team>"));
		help.add(Text.of(TextColors.RED, "/teams delete"));
		help.add(Text.of(TextColors.RED, "/teams info"));
		help.add(Text.of(TextColors.RED, "/teams invite"));
		help.add(Text.of(TextColors.RED, "/teams kick <player>"));
		help.add(Text.of(TextColors.RED, "/teams tag <tag>"));
		
		if(Sponge.getPluginManager().getPlugin("ultimatechat").isPresent() ||
				Sponge.getPluginManager().getPlugin("nucleus").isPresent()) {
			
			help.add(Text.of(TextColors.RED, "/teams chat"));
		}
		
		help.add(Text.of(TextColors.RED, "/teams base"));
		help.add(Text.of(TextColors.RED, "/teams base set"));
		help.add(Text.of(TextColors.RED, "/teams queue"));
		help.add(Text.of(TextColors.RED, "/teams queue join"));
		help.add(Text.of(TextColors.RED, "/teams queue leave"));
		help.add(Text.of(TextColors.RED, "/teams admin"));
		
		
		PaginationList.builder()
		.title(Text.of(TextColors.RED, "PokeTeams"))
		.contents(help)
		.padding(Text.of(TextColors.AQUA, "="))
		.sendTo(src);
		
		return CommandResult.success();
	}
	
	public static CommandSpec build() {
		
		return CommandSpec.builder()
				.executor(new Base())
				.permission(Permissions.BASE)
				.child(Help.build(), "help")
				.child(Reload.build(), "reload")
				.child(Create.build(), "create")
				.child(Delete.build(), "delete")
				.child(Leave.build(), "leave")
				.child(Invite.build(), "invite")
				.child(Info.build(), "info")
				.child(Chat.build(), "chat")
				.child(Promote.build(), "promote")
				.child(Demote.build(), "demote")
				.child(Kick.build(), "kick")
				.child(List.build(), "list")
				.child(Leaderboard.build(), "leaderboard")
				.child(Tag.build(), "tag")
				.child(BasesBase.build(), "base")
				.child(AdminBase.build(), "admin")
				.child(QueueBase.build(), "queue")
				.build();
	}
}
