package org.example.lab2_springboot.repository;

import org.example.lab2_springboot.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    // ✅ Hitta publika platser som inte är deleted
    List<Place> findByIsPublicTrueAndDeletedAtIsNull();

    // ✅ Hitta specifik publik plats som inte är deleted
    Optional<Place> findByIdAndIsPublicTrueAndDeletedAtIsNull(Long id);

    // ✅ Hitta publika platser i kategori som inte är deleted
    List<Place> findByCategoryIdAndIsPublicTrueAndDeletedAtIsNull(Long categoryId);

    // ✅ Hitta användarens platser som inte är deleted
    List<Place> findByUserIdAndDeletedAtIsNull(Long userId);

    // ✅ Hitta alla platser inom radie (publika endast)
    @Query("SELECT p FROM Place p WHERE p.deletedAt IS NULL AND " +
            "p.isPublic = true AND " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(p.latitude)))) < :radius")
    List<Place> findPlacesWithinRadius(@Param("lat") Double latitude,
                                       @Param("lng") Double longitude,
                                       @Param("radius") Double radiusKm);

    // ✅ Extra: Hitta alla platser (publika och privata) inom radie för en användare
    @Query("SELECT p FROM Place p WHERE p.deletedAt IS NULL AND " +
            "(p.isPublic = true OR p.userId = :userId) AND " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(p.latitude)) * " +
            "cos(radians(p.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(p.latitude)))) < :radius")
    List<Place> findPlacesWithinRadiusForUser(@Param("lat") Double latitude,
                                              @Param("lng") Double longitude,
                                              @Param("radius") Double radiusKm,
                                              @Param("userId") Long userId);
}