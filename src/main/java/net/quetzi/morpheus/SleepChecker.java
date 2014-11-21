package net.quetzi.morpheus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SleepChecker {

    @SuppressWarnings("unchecked")
    public void updatePlayerStates(World world) {

        // Iterate players and update their status
        for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
            String username = player.getGameProfile().getName();
            if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAsleep(username);
                // Alert players that this player has gone to bed
                alertPlayers(createAlert(player.dimension, player.getGameProfile().getName(), Morpheus.onSleepText), world);
            }
            else if (!player.isPlayerFullyAsleep() && Morpheus.playerSleepStatus.get(player.dimension).isPlayerSleeping(username)) {
                Morpheus.playerSleepStatus.get(player.dimension).setPlayerAwake(username);
                // Alert players that this player has woken up
                if (!world.isDaytime()) {
                    alertPlayers(createAlert(player.dimension, player.getGameProfile().getName(), Morpheus.onWakeText), world);
                }
            }
        }
        if (areEnoughPlayersAsleep(world.provider.getDimensionId())) {
            advanceToMorning(world);
        }
    }

    private void alertPlayers(ChatComponentText alert, World world) {

        if ((alert != null) && (Morpheus.isAlertEnabled())) {
            for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities) {
                player.addChatComponentMessage(alert);
            }
        }
    }

    private ChatComponentText createAlert(int dimension, String username, String text) {

        String alertText = EnumChatFormatting.GOLD + "Player " + EnumChatFormatting.WHITE + username + EnumChatFormatting.GOLD + " " + text + " "
                + Morpheus.playerSleepStatus.get(dimension).toString();
        Morpheus.mLog.info("Player " + username + " " + text + " " + Morpheus.playerSleepStatus.get(dimension).toString());
        return new ChatComponentText(alertText);
    }

    private void advanceToMorning(World world) {

        try {
            MorpheusRegistry.registry.get(world.provider.getDimensionId()).startNewDay();
        }
        catch (Exception e) {
            Morpheus.mLog.error("Exception caught while starting a new day for dimension " + world.provider.getDimensionId());
        }

        // Send Good morning message
        alertPlayers(new ChatComponentText(DateHandler.getMorningText()), world);
        Morpheus.playerSleepStatus.get(world.provider.getDimensionId()).wakeAllPlayers();
        world.getWorldInfo().setRainTime(0);
        world.getWorldInfo().setRaining(false);
        world.getWorldInfo().setThunderTime(0);
        world.getWorldInfo().setThundering(false);
    }



    private boolean areEnoughPlayersAsleep(int dimension) {

        return ((dimension == 0) || (MorpheusRegistry.registry.get(dimension) != null)) && Morpheus.playerSleepStatus.get(dimension).getPercentSleeping() >= Morpheus.perc;
    }
}
