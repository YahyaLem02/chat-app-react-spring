package com.chatRoom.packages.chatRoomSpring.service;

import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // Utilisation du répertoire de travail de l'application pour créer un dossier "uploads/profils"
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/profils/"; // Chemin absolu

    public void createUser(String fullname, String username, String mail, String password, MultipartFile profile, String role) throws IOException {
        User newUser = new User();
        newUser.setFullname(fullname);
        newUser.setUsername(username);
        newUser.setMail(mail);
        newUser.setPassword(password); // Idéalement, hachez le mot de passe avant de le sauvegarder.
        newUser.setRole(role);
        // Sauvegarder l'image dans le dossier
        if (profile != null && !profile.isEmpty()) {
            String extension = getFileExtension(profile.getOriginalFilename());
            String filename = username + "." + extension;
            File directory = new File(UPLOAD_DIRECTORY);

            // Crée le dossier si nécessaire
            if (!directory.exists()) {
                directory.mkdirs(); // Crée le dossier si nécessaire
            }

            // Chemin complet pour enregistrer le fichier
            File destinationFile = new File(directory, filename);
            profile.transferTo(destinationFile);

            // Enregistrer le chemin relatif dans la base de données
            newUser.setProfile("uploads/profils/" + filename);
        }

        userRepository.save(newUser);
    }

    // Méthode pour obtenir l'extension du fichier
    private String getFileExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return "jpg"; // Extension par défaut
    }



    public User updateUser(Double userId, String fullname, String username, String mail, MultipartFile profile) throws IOException {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            // Mettre à jour les informations de l'utilisateur
            existingUser.setFullname(fullname);
            existingUser.setUsername(username);
            existingUser.setMail(mail);


            // Si une image est uploadée, vous pouvez la traiter ici
            if (profile != null && !profile.isEmpty()) {
                String extension = getFileExtension(profile.getOriginalFilename());
                String filename = username + "." + extension;
                File directory = new File(UPLOAD_DIRECTORY);

                // Crée le dossier si nécessaire
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Enregistrer la nouvelle image
                File destinationFile = new File(directory, filename);
                profile.transferTo(destinationFile);

                // Enregistrer le chemin relatif de l'image dans la base de données
                existingUser.setProfile("uploads/profils/" + filename);
            }

            return userRepository.save(existingUser); // Enregistrer les modifications
        }

        return null; // Si l'utilisateur n'est pas trouvé
    }
}
