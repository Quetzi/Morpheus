package net.quetzi.morpheus.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.references.References;

import java.util.ArrayList;
import java.util.List;

public class CommandMorpheus implements ICommand {

    private List<String> aliases;

    public CommandMorpheus() {

        aliases = new ArrayList<String>();
    }

    @Override
    public int compareTo(Object arg0) {

        return 0;
    }

    @Override
    public String getName() {

        return "morpheus";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return References.USAGE;
    }

    @Override
    public List getAliases() {

        aliases.add("sleepvote");
        return aliases;
    }

    @Override
    public void execute(ICommandSender sender, String[] astring) {

        if (astring.length == 0) {
            sender.addChatMessage(new ChatComponentText(References.USAGE));
            return;
        }
        if (astring[0].equalsIgnoreCase("alert")) {
            Morpheus.setAlertPlayers(!Morpheus.isAlertEnabled());
            sender.addChatMessage(new ChatComponentText(Morpheus.isAlertEnabled() ? References.ALERTS_ON : References.ALERTS_OFF));
        } else if (astring[0].equalsIgnoreCase("version")) {
            sender.addChatMessage(new ChatComponentText("Morpheus version: " + References.VERSION));
        }
    }

    @Override
    public boolean canCommandSenderUse(ICommandSender icommandsender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring, BlockPos pos) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    public int getRequiredPermissionLevel() {

        return 4;
    }
}
