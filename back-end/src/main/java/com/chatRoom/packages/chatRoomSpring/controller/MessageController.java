package com.chatRoom.packages.chatRoomSpring.controller;

import com.chatRoom.packages.chatRoomSpring.DTO.MessageDTO;
import com.chatRoom.packages.chatRoomSpring.model.Message;
import com.chatRoom.packages.chatRoomSpring.model.Room;
import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.service.MessageService;
import com.chatRoom.packages.chatRoomSpring.service.RoomService;
import com.chatRoom.packages.chatRoomSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.chatRoom.packages.chatRoomSpring.DTO.MessageRequest;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    @Autowired
    private MessageService service;
    @Autowired
    private UserService userService;

    @Autowired
    private WebSocketController webSocketController;
    @Autowired
    private RoomService roomService;

    // Create a new Message
    @PostMapping("/add-message")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = service.saveMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    // Get all Messages
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = service.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // Get a Message by ID
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable double id) {
        return service.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a Message
    @PutMapping("/modifier-message/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable double id, @RequestBody Message message) {
        Message updatedMessage = service.updateMessage(id, message);
        return updatedMessage != null ? ResponseEntity.ok(updatedMessage) : ResponseEntity.notFound().build();
    }

    // Delete a Message
    @DeleteMapping("/delete-message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable double id) {
        boolean deleted = service.deleteMessage(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    //    @PostMapping("/send")
//    public Message sendMessage(@RequestBody MessageRequest messageRequest) {
//        return service.sendMessage(messageRequest.getUserId(), messageRequest.getRoomId(), messageRequest.getContent());
//    }
//
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessagesByRoomId(@PathVariable Double roomId) {
        List<Message> messages = service.findMessagesByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }



//    @PostMapping("/send-message")
//    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
//        try {
//            // Recherche de l'utilisateur et de la salle
//            User user = userService.getUserById(messageRequest.getUserId());
//            Room room = roomService.getRoomById(messageRequest.getRoomId());
//
//            if (user == null || room == null) {
//                return ResponseEntity.badRequest().body(null);
//            }
//
//            // Création du message
//            Message message = new Message();
//            message.setContenu(messageRequest.getContenu());
//            message.setDateenvoi(new Date());
//            message.setUser(user);
//            message.setRoom(room);
//
//            // Sauvegarde du message
//            Message savedMessage = service.saveMessage(message);
//
//            // Diffusion du message via WebSocket
//            webSocketController.sendMessageToClients(savedMessage);
//
//            return ResponseEntity.ok(savedMessage);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


    @PostMapping("/send-message")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest messageRequest) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Recherche de la salle
            Room room = roomService.getRoomById(messageRequest.getRoomId());
            if (room == null) {
                return ResponseEntity.badRequest().body(null);
            }

            // Création du message
            Message message = new Message();
            message.setContenu(messageRequest.getContenu());
            message.setDateenvoi(new Date());
            message.setUser(user);
            message.setRoom(room);

            // Sauvegarde et diffusion
            Message savedMessage = service.saveMessage(message);
            webSocketController.sendMessageToClients(savedMessage);

            return ResponseEntity.ok(savedMessage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Double roomId) {
        List<MessageDTO> messagesDTO = service.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(messagesDTO);
    }
}