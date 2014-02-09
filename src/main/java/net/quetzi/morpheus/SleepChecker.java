package net.quetzi.morpheus;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.quetzi.morpheus.world.WorldSleepState;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public class SleepChecker {

	public void updatePlayerStates(World world) {
		// Iterate players and update their status
		for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
			if (player.isPlayerFullyAsleep()
					&& !Morpheus.playerSleepStatus.get(player.dimension)
							.isPlayerSleeping(player.getDisplayName())) {
				Morpheus.playerSleepStatus.get(player.dimension)
						.setPlayerAsleep(player.getDisplayName());
				// Alert players that this player has gone to bed
				alertPlayers(
						createAlert(player.worldObj, player,
								Morpheus.onSleepText), world);
				// If enough are asleep set it to day
				if (areEnoughPlayersAsleep(world)) {
					advanceToMorning(world);
				}
			} else if (!player.isPlayerFullyAsleep()
					&& Morpheus.playerSleepStatus.get(player.dimension)
							.isPlayerSleeping(player.getDisplayName())) {
				Morpheus.playerSleepStatus.get(player.dimension)
						.setPlayerAwake(player.getDisplayName());
				// Alert players that this player has woken up
				alertPlayers(
						createAlert(player.worldObj, player,
								Morpheus.onWakeText), world);
			}
		}
	}

	private void alertPlayers(ChatComponentText alert, World world) {
		if ((alert != null) && (Morpheus.alertEnabled)) {
			for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
				player.addChatMessage(alert);
			}
		}
		System.out.println("[Morpheus]" + alert.getUnformattedText());
	}

	private ChatComponentText createAlert(World world, EntityPlayer player,
			String text) {
		String alertText = EnumChatFormatting.GOLD
				+ "Player "
				+ EnumChatFormatting.WHITE
				+ player.getDisplayName()
				+ EnumChatFormatting.GOLD
				+ " "
				+ text
				+ " "
				+ Morpheus.playerSleepStatus.get(world.provider.dimensionId)
						.toString();
		ChatComponentText chatAlert = new ChatComponentText(alertText);
		return chatAlert;
	}

	private void advanceToMorning(World world) {
		world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
		// Send Good morning message
		alertPlayers(new ChatComponentText(EnumChatFormatting.GOLD
				+ Morpheus.onMorningText), world);
		// Set all players as awake silently
		Morpheus.playerSleepStatus.get(world.provider.dimensionId)
				.wakeAllPlayers();
		world.provider.resetRainAndThunder();
	}

	private long getTimeToSunrise(World world) {
		long dayLength = 24000;
		long ticks = dayLength - (world.getWorldTime() % dayLength);
		return ticks;
	}

	private boolean areEnoughPlayersAsleep(World world) {
		// Disable in Twilight Forest
		if (Loader.isModLoaded("Twilight Forest")
				&& world.provider.dimensionId == 7) {
			return false;
		}
		if (Morpheus.playerSleepStatus.get(world.provider.dimensionId)
				.getPercentSleeping() >= Morpheus.perc) {
			return true;
		}
		return false;
	}
}