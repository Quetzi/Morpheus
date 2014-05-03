package net.quetzi.morpheus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.Loader;
import net.quetzi.morpheus.world.SleepState;

import java.util.ArrayList;
import java.util.List;

public class SleepChecker {

    private static List<Integer> worldsToRecheck =  new ArrayList<Integer>();

	public static void updatePlayerStates(World world) {
		// Iterate players and update their status
        for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities)
        {
            //If a player is sleeping and the player wasn't the previous check add it to the sleep list
            if (player.isPlayerFullyAsleep()
                    && !SleepState.wasPlayerSleeping(player)) {
                SleepState.addSleepingPlayer(player);
                // Alert players that this player has gone to bed
                alertPlayers(
                        createAlert(player.worldObj, player,
                                Morpheus.onSleepText), world
                );
                // If enough are asleep set it to day
                checkSleepNextCheck(world);
            }
            //If a player isn't sleeping and the player was the previous check remove it from the sleep list
            else if (!player.isPlayerFullyAsleep()
                    && SleepState.wasPlayerSleeping(player)) {
                SleepState.removeSleepingPlayer(player);
                // Alert players that this player has woken up
                if (!world.isDaytime())
                {
                    alertPlayers(
                            createAlert(player.worldObj, player,
                                    Morpheus.onWakeText), world
                    );
                }
            }

            if (worldsToRecheck.contains(world.provider.dimensionId)) {
                if (areEnoughPlayersAsleep(world)) {
                    advanceToMorning(world);
                }
                worldsToRecheck.remove(world.provider.dimensionId);
            }
        }
	}

	private static void alertPlayers(ChatComponentText alert, World world) {
		if ((alert != null) && (Morpheus.alertEnabled)) {
            MinecraftServer.getServer().getConfigurationManager()
                    .sendPacketToAllPlayersInDimension(new S02PacketChat(alert, true), world.provider.dimensionId);
		}
		Morpheus.mLog.info(alert);
	}

	private static ChatComponentText createAlert(World world, EntityPlayer player,
			String text) {
		String alertText = EnumChatFormatting.GOLD
				+ "Player "
				+ EnumChatFormatting.WHITE
				+ player.getCommandSenderName()
				+ EnumChatFormatting.GOLD
				+ " "
				+ text
				+ " "
				+ SleepState.getSleepText(world);
        return new ChatComponentText(alertText);
	}

    private static void advanceToMorning(World world) {
		world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
		// Send Good morning message
		alertPlayers(new ChatComponentText(EnumChatFormatting.GOLD
				+ Morpheus.onMorningText), world);
		world.provider.resetRainAndThunder();
	}

	private static long getTimeToSunrise(World world) {
		long dayLength = 24000;
        return dayLength - (world.getWorldTime() % dayLength);
	}

    private static boolean areEnoughPlayersAsleep(World world) {
		// Disable in Twilight Forest
		if (Loader.isModLoaded("Twilight Forest")
				&& world.provider.dimensionId == 7) {
			return false;
		}
        return SleepState.getPercentSleeping(world) >= Morpheus.perc;
    }

    public static void checkSleepNextCheck(World world) {
        if (!worldsToRecheck.contains(world.provider.dimensionId))
            worldsToRecheck.add(world.provider.dimensionId);
    }
}