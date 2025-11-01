package ru.yandex.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;

import java.util.UUID;

public interface ShoppingService {
    ProductDto addProduct(ProductDto product);

    ProductDto findProductById(UUID id);

    ProductDto updateProduct(ProductDto product);

    void removeProductFromStore(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest request);

    SearchResultDto searchProducts(ProductCategory category, Integer page, Integer size, String sortString);
}
