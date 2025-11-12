package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.feign.WarehouseOperations;
import ru.yandex.practicum.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;

    @Override
    public void addProductToWarehouse(NewProductInWarehouseRequest request) {
        warehouseService.addNewProductToWarehouse(request);
    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkShoppingCart(shoppingCartDto);
    }

    @Override
    public void increaseProductQuantity(AddProductToWarehouseRequest request) {
        warehouseService.increaseProductQuantity(request);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }


    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest shippedToDeliveryRequest) {
        warehouseService.shipToDelivery(shippedToDeliveryRequest);
    }

    @Override
    public void returnItem(Map<UUID, Integer> returnedItems) {
        warehouseService.returnProductsToWarehouse(returnedItems);
    }

    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request) {
        return warehouseService.assemblyProducts(request);
    }
}
