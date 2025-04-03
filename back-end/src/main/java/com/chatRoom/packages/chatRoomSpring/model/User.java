package com.chatRoom.packages.chatRoomSpring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double userId;

    private String fullname;
    private String profile;
    private String role; // peut être admin ou étudiant
    private String username;
    private String mail;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Relation avec Message (un-à-plusieurs)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference // Empêche les boucles pendant la sérialisation
    @ToString.Exclude     // Exclut les messages de la méthode toString()
    private List<Message> messages;

    // Relation avec Room via Appartenir (plusieurs-à-plusieurs)
    @ManyToMany
    @JoinTable(
            name = "message-room",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roomId")
    )
    @JsonIgnore // Empêche la sérialisation des rooms
    @ToString.Exclude // Exclut rooms de la méthode toString()
    private Set<Room> rooms;

    // Relation avec Demande (un-à-plusieurs)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore // Empêche la sérialisation des demandes
    @ToString.Exclude // Exclut demandes de la méthode toString()
    private List<Demande> demandes;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullname='" + fullname + '\'' +
                ", role='" + role + '\'' +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}