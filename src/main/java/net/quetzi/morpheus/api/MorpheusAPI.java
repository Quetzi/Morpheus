package net.quetzi.morpheus.api;

import cpw.mods.fml.common.Loader;

public class MorpheusAPI {
    
    private static IMorpheusAPI api;
    
    public static IMorpheusAPI getRegistry() {
    
        return api;
    }
    
    public static void initializeApi(IMorpheusAPI api) {
    
        if (Loader.instance().activeModContainer().getModId().equals("Morpheus")) MorpheusAPI.api = api;
    }
    
}
