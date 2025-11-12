package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addNewProductToWarehouse(NewProductInWarehouseRequest request);

    void increaseProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCart);

    void returnProductsToWarehouse(Map<UUID, Integer> products);

    BookedProductsDto assemblyProducts(AssemblyProductsForOrderRequest request);

    void shipToDelivery(ShippedToDeliveryRequest request);
}
