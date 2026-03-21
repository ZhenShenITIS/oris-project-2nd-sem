package itis.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm")
            .withZone(ZoneId.of("Europe/Moscow"));

    private DateFormatterUtil() {
    }

    public static String format(Instant instant) {
        if (instant == null) {
            return null;
        }
        return FORMATTER.format(instant);
    }
}
