package com.abinav.webapplication.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.dto.PostDTO;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.PostService;
import com.abinav.webapplication.exception.ResourceNotFoundException;
import com.abinav.webapplication.exception.UnauthorizedException;
import com.abinav.webapplication.exception.ValidationException;

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
        String userEmail = getCurrentUserEmail();
        if (userEmail == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        if (post.getCaption() == null || post.getCaption().trim().isEmpty()) {
            throw new ValidationException("Post caption cannot be empty");
        }

        Users user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        post.setUser(user);
        Post savedPost = postService.createPost(post);
        PostDTO dto = postService.getPostDTOById(savedPost.getId(), userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse("Post created successfully", dto));
    }

    // Get all posts (for feed)
    @GetMapping
    public ResponseEntity<ApiResponse> getAllPosts() {
        String userEmail = getCurrentUserEmail();
        List<PostDTO> posts = postService.getAllPosts(userEmail);
        return ResponseEntity.ok(
                new ApiResponse("Posts retrieved successfully", posts));
    }

    // Get user's own posts
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserPosts(@PathVariable String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        List<PostDTO> posts = postService.getUserPosts(email);
        return ResponseEntity.ok(new ApiResponse("User posts retrieved successfully", posts));
    }

    // Get single post
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        if (postId == null || postId <= 0) {
            throw new ValidationException("Valid post ID is required");
        }
        String currentUserEmail = getCurrentUserEmail();
        PostDTO dto = postService.getPostDTOById(postId, currentUserEmail);
        return ResponseEntity.ok(new ApiResponse("Post retrieved successfully", dto));
    }

    // Update post
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post post) {
        String currentUserEmail = getCurrentUserEmail();

        if (currentUserEmail == null) {
            throw new UnauthorizedException("You must be logged in to edit posts");
        }

        Post existingPost = postService.getPostById(postId);
        if (existingPost == null) {
            throw new ResourceNotFoundException("Post with ID " + postId + " not found");
        }

        if (!existingPost.getUser().getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedException("You can only edit your own posts");
        }

        Post updatedPost = postService.updatePost(postId, post);
        PostDTO dto = postService.getPostDTOById(updatedPost.getId(), currentUserEmail);
        return ResponseEntity.ok(new ApiResponse("Post updated successfully", dto));
    }

    // Delete post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        String currentUserEmail = getCurrentUserEmail();

        if (currentUserEmail == null) {
            throw new UnauthorizedException("You must be logged in to delete posts");
        }

        Post post = postService.getPostById(postId);
        if (post == null) {
            throw new ResourceNotFoundException("Post with ID " + postId + " not found");
        }

        if (!post.getUser().getEmail().equals(currentUserEmail)) {
            throw new UnauthorizedException("You can only delete your own posts");
        }

        postService.deletePost(postId);
        return ResponseEntity.ok(new ApiResponse("Post deleted successfully", null));
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
