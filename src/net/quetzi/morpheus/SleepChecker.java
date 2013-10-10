package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.quetzi.morpheus.world.WorldSleepState;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class SleepChecker implements ITickHandler {

	private void updatePlayerStates(World world) {
		// Iterate players and update their status
		for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
			if (player.isPlayerFullyAsleep() && 
					!Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(player.username)) {
				Morpheus.playerSleepStatus.get(player.dimension).setPlayerAsleep(player.username);
				// Alert players that this player has gone to bed
				alertPlayers(createAlert(player.worldObj, player,Morpheus.onSleepText), world);
			} else if (!player.isPlayerFullyAsleep() && 
					Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(player.username)) {
				Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(player.username);
				// Alert players that this player has woken up
				alertPlayers(createAlert(player.worldObj, player,Morpheus.onWakeText), world);
			}
		}
	}

	private void alertPlayers(ChatMessageComponent alert, World world) {
		if ((alert != null) && (Morpheus.alertPlayers)) {
			for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
				player.sendChatToPlayer(alert);
			}
		}
		Morpheus.mLog.info(alert.toString());
	}

	private ChatMessageComponent createAlert(World world, EntityPlayer player,
			String text) {
		ChatMessageComponent chatAlert = new ChatMessageComponent();
		chatAlert.addText(EnumChatFormatting.GOLD
				+ "Player "
				+ EnumChatFormatting.WHITE
				+ player.username
				+ EnumChatFormatting.GOLD
				+ " " + text + " "
				+ Morpheus.playerSleepStatus.get(world.provider.dimensionId).toString());
		return chatAlert;
	}

	private void advanceToMorning(World world) {
		world.setWorldTime(world.getWorldTime() + getTimeToSunrise(world));
		// Send Good morning message
		alertPlayers(ChatMessageComponent.createFromText(EnumChatFormatting.GOLD
						+ Morpheus.onMorningText), world);
		// Set all players as awake silently
		Morpheus.playerSleepStatus.get(world.provider.dimensionId)
				.wakeAllPlayers();
	}
	
	private long getTimeToSunrise(World world) {
		long dayLength = 24000L;
		long ticks = dayLength - (world.getWorldTime() % dayLength);
		return ticks;
	}

	private boolean areEnoughPlayersAsleep(World world) {
		if (Morpheus.playerSleepStatus.get(world.provider.dimensionId)
				.getPercentSleeping() >= Morpheus.perc) {
			return true;
		}
		return false;
	}

	private void worldTick(World world) {
		// This is called every tick, do something every 20 ticks
		if (world.getWorldTime() % 20L == 0) {
			if (world.playerEntities.size() > 0) {
				if (Morpheus.playerSleepStatus.get(world.provider.dimensionId) == null) {
					Morpheus.playerSleepStatus.put(world.provider.dimensionId,
							new WorldSleepState(world.provider.dimensionId));
				}
				updatePlayerStates(world);
				if (areEnoughPlayersAsleep(world)) {
					advanceToMorning(world);
				}
			}
			else {
				Morpheus.playerSleepStatus.remove(world.provider.dimensionId);
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