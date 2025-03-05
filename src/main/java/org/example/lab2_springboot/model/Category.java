package org.example.lab2_springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name cannot be blank") // 🛑 Se till att detta finns!
    @Size(max = 50, message = "Category name must be at most 50 characters long") // 🛑 Maxlängd
    private String name;

    @Size(max = 255, message = "Description cannot be longer than 255 characters") // 🛑 Maxlängd på description
    private String description;

    //  Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

