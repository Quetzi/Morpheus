package net.quetzi.morpheus.helpers;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.quetzi.morpheus.Morpheus;
import net.quetzi.morpheus.world.WorldSleepState;

public class MorpheusEventHandler {
    @SubscribeEvent
    public void loggedInEvent(PlayerLoggedInEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            if (!Morpheus.playerSleepStatus.containsKey(event.getPlayer().getEntityWorld().func_234923_W_())) {
                Morpheus.playerSleepStatus.put(event.getPlayer().getEntityWorld().func_234923_W_(), new WorldSleepState(event.getPlayer().getEntityWorld().func_234923_W_()));
            }
            Morpheus.playerSleepStatus.get(event.getPlayer().getEntityWorld().func_234923_W_()).setPlayerAwake(event.getPlayer().getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            if (Morpheus.playerSleepStatus.get(event.getPlayer().getEntityWorld().func_234923_W_()) != null) {
                Morpheus.playerSleepStatus.get(event.getPlayer().getEntityWorld().func_234923_W_()).removePlayer(event.getPlayer().getGameProfile().getName());
            }
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            if (!Morpheus.playerSleepStatus.containsKey(event.getTo())) {
                Morpheus.playerSleepStatus.put(event.getTo(), new WorldSleepState(event.getTo()));
            }
            // Remove player from old World state
            if (Morpheus.playerSleepStatus.get(event.getFrom()) != null) {
                Morpheus.playerSleepStatus.get(event.getFrom()).removePlayer(event.getPlayer().getGameProfile().getName());
            }
            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.getTo()).setPlayerAwake(event.getPlayer().getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event) {
        RegistryKey<World> dimid = event.world.func_234923_W_();
        if (!event.world.isRemote) {
            // This is called every tick, do something every 20 ticks
            if (event.world.getGameTime() % 20L == 10 && event.phase == TickEvent.Phase.END) {
                if (event.world.getPlayers().size() > 0) {
                    if (!Morpheus.playerSleepStatus.containsKey(dimid)) {
                        Morpheus.playerSleepStatus.put(dimid, new WorldSleepState(dimid));
                    }
                    if (Morpheus.playerSleepStatus.get(dimid).getPlayerCount() > event.world.getPlayers().size()) {
                        Morpheus.playerSleepStatus.get(dimid).resetPlayers();
                    }
                    Morpheus.checker.updatePlayerStates(event.world);
                } else {
                    Morpheus.playerSleepStatus.remove(dimid);
                }
            }
        }
    }
}
