package com.example.volunthero;

public class EventModel {
    private String title;
    private String category;
    private String location;
    private int imageResId;

    public EventModel() {}

    public EventModel(String title, String category, String location, int imageResId) {
        this.title = title;
        this.category = category;
        this.location = location;
        this.imageResId = imageResId;
    }

    //геттеры (для получения данных в Адаптере)
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public int getImageResId() { return imageResId; }

    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setLocation(String location) { this.location = location; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
}