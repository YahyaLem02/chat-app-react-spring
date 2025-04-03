package com.chatRoom.packages.chatRoomSpring.repository;

import com.chatRoom.packages.chatRoomSpring.model.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Double> {
}
