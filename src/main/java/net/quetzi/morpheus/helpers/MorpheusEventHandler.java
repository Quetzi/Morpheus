package net.quetzi.morpheus.helpers;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        if (!event.getPlayer().getEntityWorld().isRemote) {
            if (!Morpheus.playerSleepStatus.containsKey(event.getPlayer().dimension.getId())) {
                Morpheus.playerSleepStatus.put(event.getPlayer().dimension.getId(), new WorldSleepState(event.getPlayer().dimension.getId()));
            }
            Morpheus.playerSleepStatus.get(event.getPlayer().dimension.getId()).setPlayerAwake(event.getPlayer().getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void loggedOutEvent(PlayerLoggedOutEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            if (Morpheus.playerSleepStatus.get(event.getPlayer().dimension.getId()) != null) {
                Morpheus.playerSleepStatus.get(event.getPlayer().dimension.getId()).removePlayer(event.getPlayer().getGameProfile().getName());
            }
        }
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerChangedDimensionEvent event) {
        if (!event.getPlayer().getEntityWorld().isRemote) {
            if (!Morpheus.playerSleepStatus.containsKey(event.getTo().getId())) {
                Morpheus.playerSleepStatus.put(event.getTo().getId(), new WorldSleepState(event.getTo().getId()));
            }
            // Remove player from old World state
            if (Morpheus.playerSleepStatus.get(event.getFrom().getId()) != null) {
                Morpheus.playerSleepStatus.get(event.getFrom().getId()).removePlayer(event.getPlayer().getGameProfile().getName());
            }
            // Add player to new world state
            Morpheus.playerSleepStatus.get(event.getTo().getId()).setPlayerAwake(event.getPlayer().getGameProfile().getName());
        }
    }

    @SubscribeEvent
    public void worldTickEvent(WorldTickEvent event) {
        if (!event.world.isRemote) {
            // This is called every tick, do something every 20 ticks
            if (event.world.getGameTime() % 20L == 10 && event.phase == TickEvent.Phase.END) {
                if (event.world.getPlayers().size() > 0) {
                    if (!Morpheus.playerSleepStatus.containsKey(event.world.getDimension().getType().getId())) {
                        Morpheus.playerSleepStatus.put(event.world.getDimension().getType().getId(), new WorldSleepState(event.world.getDimension().getType().getId()));
                    }
                    Morpheus.checker.updatePlayerStates(event.world);
                } else {
                    Morpheus.playerSleepStatus.remove(event.world.getDimension().getType().getId());
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void bedClicked(PlayerInteractEvent.RightClickBlock event) {
        if (Morpheus.setSpawnDaytime) {
            PlayerEntity player = event.getEntityPlayer();
            BlockPos pos = event.getPos();
            if (!event.getWorld().isRemote && event.getWorld().isDaytime() && !event.getEntityPlayer().isSneaking()) {
                if (player.getBedLocation(player.dimension) == null || player.getBedLocation(player.dimension).compareTo(pos) > 4) {
                    BlockState state = event.getWorld().getBlockState(pos);
                    if (state.getBlock() instanceof BedBlock) {
                        if (event.getWorld().getDimension().canRespawnHere() && event.getWorld().getDimension().getBiome(pos) != Biomes.NETHER) {
                            if (!state.get(BedBlock.OCCUPIED)) {
                                player.setSpawnPoint(pos, false, event.getWorld().getDimension().getType());
                                player.sendMessage(new StringTextComponent(References.SPAWN_SET));
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
