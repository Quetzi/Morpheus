package net.quetzi.morpheus.helpers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.MorpheusRegistry;

import java.util.HashMap;

public class SleepChecker {
    private HashMap<ResourceKey<Level>, Boolean> alertSent = new HashMap<>();

    public void updatePlayerStates(Level world) {
        //Don't bother updating if there is only 1 player
        if (world.players().size() > 1) {
            // Iterate players and update their status
            for (Player player : world.players()) {
                String username = player.getGameProfile().getName();
                ResourceKey<Level> playerWorld = player.getLevel().dimension();
                if (player.isSleeping() && !Morpheus.playerSleepStatus.get(playerWorld).isPlayerSleeping(username)) {
                    Morpheus.playerSleepStatus.get(playerWorld).setPlayerAsleep(username);
                    // Alert players that this player has gone to bed
                    alertPlayers(createAlert(playerWorld, player.getDisplayName().getString(), Config.SERVER.onSleepText.get()), world);
                } else if (!player.isSleeping() && Morpheus.playerSleepStatus.get(playerWorld).isPlayerSleeping(username)) {
                    Morpheus.playerSleepStatus.get(playerWorld).setPlayerAwake(username);
                    // Alert players that this player has woken up
                    if (!world.isDay() && !alertSent.get(world.dimension())) {
                        alertPlayers(createAlert(playerWorld, player.getDisplayName().getString(), Config.SERVER.onWakeText.get()), world);
                    }
                }
            }
            if (areEnoughPlayersAsleep(world.dimension())) {
                if (!alertSent.containsKey(world.dimension())) {
                    alertSent.put(world.dimension(), false);
                }
                advanceToMorning(world);
            } else {
                alertSent.put(world.dimension(), false);
            }
        }
    }

    private void alertPlayers(Component alert, Level world) {
        if ((alert != null) && (Config.SERVER.alertEnabled.get())) {
            for (Player player : world.players()) {
                player.displayClientMessage(alert, false);
            }
        }
    }

    private Component createAlert(ResourceKey<Level> dimension, String username, String text) {
        return Component.literal(String.format("%s%s%s %s %s", ChatFormatting.WHITE, username, ChatFormatting.GOLD, text, Morpheus.playerSleepStatus.get(dimension).toString()));
    }

    private void advanceToMorning(Level world) {
        try {
            MorpheusRegistry.registry.get(world.dimension()).startNewDay();
        } catch (Exception e) {
            Morpheus.logger.error("Exception caught while starting a new day for dimension " + world.dimension());
            e.printStackTrace();
        }
        if (!alertSent.get(world.dimension())) {
            // Send Good morning message
            alertPlayers(Component.literal(DateHandler.getMorningText()), world);
            Morpheus.playerSleepStatus.get(world.dimension()).wakeAllPlayers();
            alertSent.put(world.dimension(), true);
        }
        if (world instanceof ServerLevel) {
            ((ServerLevel) world).setWeatherParameters(0, 0, false, false);
        }
    }

    private boolean areEnoughPlayersAsleep(ResourceKey<Level> dimension) {
        if (Morpheus.playerSleepStatus.get(dimension).getSleepingPlayers() > 0) {
            return ((MorpheusRegistry.registry.get(dimension) != null) && Morpheus.playerSleepStatus.get(dimension).getPercentSleeping() >= Config.SERVER.perc.get());
        }
        return false;
    }
}
