package com.chatRoom.packages.chatRoomSpring.controller;



import com.chatRoom.packages.chatRoomSpring.DTO.LoginRequest;
import com.chatRoom.packages.chatRoomSpring.DTO.UserRequest;
import com.chatRoom.packages.chatRoomSpring.SecurityConfig.JwtProvider;
import com.chatRoom.packages.chatRoomSpring.model.User;
import com.chatRoom.packages.chatRoomSpring.repository.UserRepository;
import com.chatRoom.packages.chatRoomSpring.response.AuthResponse;
import com.chatRoom.packages.chatRoomSpring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")

public class UserController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserService customUserDetails;

    @Autowired
    private UserService userService;




    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@Valid @RequestBody UserRequest userRequest) {
        String email = userRequest.getMail();
        String password = userRequest.getPassword();
        String fullName = userRequest.getFullName();
        String username = userRequest.getUsername();
        String role = userRequest.getRole();

        // Validation si l'email existe déjà
        User isEmailExist = userRepository.findByMail(email);
        if (isEmailExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(false, "Email is already in use"));
        }

        // Création de l'utilisateur
        User createdUser = new User();
        createdUser.setMail(email);
        createdUser.setFullname(fullName);
        createdUser.setUsername(username);
        createdUser.setRole(role);
        createdUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(createdUser);

        // Génération du token
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);

        // Réponse d'authentification
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        authResponse.setIdUser(createdUser.getUserId());

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }



    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
        String mail = loginRequest.getMail();
        String password = loginRequest.getPassword();

        System.out.println("Attempting login for: " + mail);

        try {
            // Authentication logic
            Authentication authentication = authenticate(mail, password);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Retrieve user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByMail(mail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse(false, "Invalid email or password"));
            }

            // Generate token
            String token = JwtProvider.generateToken(authentication);

            // Create response with role
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Login success");
            authResponse.setJwt(token);
            authResponse.setIdUser(user.getUserId());
            authResponse.setStatus(true);
            authResponse.setRole(user.getRole()); // Add role to the response

            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false, "Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "An error occurred during login"));
        }
    }




    private Authentication authenticate(String username, String password) {

        System.out.println(username+"---++----"+password);

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("Sig in in user details"+ userDetails);

        if(userDetails == null) {
            System.out.println("Sign in details - null" + userDetails);

            throw new BadCredentialsException("Invalid username and password");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())) {
            System.out.println("Sign in userDetails - password mismatch"+userDetails);

            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

    @Autowired
    UserService service;

    @RequestMapping("/api/users")
    public List<User> getUsers(){
        return  service.getUsers();
    }

    @GetMapping("/api/user/{userId}")
    public User getUserById(@PathVariable double userId) {
        return service.getUserById(userId);
    }

    @PostMapping("/api/users/add-user")
    public void addUser( @RequestBody  User user){
        service.addUser(user);
    }
    @PutMapping("/api/users/update-user")
    public void updateUser(User user){
        service.updateUser(user);
    }


    @DeleteMapping("/api/users/delete-user/{userId}")
    public void deleteUser( @PathVariable double userId){
        service.deleteUser(userId);
    }

    @PutMapping("/api/users/update-user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable double id, @RequestBody User user) {
        // Mettez à jour l'ID de l'utilisateur avant de le passer à la méthode du service
        user.setUserId(id); // Assurez-vous que l'ID est bien celui passé dans l'URL
        User updatedUser = service.updateUser(id, user);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/user/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email); // Implémentez cette méthode dans UserService
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }





}
