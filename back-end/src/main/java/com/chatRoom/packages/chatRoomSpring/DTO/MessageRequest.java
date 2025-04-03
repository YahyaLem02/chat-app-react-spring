package com.chatRoom.packages.chatRoomSpring.DTO;

public class MessageRequest {
    private String contenu;
    private double userId;  // Changement pour utiliser un ID de type double
    private double roomId;  // Changement pour utiliser un ID de type double

    // Getters et setters
    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public double getUserId() {
        return userId;
    }

    public void setUserId(double userId) {
        this.userId = userId;
    }

    public double getRoomId() {
        return roomId;
    }

    public void setRoomId(double roomId) {
        this.roomId = roomId;
    }
}
