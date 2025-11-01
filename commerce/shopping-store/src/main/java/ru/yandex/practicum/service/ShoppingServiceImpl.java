package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.exceptions.ProductNotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ShoppingStoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShoppingServiceImpl implements ShoppingService {
    ShoppingStoreRepository productRepository;
    ProductMapper productMapper;

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
    public SearchResultDto searchProducts(ProductCategory category, Integer page, Integer size, String sortString) {
        List<SortDto> sortList = parseSortString(sortString);
        Sort sort = sortList.stream()
                .map(s -> Sort.by(Sort.Direction.valueOf(s.getDirection()), s.getProperty()))
                .reduce(Sort.unsorted(), Sort::and);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> products = productRepository.findByProductCategory(category, pageable);
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::mapToProductDto)
                .toList();
        SearchResultDto result = new SearchResultDto();
        result.setContent(productDtos);
        result.setSort(sortList);
        return result;
    }

    private List<SortDto> parseSortString(String input) {
        List<SortDto> result = new ArrayList<>();
        if (input == null || input.isEmpty()) return result;
        String[] parts = input.split(",");
        for (int i = 0; i < parts.length - 1; i += 2) {
            SortDto dto = new SortDto();
            dto.setProperty(parts[i].trim());
            dto.setDirection(parts[i + 1].trim());
            result.add(dto);
        }
        return result;
    }

    private Product getProductFromStore(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product is not found"));
    }
}
