package net.quetzi.morpheus.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;

import java.util.HashMap;

public class SleepChecker {
    private HashMap<Integer, Boolean> alertSent = new HashMap<>();

    public void updatePlayerStates(World world) {
        //Don't bother updating if there is only 1 player
        if (world.getPlayers().size() > 1) {
            // Iterate players and update their status
            for (PlayerEntity player : world.getPlayers()) {
                String username = player.getGameProfile().getName();
                if (player.isPlayerFullyAsleep() && !Morpheus.playerSleepStatus.get(player.dimension.getId()).isPlayerSleeping(username)) {
                    Morpheus.playerSleepStatus.get(player.dimension.getId()).setPlayerAsleep(username);
                    // Alert players that this player has gone to bed
                    alertPlayers(createAlert(player.dimension.getId(), player.getDisplayName().getString(), Config.SERVER.onSleepText.get()), world);
                } else if (!player.isPlayerFullyAsleep() && Morpheus.playerSleepStatus.get(player.dimension.getId()).isPlayerSleeping(username)) {
                    Morpheus.playerSleepStatus.get(player.dimension.getId()).setPlayerAwake(username);
                    // Alert players that this player has woken up
                    if (!world.isDaytime() && !alertSent.get(world.getDimension().getType().getId())) {
                        alertPlayers(createAlert(player.dimension.getId(), player.getDisplayName().getString(), Config.SERVER.onWakeText.get()), world);
                    }
                }
            }
            if (areEnoughPlayersAsleep(world.getDimension().getType().getId())) {
                if (!alertSent.containsKey(world.getDimension().getType().getId())) {
                    alertSent.put(world.getDimension().getType().getId(), false);
                }
                advanceToMorning(world);
            } else {
                alertSent.put(world.getDimension().getType().getId(), false);
            }
        }
    }

    private void alertPlayers(StringTextComponent alert, World world) {
        if ((alert != null) && (Morpheus.isAlertEnabled())) {
            for (PlayerEntity player : world.getPlayers()) {
                player.sendMessage(alert);
            }
        }
    }

    private StringTextComponent createAlert(int dimension, String username, String text) {
        Morpheus.logger.info(String.format("%s %s %s", username, text, Morpheus.playerSleepStatus.get(dimension).toString()));
        return new StringTextComponent(String.format("%s%s%s %s %s", TextFormatting.WHITE, username, TextFormatting.GOLD, text, Morpheus.playerSleepStatus.get(dimension).toString()));
    }

    private void advanceToMorning(World world) {
        try {
            MorpheusRegistry.registry.get(world.getDimension().getType().getId()).startNewDay();
        } catch (Exception e) {
            Morpheus.logger.error("Exception caught while starting a new day for dimension " + world.getDimension().getType().getId());
        }
        if (!alertSent.get(world.getDimension().getType().getId())) {
            // Send Good morning message
            alertPlayers(new StringTextComponent(DateHandler.getMorningText()), world);
            Morpheus.playerSleepStatus.get(world.getDimension().getType().getId()).wakeAllPlayers();
            alertSent.put(world.getDimension().getType().getId(), true);
        }
        world.getDimension().resetRainAndThunder();
    }

    private boolean areEnoughPlayersAsleep(int dimension) {
        if (Morpheus.playerSleepStatus.get(dimension).getSleepingPlayers() > 0) {
            return ((dimension == 0) || (MorpheusRegistry.registry.get(dimension) != null)) && Morpheus.playerSleepStatus.get(dimension).getPercentSleeping() >= Config.SERVER.perc.get();
        }
        return false;
    }
}
