package com.abinav.webapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
