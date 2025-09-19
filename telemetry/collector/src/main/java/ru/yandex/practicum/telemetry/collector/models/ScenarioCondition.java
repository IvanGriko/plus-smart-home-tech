package ru.yandex.practicum.telemetry.collector.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScenarioCondition {
    String sensorId;
    ScenarioType type;
    ScenarioOperation operation;
    int value;
}

