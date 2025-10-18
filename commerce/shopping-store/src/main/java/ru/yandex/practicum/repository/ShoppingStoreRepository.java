package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.model.Product;

import java.util.List;
import java.util.UUID;

//public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {
//    List<Product> getProductsByProductCategory(ProductCategory productCategory, Pageable pageable);
//}

public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE p.productCategory = ?1")
    List<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
}