package net.quetzi.morpheus;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.quetzi.morpheus.world.SleepState;

public class MorpheusEventHandler {

	@SubscribeEvent
	public void worldTickEvent(WorldTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			// This is called every tick, do something every 20 ticks
			if (event.world.getWorldTime() % 20 == 10) {
				if (event.world.playerEntities.size() > 0) {
					SleepChecker.updatePlayerStates(event.world);
				}
			}
		}
	}

    @SubscribeEvent
    public void loggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        SleepState.removeSleepingPlayer(event.player);
        //A player left, make sure the world he left from is rechecked next check!
        SleepChecker.checkSleepNextCheck(event.player.worldObj);
    }

    @SubscribeEvent
    public void changedDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        SleepState.removeSleepingPlayer(event.player);
        //A player left, make sure the world he left from is rechecked next check!
        SleepChecker.checkSleepNextCheck(event.player.worldObj);
    }

}
