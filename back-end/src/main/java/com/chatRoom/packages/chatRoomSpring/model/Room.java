package com.chatRoom.packages.chatRoomSpring.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private double roomId;
    private String titre;
    private String description;
    private String profile;
    private String code;

    // Getter et Setter pour isMember
    public boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    @Transient
    private boolean isMember;

    // Relation avec Message (un-à-plusieurs)
    @OneToMany(mappedBy = "room")
    @JsonManagedReference
    private List<Message> messages;

    // Relation avec User via Appartenir (plusieurs-à-plusieurs)
    @ManyToMany(mappedBy = "rooms")
    private Set<User> users;

    // Relation avec Demande (un-à-plusieurs)
    @OneToMany(mappedBy = "room")
    private List<Demande> demandes;
}
