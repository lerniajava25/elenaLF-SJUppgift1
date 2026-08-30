package elprices.services;

import elprices.application.PriceRepository;
import elprices.domain.ElectricityArea;
import elprices.domain.HourlyPrice;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

//  the middleman: Application service Keeps the CLI free of date handling and repository, that the menu just asks for "today's prices for this area".
// User / CLI -> PriceService -> PriceRepository -> API

public class PriceService {

    private static final ZoneId SWEDEN = ZoneId.of("Europe/Stockholm");

    private final PriceRepository repository;

    public PriceService(PriceRepository repository) {
        this.repository = repository;
    }

    // Today's hourly prices for the given area (today = current date in Sweden).
    public List<HourlyPrice> today(ElectricityArea area) {
        return repository.pricesFor(area, LocalDate.now(SWEDEN));
    }

    // Hourly prices for an exact date for testing or extending the menu.
    public List<HourlyPrice> pricesFor(ElectricityArea area, LocalDate date) {
        return repository.pricesFor(area, date);
    }
}
