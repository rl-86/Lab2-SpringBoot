package org.example.lab2_springboot.controller;

import jakarta.validation.Valid;
import org.example.lab2_springboot.dto.PlaceDTO;
import org.example.lab2_springboot.dto.CreatePlaceRequest;
import org.example.lab2_springboot.dto.UpdatePlaceRequest;
import org.example.lab2_springboot.dto.CategoryDTO;
import org.example.lab2_springboot.model.Place;
import org.example.lab2_springboot.model.Category;
import org.example.lab2_springboot.repository.PlaceRepository;
import org.example.lab2_springboot.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.example.lab2_springboot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/places") // Matchar SecurityConfig
public class PlaceController {

    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public PlaceController(PlaceRepository placeRepository,
                           CategoryRepository categoryRepository,
                            UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // GET alla publika platser - tillgängligt för alla
    @GetMapping("/public")
    public ResponseEntity<List<PlaceDTO>> getPublicPlaces() {
        List<Place> places = placeRepository.findByIsPublicTrueAndDeletedAtIsNull();
        List<PlaceDTO> placeDTOs = places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(placeDTOs);
    }

    // GET specifik publik plats - tillgängligt för alla
    @GetMapping("/public/{id}")
    public ResponseEntity<?> getPublicPlaceById(@PathVariable Long id) {
        Optional<Place> place = placeRepository.findByIdAndIsPublicTrueAndDeletedAtIsNull(id);

        if (place.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Public place not found with id: " + id));
        }

        return ResponseEntity.ok(convertToDTO(place.get()));
    }

    // GET publika platser i kategori - tillgängligt för alla
    @GetMapping("/category/{categoryId}/public")
    public ResponseEntity<List<PlaceDTO>> getPublicPlacesByCategory(@PathVariable Long categoryId) {
        List<Place> places = placeRepository.findByCategoryIdAndIsPublicTrueAndDeletedAtIsNull(categoryId);
        List<PlaceDTO> placeDTOs = places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(placeDTOs);
    }

    // GET användarens egna platser - kräver inloggning
    @GetMapping("/my")
    public ResponseEntity<?> getMyPlaces() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not authenticated"));
        }

        List<Place> places = placeRepository.findByUserIdAndDeletedAtIsNull(userId);
        List<PlaceDTO> placeDTOs = places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(placeDTOs);
    }

    // GET platser inom radie - publika endast
    @GetMapping("/search")
    public ResponseEntity<List<PlaceDTO>> searchPlacesWithinRadius(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {

        List<Place> places = placeRepository.findPlacesWithinRadius(latitude, longitude, radiusKm);
        List<PlaceDTO> placeDTOs = places.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(placeDTOs);
    }

    // POST skapa ny plats - kräver inloggning
    @PostMapping
    public ResponseEntity<?> createPlace(@Valid @RequestBody CreatePlaceRequest createRequest) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            // Kontrollera att kategorin finns
            Category category = categoryRepository.findById(createRequest.getCategoryId())
                    .orElse(null);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Category not found with id: " + createRequest.getCategoryId()));
            }

            // Skapa ny plats
            Place place = new Place();
            place.setName(createRequest.getName());
            place.setCategory(category);
            place.setUserId(userId);
            place.setIsPublic(createRequest.getIsPublic());
            place.setDescription(createRequest.getDescription());
            place.setLatitude(createRequest.getLatitude());
            place.setLongitude(createRequest.getLongitude());

            Place savedPlace = placeRepository.save(place);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertToDTO(savedPlace));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create place: " + e.getMessage()));
        }
    }

    // PUT uppdatera plats - endast sin egen
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Long id,
                                         @Valid @RequestBody UpdatePlaceRequest updateRequest) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            Place existingPlace = placeRepository.findById(id).orElse(null);
            if (existingPlace == null || existingPlace.getDeletedAt() != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Place not found with id: " + id));
            }

            // Kontrollera att användaren äger platsen
            if (!existingPlace.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You can only update your own places"));
            }

            // Uppdatera fält som angetts
            if (updateRequest.getName() != null) {
                existingPlace.setName(updateRequest.getName());
            }
            if (updateRequest.getDescription() != null) {
                existingPlace.setDescription(updateRequest.getDescription());
            }
            if (updateRequest.getIsPublic() != null) {
                existingPlace.setIsPublic(updateRequest.getIsPublic());
            }
            if (updateRequest.getLatitude() != null) {
                existingPlace.setLatitude(updateRequest.getLatitude());
            }
            if (updateRequest.getLongitude() != null) {
                existingPlace.setLongitude(updateRequest.getLongitude());
            }
            if (updateRequest.getCategoryId() != null) {
                Category category = categoryRepository.findById(updateRequest.getCategoryId())
                        .orElse(null);
                if (category == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Category not found"));
                }
                existingPlace.setCategory(category);
            }

            Place savedPlace = placeRepository.save(existingPlace);
            return ResponseEntity.ok(convertToDTO(savedPlace));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update place: " + e.getMessage()));
        }
    }

    // DELETE plats (soft delete) - endast sin egen
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not authenticated"));
            }

            Place place = placeRepository.findById(id).orElse(null);
            if (place == null || place.getDeletedAt() != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Place not found with id: " + id));
            }

            if (!place.getUserId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "You can only delete your own places"));
            }

            // Soft delete
            place.markAsDeleted();
            placeRepository.save(place);

            return ResponseEntity.ok(Map.of("message", "Place deleted successfully"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete place: " + e.getMessage()));
        }
    }

    // Helper methods
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String username = auth.getName();
            return userRepository.findByUsername(username)
                    .map(user -> user.getId())
                    .orElse(null);
        }
        return null;
    }

    private PlaceDTO convertToDTO(Place place) {
        CategoryDTO categoryDTO = new CategoryDTO(
                place.getCategory().getId(),
                place.getCategory().getName(),
                place.getCategory().getSymbol(),
                place.getCategory().getDescription()
        );

        return new PlaceDTO(
                place.getId(),
                place.getName(),
                categoryDTO,
                place.getUserId(),
                place.getIsPublic(),
                place.getDescription(),
                place.getLatitude(),
                place.getLongitude(),
                place.getCreatedDate(),
                place.getLastModified()
        );
    }
}