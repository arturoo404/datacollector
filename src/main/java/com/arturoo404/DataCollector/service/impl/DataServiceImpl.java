package com.arturoo404.DataCollector.service.impl;

import com.arturoo404.DataCollector.service.DataService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class DataServiceImpl implements DataService {

    //https://stackoverflow.com/questions/25229124/unsupportedtemporaltypeexception-when-formatting-instant-to-string
    @Override
    public String currentData() {
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    @Override
    public String timeBetweenTwoData(Instant start, Instant end) {
        Duration duration = Duration.between(start, end);
        long minutes = duration.toMinutes();

        return String.format("%d min, %d sec",
                minutes,
                duration.minusMinutes(minutes).getSeconds());
    }

    @Override
    public String timePerOneScan(Instant start, Instant end, int offerQuantity) {
        Duration duration = Duration.between(start, end);
        Duration perScan =  Duration.ofMillis(duration.toMillis() / offerQuantity);
        return String.format("%d:%d sec",
                perScan.toSecondsPart(),
                perScan.toMillisPart());
    }
}
