package algonquin.cst2335.parm0080.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Dao;
import androidx.room.Room;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.parm0080.R;
import algonquin.cst2335.parm0080.data.ChatMessage;
import algonquin.cst2335.parm0080.data.ChatMessageDAO;
import algonquin.cst2335.parm0080.data.ChatRoomViewModel;
import algonquin.cst2335.parm0080.data.MessageDatabase;
import algonquin.cst2335.parm0080.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.parm0080.databinding.ReceiveMessageBinding;
import algonquin.cst2335.parm0080.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    ChatRoomViewModel chatModel ;
    private RecyclerView.Adapter myAdapter;
    ArrayList<ChatMessage> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{
        });
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        super.onCreate(savedInstanceState);

        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            messages = new ArrayList<>();
            chatModel.messages.postValue(messages);
        }
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.sendbutton.setOnClickListener(click->{
            String messagetext = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(messagetext,currentDateandTime,true);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });
        binding.receiveButton.setOnClickListener(click->{
            String messagetext = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(messagetext,currentDateandTime,false);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.textInput.setText("");
        });
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding sendBinding = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(sendBinding.getRoot());
                } else {
                    ReceiveMessageBinding receiveBinding = ReceiveMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(receiveBinding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
            @Override
            public int getItemViewType(int position){
                ChatMessage chatMessage = messages.get(position);
                if (chatMessage.getIsSentButton()) {
                    return 0; // Sent button view type
                } else {
                    return 1; // Receive button view type
                }
            }
        });
    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk ->{

            int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage("Do you want to delete this message: " + messageText.getText())
                        .setTitle("Question: ")
                        .setNegativeButton("NO", (dialog, cl) -> { })
                        .setPositiveButton("Yes", (dialog, cl) -> {

                   ChatMessage message = messages.get(position);
                    messages.remove(position);
                    myAdapter.notifyItemInserted(position);

                    Snackbar.make(messageText,"You Deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {


                                messages.add(position, removedMessage);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                }).create().show();


            });
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }

        private int getAbsoluteAdapterPosition() {
            return 0;
        }
    }
}