package algonquin.cst2335.parm0080.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;
    @ColumnInfo(name="SendOrReceive")
    protected boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton)
    {
      this.message = message;
        this.timeSent= timeSent;
        this.isSentButton= isSentButton;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean getIsSentButton(){
        return isSentButton;
    }


}
