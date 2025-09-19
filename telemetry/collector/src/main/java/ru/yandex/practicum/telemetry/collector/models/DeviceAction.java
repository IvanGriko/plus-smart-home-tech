package ru.yandex.practicum.telemetry.collector.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceAction {
    String sensorId;
    DeviceActionType type;
    int value;
}
