package org.example.lab2_springboot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @ManyToOne
    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "User ID is required")
    private Long userId;

    private Boolean isPublic = true;
    private LocalDateTime lastModified;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private String coordinates;
    private LocalDateTime createdDate;

    public Place() {
        this.createdDate = LocalDateTime.now();
    }

    public Place(String name, Category category, Long userId, Boolean isPublic, String description, String coordinates) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.coordinates = coordinates;
        this.createdDate = LocalDateTime.now();
    }

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoordinates() { return coordinates; }
    public void setCoordinates(String coordinates) { this.coordinates = coordinates; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}