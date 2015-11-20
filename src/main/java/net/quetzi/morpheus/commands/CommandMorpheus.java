package net.quetzi.morpheus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.helpers.References;

import java.util.ArrayList;
import java.util.List;

public class CommandMorpheus extends CommandBase {

    private final List<String> aliases;

    public CommandMorpheus() {

        aliases = new ArrayList<String>();
        aliases.add("sleepvote");
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
    public String getCommandUsage(ICommandSender sender) {

        return References.USAGE;
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] astring) throws NumberInvalidException {

        if (astring.length == 0) {
            sender.addChatMessage(new ChatComponentText(References.USAGE));
            return;
        }
        if (astring[0].equalsIgnoreCase("alert")) {
            if (Morpheus.isAlertEnabled()) {
                Morpheus.setAlertPlayers(false);
                sender.addChatMessage(new ChatComponentText(References.ALERTS_OFF));
            } else {
                Morpheus.setAlertPlayers(true);
                sender.addChatMessage(new ChatComponentText(References.ALERTS_ON));
            }
        } else if (astring[0].equalsIgnoreCase("disable")) {
            if (astring[1] != null) {
                int ageToDisable = parseInt(astring[1]);
                if (Morpheus.register.isDimRegistered(ageToDisable)) {
                    Morpheus.register.unregisterHandler(ageToDisable);
                    sender.addChatMessage(new ChatComponentText("Disabled sleep vote checks in dimension " + ageToDisable));
                } else {
                    sender.addChatMessage(new ChatComponentText("Sleep vote checks are already disabled in dimension " + ageToDisable));
                }
            } else {
                sender.addChatMessage(new ChatComponentText(References.DISABLE_USAGE));
            }
        } else if (astring[0].equalsIgnoreCase("version")) {
            sender.addChatMessage(new ChatComponentText("Morpheus version: " + References.VERSION));
        } else if (astring[0].equalsIgnoreCase("percent")) {
            if (astring[1] != null) {
                Morpheus.config.get("settings", "SleeperPerc",50).set(parseInt(astring[1]));
                Morpheus.config.save();
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {

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

    @Override
    public int getRequiredPermissionLevel() {

        return 3;
    }
}
