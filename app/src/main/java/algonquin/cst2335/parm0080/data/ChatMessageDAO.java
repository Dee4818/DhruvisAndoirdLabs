package algonquin.cst2335.parm0080.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Collection;
import java.util.List;

@Dao
public interface ChatMessageDAO {
    @Insert
    public long insertMessage(ChatMessage message);



    @Query("Select * from ChatMessage")
    public List<ChatMessage> getAllMessages();

    @Delete
    void deleteMesssge(ChatMessage message);
}

