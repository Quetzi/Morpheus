package net.quetzi.morpheus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;
import net.quetzi.morpheus.references.References;

import java.util.ArrayList;
import java.util.List;

public class CommandMorpheus extends CommandBase {

    private List<String> aliases;

    public CommandMorpheus() {

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
    public String getCommandUsage(ICommandSender sender) {

        return References.USAGE;
    }

    @Override
    public List getCommandAliases() {

        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] astring) {

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
                int ageToDisable = parseInt(sender, astring[1]);
                if (MorpheusRegistry.registry.containsKey(ageToDisable)) {
                    Morpheus.register.unregisterHandler(ageToDisable);
                    sender.addChatMessage(new ChatComponentText("Sleep voting has been disabled in dimension " + ageToDisable));
                } else {
                    sender.addChatMessage(new ChatComponentText("Sleep voting was not enabled for dimension " + ageToDisable));
                }
            } else {
                sender.addChatMessage(new ChatComponentText(References.DISABLE_USAGE));
            }
        } else if (astring[0].equalsIgnoreCase("status")) {
            for (WorldServer server : MinecraftServer.getServer().worldServers) {
                if (Morpheus.register.isDimRegistered(server.provider.dimensionId)) {
                    sender.addChatMessage(new ChatComponentText("Dim " + server.provider.dimensionId + ": " + EnumChatFormatting.GREEN + "ENABLED" + EnumChatFormatting.RESET));
                    } else {
                    sender.addChatMessage(new ChatComponentText("Dim " + server.provider.dimensionId + ": " + EnumChatFormatting.RED + "DISABLED" + EnumChatFormatting.RESET));
                }
            }
            sender.addChatMessage(new ChatComponentText("Alerts are currently: " + (Morpheus.isAlertEnabled() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                                                        + "ENABLED" + EnumChatFormatting.RESET));
            sender.addChatMessage(new ChatComponentText("Percentage of players required to sleep: " + Morpheus.perc));
            sender.addChatMessage(new ChatComponentText("Mining players are " + (Morpheus.includeMiners ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                                                        + (Morpheus.includeMiners ? "INCLUDED"  + EnumChatFormatting.RESET + " in votes": "EXCLUDED" + EnumChatFormatting.RESET + " from votes")));
            if (!Morpheus.includeMiners) {
                sender.addChatMessage(new ChatComponentText("Players below y" + Morpheus.groundLevel + " are considered to be mining"));
            }
        }
        else if (astring[0].equalsIgnoreCase("version")) {
            sender.addChatMessage(new ChatComponentText("Morpheus version: " + References.VERSION));
        } else if (astring[0].equalsIgnoreCase("percent")) {
            if (astring[1] != null) {
                int newPercent = parseInt(sender, astring[1]);
                if (newPercent > 0 && newPercent <= 100) {
                    Morpheus.perc = newPercent;
                    Morpheus.config.get("settings", "SleeperPerc", 50).set(newPercent);
                    Morpheus.config.save();
                    sender.addChatMessage(new ChatComponentText("Sleep vote percentage set to " + Morpheus.perc + "%"));
                } else {
                    sender.addChatMessage(new ChatComponentText("Invalid percentage value, round numbers between 0 and 100 are acceptable."));
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {

        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender, String[] astring) {

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {

        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 4;
    }
}
