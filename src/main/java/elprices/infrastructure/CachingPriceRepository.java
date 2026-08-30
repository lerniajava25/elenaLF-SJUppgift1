package elprices.infrastructure;

import elprices.application.PriceRepository;
import elprices.domain.ElectricityArea;
import elprices.domain.HourlyPrice;

import java.time.LocalDate;
import java.util.List;

 // Gets electricity prices. checks if the data is already saved in a local file: 1-yes: the file is used. 2- no: the data is downloaded from the API and saved so it can be reused next time.

public class CachingPriceRepository implements PriceRepository {

    private final ElPriceApiClient apiClient;
    private final FileCache cache;
    private final PriceJsonMapper mapper;

    public CachingPriceRepository(
            ElPriceApiClient apiClient,
            FileCache cache,
            PriceJsonMapper mapper) {

        this.apiClient = apiClient;
        this.cache = cache;
        this.mapper = mapper;
    }
    @Override
    public List<HourlyPrice> pricesFor(ElectricityArea area, LocalDate date) {
        var cached = cache.read(area, date);
        if (cached.isPresent()) {
            return mapper.toHourlyPrices(cached.get());
        }

        var json = apiClient.fetchJson(area, date);
        cache.write(area, date, json);
        return mapper.toHourlyPrices(json);
    }
}

