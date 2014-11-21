package net.quetzi.morpheus;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
//import net.minecraftforge.common.config.Configuration;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.commands.CommandVersion;
import net.quetzi.morpheus.references.References;
import net.quetzi.morpheus.world.WorldSleepState;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, acceptableRemoteVersions = "*")
public class Morpheus {

    public static int                               perc;
    public static String                            onSleepText, onWakeText, onMorningText;
    public static Logger                            mLog;
    public static HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
    public static SleepChecker                      checker           = new SleepChecker();
    private static boolean                          alertEnabled;
    public static boolean                           includeMiners;
    public static int                               groundLevel;

    public static boolean isAlertEnabled() {

        return alertEnabled;
    }

    public static void setAlertPlayers(boolean state) {

        alertEnabled = state;
    }

    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {

        Morpheus.mLog = event.getModLog();
//        mLog.info("Loading configuration");
        // Read configs
//        Configuration config = new Configuration(event.getSuggestedConfigurationFile());
//        config.load();

        perc = 50; //config.get("settings", "SleeperPerc", 50).getInt();
        alertEnabled = true; //config.get("settings", "AlertEnabled", true).getBoolean();
        onSleepText = "is now sleeping."; //config.get("settings", "OnSleepText", "is now sleeping.").getString();
        onWakeText = "has left their bed."; //config.get("settings", "OnWakeText", "has left their bed.").getString();
        onMorningText = "Good Morning!"; //config.get("settings", "OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!").getString();
        includeMiners = true; //config.get("settings", "IncludeMiners", true).getBoolean();
        groundLevel = 64; //config.getInt("settings", "GroundLevel", 64, 1, 255, "Ground Level (1-255)");
//        config.save();
        MorpheusRegistry register = new MorpheusRegistry();
    }

    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {

        FMLCommonHandler.instance().bus().register(new MorpheusEventHandler());
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandMorpheus());
        event.registerServerCommand(new CommandVersion());
    }

}
