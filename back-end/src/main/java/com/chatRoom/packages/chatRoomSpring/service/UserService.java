package com.chatRoom.packages.chatRoomSpring.service;

import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired

    UserRepository repo;
    @Autowired
    private UserRepository userRepository;
    public List<User> getUsers() {
        return repo.findAll();
    }

//    public User getUserById(double userId) {
//        return repo.findById(userId).get();
//    }
    public void addUser(User user) {
        repo.save(user);
    }


    public void updateUser(User user) {
        repo.save(user);
    }
    public User updateUser(double userId, User updatedUser) {
        // Cherche si l'utilisateur existe avec cet ID
        Optional<User> existingUserOptional = repo.findById(userId);

        if (existingUserOptional.isPresent()) {
            // L'utilisateur existe, donc on le met à jour avec les nouvelles valeurs
            User existingUser = existingUserOptional.get();

            // Mettre à jour les champs de l'utilisateur existant avec les nouvelles valeurs
            existingUser.setFullname(updatedUser.getFullname());
            existingUser.setProfile(updatedUser.getProfile());
            existingUser.setMail(updatedUser.getMail());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setPassword(updatedUser.getPassword());

            // Enregistrer l'utilisateur mis à jour
            return repo.save(existingUser);
        } else {
            // Si l'utilisateur n'existe pas, retourner null (ou vous pouvez lever une exception)
            return null;
        }
    }

    public void deleteUser(double userId) {
        repo.deleteById(userId);
    }

    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByMail(username);
        System.out.println(user);

        if(user==null) {
            throw new UsernameNotFoundException("User not found with this email"+username);

        }


        System.out.println("Loaded user: " + user.getMail() + ", Role: " + user.getRole());
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(
                user.getMail(),
                user.getPassword(),
                authorities);
    }


    public User getUserById(double userId) {
        return repo.findById(userId).orElse(null); // Retourner null si l'utilisateur n'est pas trouvé
    }




    public User getUserByEmail(String email) {
        return userRepository.findByMail(email); // Méthode existante dans votre `UserRepository`
    }


}
