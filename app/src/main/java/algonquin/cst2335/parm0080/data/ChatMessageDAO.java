package algonquin.cst2335.parm0080.data;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Collection;

public interface ChatMessageDAO {
    @Insert
    public long insertMessage(ChatMessageDAO m);

    @Query("Select * from ChatMessage")
    public Collection<? extends ChatMessage> getAllMessages();

    @Delete
    void deleteMessage(ChatMessageDAO m);
}
