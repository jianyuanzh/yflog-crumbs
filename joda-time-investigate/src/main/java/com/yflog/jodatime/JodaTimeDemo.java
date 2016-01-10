package com.yflog.jodatime;

import org.joda.time.DateTime;
import org.joda.time.format.*;

import java.util.Arrays;
import java.util.TimeZone;

/**
 * Created by vincent on 1/10/16.
 * Three-letter time zone IDs For compatibility with JDK 1.1.x,
 * some other three-letter time zone IDs (such as "PST", "CTT", "AST") are also supported.
 * However, their use is deprecated because the same abbreviation is often used for multiple time zones
 * (for example, "CST" could be U.S. "Central Standard Time" and "China Standard Time"),
 * and the Java platform can then only recognize one of them.
 */
public class JodaTimeDemo {
    private static final DateTimeParser[] parsers = {

            DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss.SSSZZZ").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss.SSS").getParser(),
            DateTimeFormat.forPattern("yyyy-MM-ddHH:mm:ss").getParser(),
            // All ISO8601 formats (use 'T' as separator)
            ISODateTimeFormat.dateTimeParser().withOffsetParsed().getParser(),

            // Fri Jun 07 09:27:37 GMT 2015
            DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy").getParser(),
            // Wed, 09 Feb 1994 22:23:32 GMT
            DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss zzz").getParser(),
            // Thu Feb 3 00:00:00 1994
            DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy").getParser(),
            // Tuesday, 08-Feb-94 14:15:29 GMT
            DateTimeFormat.forPattern("EEEE, dd-MMM-yy HH:mm:ss zzz").getParser(),
            // Tuesday, 08-Feb-1994 14:15:29 GMT
            DateTimeFormat.forPattern("EEEE, dd-MMM-yyyy HH:mm:ss zzz").getParser(),
            //  common logfile format 03/Feb/1994:17:03:55 -0700
            DateTimeFormat.forPattern("dd/MMM/yyy:HH:mm:ss Z").getParser(),
            //  HTTP format (no weekday)  09 Feb 1994 22:23:32 GMT
            DateTimeFormat.forPattern("dd MMM yyyy HH:mm:ss zzz").getParser(),
            // rfc850 format (no weekday) 08-Feb-94 14:15:29 GMT
            DateTimeFormat.forPattern("dd-MMM-yy HH:mm:ss zzz").getParser(),
            // broken rfc850 format (no weekday) 08-Feb-1994 14:15:29 GMT
            DateTimeFormat.forPattern("dd-MMM-yyyy HH:mm:ss zzz").getParser()
    };

    private static void testHttpTimes() {
        String iso = "1994-02-09T22:23:32.000-06:00";

        String http = "Wed, 09 Feb 1994 22:23:32 CST";
        DateTimeFormatter fmtter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();
        System.out.println(fmtter.parseDateTime(http));
    }


    public static void main(String[] args) {
        testHttpTimes();
    }

    private static void printTime() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendDayOfWeekShortText().appendLiteral(' ')

                .appendMonthOfYearShortText().appendLiteral(' ')
                .appendDayOfMonth(2)
                .appendLiteral(' ')
                .appendHourOfDay(2)
                .appendLiteral(':').appendMinuteOfHour(2)
                .appendLiteral(':').appendSecondOfMinute(2).appendLiteral(' ')
                .appendTimeZoneName().appendLiteral(' ')
                .appendYear(4, 4).toFormatter();

        DateTime time = new DateTime();
        System.out.println(time.toString());
        String timeStr = formatter.print(time);
    }
}
