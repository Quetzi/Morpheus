package net.quetzi.morpheus.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.quetzi.morpheus.helpers.Config;
import net.quetzi.morpheus.helpers.References;

public class CommandMorpheus {
    public static void register(CommandDispatcher<CommandSource> cmdDisp) {
        LiteralArgumentBuilder<CommandSource> morpheusCommand = Commands.literal("morpheus");
        morpheusCommand.executes((command) -> {
            command.getSource().sendFeedback(new StringTextComponent("Usage: " + References.USAGE), true);
            return 1;
        });
        morpheusCommand
                .then(Commands.literal("version")
                .executes((command) -> {
                    command.getSource().sendFeedback(new StringTextComponent("Morpheus version: " + References.VERSION), true);
                    return 1;
                }));
        morpheusCommand
                .then(Commands.literal("alert")
                .executes((command) -> {
                    if (Config.SERVER.alertEnabled.get()) {
                        Config.SERVER.alertEnabled.set(false);
                        command.getSource().sendFeedback(new StringTextComponent(References.ALERTS_OFF), true);
                    } else {
                        Config.SERVER.alertEnabled.set(true);
                        command.getSource().sendFeedback(new StringTextComponent(References.ALERTS_ON), true);
                    }
                    return 1;
                }));
        morpheusCommand
                .then(Commands.literal("percent")
                .then(Commands.argument("value", IntegerArgumentType.integer(0,100))
                .executes((command) -> {
                    if (command.getSource().hasPermissionLevel(2)) {
                        int newPercent = IntegerArgumentType.getInteger(command, "value");
                        Config.SERVER.perc.set(newPercent);
                        Config.SERVER.perc.save();
                        command.getSource().sendFeedback(new StringTextComponent("Sleep vote percentage set to " + Config.SERVER.perc.get() + "%"), true);
                    }
                    return 1;
                })));
//        morpheusCommand
//                .then(Commands.literal("disable")
//                .then(Commands.argument("dim", IntegerArgumentType.integer())
//                .executes((command) -> {
//                    if (command.getSource().hasPermissionLevel(2)) {
//                        int ageToDisable = command.getArgument("dim", Integer.class);
//                        if (Morpheus.register.isDimRegistered(ageToDisable)) {
//                            Morpheus.register.unregisterHandler(ageToDisable);
//                            command.getSource().sendFeedback(new StringTextComponent("Disabled sleep vote checks in dimension " + ageToDisable), true);
//                        } else {
//                            command.getSource().sendFeedback(new StringTextComponent("Sleep vote checks are already disabled in dimension " + ageToDisable), true);
//                        }
//                    }
//                    return 1;
//                })));
        cmdDisp.register(morpheusCommand);
    }
}
