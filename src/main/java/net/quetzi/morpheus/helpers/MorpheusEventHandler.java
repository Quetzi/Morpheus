package net.quetzi.morpheus.helpers;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.world.WorldSleepState;

public class MorpheusEventHandler {

    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {

        if (!event.player.getEntityWorld().getMinecraftServer().worldServerForDimension(event.player.dimension).isRemote) {
            if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) {
                Morpheus.playerSleepStatus.put(event.player.dimension, new WorldSleepState(event.player.dimension));
            }
            Morpheus.playerSleepStatus.get(event.player.dimension).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {

        if (!event.player.getEntityWorld().getMinecraftServer().worldServerForDimension(event.player.dimension).isRemote) {
            if (Morpheus.playerSleepStatus.get(event.player.dimension) == null) { return; }
            Morpheus.playerSleepStatus.get(event.player.dimension).removePlayer(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {

        if (!event.player.getEntityWorld().getMinecraftServer().worldServerForDimension(event.player.dimension).isRemote) {
            if (Morpheus.playerSleepStatus.get(event.toDim) == null) {
                Morpheus.playerSleepStatus.put(event.toDim, new WorldSleepState(event.toDim));
            }
            // Remove player from old World state
            if (Morpheus.playerSleepStatus.get(event.fromDim) != null) {
                Morpheus.playerSleepStatus.get(event.fromDim).removePlayer(event.player.getGameProfile().getName());
            }
            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.toDim).setPlayerAwake(event.player.getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event) {

        if (!event.world.isRemote) {
            // This is called every tick, do something every 20 ticks
            if (event.world.getWorldTime() % 20L == 10 && event.phase == TickEvent.Phase.END) {
                if (event.world.playerEntities.size() > 0) {
                    if (Morpheus.playerSleepStatus.get(event.world.provider.getDimension()) == null) {
                        Morpheus.playerSleepStatus.put(event.world.provider.getDimension(), new WorldSleepState(event.world.provider.getDimension()));
                    }
                    Morpheus.checker.updatePlayerStates(event.world);
                } else {
                    Morpheus.playerSleepStatus.remove(event.world.provider.getDimension());
                }
            }
        }
    }
}
