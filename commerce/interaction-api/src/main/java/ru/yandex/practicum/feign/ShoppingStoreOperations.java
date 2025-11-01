package ru.yandex.practicum.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.Pageable;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SearchResultDto;

import java.util.Collection;
import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreOperations {

    @GetMapping
    SearchResultDto searchProducts(
            @RequestParam(value = "category", required = false) ProductCategory category,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "productId") String sortField
    );

    @PutMapping
    ProductDto addProduct(@Valid @RequestBody ProductDto product);

//    @GetMapping
//    Collection<ProductDto> searchProducts(
//            @RequestParam(value = "category", required = false) String category,
//            Pageable pageable
//    );

//    @GetMapping
//    SearchResultDto searchProducts(
//            @RequestParam(value = "category", required = false) String category,
//            Pageable pageable
//    );

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable UUID productId);

    @PostMapping
    ProductDto updateProduct(@Valid @RequestBody ProductDto product);

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean updateProductQuantityState(
            @RequestParam("productId") UUID productId,
            @RequestParam("quantityState") String quantityState);
}
