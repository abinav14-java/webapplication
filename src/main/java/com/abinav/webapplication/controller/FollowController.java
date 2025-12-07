package com.abinav.webapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.abinav.webapplication.model.Follow;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.utility.UserMapper;
import com.abinav.webapplication.serviceImpl.FollowServiceImpl;
import com.abinav.webapplication.serviceImpl.UserServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/users")
public class FollowController {

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    /**
     * Follow a user
     * POST /api/users/{userId}/follow
     */
    @PostMapping("/{userId}/follow")
public ResponseEntity<?> followUser(@PathVariable Long userId, Authentication auth) {
    try {
        // 1. Check if user is authenticated

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            logger.warn("Unauthenticated follow attempt to userId: {}", userId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You must be logged in to follow a user");
        }

        String email = auth.getName(); // this is safe now
        Users currentUser = userService.findByEmail(email).orElse(null);

        if (currentUser == null) {
            logger.warn("Authenticated user not found in database: {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        if (currentUser.getId().equals(userId)) {
            logger.warn("User {} is trying to follow themselves", email);
            return ResponseEntity.badRequest().body("Cannot follow yourself");
        }

        Follow follow = followService.followUser(currentUser.getId(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User followed successfully");
        response.put("follower", UserMapper.toDTO(follow.getFollower()));
        response.put("following", UserMapper.toDTO(follow.getFollowing()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
        logger.error("Error while following user: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
}


    /**
     * Unfollow a user
     * DELETE /api/users/{userId}/unfollow
     */
    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable Long userId, Authentication auth) {
        try {
            String email = auth.getName();
            Users currentUser = userService.findByEmail(email).orElse(null);
            if (currentUser == null) {
                logger.warn("Authenticated user not found in database: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            followService.unfollowUser(currentUser.getId(), userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User unfollowed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while unfollowing user: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get followers of a user
     * GET /api/users/{userId}/followers
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
        try {
            List<Follow> followers = followService.getFollowers(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("count", followers.size());
            response.put("followers", UserMapper.followersToDTOs(followers));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting followers: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get users that a user is following
     * GET /api/users/{userId}/following
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
        try {
            List<Follow> following = followService.getFollowing(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("count", following.size());
            response.put("following", UserMapper.followingToDTOs(following));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting following: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get followers count
     * GET /api/users/{userId}/followers/count
     */
    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> getFollowersCount(@PathVariable Long userId) {
        try {
            long count = followService.getFollowersCount(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting followers count: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get following count
     * GET /api/users/{userId}/following/count
     */
    @GetMapping("/{userId}/following/count")
    public ResponseEntity<?> getFollowingCount(@PathVariable Long userId) {
        try {
            long count = followService.getFollowingCount(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while getting following count: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Check if current user follows another user
     * GET /api/users/{userId}/is-following
     */
    @GetMapping("/{userId}/is-following")
    public ResponseEntity<?> isFollowing(@PathVariable Long userId, Authentication auth) {
        try {
            String email = auth.getName();
            Users currentUser = userService.findByEmail(email).orElse(null);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            boolean isFollowing = followService.isFollowing(currentUser.getId(), userId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("following", isFollowing);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error while checking following status: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get user profile with stats
     * GET /api/users/{userId}/profile
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            Users user = userService.findById(userId).orElse(null);
            if (user == null) {
                logger.warn("User profile not found for userId: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            long followersCount = followService.getFollowersCount(userId);
            long followingCount = followService.getFollowingCount(userId);

            Map<String, Object> profile = new HashMap<>();
            profile.put("user", UserMapper.toDTO(user));
            profile.put("followers_count", followersCount);
            profile.put("following_count", followingCount);

            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            logger.error("Error while getting user profile: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
