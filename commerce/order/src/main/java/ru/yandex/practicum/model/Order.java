package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import ru.yandex.practicum.dto.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Table(name = "orders")
public class Order {

    @Id
    @UuidGenerator
    UUID orderId;

    String username;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Integer> products;

    UUID shoppingCartId;

    UUID paymentId;

    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    OrderState state;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    BigDecimal totalPrice;

    BigDecimal deliveryPrice;

    BigDecimal productPrice;
}