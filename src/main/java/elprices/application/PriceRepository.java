package elprices.application;

import elprices.domain.ElectricityArea;
import elprices.domain.HourlyPrice;

import java.time.LocalDate;
import java.util.List;

//using a port: the application layer depends only on this interface, never on HttpClient, Jackson  so doesn't get swapped or faked without touching the analysis or the CLI. something capable of giving me prices.not directly from ApiClient

public interface PriceRepository {

    List<HourlyPrice> pricesFor(ElectricityArea area, LocalDate date);
}
