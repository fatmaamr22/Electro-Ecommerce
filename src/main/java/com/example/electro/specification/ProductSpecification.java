package com.example.electro.specification;

import com.example.electro.model.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    // Base specifications
    public static class ProductSpecifications {
        public static Specification<Product> hasDeletedStatus(Boolean deleted) {
            return (root, query, cb) -> deleted == null ? null :
                    cb.equal(root.get("deleted"), deleted);
        }

        public static Specification<Product> inCategories(List<Integer> categories) {
            return (root, query, cb) -> categories == null || categories.isEmpty() ? null :
                    root.get("category").get("id").in(categories);
        }

        public static Specification<Product> hasBrands(List<String> brands) {
            return (root, query, cb) -> brands == null || brands.isEmpty() ? null :
                    root.get("brandName").in(brands);
        }

        public static Specification<Product> hasProcessors(List<String> processors) {
            return (root, query, cb) -> {
                if (processors == null || processors.isEmpty()) return null;
                Join<Object, Object> specsJoin = root.join("specs", JoinType.INNER);
                return specsJoin.get("processor").in(processors);
            };
        }

        public static Specification<Product> hasOperatingSystem(List<String> operatingSystems) {
            return (root, query, cb) -> {
                if (operatingSystems == null || operatingSystems.isEmpty()) return null;
                Join<Object, Object> specsJoin = root.join("specs", JoinType.INNER);
                return specsJoin.get("os").in(operatingSystems);
            };
        }

        public static Specification<Product> hasMemoryOptions(List<Integer> memoryOptions) {
            return (root, query, cb) -> {
                if (memoryOptions == null || memoryOptions.isEmpty()) return null;
                Join<Object, Object> specsJoin = root.join("specs", JoinType.INNER);
                return specsJoin.get("memory").in(memoryOptions);
            };
        }

        public static Specification<Product> isPriceInRange(Integer minPrice, Integer maxPrice) {
            return (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (minPrice != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
                }
                return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[0]));
            };
        }

        public static Specification<Product> nameContains(String searchInput) {
            return (root, query, cb) -> searchInput == null || searchInput.trim().isEmpty() ? null :
                    cb.like(cb.lower(root.get("name")), "%" + searchInput.toLowerCase() + "%");
        }
    }

    public static class ProductSpecificationBuilder {
        private String searchInput;
        private List<Integer> categories;
        private List<String> brands;
        private List<String> processors;
        private List<String> operatingSystem;
        private List<Integer> memoryOptions;
        private Integer minPrice;
        private Integer maxPrice;
        private Boolean deleted;

        public ProductSpecificationBuilder withSearchInput(String searchInput) {
            this.searchInput = searchInput;
            return this;
        }

        public ProductSpecificationBuilder withCategories(List<Integer> categories) {
            this.categories = categories;
            return this;
        }

        public ProductSpecificationBuilder withBrands(List<String> brands) {
            this.brands = brands;
            return this;
        }

        public ProductSpecificationBuilder withProcessors(List<String> processors) {
            this.processors = processors;
            return this;
        }

        public ProductSpecificationBuilder withOperatingSystem(List<String> operatingSystem) {
            this.operatingSystem = operatingSystem;
            return this;
        }

        public ProductSpecificationBuilder withMemoryOptions(List<Integer> memoryOptions) {
            this.memoryOptions = memoryOptions;
            return this;
        }

        public ProductSpecificationBuilder withPriceRange(Integer minPrice, Integer maxPrice) {
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            return this;
        }

        public ProductSpecificationBuilder withDeleted(Boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Specification<Product> build() {
            return Specification.where(ProductSpecifications.hasDeletedStatus(deleted))
                    .and(ProductSpecifications.inCategories(categories))
                    .and(ProductSpecifications.hasBrands(brands))
                    .and(ProductSpecifications.hasProcessors(processors))
                    .and(ProductSpecifications.hasOperatingSystem(operatingSystem))
                    .and(ProductSpecifications.hasMemoryOptions(memoryOptions))
                    .and(ProductSpecifications.isPriceInRange(minPrice, maxPrice))
                    .and(ProductSpecifications.nameContains(searchInput));
        }
    }

    // Factory method for builder
    public static ProductSpecificationBuilder builder() {
        return new ProductSpecificationBuilder();
    }
}