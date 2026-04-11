package com.example.volunthero;

public class ChatModel {
    private String name;
    private String lastMessage;
    private String time;
    private int avatarRes;

    public ChatModel(String name, String lastMessage, String time, int avatarRes) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.avatarRes = avatarRes;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public int getAvatarRes() {
        return avatarRes;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTime(String time) {
        this.time = time;
    }
}