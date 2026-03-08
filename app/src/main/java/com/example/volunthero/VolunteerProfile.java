package com.example.volunthero;

public class VolunteerProfile {
    private String name;
    private String skills;
    private String bio;

    public VolunteerProfile() {}

    public VolunteerProfile(String name, String skills, String bio) {
        this.name = name;
        this.skills = skills;
        this.bio = bio;
    }

    //геттеры нужны
    public String getName() { return name; }
    public String getSkills() { return skills; }
    public String getBio() { return bio; }
}