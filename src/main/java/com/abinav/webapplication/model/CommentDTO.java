package com.abinav.webapplication.model;

import java.time.LocalDateTime;

public class CommentDTO {

    private Long id;
    private String username;
    private String userEmail;
    private String content;
    private LocalDateTime createdAt;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String username, String userEmail, String content, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
