package ru.yandex.practicum.telemetry.collector.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
    private boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
