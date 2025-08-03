package org.example.lab2_springboot.dto;

import java.time.LocalDateTime;

public class PlaceDTO {
    private Long id;
    private String name;
    private CategoryDTO category;
    private Long userId;
    private Boolean isPublic;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    // Constructors
    public PlaceDTO() {}

    public PlaceDTO(Long id, String name, CategoryDTO category, Long userId,
                    Boolean isPublic, String description, Double latitude,
                    Double longitude, LocalDateTime createdDate, LocalDateTime lastModified) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.userId = userId;
        this.isPublic = isPublic;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
        this.lastModified = lastModified;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryDTO getCategory() { return category; }
    public void setCategory(CategoryDTO category) { this.category = category; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getLastModified() { return lastModified; }
    public void setLastModified(LocalDateTime lastModified) { this.lastModified = lastModified; }
}