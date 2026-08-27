package elprices.infrastructure;

import elprices.domain.HourlyPrice;

import tools.jackson.databind.ObjectMapper;
import elprices.application.PriceUnavailableException;

//time
import java.time.OffsetDateTime; // to include daylight saving time
import java.time.temporal.ChronoUnit; // standard time unit, so i can remove minuites and seconds

//collection imports
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//JSON → RAW points → hourly price GROUP → AVERAGE → DOMAIN
// to convert raw JSON to clean domain objects. (JSON-API  -> Jackson ObjectMapper -> RawPricePoint[] -> group points by hour ->
// Average all prices within each hour -> hourlyPrice object ->create list <hourly price>)

//{"SEK_per_kWh":0.04527,"EUR_per_kWh":0.00413,"EXR":10.961095,"time_start":"2026-08-10T00:00:00+02:00","time_end":"2026-08-10T00:15:00+02:00"}
// eg, turn 96 points from the API to 24 hours., SEK ->Öre , average ->Domain.. this is to be sent to the analyzer

public class PriceJsonMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<HourlyPrice> toHourlyPrices(String json) {
        var rawPoints = parse(json);

        // TreeMap keeps the hours in chronological order automatically. and truncate to hours for examole 13:00, 13:15, 13:30. 13:45 to 13:00
        Map<OffsetDateTime, List<Double>> pricesByHour = new TreeMap<>();
        for (var point : rawPoints) {
            var hour = OffsetDateTime.parse(point.timeStart()).truncatedTo(ChronoUnit.HOURS);
            pricesByHour.computeIfAbsent(hour, h -> new ArrayList<>())
                    .add(point.sekPerKwh() * 100.0); // SEK/kWh -> öre/kWh
        }

        return pricesByHour.entrySet().stream()
                .map(entry -> new HourlyPrice(
                        entry.getKey(),
                        entry.getValue().stream()
                                .mapToDouble(Double::doubleValue)
                                .average()
                                .orElse(0.0)))
                .toList();
    }

    // first impliments json parsing only, to turn JSON to RowPricePoint[] --- only the conversion

    private RawPricePoint[] parse(String json) {
        try {
            return objectMapper.readValue(json, RawPricePoint[].class);
        } catch (Exception e) {
            throw new PriceUnavailableException("Could not parse price data from the API.", e);
        }
    }
}
