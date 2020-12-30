package ru.ilka.onlinerauto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleResponse {
    long id;
    String title;
    String description;
    String url;
    @JsonProperty("html_url")
    String htmlUrl;
}
