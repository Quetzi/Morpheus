package net.quetzi.morpheus.helpers;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config
{
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;
    static {
        final Pair<ServerConfig,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static int perc;
    public static boolean alertEnabled;
    public static String onSleepText;
    public static String onWakeText;
    public static String onMorningText;
    public static boolean includeMiners;
    public static int groundLevel;
    public static boolean setSpawnDaytime;

    public static void load()
    {
        perc = SERVER.perc.get();
        alertEnabled = SERVER.alertEnabled.get();
        onSleepText = SERVER.onSleepText.get();
        onWakeText = SERVER.onWakeText.get();
        onMorningText = SERVER.onMorningText.get();
        includeMiners = SERVER.includeMiners.get();
        groundLevel = SERVER.groundLevel.get();
        setSpawnDaytime = SERVER.setSpawnDaytime.get();
    }

    public static class ServerConfig
    {
        public ForgeConfigSpec.IntValue            perc;
        public ForgeConfigSpec.BooleanValue        alertEnabled;
        public ForgeConfigSpec.ConfigValue<String> onSleepText;
        public ForgeConfigSpec.ConfigValue<String> onWakeText;
        public ForgeConfigSpec.ConfigValue<String> onMorningText;
        public ForgeConfigSpec.BooleanValue        includeMiners;
        public ForgeConfigSpec.IntValue            groundLevel;
        public ForgeConfigSpec.BooleanValue        setSpawnDaytime;

        ServerConfig(ForgeConfigSpec.Builder builder)
        {
            builder.push("settings");
            perc = builder
                    .comment("Percentage of players required to trigger a successful sleep.")
                    .defineInRange("SleeperPerc", 50, 0, 100);
            alertEnabled = builder
                    .comment("Enable alerts")
                    .define("AlertEnabled", true);
            onSleepText = builder
                    .comment("Text used to alert players that someone is trying to sleep")
                    .define("OnSleepText", "is now sleeping.");
            onWakeText = builder
                    .comment("Text used to alert players that someone stopped trying to sleep")
                    .define("OnWakeText", "has left their bed.");
            onMorningText = builder
                    .comment("Text to alert the server that sleeping was successful")
                    .define("OnMorningText", "Wakey, wakey, rise and shine... Good Morning everyone!");
            includeMiners = builder
                    .comment("Include miners in player calculations")
                    .define("IncludeMiners", true);
            groundLevel = builder
                    .comment("Players below this Y level will be counted as miners")
                    .defineInRange("GroundLevel", 64, 1, 255);
            setSpawnDaytime = builder
                    .comment("Allow players to set their spawn point using a bed during the day")
                    .define("AllowSetSpawnDaytime", true);
            builder.pop();
        }
    }
}
