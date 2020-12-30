package ru.ilka.onlinerauto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VehicleAdvertResponse {
    long id;
    String title;
    List<Object> images;
    String created_at;
    String updated_at;
    String last_up_at;
    String url;
}
