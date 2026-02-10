package com.aiform

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class TimeZoneManagerTest {
    @Test
    fun computeDayKey_supportsDifferentTimeZonesNearMidnight() {
        val manager = TimeZoneManagerForTest()
        val utc = Instant.parse("2026-02-10T23:59:00Z")

        assertEquals("2026-02-10", manager.computeDayKey(utc, "America/Mexico_City").toString())
        assertEquals("2026-02-11", manager.computeDayKey(utc, "Europe/Madrid").toString())
        assertEquals("2026-02-11", manager.computeDayKey(utc, "Asia/Tokyo").toString())
    }

    @Test
    fun computeDayKey_handlesDayBoundary() {
        val manager = TimeZoneManagerForTest()
        val before = Instant.parse("2026-02-10T23:59:00Z")
        val after = Instant.parse("2026-02-11T00:01:00Z")

        assertEquals("2026-02-10", manager.computeDayKey(before, "UTC").toString())
        assertEquals("2026-02-11", manager.computeDayKey(after, "UTC").toString())
    }
}

private class TimeZoneManagerForTest {
    fun computeDayKey(nowUtc: Instant, tzId: String) = nowUtc.atZone(java.time.ZoneId.of(tzId)).toLocalDate()
}
