package com.chatRoom.packages.chatRoomSpring.service;

import com.chatRoom.packages.chatRoomSpring.DTO.MessageDTO;
import com.chatRoom.packages.chatRoomSpring.model.Message;
import com.chatRoom.packages.chatRoomSpring.model.Room;
import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.repository.MessageRepository;
import com.chatRoom.packages.chatRoomSpring.repository.RoomRepository;
import com.chatRoom.packages.chatRoomSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Message saveMessage(Message message) {
        return repository.save(message);
    }

    public List<Message> getAllMessages() {
        return repository.findAll();
    }

    public Optional<Message> getMessageById(double id) {
        return repository.findById(id);
    }

    public Message updateMessage(double id, Message message) {
        if (repository.existsById(id)) {
            message.setMessageId(id);
            return repository.save(message);
        }
        return null;
    }

    public boolean deleteMessage(double id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Message sendMessage(double userId, double roomId, String content) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        Message message = new Message();
        message.setContenu(content);
        message.setDateenvoi(new java.util.Date());
        message.setUser(user);
        message.setRoom(room);

        return repository.save(message);
    }
    public List<Message> findMessagesByRoomId(Double roomId) {
        return repository.findMessagesByRoomId(roomId);
    }


    // Méthode modifiée pour utiliser MessageDTO
    public List<MessageDTO> getMessagesByRoomId(Double roomId) {
        // Récupère les messages de la room
        List<Message> messages = repository.findMessagesByRoomId(roomId);

        // Convertit les messages en DTOs
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getMessageId(),
                        message.getContenu(),
                        message.getDateenvoi(),
                        message.getUser().getUsername(),
                        message.getUser().getFullname(),
                        message.getUser().getProfile(),
                        message.getUser().getUserId()
                        ))
                .collect(Collectors.toList());
    }
}