package com.chatRoom.packages.chatRoomSpring.service;

import com.chatRoom.packages.chatRoomSpring.model.Room;
import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.repository.MessageRepository;
import com.chatRoom.packages.chatRoomSpring.repository.RoomRepository;
import com.chatRoom.packages.chatRoomSpring.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/profils/";

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 10;
    private static final SecureRandom RANDOM = new SecureRandom();

    private String generateUniqueCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
    public void createRoom(String titre, String description, MultipartFile profile) throws IOException {
        Room newRoom = new Room();
        newRoom.setTitre(titre);
        newRoom.setDescription(description);

        // Générer un code unique pour la room
        String uniqueCode;
        do {
            uniqueCode = generateUniqueCode();
        } while (roomRepository.findByCode(uniqueCode) != null); // Vérifie l'unicité
        newRoom.setCode(uniqueCode);

        // Sauvegarder l'image de profil de la room
        if (profile != null && !profile.isEmpty()) {
            String extension = getFileExtension(profile.getOriginalFilename());
            String filename = titre.replaceAll("\\s+", "_") + "." + extension; // Nettoyer le titre
            File directory = new File(UPLOAD_DIRECTORY);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File destinationFile = new File(directory, filename);
            profile.transferTo(destinationFile);

            newRoom.setProfile("uploads/profils/" + filename);
        }

        roomRepository.save(newRoom);
    }
    // Méthode pour obtenir l'extension du fichier
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return "jpg"; // Extension par défaut
    }




    public Room getRoomWithMessagesAndUsers(double roomId) {
        return roomRepository.findRoomWithMessagesAndUsersById(roomId);
    }

    public Room updateRoom(Double roomId, String titre, String description, MultipartFile profile) throws IOException {
        Optional<Room> existingRoomOptional = roomRepository.findById(roomId);

        if (existingRoomOptional.isPresent()) {
            Room existingRoom = existingRoomOptional.get();

            // Mettre à jour les informations de la Room
            existingRoom.setTitre(titre);
            existingRoom.setDescription(description);

            // Si une nouvelle image est uploadée, traiter ici
            if (profile != null && !profile.isEmpty()) {
                String extension = getFileExtension(profile.getOriginalFilename());
                String filename = titre.replaceAll("\\s+", "_") + "." + extension; // Nettoyer le titre pour le nom du fichier
                File directory = new File(UPLOAD_DIRECTORY);

                // Crée le dossier si nécessaire
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Enregistrer la nouvelle image
                File destinationFile = new File(directory, filename);
                profile.transferTo(destinationFile);

                // Enregistrer le chemin relatif de l'image dans la base de données
                existingRoom.setProfile("uploads/profils/" + filename);
            }

            return roomRepository.save(existingRoom); // Enregistrer les modifications
        } else {
            throw new IllegalArgumentException("Room with ID " + roomId + " not found.");
        }
    }
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Transactional
    public boolean deleteRoom(double roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room != null) {
            // Supprimer les messages associés
            if (room.getMessages() != null && !room.getMessages().isEmpty()) {
                messageRepository.deleteAll(room.getMessages());
            }

            // Supprimer la room
            roomRepository.delete(room);
            return true;
        }
        return false;
    }

    public List<Room> getRoomsWithMembershipStatus(double userId) {
        List<Room> allRooms = roomRepository.findAll();
        return allRooms.stream().peek(room -> {
            boolean isMember = room.getUsers().stream().anyMatch(user -> user.getUserId() == userId);
            room.setIsMember(isMember); // Appel correct du setter
        }).collect(Collectors.toList());
    }

    public void addUserToRoom(double userId, double roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room introuvable"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Ajouter l'utilisateur à la room
        room.getUsers().add(user);
        // Ajouter la room à l'utilisateur
        user.getRooms().add(room);

        // Sauvegarder les deux entités pour persister la relation
        roomRepository.save(room);
        userRepository.save(user);
    }


    @Transactional
    public void removeUserFromRoom(double roomId, double userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room non trouvée."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé."));

        room.getUsers().remove(user);
        user.getRooms().remove(room);

        roomRepository.save(room); // Mettre à jour la Room
        userRepository.save(user); // Mettre à jour l'Utilisateur
    }

    public List<User> getUsersByRoomId(double roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room non trouvée."));
        return new ArrayList<>(room.getUsers()); // Convertir Set<User> en List<User>
    }


    public Room getRoomById(double roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }
}
