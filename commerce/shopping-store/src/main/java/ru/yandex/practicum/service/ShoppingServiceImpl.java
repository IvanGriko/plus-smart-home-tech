package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
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

import static ru.yandex.practicum.mapper.ProductMapper.mapToProductDto;

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
        return mapToProductDto(productDb);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findProductById(UUID id) {
        Product product = getProductFromStore(id);
        return mapToProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        getProductFromStore(product.getProductId());
        Product productUpdated = productMapper.mapToProduct(product);
        productUpdated = productRepository.save(productUpdated);
        return mapToProductDto(productUpdated);
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

//    @Override
//    @Transactional(readOnly = true)
//    public Collection<ProductDto> findByProductCategory(ProductCategory category, Pageable params) {
//        Sort sort = Sort.by("productName", "price");
//        int pageSize = Math.max(params.getSize(), 1);
//        PageRequest pageable = PageRequest.of(params.getPage(), pageSize, sort);
//        List<Product> products = productRepository.findByProductCategory(category, pageable);
//        return productMapper.mapToListProductDto(products);
//    }

//    @Override
//    @Transactional(readOnly = true)
//    public SearchResultDto findByProductCategory(ProductCategory category, Pageable params) {
//        Sort sort = Sort.by("productName", "price");
//        int pageSize = Math.max(params.getSize(), 1);
//        PageRequest pageable = PageRequest.of(params.getPage(), pageSize, sort);
//        List<Product> products = productRepository.findByProductCategory(category, pageable);
//        List<ProductDto> dtos = (List<ProductDto>) productMapper.mapToListProductDto(products);
//        SearchResultDto result = new SearchResultDto();
//        result.setDescription("Очередная страница товаров в соответствии с типом");
//        result.setContent(dtos);
//        return result;
//    }

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
                .map(ProductMapper::mapToProductDto)
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
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product is not found"));
    }
}
