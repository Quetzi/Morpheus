package net.quetzi.morpheus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.quetzi.morpheus.helpers.Config;
import net.quetzi.morpheus.helpers.References;

public class CommandMorpheus {
    public static void register(CommandDispatcher<CommandSourceStack> cmdDisp) {
        LiteralArgumentBuilder<CommandSourceStack> morpheusCommand = Commands.literal("morpheus");
        morpheusCommand.executes((command) -> {
            if(command.getSource().hasPermission(2)) {
                command.getSource().sendSuccess(Component.literal("Usage: " + References.USAGE), true);
            }else {
                command.getSource().sendSuccess(Component.literal("Usage: " + References.USAGE_NOT_OP), true);
            }
            return 1;
        });
        morpheusCommand
                .then(Commands.literal("version")
                .executes((command) -> {
                    command.getSource().sendSuccess(Component.literal("Morpheus version: " + References.VERSION), true);
                    return 1;
                }));
        morpheusCommand
                .then(Commands.literal("alert")
                .requires(commandSource -> commandSource.hasPermission(2))
                .executes((command) -> {
                    if (Config.SERVER.alertEnabled.get()) {
                        Config.SERVER.alertEnabled.set(false);
                        command.getSource().sendSuccess(Component.literal(References.ALERTS_OFF), true);
                    } else {
                        Config.SERVER.alertEnabled.set(true);
                        command.getSource().sendSuccess(Component.literal(References.ALERTS_ON), true);
                    }
                    return 1;
                }));
        morpheusCommand
                .then(Commands.literal("percent")
                .requires(commandSource -> commandSource.hasPermission(2))
                .then(Commands.argument("value", IntegerArgumentType.integer(0,100))
                .executes((command) -> {
                    int newPercent = IntegerArgumentType.getInteger(command, "value");
                    Config.SERVER.perc.set(newPercent);
                    Config.SERVER.perc.save();
                    command.getSource().sendSuccess(Component.literal("Sleep vote percentage set to " + Config.SERVER.perc.get() + "%"), true);
                    return 1;
                })));
        cmdDisp.register(morpheusCommand);
    }
}
