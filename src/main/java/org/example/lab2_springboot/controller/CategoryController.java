package org.example.lab2_springboot.controller;

import jakarta.validation.Valid;
import org.example.lab2_springboot.dto.CategoryDTO;
import org.example.lab2_springboot.model.Category;
import org.example.lab2_springboot.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories") // ✅ Matchar SecurityConfig
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // GET alla kategorier - tillgängligt för alla
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryDTOs);
    }

    // GET specifik kategori - tillgängligt för alla
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Category not found with id: " + id));
        }

        return ResponseEntity.ok(convertToDTO(category.get()));
    }

    // POST ny kategori - endast ADMIN (SecurityConfig hanterar detta)
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        try {
            // Kontrollera om namnet redan finns
            if (categoryRepository.existsByName(categoryDTO.getName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Category with name '" + categoryDTO.getName() + "' already exists"));
            }

            // Konvertera DTO till Entity
            Category category = convertToEntity(categoryDTO);

            // Spara kategorin
            Category savedCategory = categoryRepository.save(category);

            System.out.println("✅ Category created: " + savedCategory.getName() +
                    " (ID: " + savedCategory.getId() + ")");

            // Returnera som DTO
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDTO(savedCategory));

        } catch (Exception e) {
            System.err.println("Error creating category: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create category: " + e.getMessage()));
        }
    }

    // Helper methods för konvertering mellan Entity och DTO
    private CategoryDTO convertToDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getSymbol(),
                category.getDescription()
        );
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setSymbol(categoryDTO.getSymbol());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }
}