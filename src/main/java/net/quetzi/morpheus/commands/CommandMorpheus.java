package net.quetzi.morpheus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
    public String getCommandName() {

        return "morpheus";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return References.USAGE;
    }

    @Override
    public List<String> getCommandAliases() {

        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] astring) throws NumberInvalidException {

        if (astring.length == 0) {
            sender.addChatMessage(new TextComponentString(References.USAGE));
            return;
        }
        if (astring[0].equalsIgnoreCase("alert")) {
            if (Morpheus.isAlertEnabled()) {
                Morpheus.setAlertPlayers(false);
                sender.addChatMessage(new TextComponentString(References.ALERTS_OFF));
            } else {
                Morpheus.setAlertPlayers(true);
                sender.addChatMessage(new TextComponentString(References.ALERTS_ON));
            }
        } else if (astring[0].equalsIgnoreCase("disable")) {
            if (astring[1] != null) {
                int ageToDisable = parseInt(astring[1]);
                if (Morpheus.register.isDimRegistered(ageToDisable)) {
                    Morpheus.register.unregisterHandler(ageToDisable);
                    sender.addChatMessage(new TextComponentString("Disabled sleep vote checks in dimension " + ageToDisable));
                } else {
                    sender.addChatMessage(new TextComponentString("Sleep vote checks are already disabled in dimension " + ageToDisable));
                }
            } else {
                sender.addChatMessage(new TextComponentString(References.DISABLE_USAGE));
            }
        } else if (astring[0].equalsIgnoreCase("version")) {
            sender.addChatMessage(new TextComponentString("Morpheus version: " + References.VERSION));
        } else if (astring[0].equalsIgnoreCase("percent")) {
            if (astring[1] != null) {
                int newPercent = parseInt(astring[1]);
                if (newPercent > 0 && newPercent <= 100) {
                    Morpheus.perc = newPercent;
                    Morpheus.config.get("settings", "SleeperPerc", 50).set(newPercent);
                    Morpheus.config.save();
                    sender.addChatMessage(new TextComponentString("Sleep vote percentage set to " + Morpheus.perc + "%"));
                } else {
                    sender.addChatMessage(new TextComponentString("Invalid percentage value, round numbers between 0 and 100 are acceptable."));
                }
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

        return true;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {

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
