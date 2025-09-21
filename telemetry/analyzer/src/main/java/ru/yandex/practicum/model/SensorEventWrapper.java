package ru.yandex.practicum.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SensorEventWrapper {
    String id;
    Object data;
}
