package org.example.lab2_springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePlaceRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 200, message = "Name cannot exceed 200 characters")
    private String name;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Boolean isPublic = true; // Default to public

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Double latitude;
    private Double longitude;

    // Constructors
    public CreatePlaceRequest() {}

    public CreatePlaceRequest(String name, Long categoryId, Boolean isPublic,
                              String description, Double latitude, Double longitude) {
        this.name = name;
        this.categoryId = categoryId;
        this.isPublic = isPublic;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}