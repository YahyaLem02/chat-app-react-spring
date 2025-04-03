package com.chatRoom.packages.chatRoomSpring.repository;

import com.chatRoom.packages.chatRoomSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Double> {

    User findByMail(String mail);

}
