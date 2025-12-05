package com.abinav.webapplication.service;

import java.util.List;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.PostDTO;
import com.abinav.webapplication.model.Users;

public interface PostService {
    Post createPost(Post post) throws Exception;
    Post updatePost(Long postId, Post post) throws Exception;
    void deletePost(Long postId) throws Exception;
    Post getPostById(Long postId) throws Exception;
    // Return DTO for single post (sanitized)
    PostDTO getPostDTOById(Long postId, String currentUserEmail) throws Exception;
    List<PostDTO> getUserPosts(String email) throws Exception;
    List<PostDTO> getAllPosts(String currentUserEmail) throws Exception;
}
