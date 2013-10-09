package net.quetzi.morpheus;

import java.util.HashMap;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.quetzi.morpheus.commands.AlertToggleCommand;
import net.quetzi.morpheus.references.References;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION,dependencies="after:Mystcraft")
@NetworkMod(clientSideRequired = false, serverSideRequired = true)
public class Morpheus {
	public static int perc;
	public static boolean alertPlayers;
	public static String onSleepText, onWakeText, onMorningText;
	public static Logger mLog = Logger.getLogger("Morpheus");
	public static HashMap<Integer,WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
	
	@EventHandler
	@SideOnly(Side.SERVER)
	public void Init(FMLInitializationEvent event) {
	}

	public static boolean isAlertPlayers() {
		return alertPlayers;
	}

	public static void setAlertPlayers(boolean alertPlayers) {
		Morpheus.alertPlayers = alertPlayers;
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PreInit(FMLPreInitializationEvent event) {
		mLog.setParent(FMLLog.getLogger());
		mLog.info("Loading configuration");
		// Read configs
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		perc = config.get("settings", "SleeperPerc", 50).getInt();
		alertPlayers = config.get("settings", "AlertPlayers", true).getBoolean(true);
		onSleepText = config.get("settings", "OnSleepText", "is now sleeping.").getString();
		onWakeText = config.get("settings", "OnWakeText", "has left their bed.").getString();
		onMorningText = config.get("settings", "OnMorningText", "Wakey, wakey, rise and shine...Good Morning everyone!")
				.getString();
		config.save();
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PostInit(FMLPostInitializationEvent event) {
		GameRegistry.registerPlayerTracker(new MorpheusTracker());
		TickRegistry.registerTickHandler(new SleepChecker(), Side.SERVER);
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new AlertToggleCommand());
	}
}