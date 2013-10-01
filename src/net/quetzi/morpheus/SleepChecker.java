package net.quetzi.morpheus;

import java.util.ArrayList;
import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class SleepChecker implements ITickHandler {

	public void updatePlayerStates(World world) {
		if (world.playerEntities.size() == 0) {
			Morpheus.playerSleepStatus.remove(world.provider.dimensionId);
		}
		for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
			if (player.isPlayerFullyAsleep()
					&& !Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(player.username)) {
				Morpheus.playerSleepStatus.get(player.dimension).setPlayerAsleep(player.username);
				// Alert players that this player has gone to bed
				alertPlayers(
						createAlert(player.worldObj, player,
								Morpheus.onSleepText), world);
			} else if (!player.isPlayerFullyAsleep()
					&& Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(player.username)) {
				// Alert players that this player has woken up
				alertPlayers(
						createAlert(player.worldObj, player,
								Morpheus.onWakeText), world);
				Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(player.username);;
			}
		}
	}
	
	private void alertPlayers(ChatMessageComponent alert, World world) {
		if (alert != null) {
			if (!Morpheus.alertPlayers) {
				return;
			}
			for (EntityPlayer player : (ArrayList<EntityPlayer>) world.playerEntities) {
				player.sendChatToPlayer(alert);
			}
			Morpheus.mLog.info(alert.toString());
		}
	}

	private ChatMessageComponent createAlert(World world, EntityPlayer player,
			String text) {
		ChatMessageComponent chatAlert = new ChatMessageComponent();
		chatAlert.addText(EnumChatFormatting.GOLD + "Player "
				+ EnumChatFormatting.WHITE + player.username
				+ EnumChatFormatting.GOLD + " " + text + " "
				+ Morpheus.playerSleepStatus.get(world.provider.dimensionId).getSleepingPlayers() + "/"
				+ player.worldObj.playerEntities.size() + " ("
				+ Morpheus.playerSleepStatus.get(world.provider.dimensionId).getPercentSleeping() + "%)");
		return chatAlert;
	}
	
	private void advanceToMorning2(World world) {
		long ticks = world.getWorldTime()
				+ (24000L - (world.getWorldTime() % 24000L));
		world.setWorldTime(ticks);
		// Send Good morning message
		alertPlayers(
				new ChatMessageComponent().addText(EnumChatFormatting.GOLD
						+ Morpheus.onMorningText), world);
		// Set all players as awake silently
		Morpheus.playerSleepStatus.get(world.provider.dimensionId).wakeAllPlayers();
	}

	private void areEnoughPlayersAsleep(World world) {
		if (Morpheus.playerSleepStatus.get(world.provider.dimensionId).getPercentSleeping() >= Morpheus.perc) {
			advanceToMorning2(world);
		}
	}

	public void worldTick(World world) {
		// This is called every tick, do something every 20 ticks
		if ((world.getWorldTime() % 20L == 0)
				&& (world.playerEntities.size() > 0)) {
			
			updatePlayerStates(world);
			
			if (Morpheus.playerSleepStatus.get(world.provider.dimensionId) != null) {
				areEnoughPlayersAsleep(world);
			} else {
				WorldSleepState worldStatus = new WorldSleepState(world.provider.dimensionId);
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