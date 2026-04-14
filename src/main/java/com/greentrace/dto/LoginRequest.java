package com.greentrace.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Email is required")
    private String email;      // Changed from "username" to "email"

    @NotBlank(message = "Password is required")
    private String password;

    // Constructors
    public LoginRequest() {}

    // Getters and Setters
    public String getEmail() { return email; }      // Changed
    public void setEmail(String email) { this.email = email; }  // Changed

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}