package net.quetzi.morpheus;

import net.minecraft.util.EnumChatFormatting;
import net.quetzi.morpheus.references.References;

import java.util.Calendar;

/**
 * Created by Quetzi on 15/10/14.
 */
public class DateHandler {

    public static enum Event {
        XMAS(25, 12, EnumChatFormatting.RED + References.XMASTEXT),
        NEW_YEAR(1, 1, EnumChatFormatting.GOLD + References.NEWYEARTEXT),
        STPATRICKS(17, 3, EnumChatFormatting.DARK_GREEN + References.STPATRICKSTEXT),
        HALLOWEEN(31, 10, EnumChatFormatting.DARK_PURPLE + References.HALLOWEENTEXT),
        NONE(0,0, EnumChatFormatting.GOLD + Morpheus.onMorningText);

        private final int month;
        private final int day;
        private final String text;

        Event(int day, int month, String text) {
            this.month = month;
            this.day = day;
            this.text = text;
        }

        public boolean isEvent(Calendar calendar) {
            return calendar.get(2) + 1 == month && calendar.get(5) == day;
        }
    }

    private static Event getEvent(Calendar calendar) {

        for (Event event : Event.values()) {
            if (event.isEvent(calendar)) {
                return event;
            }
        }
        return Event.NONE;
    }

    public static String getMorningText() {

        return getEvent(Calendar.getInstance()).text;
    }
}
