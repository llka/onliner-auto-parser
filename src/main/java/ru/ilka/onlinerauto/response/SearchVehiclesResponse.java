package ru.ilka.onlinerauto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchVehiclesResponse {
    List<VehicleAdvertResponse> adverts;

}
