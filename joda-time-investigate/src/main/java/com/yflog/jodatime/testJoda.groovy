package com.yflog.jodatime

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormatterBuilder

/**
 * Created by vincent on 1/11/16.
 */
class testJoda {

    static void main(String[] args) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendDayOfWeekShortText().appendLiteral(' ')

                .appendMonthOfYearShortText().appendLiteral(' ')
                .appendDayOfMonth(2)
                .appendLiteral(' ')
                .appendHourOfDay(2)
                .appendLiteral(':').appendMinuteOfHour(2)
                .appendLiteral(':').appendSecondOfMinute(2).appendLiteral(' ')
                .appendTimeZoneShortName().appendLiteral(' ')
                .appendYear(4, 4).toFormatter();

        DateTime time = new DateTime();
        String timeStr = formatter.print(time);

        println timeStr;

        formatter = DateTimeFormat.forPattern("dd-MMM-yy HH:mm:ss zzz");
        println formatter.print(time);
    }

}
