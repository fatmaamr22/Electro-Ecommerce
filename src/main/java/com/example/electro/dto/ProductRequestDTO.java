package com.example.electro.dto;


import java.util.List;

public record ProductRequestDTO( List<Integer> categories,
                                 List<String> brands,
                                 Boolean deleted,
                                 List<String> processors,
                                 List<Integer> memoryOptions,
                                 Integer minPrice,
                                 Integer maxPrice,
                                 List<String> operatingSystem,
                                 int page,
                                 int size,
                                 String searchInput) {
}
