package ru.yandex.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductDto;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreOperations {

    @PutMapping
    ProductDto addProduct(@Valid @RequestBody ProductDto product);

    @GetMapping
    Collection<ProductDto> searchProducts(
            @RequestParam(value = "category", required = false) String category,
            Pageable pageable
    );

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody UUID productId);

    @PostMapping("/quantityState/{productId}")
    void updateProductQuantityState(@PathVariable String productId, @RequestParam String quantityState);
}
