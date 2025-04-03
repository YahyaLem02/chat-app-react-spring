package com.chatRoom.packages.chatRoomSpring.repository;

import com.chatRoom.packages.chatRoomSpring.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Double> {
    @Query("SELECT r FROM Room r " +
            "LEFT JOIN FETCH r.messages m " +
            "LEFT JOIN FETCH m.user " + // Inclure les utilisateurs associés aux messages
            "LEFT JOIN FETCH r.users u " +
            "WHERE r.roomId = :roomId")
    Room findRoomWithMessagesAndUsersById(double roomId);
    Room findByCode(String code);
}