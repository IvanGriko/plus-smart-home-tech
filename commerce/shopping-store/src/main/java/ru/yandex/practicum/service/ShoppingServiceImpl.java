package ru.yandex.practicum.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ShoppingStoreRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {
    private final ShoppingStoreRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto addProduct(ProductDto product) {
        Product productDb = productMapper.mapToProduct(product);
        productDb = productRepository.save(productDb);
        return productMapper.mapToProductDto(productDb);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findProductById(UUID id) {
        Product product = getProductFromStore(id);
        return productMapper.mapToProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        getProductFromStore(product.getProductId());
        Product productUpdated = productMapper.mapToProduct(product);
        productUpdated = productRepository.save(productUpdated);
        return productMapper.mapToProductDto(productUpdated);
    }

    @Override
    @Transactional
    public void removeProductFromStore(UUID productId) {
        Product product = getProductFromStore(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = getProductFromStore(request.getProductId());
        product.setQuantityState(request.getQuantityState());
        productRepository.save(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ProductDto> findByProductCategory(ProductCategory category, Pageable params) {
        Sort sort = Sort.by("productName", "price");
        int pageSize = Math.max(params.getSize(), 1);
        PageRequest pageable = PageRequest.of(params.getPage(), pageSize, sort);
        List<Product> products = productRepository.findByProductCategory(category, pageable);
        return productMapper.mapToListProductDto(products);
    }

    private Product getProductFromStore(UUID productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product is not found"));
    }
}
