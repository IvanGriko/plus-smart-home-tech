package ru.practicum.event.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.event.model.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HubEventMapper {

    public static SpecificRecordBase toHubEventAvro(HubEvent hubEvent) {
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(toHubEventPayloadAvro(hubEvent))
                .build();
    }

    public static SpecificRecordBase toHubEventPayloadAvro(HubEvent hubEvent) {
        switch (hubEvent.getType()) {
            case DEVICE_ADDED -> {
                DeviceAddedEvent event = (DeviceAddedEvent) hubEvent;
                return DeviceAddedEventAvro.newBuilder()
                        .setId(event.getId())
                        .setType(toDeviceTypeAvro(event.getDeviceType()))
                        .build();
            }

            case DEVICE_REMOVED -> {
                DeviceRemovedEvent event = (DeviceRemovedEvent) hubEvent;
                return DeviceRemovedEventAvro.newBuilder()
                        .setId(event.getId())
                        .build();
            }

            case SCENARIO_ADDED -> {
                ScenarioAddedEvent event = (ScenarioAddedEvent) hubEvent;
                String name = Optional.ofNullable(event.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Name cannot be null"));

                List<DeviceAction> actions = Optional.ofNullable(event.getActions())
                        .orElse(Collections.emptyList());

                List<ScenarioCondition> conditions = Optional.ofNullable(event.getConditions())
                        .orElse(Collections.emptyList());
                return ScenarioAddedEventAvro.newBuilder()
                        .setName(name)
                        .setActions(actions.stream()
                                .map(HubEventMapper::toDeviceActionAvro)
                                .toList())
                        .setConditions(conditions.stream()
                                .map(condition -> {
                                    ScenarioConditionAvro avroCondition = HubEventMapper.toScenarioConditionAvro(condition);
                                    if (avroCondition == null) {
                                        throw new IllegalArgumentException("toScenarioConditionAvro returned null");
                                    }
                                    return avroCondition;
                                })
                                .toList())
                        .build();
            }

            case SCENARIO_REMOVED -> {
                ScenarioRemovedEvent event = (ScenarioRemovedEvent) hubEvent;
                return ScenarioRemovedEventAvro.newBuilder()
                        .setName(event.getName())
                        .build();
            }

            default -> throw new IllegalStateException("Invalid payload: " + hubEvent.getType());
        }
    }

    public static DeviceTypeAvro toDeviceTypeAvro(DeviceType deviceType) {
        return DeviceTypeAvro.valueOf(deviceType.name());
    }

    public static DeviceActionAvro toDeviceActionAvro(DeviceAction deviceAction) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(deviceAction.getSensorId())
                .setType(toActionTypeAvro(deviceAction.getType()))
                .setValue(deviceAction.getValue())
                .build();
    }

    public static ActionTypeAvro toActionTypeAvro(ActionType actionType) {
        return ActionTypeAvro.valueOf(actionType.name());
    }

    public static ConditionTypeAvro toConditionTypeAvro(ConditionType conditionType) {
        return ConditionTypeAvro.valueOf(conditionType.name());
    }

    public static ConditionOperationAvro toConditionOperationAvro(ConditionOperation conditionOperation) {
        return ConditionOperationAvro.valueOf(conditionOperation.name());
    }

    public static ScenarioConditionAvro toScenarioConditionAvro(ScenarioCondition scenarioCondition) {
        if (scenarioCondition == null) {
            throw new IllegalArgumentException("scenarioCondition cannot be null");
        }

        ConditionType conditionTypeAvro = Optional.ofNullable(scenarioCondition.getConditionType())
                .map(HubEventMapper::toConditionTypeAvro)
                .orElse(null);

        ConditionOperation conditionOperationAvro = Optional.ofNullable(scenarioCondition.getConditionOperation())
                .map(HubEventMapper::toConditionOperationAvro)
                .orElse(null);

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenarioCondition.getSensorId())
                .setType(conditionTypeAvro)
                .setOperation(conditionOperationAvro)
                .setValue(scenarioCondition.getValue())
                .build();
    }
}