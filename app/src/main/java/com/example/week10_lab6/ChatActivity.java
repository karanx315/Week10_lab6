package com.example.week10_lab6;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.lifecycleScope;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {

    private ChatDatabase chatDatabase;
    private ChatRoomDao chatRoomDao;
    private ChatMessage deletedMessage;
    private EditText messageInput;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatDatabase = ChatDatabase.getDatabase(this);
        chatRoomDao = chatDatabase.chatRoomDao();
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageInput.getText().toString().trim();
                if (!text.isEmpty()) {
                    addMessage(text);  // Call function to add the message to the database
                    messageInput.setText("");  // Clear input after sending
                }
            }

            private void addMessage(String text) {
            }
        });
    }


    private void deleteMessage(final ChatMessage chatMessage) {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to delete this message?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        chatRoomDao.deleteMessage(chatMessage);
                        runOnUiThread(() -> showUndoSnackbar(chatMessage));
                    });
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showUndoSnackbar(final ChatMessage chatMessage) {
        deletedMessage = chatMessage;
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, "Message deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", v -> undoDeleteMessage())
                .show();
    }

    private void undoDeleteMessage() {
        if (deletedMessage != null) {
            Executors.newSingleThreadExecutor().execute(() -> {
                chatRoomDao.insertMessage(deletedMessage);
                deletedMessage = null; // Clear reference after undo
            });
        }}private void addMessage(String text) {
    long timestamp = System.currentTimeMillis();
    ChatMessage chatMessage = new ChatMessage(text, timestamp);

    // Insert the message into the database asynchronously
    Executors.newSingleThreadExecutor().execute(() -> chatRoomDao.insertMessage(chatMessage));
}
}

