package elprices;

import elprices.infrastructure.ElPriceApiClient;
import  java.time.LocalDate;
import java.net.http.HttpClient;

import static elprices.domain.ElectricityArea.SE1;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    void main() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ElPriceApiClient service = new ElPriceApiClient(httpClient);
       IO.println(service.fetchJson(SE1,  LocalDate.now()));
    }
}
