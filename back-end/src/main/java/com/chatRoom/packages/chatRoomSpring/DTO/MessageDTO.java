package com.chatRoom.packages.chatRoomSpring.DTO;

import java.util.Date;

public class MessageDTO {
    private double messageId;
    private String contenu;
    private Date dateenvoi;
    private String username;
    private String fullname;
    private String profile;

    public Double getIdUser() {
        return idUser;
    }

    public void setIdUser(Double idUSer) {
        this.idUser = idUSer;
    }

    private Double idUser;

    public MessageDTO(double messageId, String contenu, Date dateenvoi, String username, String fullname, String profile, double idUser) {
        this.messageId = messageId;
        this.contenu = contenu;
        this.dateenvoi = dateenvoi;
        this.username = username;
        this.fullname = fullname;
        this.profile = profile;
        this.idUser = idUser;
    }




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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public MessageDTO() {
    }
}
