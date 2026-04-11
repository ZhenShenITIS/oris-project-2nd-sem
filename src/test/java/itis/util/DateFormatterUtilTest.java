package itis.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DateFormatterUtilTest {

    @Test
    void format_nullInstant_returnsNull() {
        assertNull(DateFormatterUtil.format(null));
    }

    @Test
    void format_validInstant_returnsFormattedStringInMoscowTime() {
        // 2024-01-15T12:30:00Z -> Moscow time (UTC+3) -> 15.01.2024 15:30
        Instant instant = Instant.parse("2024-01-15T12:30:00Z");
        String result = DateFormatterUtil.format(instant);
        assertEquals("15.01.2024 15:30", result);
    }

    @Test
    void format_winterInstant_appliesCorrectOffset() {
        // 2024-03-10T00:00:00Z -> Moscow +3 -> 10.03.2024 03:00
        Instant instant = Instant.parse("2024-03-10T00:00:00Z");
        String result = DateFormatterUtil.format(instant);
        assertEquals("10.03.2024 03:00", result);
    }
}