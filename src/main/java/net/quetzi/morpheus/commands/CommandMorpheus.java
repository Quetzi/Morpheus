package net.quetzi.morpheus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.helpers.Config;
import net.quetzi.morpheus.helpers.References;

public class CommandMorpheus {
    public static void register(CommandDispatcher<CommandSource> cmdDisp) {
        LiteralArgumentBuilder<CommandSource> morpheusCommand = Commands.literal("wallet");
        morpheusCommand.executes((command) -> {
            command.getSource().sendFeedback(new TextComponentString("Usage: " + References.USAGE).setStyle(new Style().setColor(TextFormatting.RED)), true);
            return 1;
        });
        morpheusCommand.then(Commands.literal("version").executes((command) -> {
            command.getSource().sendFeedback(new TextComponentString("Morpheus version: " + References.VERSION), true);
            return 1;
        }));
        morpheusCommand.then(Commands.literal("alert").executes((command) -> {
            if (Morpheus.isAlertEnabled()) {
                Morpheus.setAlertPlayers(false);
                command.getSource().sendFeedback(new TextComponentString(References.ALERTS_OFF), true);
            } else {
                Morpheus.setAlertPlayers(true);
                command.getSource().sendFeedback(new TextComponentString(References.ALERTS_ON), true);
            }
            return 1;
        }));
        morpheusCommand.then(Commands.literal("percent")).then(Commands.argument("value", IntegerArgumentType.integer(0, 100)).executes((command) -> {
            if (command.getSource().hasPermissionLevel(2)) {
                int newPercent = command.getArgument("value", Integer.class);
                Morpheus.perc = newPercent;
                Config.perc = newPercent;
                command.getSource().sendFeedback(new TextComponentString("Sleep vote percentage set to " + Morpheus.perc + "%"), true);
            }
            return 1;
        }));
        morpheusCommand.then(Commands.literal("disable").then(Commands.argument("dim", IntegerArgumentType.integer())).executes((command) -> {
            if (command.getSource().hasPermissionLevel(2)) {
                int ageToDisable = command.getArgument("dim", Integer.class);
                if (Morpheus.register.isDimRegistered(ageToDisable)) {
                    Morpheus.register.unregisterHandler(ageToDisable);
                    command.getSource().sendFeedback(new TextComponentString("Disabled sleep vote checks in dimension " + ageToDisable), true);
                } else {
                    command.getSource().sendFeedback(new TextComponentString("Sleep vote checks are already disabled in dimension " + ageToDisable), true);
                }
            }
            return 1;
        }));

        cmdDisp.register(morpheusCommand);
    }
}
