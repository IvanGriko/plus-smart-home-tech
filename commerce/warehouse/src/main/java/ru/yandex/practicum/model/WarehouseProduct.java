package ru.yandex.practicum.model;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "warehouse_product")
public class WarehouseProduct {

    @Id
    UUID productId;

    double weight;

    double width;

    double height;

    double depth;

    boolean fragile;

    int quantity;
}
