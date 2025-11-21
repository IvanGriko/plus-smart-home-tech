package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.DeliveryState;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ShippedToDeliveryRequest;
import ru.yandex.practicum.exceptions.NoDeliveryFoundException;
import ru.yandex.practicum.feign.OrderOperations;
import ru.yandex.practicum.feign.WarehouseOperations;
import ru.yandex.practicum.mapper.DeliveryMapper;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeliveryServiceImpl implements DeliveryService {
    DeliveryRepository deliveryRepository;
    DeliveryMapper deliveryMapper;
    OrderOperations orderClient;
    WarehouseOperations warehouseClient;

    static BigDecimal BASE_RATE = new BigDecimal("5.0");
    static BigDecimal WAREHOUSE_1_ADDRESS_MULTIPLIER = new BigDecimal("1.0");
    static BigDecimal WAREHOUSE_2_ADDRESS_MULTIPLIER = new BigDecimal("2.0");
    static BigDecimal FRAGILE_MULTIPLIER = new BigDecimal("0.2");
    static BigDecimal WEIGHT_MULTIPLIER = new BigDecimal("0.3");
    static BigDecimal VOLUME_MULTIPLIER = new BigDecimal("0.2");
    static BigDecimal STREET_MULTIPLIER = new BigDecimal("0.2");

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.mapToDelivery(deliveryDto);
        delivery = deliveryRepository.save(delivery);
        return deliveryMapper.mapToDeliveryDto(delivery);
    }

    @Override
    @Transactional
    public void completeDelivery(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        delivery = deliveryRepository.save(delivery);
        orderClient.delivery(delivery.getOrderId());
        deliveryMapper.mapToDeliveryDto(delivery);
    }

    @Override
    @Transactional
    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        delivery.setDeliveryState(DeliveryState.FAILED);
        delivery = deliveryRepository.save(delivery);
        orderClient.deliveryFailed(delivery.getOrderId());
        deliveryMapper.mapToDeliveryDto(delivery);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateDeliveryCost(OrderDto order) {
        Delivery delivery = getDelivery(order.getDeliveryId());
        Address warehouseAddress = delivery.getFromAddress();
        Address destinationAddress = delivery.getToAddress();
        BigDecimal totalCost = BASE_RATE;

        if ("ADDRESS_1".equals(warehouseAddress.getCity())) {
            totalCost = totalCost.multiply(WAREHOUSE_1_ADDRESS_MULTIPLIER.add(BigDecimal.ONE));
        } else {
            totalCost = totalCost.multiply(WAREHOUSE_2_ADDRESS_MULTIPLIER.add(BigDecimal.ONE));
        }

        if (Boolean.TRUE.equals(order.getFragile())) {
            totalCost = totalCost.add(totalCost.multiply(FRAGILE_MULTIPLIER));
        }

        totalCost = totalCost.add(new BigDecimal(order.getDeliveryWeight()).multiply(WEIGHT_MULTIPLIER));
        totalCost = totalCost.add(new BigDecimal(order.getDeliveryVolume()).multiply(VOLUME_MULTIPLIER));

        if (!warehouseAddress.getStreet().equals(destinationAddress.getStreet())) {
            totalCost = totalCost.add(totalCost.multiply(STREET_MULTIPLIER));
        }

        return totalCost.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public void setDeliveryPicked(UUID deliveryId) {
        Delivery delivery = getDelivery(deliveryId);
        UUID orderId = delivery.getOrderId();
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        warehouseClient.shippedToDelivery(new ShippedToDeliveryRequest(orderId, deliveryId));
        orderClient.assembly(orderId);
        delivery = deliveryRepository.save(delivery);
        deliveryMapper.mapToDeliveryDto(delivery);
    }

    private Delivery getDelivery(UUID id) {
        return deliveryRepository.findById(id).orElseThrow(() ->
                new NoDeliveryFoundException("Delivery is not found")
        );
    }
}