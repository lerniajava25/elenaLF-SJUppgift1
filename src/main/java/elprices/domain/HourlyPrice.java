package elprices.domain;

import java.time.OffsetDateTime;

//the average el price for a time, considering the time zone

public record HourlyPrice(OffsetDateTime start, double orePerKwh) {

    public int hour() {
        return start.getHour();
    }

    // for formating
    public String label() {
        return "%02d–%02d".formatted(hour(), (hour() + 1) % 24); // eg, "13 - 14" o'cklock
    }

}
