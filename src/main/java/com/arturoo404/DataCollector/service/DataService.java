package com.arturoo404.DataCollector.service;

import java.time.Instant;

public interface DataService {
    String currentData();
    String timeBetweenTwoData(Instant start, Instant end);
    String timePerOneScan(Instant start, Instant end, int offerQuantity);
}
