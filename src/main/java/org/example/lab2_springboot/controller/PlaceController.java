package org.example.lab2_springboot.controller;

import org.example.lab2_springboot.model.Place;
import org.example.lab2_springboot.repository.PlaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/places")
public class PlaceController {

    private final PlaceRepository placeRepository;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @GetMapping
    public List<Place> getPublicPlaces() {
        return placeRepository.findByIsPublicTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Optional<Place> place = placeRepository.findById(id);
        return place.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Place> createPlace(@RequestBody Place place) {
        return ResponseEntity.ok(placeRepository.save(place));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @RequestBody Place updatedPlace) {
        return placeRepository.findById(id)
                .map(existingPlace -> {
                    existingPlace.setName(updatedPlace.getName());
                    existingPlace.setDescription(updatedPlace.getDescription());
                    existingPlace.setCoordinates(updatedPlace.getCoordinates());
                    existingPlace.setLastModified(updatedPlace.getLastModified());
                    return ResponseEntity.ok(placeRepository.save(existingPlace));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        if (placeRepository.existsById(id)) {
            placeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
