package elprices.infrastructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// a variable converter JSON <-> Java
//** note to me: research hashcode **

@JsonIgnoreProperties(ignoreUnknown = true)  //ignore all properties from JSON that doesn't exist in this record

public record RawPricePoint(
        @JsonProperty("SEK_per_kWh") double sekPerKwh,
        @JsonProperty("EUR_per_kWh") double eurPerkwh,
        @JsonProperty("EXR") double exchangeRate,
        @JsonProperty("time_start") String timeStart,
        @JsonProperty("time_end") String timeEnd
) {
}
//[{"SEK_per_kWh":0.04527,"EUR_per_kWh":0.00413,"EXR":10.961095,"time_start":"2026-08-10T00:00:00+02:00","time_end":"2026-08-10T00:15:00+02:00"},{"SEK_per_kWh":0.04516,"EUR_per_kWh":0.00412,"EXR":10.961095,"time_start":"2026-08-10T00:15:00+02:00","time_end":"2026-08-10T00:30:00+02:00"},{"SEK_per_kWh":0.04286,"EUR_per_kWh":0.00391,"EXR":10.961095,"time_start":"2026-08-10T00:30:00+02:00","time_end":"2026-08-10T00:45:00+02:00"},{"SEK_per_kWh"