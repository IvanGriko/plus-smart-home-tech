package ru.yandex.practicum.telemetry.collector.services;

import ru.yandex.practicum.telemetry.collector.models.HubEvent;
import ru.yandex.practicum.telemetry.collector.models.SensorEvent;

public interface EventsService {
    void publishToSensors(SensorEvent event);

    void publishToHubs(HubEvent event);
}
