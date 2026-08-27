package elprices.domain;

import java.time.OffsetDateTime;
import java.util.List;

// start and end of the cheapest 4 hours  window with the cheapest time to charge object

public record ChargingWindow(
        OffsetDateTime start,
        OffsetDateTime end,
        double averageOre,
        List<HourlyPrice> hours
) {

    //  add a constructor to have an immutable copy of this record i am going to copy it
    public ChargingWindow {
        hours = List.copyOf(hours);
    }
}

