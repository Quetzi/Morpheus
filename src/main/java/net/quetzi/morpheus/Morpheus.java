package net.quetzi.morpheus;

import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;
import net.quetzi.morpheus.commands.CommandMorpheus;
import net.quetzi.morpheus.references.References;
import net.quetzi.morpheus.world.WorldSleepState;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION)
public class Morpheus {
	public static int perc;
	public static boolean alertEnabled;
	public static String onSleepText, onWakeText, onMorningText;
//	public static Logger mLog = FMLLog.getLogger();
	public static HashMap<Integer, WorldSleepState> playerSleepStatus = new HashMap<Integer, WorldSleepState>();
	public static SleepChecker checker = new SleepChecker();

	@EventHandler
	@SideOnly(Side.SERVER)
	public void Init(FMLInitializationEvent event) {
	}

	public static boolean isAlertEnabled() {
		return alertEnabled;
	}

	public static void setAlertPlayers(boolean state) {
		Morpheus.alertEnabled = state;
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PreInit(FMLPreInitializationEvent event) {
//		mLog.info("Loading configuration");
		// Read configs
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();

		perc = config.get("settings", "SleeperPerc", 50).getInt();
		alertEnabled = config.get("settings", "AlertEnabled", true).getBoolean(true);
		onSleepText = config.get("settings", "OnSleepText", "is now sleeping.").getString();
		onWakeText = config.get("settings", "OnWakeText", "has left their bed.").getString();
		onMorningText = config.get("settings", "OnMorningText",	"Wakey, wakey, rise and shine...Good Morning everyone!").getString();

		config.save();
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void PostInit(FMLPostInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new MorpheusEventHandler());
	}

	@EventHandler
	@SideOnly(Side.SERVER)
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMorpheus());
	}

}