package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;

import java.util.Collection;
import java.util.UUID;

public interface ShoppingService {
    ProductDto addProduct(ProductDto product);

    ProductDto findProductById(UUID id);

    ProductDto updateProduct(ProductDto product);

    void removeProductFromStore(UUID productId);

    boolean setProductQuantityState(UUID productId, String quantityState);

    Collection<ProductDto> findByProductCategory(ProductCategory category, Pageable params);
}
