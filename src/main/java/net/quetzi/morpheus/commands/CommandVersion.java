package net.quetzi.morpheus.commands;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.quetzi.morpheus.references.References;

public class CommandVersion implements ICommand {
    public CommandVersion() {
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "morpheusversion";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return References.USAGE;
    }

    @Override
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] var2) {
        sender.addChatMessage(new ChatComponentText("Morpheus version: " + References.VERSION));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] var2) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2) {
        return false;
    }

}
