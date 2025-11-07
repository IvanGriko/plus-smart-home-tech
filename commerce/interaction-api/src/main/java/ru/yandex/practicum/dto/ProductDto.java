package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    UUID productId;

    @NotNull
    @Size(min = 1)
    String productName;

    @NotNull
    @Size(min = 1)
    String description;

    String imageSrc;

    QuantityState quantityState;

    ProductState productState;

    Double rating;

    ProductCategory productCategory;

    @NotNull
    BigDecimal price;
}
