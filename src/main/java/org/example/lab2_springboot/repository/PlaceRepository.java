package org.example.lab2_springboot.repository;

import org.example.lab2_springboot.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    List<Place> findByIsPublicTrue();
    List<Place> findByCategoryId(Long categoryId);
}
