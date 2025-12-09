package com.abinav.webapplication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.abinav.webapplication.model.Comment;
import com.abinav.webapplication.model.Like;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.utility.UserMapper;
import com.abinav.webapplication.serviceImpl.CommentServiceImpl;
import com.abinav.webapplication.serviceImpl.LikeServiceImpl;
import com.abinav.webapplication.serviceImpl.UserServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class LikeCommentController {

    @Autowired
    private LikeServiceImpl likeService;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(LikeCommentController.class);

    // ========== LIKES ==========

    /**
     * Like a post
     * POST /api/posts/{postId}/like
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId, Authentication auth) {
        try {
            String email = auth.getName();
            Users user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                logger.warn("Authenticated user not found in database: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Like like = likeService.likePost(postId, user.getId());
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Post liked successfully");
            java.util.Map<String, Object> likeMap = new java.util.HashMap<>();
            likeMap.put("id", like.getId());
            likeMap.put("createdAt", like.getCreatedAt() != null ? like.getCreatedAt().toString() : null);
            likeMap.put("user", UserMapper.toDTO(like.getUser()));
            response.put("like", likeMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while liking post: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Unlike a post
     * DELETE /api/posts/{postId}/unlike
     */
    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId, Authentication auth) {
        try {
            String email = auth.getName();
            Users user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                logger.warn("Authenticated user not found in database: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            likeService.unlikePost(postId, user.getId());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Post unliked successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while unliking post: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get likes count for a post
     * GET /api/posts/{postId}/likes/count
     */
    @GetMapping("/{postId}/likes/count")
    public ResponseEntity<?> getLikesCount(@PathVariable Long postId) {
        try {
            long count = likeService.getLikesCount(postId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all likes for a post
     * GET /api/posts/{postId}/likes
     */
    @GetMapping("/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable Long postId) {
        try {
            List<Like> likes = likeService.getPostLikes(postId);
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("count", likes.size());
            response.put("likes", UserMapper.likesToDTOs(likes));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Check if user liked a post
     * GET /api/posts/{postId}/liked-by-user
     */
    @GetMapping("/{postId}/liked-by-user")
    public ResponseEntity<?> isLikedByUser(@PathVariable Long postId, Authentication auth) {
        try {
            String email = auth.getName();
            Users user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            boolean isLiked = likeService.isLikedByUser(postId, user.getId());
            Map<String, Boolean> response = new HashMap<>();
            response.put("liked", isLiked);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // ========== COMMENTS ==========

    /**
     * Add comment to a post
     * POST /api/posts/{postId}/comments
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody Map<String, String> request,
                                        Authentication auth) {
        try {
            String text = request.get("text");
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Comment text is required");
            }

            String email = auth.getName();
            Users user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Comment comment = commentService.addComment(postId, user.getId(), text);
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Comment added successfully");
            // sanitized comment map
            Object cm = UserMapper.commentsToMaps(java.util.Arrays.asList(comment)).get(0);
            response.put("comment", cm);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Delete comment
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                           Authentication auth) {
        try {
            String email = auth.getName();
            Users user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            commentService.deleteComment(commentId, user.getId());
            java.util.Map<String, String> response = new java.util.HashMap<>();
            response.put("message", "Comment deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get all comments for a post
     * GET /api/posts/{postId}/comments
     */
    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsList(postId);
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("count", comments.size());
            response.put("comments", UserMapper.commentsToMaps(comments));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Update comment
     * PUT /api/comments/{commentId}
     */
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
                                          @RequestBody Map<String, String> request, Authentication auth) {
        try {
            String text = request.get("text");
            if (text == null || text.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Comment text is required");
            }

            String email = auth.getName();
            Users user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            Comment comment = commentService.updateComment(commentId, user.getId(), text);
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("message", "Comment updated successfully");
            Object cm = UserMapper.commentsToMaps(java.util.Arrays.asList(comment)).get(0);
            response.put("comment", cm);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
