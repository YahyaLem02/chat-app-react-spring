package com.chatRoom.packages.chatRoomSpring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private double demandeId;
    private Date dateenvoi;
    private Date dateaction;
    private Boolean etat;

    // Relation avec User (plusieurs-à-un)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Relation avec Room (plusieurs-à-un)
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
