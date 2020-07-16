package net.quetzi.morpheus;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.quetzi.morpheus.api.IMorpheusAPI;
import net.quetzi.morpheus.api.INewDayHandler;
import net.quetzi.morpheus.world.DefaultOverworldHandler;

import java.util.HashMap;

public class MorpheusRegistry implements IMorpheusAPI {
    public static HashMap<RegistryKey<World>, INewDayHandler> registry;

    public MorpheusRegistry() {
        registry = new HashMap<>();
        registerHandler(new DefaultOverworldHandler(), ServerWorld.field_234918_g_);
    }

    @Override
    public void registerHandler(INewDayHandler newDayHandler, RegistryKey<World> dimension) {
        if (registry.containsKey(dimension))
            Morpheus.logger.warn("New day handler for dimension " + dimension + " has been replaced");
        registry.put(dimension, newDayHandler);
    }

    @Override
    public void unregisterHandler(RegistryKey<World> dimension) {
        Morpheus.logger.warn("New day handler for dimension " + dimension + " has been removed");
        registry.remove(dimension);
    }

    public boolean isDimRegistered(RegistryKey<World> dim) {
        return registry.containsKey(dim);
    }
}
