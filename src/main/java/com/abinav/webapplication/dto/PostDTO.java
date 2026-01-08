package com.abinav.webapplication.dto;

import java.time.LocalDateTime;

public class PostDTO {

    private Long id;
    private String username;
    private String userEmail;
    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;
    private long likeCount;
    private long commentCount;
    private boolean likedByCurrentUser;
    private Long authorId;
    private boolean followingAuthor;

    public PostDTO() {
    }

    public PostDTO(Long id, String username, String userEmail, String caption,
            String imageUrl, LocalDateTime createdAt,
            long likeCount, long commentCount, boolean likedByCurrentUser) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCaption() {
        return caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    // ---- Setters ----

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public boolean isFollowingAuthor() {
        return followingAuthor;
    }

    public void setFollowingAuthor(boolean followingAuthor) {
        this.followingAuthor = followingAuthor;
    }
}
