package com.chatRoom.packages.chatRoomSpring.controller;

import com.chatRoom.packages.chatRoomSpring.model.Message;
import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessageToClients(Message message) {
        try {
            // Ajouter le champ username
            User user = message.getUser();
            if (user != null) {
                message.setUsername(user.getUsername());
                message.setIdUser(user.getUserId());
            } else {
                message.setUsername("Unknown User");
            }

            // Diffuser le message
            messagingTemplate.convertAndSend("/topic/messages", message);

        } catch (Exception e) {
            System.err.println("Erreur lors de la diffusion du message WebSocket : " + e.getMessage());
        }
    }
}