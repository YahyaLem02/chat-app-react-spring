package com.chatRoom.packages.chatRoomSpring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private double messageId;
    private String contenu;
    private Date dateenvoi;

    // Relation avec User (plusieurs-à-un)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    @Transient // Ce champ ne sera pas stocké en base de données
    private String username;

    public Double getIdUser() {
        return idUser;
    }

    public void setIdUser(Double idUser) {
        this.idUser = idUser;
    }

    @Transient
    private  Double idUser;

    // Relation avec Room (plusieurs-à-un)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "FK_message_room"))    @JsonBackReference
    private Room room;

    public double getMessageId() {
        return messageId;
    }

    public void setMessageId(double messageId) {
        this.messageId = messageId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateenvoi() {
        return dateenvoi;
    }

    public void setDateenvoi(Date dateenvoi) {
        this.dateenvoi = dateenvoi;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String userFullname) {
        this.username = userFullname;
    }
}