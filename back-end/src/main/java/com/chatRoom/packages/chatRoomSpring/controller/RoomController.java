package com.chatRoom.packages.chatRoomSpring.controller;

import com.chatRoom.packages.chatRoomSpring.DTO.JoinRoomRequest;
import com.chatRoom.packages.chatRoomSpring.DTO.MessageDTO;
import com.chatRoom.packages.chatRoomSpring.model.Room;
import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.service.MessageService;
import com.chatRoom.packages.chatRoomSpring.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8080"})

public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private MessageService messageService;

    // Create a new Room
//    @PostMapping("/add-room")
//    public ResponseEntity<String> createRoom(
//            @RequestParam("titre") String titre,
//            @RequestParam("description") String description,
//            @RequestParam("code") String code,
//            @RequestParam(value = "profile", required = false) MultipartFile profile
//    ) {
//        try {
//            roomService.createRoom(titre, description, code, profile);
//            return ResponseEntity.ok("Room created successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body("Error creating room: " + e.getMessage());
//        }
//    }


    @PostMapping("/add-room")
    public ResponseEntity<String> createRoom(
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,
            @RequestParam(value = "profile", required = false) MultipartFile profile
    ) {
        try {
            roomService.createRoom(titre, description, profile);
            return ResponseEntity.ok("Room created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating room: " + e.getMessage());
        }
    }


    // Get all Rooms
//    @GetMapping
//    public ResponseEntity<List<Room>> getAllRooms() {
//        List<Room> rooms = roomService.getAllRooms();
//
//
//        return ResponseEntity.ok(rooms);
//    }

    // Get a Room by ID with messages and users
//    @GetMapping("/{id}")
//    public ResponseEntity<Room> getRoomWithMessagesAndUsers(@PathVariable double id) {
//        Room room = roomService.getRoomWithMessagesAndUsers(id);
//        return room != null ? ResponseEntity.ok(room) : ResponseEntity.notFound().build();
//    }

    // Update a Room
    @PutMapping("/update-room/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Double id,
            @RequestParam("titre") String titre,
            @RequestParam("description") String description,

            @RequestParam(value = "profile", required = false) MultipartFile profile

    ) {
        try {

            Room updatedUser = roomService.updateRoom(id, titre, description, profile);
            if (updatedUser != null) {
                return ResponseEntity.ok("User updated successfully!");
            } else {
                return ResponseEntity.badRequest().body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user: " + e.getMessage());
        }
    }

    // Delete a Room
    @DeleteMapping("/delete-room/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable double id) {
        boolean deleted = roomService.deleteRoom(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting room.");
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable Double roomId) {
        List<MessageDTO> messagesDTO = messageService.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(messagesDTO);
    }
    @GetMapping("/details/{roomId}")
    public ResponseEntity<Room> getDetailsRoom(@PathVariable double roomId) {
        Optional<Room> room = Optional.ofNullable(roomService.getRoomById(roomId));
        return room.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/liste")
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
    @GetMapping("/user-rooms/{userId}")
    public ResponseEntity<List<Room>> getUserRooms(@PathVariable double userId) {
        List<Room> rooms = roomService.getRoomsWithMembershipStatus(userId);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/join-room")
    public ResponseEntity<String> joinRoom(@RequestBody JoinRoomRequest request) {
        try {
            roomService.addUserToRoom(request.getUserId(), request.getRoomId());
            return ResponseEntity.ok("Vous avez rejoint la room avec succès !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @DeleteMapping("/{roomId}/users/{userId}")
    public ResponseEntity<String> removeUserFromRoom(@PathVariable double roomId, @PathVariable double userId) {
        try {
            roomService.removeUserFromRoom(roomId, userId);
            return ResponseEntity.ok("Utilisateur retiré avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }

    @GetMapping("/{roomId}/users")
    public ResponseEntity<List<User>> getRoomUsers(@PathVariable double roomId) {
        try {
            List<User> users = roomService.getUsersByRoomId(roomId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


    @DeleteMapping("/{roomId}/leave/{userId}")
    public ResponseEntity<String> leaveRoom(@PathVariable double roomId, @PathVariable double userId) {
        try {
            roomService.removeUserFromRoom(roomId, userId);
            return ResponseEntity.ok("Vous avez quitté la room avec succès.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }





}
