package com.example.week10_lab6;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ChatRoomDao {
    @Insert
    void insertMessage(ChatMessage chatMessage);

    @Delete
    void deleteMessage(ChatMessage chatMessage);

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    List<ChatMessage> getAllMessages();
}

