package elprices;

//import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ElPriceService{

    //call API -> receive JSON -> parse JSON -> Add to List<Price>

    //the driver object
    private final HttpClient httpClient= HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

    // the what: get, url,headers, body
    private static final String URL= "https://www.elprisetjustnu.se/api/v1/prices/2026/08-10_SE3.json";
    private final  HttpRequest httpRequest= HttpRequest.newBuilder().GET().uri(URI.create(URL)).build();


    public void fetchPrices() {


        //send the request( the httpClient) var=HttpResponse<String>

        try {
            HttpResponse<String> response =
                    httpClient.send(
                            httpRequest,
                            HttpResponse.BodyHandlers.ofString()
                    );

            IO.println("HTTP GET: " + response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
//    ObjectMapper mapper = new ObjectMapper(); // changing formats : maper.readvalue(response.body(), object) JSON ->Java ¤¤¤¤¤¤>> //mapper.writeValueString(object) Java ->JSON
}
