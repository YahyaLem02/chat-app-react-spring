package com.chatRoom.packages.chatRoomSpring.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/uploads")
public class FileController {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/profils/";

    private static final String UPLOAD_DIRECTORYROOM = System.getProperty("room.dir") + "/uploads/profils/"; // Correction du chemin


    @GetMapping("/profils/{filename}")
    public Resource getProfileImage(@PathVariable String filename) {
        File file = new File(UPLOAD_DIRECTORY + filename);
        if (!file.exists()) {
            return null;  // Vous pouvez aussi retourner une image par défaut ou une réponse d'erreur.
        }
        return new FileSystemResource(file);
    }


    @GetMapping("/profil/{filename}")
    public Resource getRoomProfile(@PathVariable String filename) {
        File file = new File(UPLOAD_DIRECTORYROOM + filename);
        if (!file.exists()) {
            return null;  // Vous pouvez aussi retourner une image par défaut ou une réponse d'erreur.
        }
        return new FileSystemResource(file);
    }



}




