package ru.yandex.practicum.telemetry.collector.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.models.HubEvent;
import ru.yandex.practicum.telemetry.collector.models.SensorEvent;
import ru.yandex.practicum.telemetry.collector.services.EventsService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {
    private final EventsService eventsService;

    @PostMapping("/sensors")
    public void sensors(@Valid @RequestBody SensorEvent event) {
        log.info("Request with sensor event: {}", event);
        eventsService.publishToSensors(event);
    }

    @PostMapping("/hubs")
    public void hubs(@Valid @RequestBody HubEvent event) {
        log.info("Request with hub event: {}", event);
        eventsService.publishToHubs(event);
    }
}
