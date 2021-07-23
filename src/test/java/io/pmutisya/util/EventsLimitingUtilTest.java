package io.pmutisya.util;

import io.pmutisya.kafkaproducer.EventsLimitingUtil;
import junit.framework.TestCase;

public class EventsLimitingUtilTest extends TestCase {

    public void testSleepTime() {

        assertEquals(EventsLimitingUtil.getSleepTime(10, 1), 99);
        assertEquals(EventsLimitingUtil.getSleepTime(10, 100), 0);
        assertEquals(EventsLimitingUtil.getSleepTime(100, 100), 0);
        assertEquals(EventsLimitingUtil.getSleepTime(10, 0), 100);
    }

}
