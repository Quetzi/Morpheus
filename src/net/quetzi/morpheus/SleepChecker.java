package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class SleepChecker implements ITickHandler {

	private HashMap<Integer, Integer> sleepingPlayers = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> sleepingPercent = new HashMap<Integer, Integer>();

	// @ForgeSubscribe
	// @SideOnly(Side.SERVER)
	// public void onPlayerSleeping(PlayerSleepInBedEvent event) {
	// if (!event.entityPlayer.worldObj.isDaytime()) {
	// HashMap<String, Boolean> worldStatus = new HashMap<String, Boolean>();
	// Morpheus.playerSleepStatus.put(event.entityPlayer.dimension,
	// worldStatus);
	// Morpheus.playerSleepStatus.get(event.entityPlayer.dimension).put(
	// event.entityPlayer.username, true);
	// Morpheus.playerSleepStatus.get(event.entityPlayer.dimension).put(
	// event.entityPlayer.username, true);
	// }
	// }

	public void updatePlayerStates(World world) {
		if (world.playerEntities.size() == 0) {
			Morpheus.playerSleepStatus.remove(world.provider.dimensionId);
		}
		for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
			if (Morpheus.playerSleepStatus.get(player.dimension).get(player.username) == null) {
				Morpheus.playerSleepStatus.get(player.dimension).put(player.username, false);
			}
			if (player.isPlayerFullyAsleep()
					&& !Morpheus.playerSleepStatus.get(player.dimension).get(
							player.username)) {
				Morpheus.playerSleepStatus.get(player.dimension).put(
						player.username, true);
				areEnoughPlayersAsleep(world);
				// Alert players that this player has gone to bed
				alertPlayers(
						createAlert(player.worldObj, player,
								Morpheus.onSleepText), world);
			} else if (!player.isPlayerFullyAsleep()
					&& Morpheus.playerSleepStatus.get(player.dimension).get(
							player.username)) {
				// Alert players that this player has woken up
				alertPlayers(
						createAlert(player.worldObj, player,
								Morpheus.onWakeText), world);
				Morpheus.playerSleepStatus.get(player.dimension).put(
						player.username, false);
			}
		}
	}

	public int countSleepingPlayers(World world) {
		int sleeperCount = 0;
		for (Entry<String, Boolean> entry : Morpheus.playerSleepStatus.get(
				world.provider.dimensionId).entrySet()) {
			if (entry.getValue()) {
				sleeperCount++;
			}
		}
		sleepingPlayers.put(world.provider.dimensionId, sleeperCount);
		return sleeperCount;
	}

	private void alertPlayers(ChatMessageComponent alert, World world) {
		if (!Morpheus.alertPlayers) {
			return;
		}
		for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
			player.sendChatToPlayer(alert);
		}
		Morpheus.mLog.info(alert.toString());
	}

	private ChatMessageComponent createAlert(World world, EntityPlayer player,
			String text) {
		ChatMessageComponent chatAlert = new ChatMessageComponent();
		chatAlert.addText(EnumChatFormatting.GOLD + "Player "
				+ EnumChatFormatting.WHITE + player.username
				+ EnumChatFormatting.GOLD + " " + text + " "
				+ sleepingPlayers.get(world.provider.dimensionId) + "/"
				+ player.worldObj.playerEntities.size() + " ("
				+ sleepingPercent.get(world.provider.dimensionId) + "%)");
		return chatAlert;
	}

	private void advanceToMorning(World world) {
		long ticks = world.getWorldTime()
				+ (24000 - (world.getWorldTime() % 24000));
		world.setWorldTime(ticks);
		// Send Good morning message
		alertPlayers(new ChatMessageComponent().addText(EnumChatFormatting.GOLD + Morpheus.onMorningText), world);
	}

	private void areEnoughPlayersAsleep(World world) {
		sleepingPercent.put(
				world.provider.dimensionId,
				(countSleepingPlayers(world) * 100)
						/ Morpheus.playerSleepStatus.get(
								world.provider.dimensionId).size());
		if (sleepingPercent.get(world.provider.dimensionId) >= Morpheus.perc) {
			advanceToMorning(world);
		}
	}

	public void worldTick(World world) {
		// This is called every tick, do something every 20 ticks
		if ((world.getWorldTime() % 20 == 0)
				&& (world.playerEntities.size() > 0)) {
			updatePlayerStates(world);
			if (Morpheus.playerSleepStatus.get(world.provider.dimensionId) != null) {
				areEnoughPlayersAsleep(world);
			} else {
				HashMap<String, Boolean> worldStatus = new HashMap<String, Boolean>();
				Morpheus.playerSleepStatus.put(world.provider.dimensionId,
						worldStatus);
			}
		}
	}

	// ITickHandler implementation
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if (type.equals(EnumSet.of(TickType.WORLD))) {
			worldTick((World) tickData[0]);
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "MorpheusTicker";
	}
}