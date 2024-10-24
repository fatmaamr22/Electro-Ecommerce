package com.example.electro.specification;

import com.example.electro.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> withFilters(List<Integer> categories, List<String> brands,
                                                     List<String> processors,List<String> operatingSystem, List<Integer> memoryOptions,
                                                     Integer minPrice, Integer maxPrice) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by Category
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categories));
            }

            // Filter by Brand Name
            if (brands != null && !brands.isEmpty()) {
                predicates.add(root.get("brandName").in(brands));
            }

            // Join with ProductSpecs entity to filter by processor
            if (processors != null && !processors.isEmpty()) {
                Join<Object, Object> specsJoin = root.join("specs", JoinType.INNER);
                predicates.add(specsJoin.get("processor").in(processors));
            }
            if (operatingSystem != null && !operatingSystem.isEmpty()) {
                Join<Object, Object> specsJoin = root.join("specs", JoinType.INNER);
                predicates.add(specsJoin.get("os").in(operatingSystem));
            }

            // Filter by Memory from ProductSpecs
            if (memoryOptions != null && !memoryOptions.isEmpty()) {
                Join<Object, Object> specsJoin = root.join("specs", JoinType.INNER);
                predicates.add(specsJoin.get("memory").in(memoryOptions));
            }

            // Filter by Price Range
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            // Build final query with all predicates
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
