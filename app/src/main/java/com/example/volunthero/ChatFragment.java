package com.example.volunthero;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView rvChats;
    private ChatAdapter adapter;
    private List<ChatModel> chatList;

    public ChatFragment() {
        super(R.layout.fragment_chats);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChats = view.findViewById(R.id.rvChats);
        FloatingActionButton btnAdd = view.findViewById(R.id.btnNewChat);

        chatList = new ArrayList<>();
        chatList.add(new ChatModel("UWC Dilijan Support", "Твоя заявка одобрена!", "10:45", R.drawable.logoapp));
        chatList.add(new ChatModel("Команда VoluntHero", "Az, когда дедлайн по коду?", "Вчера", R.drawable.logoapp));

        adapter = new ChatAdapter(chatList);
        rvChats.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChats.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            chatList.add(0, new ChatModel("Новый контакт", "Привет! Я хочу предложить...", "Сейчас", R.drawable.logoapp));

            adapter.notifyItemInserted(0);

            rvChats.scrollToPosition(0);
        });
    }
}