package com.greentrace.dto;

import java.util.List;

public class UpdateProfileRequest {
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private List<String> dietaryPreferences;
    private String skillLevel;
    private String bio;
    private String profilePic;

    // Constructors
    public UpdateProfileRequest() {}

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<String> getDietaryPreferences() { return dietaryPreferences; }
    public void setDietaryPreferences(List<String> dietaryPreferences) { this.dietaryPreferences = dietaryPreferences; }

    public String getSkillLevel() { return skillLevel; }
    public void setSkillLevel(String skillLevel) { this.skillLevel = skillLevel; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
}