package net.quetzi.morpheus.commands;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;
import net.quetzi.morpheus.Morpheus;

public class AlertToggleCommand implements ICommand {
	private List<String> aliases;

	public AlertToggleCommand() {

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
		return "Usage: /morpheus alert";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length == 0) {
			icommandsender.sendChatToPlayer(new ChatMessageComponent()
					.addText("Usage: /morpheus alert"));
			return;
		}
		if (astring.toString().matches("alert")) {
			if (Morpheus.alertPlayers) {
				Morpheus.setAlertPlayers(false);
				icommandsender.sendChatToPlayer(new ChatMessageComponent()
						.addText("Text alerts turned off"));
			} else {
				Morpheus.setAlertPlayers(true);
				icommandsender.sendChatToPlayer(new ChatMessageComponent()
						.addText("Text alerts turned on"));
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
