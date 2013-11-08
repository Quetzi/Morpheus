package net.quetzi.morpheus.commands;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.references.References;

public class MorpheusCommand implements ICommand {
	private List<String> aliases;

	public MorpheusCommand() {
		aliases = new ArrayList<String>();
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "morpheus";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return References.USAGE;
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length == 0) {
			icommandsender.sendChatToPlayer(ChatMessageComponent
					.createFromText(References.USAGE));
			return;
		}
		if (astring[0].equalsIgnoreCase("alert")) {
			if (Morpheus.alertEnabled) {
				Morpheus.setAlertPlayers(false);
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText(References.ALERTS_OFF));
			} else {
				Morpheus.setAlertPlayers(true);
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText(References.ALERTS_ON));
			}
		} else if (astring[0].equalsIgnoreCase("disable")) {
			if (astring[1] != null) {
				// Disable age tracking
			} else {
				icommandsender.sendChatToPlayer(ChatMessageComponent
						.createFromText(References.DISABLE_USAGE));
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender icommandsender,
			String[] astring) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i) {
		return false;
	}

	public int getRequiredPermissionLevel() {
		return 3;
	}
}
