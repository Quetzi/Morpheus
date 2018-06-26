package net.quetzi.morpheus.helpers;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.quetzi.morpheus.Morpheus;

import java.util.Calendar;

/**
 * Created by Quetzi on 15/10/14.
 */
public class DateHandler
{
    public enum Event
    {
        XMAS(25, 12, new Style().setColor(TextFormatting.RED), References.XMASTEXT),
        NEW_YEAR(1, 1, new Style().setColor(TextFormatting.GOLD), References.NEWYEARTEXT),
        STPATRICKS(17, 3, new Style().setColor(TextFormatting.DARK_GREEN), References.STPATRICKSTEXT),
        HALLOWEEN(31, 10, new Style().setColor(TextFormatting.DARK_PURPLE), References.HALLOWEENTEXT),
        NONE(0, 0, new Style().setColor(TextFormatting.GOLD), Morpheus.onMorningText);

        private final int    month;
        private final int    day;
        private final Style  style;
        private final String text;

        Event(int day, int month, Style style, String text)
        {
            this.month = month;
            this.day = day;
            this.style = style;
            this.text = text;
        }

        public boolean isEvent(Calendar calendar)
        {
            return calendar.get(2) + 1 == month && calendar.get(5) == day;
        }
    }

    private static Event getEvent(Calendar calendar)
    {
        for (Event event : Event.values())
        {
            if (event.isEvent(calendar))
            {
                return event;
            }
        }
        return Event.NONE;
    }

    public static ITextComponent getMorningTextComponent()
    {
        DateHandler.Event event = getEvent(Calendar.getInstance());
        return new TextComponentString(event.text).setStyle(event.style);
    }
}
