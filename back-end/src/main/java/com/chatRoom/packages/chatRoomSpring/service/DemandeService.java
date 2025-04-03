package com.chatRoom.packages.chatRoomSpring.service;

import com.chatRoom.packages.chatRoomSpring.model.Demande;
import com.chatRoom.packages.chatRoomSpring.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandeService {

    @Autowired
    private DemandeRepository repository;

    // Create a new Demande
    public Demande saveDemande(Demande demande) {
        return repository.save(demande);
    }

    // Get all Demandes
    public List<Demande> getAllDemandes() {
        return repository.findAll();
    }

    // Get a Demande by ID
    public Optional<Demande> getDemandeById(double id) {
        return repository.findById(id);
    }

    // Update a Demande
    public Demande updateDemande(double id, Demande demande) {
        if (repository.existsById(id)) {
            demande.setDemandeId(id);
            return repository.save(demande);
        }
        return null;
    }

    // Delete a Demande
    public boolean deleteDemande(double id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
