package elprices;

import elprices.application.PriceRepository;
import elprices.application.PriceService;
import elprices.cli.ConsoleApp;
import elprices.cli.ConsolePresenter;
import elprices.domain.PriceAnalyzer;
import elprices.infrastructure.ElPriceApiClient;
import elprices.infrastructure.PriceJsonMapper;
import elprices.infrastructure.CachingPriceRepository;
import elprices.infrastructure.FileCache;

import java.net.http.HttpClient;
import java.nio.file.Path;
import java.time.Duration;

public class Main {
    void main() {

        //create the http
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // build infrastructure objects (adapters)
        var apiClient = new ElPriceApiClient(httpClient);
        var cache = new FileCache(Path.of("cache"));
        var mapper = new PriceJsonMapper();
        PriceRepository repository = new CachingPriceRepository(apiClient, cache, mapper);

        //  build the application + domain
        var priceService = new PriceService(repository);
        var analyzer = new PriceAnalyzer();

        // presentation
        var presenter = new ConsolePresenter();
// create and run the CLI
        new ConsoleApp(priceService, analyzer, presenter).run();
    }
}