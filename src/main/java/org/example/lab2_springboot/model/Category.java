package org.example.lab2_springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 50, message = "Category name must be at most 50 characters long")
    @Column(unique = true, nullable = false) // ← Lägg till unique constraint
    private String name;

    @Size(max = 10, message = "Symbol cannot be longer than 10 characters") // ← Nytt fält
    private String symbol; // ← För emojis som ❤️, 🏠, 🍕 etc.

    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    private String description;

    // Constructors
    public Category() {}

    public Category(String name, String symbol, String description) {
        this.name = name;
        this.symbol = symbol;
        this.description = description;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSymbol() { return symbol; } // ← Ny getter
    public void setSymbol(String symbol) { this.symbol = symbol; } // ← Ny setter

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}