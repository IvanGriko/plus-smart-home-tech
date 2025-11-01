package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "actions")
@SecondaryTable(name = "scenario_actions", pkJoinColumns = @PrimaryKeyJoinColumn(name = "action_id"))
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    ActionType type;
    int value;

    @ManyToOne
    @JoinColumn(name = "scenario_id", table = "scenario_actions")
    Scenario scenario;

    @ManyToOne()
    @JoinColumn(name = "sensor_id", table = "scenario_actions")
    Sensor sensor;
}
