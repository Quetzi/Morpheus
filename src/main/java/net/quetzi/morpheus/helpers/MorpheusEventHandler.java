package net.quetzi.morpheus.helpers;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.world.WorldSleepState;

public class MorpheusEventHandler {
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        ResourceKey<Level> playerWorld = event.getEntity().getLevel().dimension();
        if (!event.getEntity().getLevel().isClientSide) {
            if (!Morpheus.playerSleepStatus.containsKey(playerWorld)) {
                Morpheus.playerSleepStatus.put(playerWorld, new WorldSleepState(playerWorld));
            }
            Morpheus.playerSleepStatus.get(playerWorld).setPlayerAwake(event.getEntity().getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {
        ResourceKey<Level> playerWorld = event.getEntity().getLevel().dimension();
        if (!event.getEntity().getLevel().isClientSide) {
            if (Morpheus.playerSleepStatus.get(playerWorld) != null) {
                Morpheus.playerSleepStatus.get(playerWorld).removePlayer(event.getEntity().getGameProfile().getName());
            }
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {
        if (!event.getEntity().getLevel().isClientSide) {
            if (!Morpheus.playerSleepStatus.containsKey(event.getTo())) {
                Morpheus.playerSleepStatus.put(event.getTo(), new WorldSleepState(event.getTo()));
            }
            // Remove player from old World state
            if (Morpheus.playerSleepStatus.get(event.getFrom()) != null) {
                Morpheus.playerSleepStatus.get(event.getFrom()).removePlayer(event.getEntity().getGameProfile().getName());
            }
            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.getTo()).setPlayerAwake(event.getEntity().getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(LevelTickEvent event) {
        ResourceKey<Level> dimid = event.level.dimension();
        if (!event.level.isClientSide) {
            // This is called every tick, do something every 20 ticks
            if (event.level.getGameTime() % 20L == 10 && event.phase == TickEvent.Phase.END) {
                if (event.level.players().size() > 1) {
                    if (!Morpheus.playerSleepStatus.containsKey(dimid)) {
                        Morpheus.playerSleepStatus.put(dimid, new WorldSleepState(dimid));
                    }
                    if (Morpheus.playerSleepStatus.get(dimid).getPlayerCount() > event.level.players().size()) {
                        Morpheus.playerSleepStatus.get(dimid).resetPlayers();
                    }
                    Morpheus.checker.updatePlayerStates(event.level);
                } else {
                    Morpheus.playerSleepStatus.remove(dimid);
                }
            }
        }
    }
}
