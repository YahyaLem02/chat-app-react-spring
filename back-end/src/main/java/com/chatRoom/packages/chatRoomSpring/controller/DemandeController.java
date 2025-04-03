package com.chatRoom.packages.chatRoomSpring.controller;

import com.chatRoom.packages.chatRoomSpring.model.Demande;
import com.chatRoom.packages.chatRoomSpring.service.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
public class DemandeController {

    @Autowired
    private DemandeService service;

    // Create a new Demande
    @PostMapping("/add-demande")
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande) {
        Demande createdDemande = service.saveDemande(demande);
        return ResponseEntity.ok(createdDemande);
    }

    // Get all Demandes
    @GetMapping
    public ResponseEntity<List<Demande>> getAllDemandes() {
        List<Demande> demandes = service.getAllDemandes();
        return ResponseEntity.ok(demandes);
    }

    // Get a Demande by ID
    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable double id) {
        Optional<Demande> demande = service.getDemandeById(id);
        return demande.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a Demande
    @PutMapping("/modifier-demande/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable double id, @RequestBody Demande demande) {
        Demande updatedDemande = service.updateDemande(id, demande);
        return updatedDemande != null ? ResponseEntity.ok(updatedDemande) : ResponseEntity.notFound().build();
    }

    // Delete a Demande
    @DeleteMapping("delete-demande/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable double id) {
        boolean deleted = service.deleteDemande(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
