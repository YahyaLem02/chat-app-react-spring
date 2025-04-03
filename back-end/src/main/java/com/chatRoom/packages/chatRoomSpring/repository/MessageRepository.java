package com.chatRoom.packages.chatRoomSpring.repository;

import com.chatRoom.packages.chatRoomSpring.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Double> {
    @Query("SELECT m FROM Message m WHERE m.room.roomId = :roomId")
    List<Message> findMessagesByRoomId(@Param("roomId") Double roomId);

}
