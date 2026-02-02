package com.abinav.webapplication.service;

import java.util.List;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.dto.PostDTO;

public interface PostService {
    Post createPost(Post post);

    Post updatePost(Long postId, Post post);

    void deletePost(Long postId);

    Post getPostById(Long postId);

    // Return DTO for single post (sanitized)
    PostDTO getPostDTOById(Long postId, String currentUserEmail);

    List<PostDTO> getUserPosts(String email);

    List<PostDTO> getAllPosts(String currentUserEmail);
}
