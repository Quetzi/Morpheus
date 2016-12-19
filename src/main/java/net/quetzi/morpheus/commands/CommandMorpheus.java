package net.quetzi.morpheus.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;
import net.quetzi.morpheus.helpers.References;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandMorpheus extends CommandBase
{
    private final List<String> aliases;

    public CommandMorpheus()
    {
        aliases = new ArrayList<>();
        aliases.add("sleepvote");
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "morpheus";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return References.USAGE;
    }

    @Nonnull
    @Override
    public List<String> getAliases()
    {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws NumberInvalidException
    {
        if (args.length == 0)
        {
            sender.sendMessage(new TextComponentString("Usage: " + References.USAGE).setStyle(new Style().setColor(TextFormatting.RED)));
            return;
        }
        if (args[0].equalsIgnoreCase("alert"))
        {
            if (Morpheus.isAlertEnabled())
            {
                Morpheus.setAlertPlayers(false);
                sender.sendMessage(new TextComponentString(References.ALERTS_OFF));
            }
            else
            {
                Morpheus.setAlertPlayers(true);
                sender.sendMessage(new TextComponentString(References.ALERTS_ON));
            }
        }
        else if (args[0].equalsIgnoreCase("version"))
        {
            sender.sendMessage(new TextComponentString("Morpheus version: " + References.VERSION));
        }
        else
        {
            boolean setPercent = args[0].equalsIgnoreCase("percent");
            if (setPercent || args[0].equalsIgnoreCase("disable"))
            {
                if (args.length > 1)
                {
                    // Do op check
                    if (isPlayerOpped(sender))
                    {
                        if (setPercent)
                        {
                            int newPercent = parseInt(args[1]);
                            if (newPercent > 0 && newPercent <= 100)
                            {
                                Morpheus.perc = newPercent;
                                Morpheus.config.get("settings", "SleeperPerc", 50).set(newPercent);
                                Morpheus.config.save();
                                sender.sendMessage(new TextComponentString("Sleep vote percentage set to " + Morpheus.perc + "%"));
                            }
                            else
                            {
                                sender.sendMessage(new TextComponentString("Invalid percentage value, round numbers between 0 and 100 are acceptable."));
                            }
                        }
                        else
                        {
                            int ageToDisable = parseInt(args[1]);
                            if (Morpheus.register.isDimRegistered(ageToDisable))
                            {
                                Morpheus.register.unregisterHandler(ageToDisable);
                                sender.sendMessage(new TextComponentString("Disabled sleep vote checks in dimension " + ageToDisable));
                            }
                            else
                            {
                                sender.sendMessage(new TextComponentString("Sleep vote checks are already disabled in dimension " + ageToDisable));
                            }
                        }
                    }
                    else
                    {
                        sender.sendMessage(new TextComponentString("You must be opped to " + (setPercent ? "set the sleep vote percentage." : "disable dimensions.")));
                    }
                }
                else
                {
                    sender.sendMessage(new TextComponentString(setPercent ? References.PERCENT_USAGE : References.DISABLE_USAGE).setStyle(new Style().setColor(TextFormatting.RED)));
                }
            }
        }
    }

    @Override
    public boolean checkPermission(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private boolean isPlayerOpped(ICommandSender sender)
    {
        if (sender instanceof EntityPlayer)
        {
            for (String player : sender.getServer().getPlayerList().getOppedPlayerNames())
            {
                if (player.equals(sender.getName()))
                {
                    return true;
                }
            }
            return false;
        }
        return true; // If it isn't a player, then it's the console
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "alert", "disable", "percent", "version");
        }
        List options = new ArrayList();
        if (args[0].equalsIgnoreCase("disable"))
        {
            for (Integer dimensionId : MorpheusRegistry.registry.keySet())
            {
                options.add(dimensionId.toString());
            }
            options = getListOfStringsMatchingLastWord(args, options);
        }
        return options;
    }
}
