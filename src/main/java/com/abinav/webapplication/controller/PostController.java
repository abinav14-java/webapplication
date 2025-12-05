package com.abinav.webapplication.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.PostDTO;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    // Get current user email from JWT token
    private String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    // Create a new post
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        try {
            String userEmail = getCurrentUserEmail();
            Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new Exception("User not found"));
            
            post.setUser(user);
            Post savedPost = postService.createPost(post);
            // return sanitized DTO for created post
            PostDTO dto = postService.getPostDTOById(savedPost.getId(), userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse("Post created successfully", dto)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Error creating post: " + e.getMessage(), null));
        }
    }

    // Get all posts (for feed)
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        try {
            String userEmail = getCurrentUserEmail();
            List<PostDTO> posts = postService.getAllPosts(userEmail);
            return ResponseEntity.ok(new ApiResponse("Posts retrieved successfully", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Error retrieving posts: " + e.getMessage(), null));
        }
    }

    // Get user's own posts
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserPosts(@PathVariable String email) {
        try {
            List<PostDTO> posts = postService.getUserPosts(email);
            return ResponseEntity.ok(new ApiResponse("User posts retrieved successfully", posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Error retrieving user posts: " + e.getMessage(), null));
        }
    }

    // Get single post
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        try {
            String currentUserEmail = getCurrentUserEmail();
            PostDTO dto = postService.getPostDTOById(postId, currentUserEmail);
            return ResponseEntity.ok(new ApiResponse("Post retrieved successfully", dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse("Post not found: " + e.getMessage(), null));
        }
    }

    // Update post
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post post) {
        try {
            Post updatedPost = postService.updatePost(postId, post);
            String currentUserEmail = getCurrentUserEmail();
            PostDTO dto = postService.getPostDTOById(updatedPost.getId(), currentUserEmail);
            return ResponseEntity.ok(new ApiResponse("Post updated successfully", dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Error updating post: " + e.getMessage(), null));
        }
    }

    // Delete post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok(new ApiResponse("Post deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse("Error deleting post: " + e.getMessage(), null));
        }
    }

    // Helper class for API responses
    static class ApiResponse {
        private String message;
        private Object data;

        public ApiResponse(String message, Object data) {
            this.message = message;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public Object getData() {
            return data;
        }
    }
}
