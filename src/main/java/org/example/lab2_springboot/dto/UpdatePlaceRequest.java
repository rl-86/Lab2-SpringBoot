package org.example.lab2_springboot.dto;

import jakarta.validation.constraints.Size;

public class UpdatePlaceRequest {
    @Size(max = 200, message = "Name cannot exceed 200 characters")
    private String name;

    private Long categoryId;
    private Boolean isPublic;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private Double latitude;
    private Double longitude;

    // Constructors
    public UpdatePlaceRequest() {}

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