package elprices.infrastructure;

import elprices.application.PriceUnavailableException;
import elprices.domain.ElectricityArea;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

public class ElPriceApiClient {

    //call API -> receive JSON -> parse JSON -> Add to List<Price>

    //the driver object
//    private final HttpClient httpClient= HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    // the what: get, url,headers, body
    private static final String URL_TEMPLATE = "https://www.elprisetjustnu.se/api/v1/prices/%d/%02d-%02d_%s.json";

    private final HttpClient httpClient;

    public ElPriceApiClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    //send the request( the httpClient) var=HttpResponse<String>

    public String fetchJson(ElectricityArea area, LocalDate date) {
        var url = URL_TEMPLATE.formatted(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth(), area.name());

        var request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return switch (response.statusCode()) {
                case 200 -> response.body();
                case 404 -> throw new PriceUnavailableException(
                        "Inga priser publicerade för %s den %s ännu.".formatted(area, date));
                default -> throw new PriceUnavailableException(
                        "API:et svarade med HTTP %d.".formatted(response.statusCode()));
            };
        } catch (IOException e) {
            throw new PriceUnavailableException("Nätverksfel vid anrop till pris-API:et.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore the interrupt flag
            throw new PriceUnavailableException("Anropet avbröts.", e);
        }
    }
}

//    ObjectMapper mapper = new ObjectMapper(); // changing formats : maper.readvalue(response.body(), object) JSON ->Java ¤¤¤¤¤¤>> //mapper.writeValueString(object) Java ->JSON

