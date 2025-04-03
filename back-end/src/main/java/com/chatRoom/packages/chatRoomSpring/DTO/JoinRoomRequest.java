package com.chatRoom.packages.chatRoomSpring.DTO;

public class JoinRoomRequest {
    private double userId;
    private double roomId;

    // Getters et Setters
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
