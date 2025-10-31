package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.model.Product;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    static ProductDto mapToProductDto(Product product) {
        return null;
    }

    Product mapToProduct(ProductDto dto);

    Collection<ProductDto> mapToListProductDto(List<Product> products);
}
