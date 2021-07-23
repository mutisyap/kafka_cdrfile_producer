package io.pmutisya.kafkaproducer;

public class EventsLimitingUtil {
    public static int getSleepTime(int targetTPS, int timeTakenWithRequestMs) {
        int targetTimePerRequestMs = 1000 / targetTPS;

        if (timeTakenWithRequestMs < targetTimePerRequestMs) {
            return targetTimePerRequestMs - timeTakenWithRequestMs;
        }
        return 0;
    }
}
