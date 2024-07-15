package com.code.foodapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.adapters.MessageAdapter;
import com.code.foodapp.models.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {
    private static final String TAG = "ChatFragment";
    private static final String SERVER_IP = "172.16.1.5"; // Update this to your server's IP
    private static final int SERVER_PORT = 8000;

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageAdapter messageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String nameOfUser;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        sendButton = view.findViewById(R.id.sendButton);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", requireContext().MODE_PRIVATE);
        nameOfUser = sharedPreferences.getString("user_fullname", "");

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        messageAdapter = new MessageAdapter(messageList, nameOfUser);
        recyclerView.setAdapter(messageAdapter);

        connectToServer();

        sendButton.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            if (!message.isEmpty() && out != null) {
                String sender = nameOfUser; // Set the sender name as needed
                String time = getCurrentTime();
                String fullMessage = sender + "|" + message + "|" + time;
                new Thread(() -> {
                    out.println(fullMessage);
                    requireActivity().runOnUiThread(() -> {
                        addMessage(sender, message, time);
                        editTextMessage.setText("");
                    });
                }).start();
            }
        });

        return view;
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                requireActivity().runOnUiThread(() -> addMessage(nameOfUser, "Connected to server", getCurrentTime()));

                new Thread(() -> {
                    try {
                        String receivedMessage;
                        while ((receivedMessage = in.readLine()) != null) {
                            String[] parts = receivedMessage.split("\\|");
                            if (parts.length >= 3) {
                                String sender = parts[0];
                                String message = parts[1];
                                String time = parts[2];
                                requireActivity().runOnUiThread(() -> addMessage(sender, message, time));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() -> addMessage(nameOfUser, "Failed to connect to server", getCurrentTime()));
            }
        }).start();
    }

    private void disconnectFromServer() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMessage(String sender, String message, String time) {
        messageList.add(new Message(sender, message, time));
        requireActivity().runOnUiThread(() -> {
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        });
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectFromServer();
    }
}
