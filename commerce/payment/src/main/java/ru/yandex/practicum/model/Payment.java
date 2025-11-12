package ru.yandex.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.Id;
import ru.yandex.practicum.dto.PaymentState;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payment")
public class Payment {

    @Id
    @UuidGenerator
    UUID paymentId;

    UUID orderId;

    BigDecimal totalPayment;

    BigDecimal deliveryTotal;

    BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    PaymentState paymentState;

}