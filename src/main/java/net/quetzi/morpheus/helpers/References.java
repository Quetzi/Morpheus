package net.quetzi.morpheus.helpers;

public class References
{
    public static final  String MODID     = "Morpheus";
    public static final  String NAME      = "Morpheus";
    private static final String MAJOR     = "@MAJOR@";
    private static final String MINOR     = "@MINOR@";
    private static final String BUILD     = "@BUILD_NUMBER@";
    private static final String MCVERSION = "@MC_VERSION@";
    public static final  String VERSION   = MCVERSION + "-" + MAJOR + "." + MINOR + "." + BUILD;

    public static final String ALERTS_OFF    = "Text alerts turned off";
    public static final String ALERTS_ON     = "Text alerts turned on";
    public static final String PERCENT_USAGE = "Usage: /morpheus percent <percentage>";
    public static final String DISABLE_USAGE = "Usage: /morpheus disable <dimension number>";
    public static final String USAGE         = "/morpheus <alert : version> | /morpheus percent <percentage> | /morpheus disable <dimension number>";

    public static final String NEWYEARTEXT    = "Good morning everyone! HAPPY NEW YEAR!";
    public static final String XMASTEXT       = "Good morning everyone! HAPPY CHRISTMAS!";
    public static final String STPATRICKSTEXT = "Good morning everyone! HAPPY ST PATRICKS DAY!";
    public static final String HALLOWEENTEXT  = "Good morning everyone! HAPPY SAMHAIN!";

    public static final String SPAWN_SET = "New spawnpoint has been set!";
}
