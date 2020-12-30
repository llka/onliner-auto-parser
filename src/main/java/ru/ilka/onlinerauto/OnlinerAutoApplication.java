package ru.ilka.onlinerauto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.ilka.onlinerauto.response.SearchVehiclesResponse;
import ru.ilka.onlinerauto.response.VehicleAdvertResponse;
import ru.ilka.onlinerauto.response.VehicleResponse;

import java.util.List;
import java.util.stream.Collectors;

import static ru.ilka.onlinerauto.AdvertValidationConstants.IMAGES_MIN_COUNT;

@Slf4j
@SpringBootApplication
public class OnlinerAutoApplication {
    private static final String ONLINER_API = "https://ab.onliner.by/sdapi/ab.api/search/vehicles";

    public static void main(String[] args) {
        SpringApplication.run(OnlinerAutoApplication.class, args);

        RestTemplate restTemplate = new RestTemplate();

        String vwSearchTemplate = "/search/vehicles?order=created_at%3Adesc&" +
                "location%5Bcountry%5D=248&" +
                "seller_type%5B0%5D=individual&" +
                "seller_type%5B1%5D=dealer&" +
                "engine_type%5B0%5D=gasoline&" +
                "body_type%5B0%5D=hatchback&" +
                "body_type%5B1%5D=universal&" +
                "body_type%5B2%5D=suv&" +
                "state%5B0%5D=owned&" +
                "price%5Bto%5D=8000&" +
                "price%5Bcurrency%5D=usd&" +
                "year%5Bfrom%5D=2004&" +
                "engine_capacity%5Bfrom%5D=1.4&" +
                "odometer%5Bto%5D=250000&" +
                "car%5B0%5D%5Bmanufacturer%5D=62&" +
                "extended=true&limit=50";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ONLINER_API)
                .queryParam("car[0][manufacturer]", 62)
                .queryParam("year[from]", 2004);

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<SearchVehiclesResponse> searchResponse = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                SearchVehiclesResponse.class);

        if (!searchResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Not successful response! " + searchResponse);
        }
        List<String> firstValidAdvertsUrls = searchResponse.getBody().getAdverts()
                .stream()
                .filter(advertation -> advertation.getImages().size() > IMAGES_MIN_COUNT)
                .map(VehicleAdvertResponse::getUrl)
                .collect(Collectors.toList());

        for (String url : firstValidAdvertsUrls) {
            ResponseEntity<VehicleResponse> vehicleResponse = restTemplate.getForEntity(url, VehicleResponse.class);
            if (vehicleResponse.getStatusCode().is2xxSuccessful()) {
                VehicleResponse vehicle = vehicleResponse.getBody();
                if (vehicle != null && vehicle.getDescription() != null &&
                        (vehicle.getDescription().contains("drive") || vehicle.getDescription().contains("drive2.ru"))) {
                    log.debug("With drive2 in description:");
                    log.info(vehicle.getHtmlUrl());
                }
            } else {
                log.error("Not successful response for vehicle: " + url);
            }
        }
    }
}
