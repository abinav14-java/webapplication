package com.abinav.webapplication.controller;

import com.abinav.webapplication.model.Follow;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.serviceImpl.FollowServiceImpl;
import com.abinav.webapplication.serviceImpl.UserServiceImpl;
import com.abinav.webapplication.utility.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    /*
     * =========================================================
     * FOLLOW USER
     * POST /api/users/{userId}/follow
     * =========================================================
     */

    @PostMapping("/{userId}/follow")
    public ResponseEntity<?> followUser(
            @PathVariable Long userId,
            Authentication auth) {

        try {
            // 🔐 Authentication check
            if (auth == null || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal())) {
                logger.warn("Unauthenticated follow attempt to userId: {}", userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You must be logged in to follow a user");
            }

            String email = auth.getName();
            Users currentUser = userService.findByEmail(email)
                    .orElse(null);

            if (currentUser == null) {
                logger.warn("Authenticated user not found in database: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            // 🚫 Self-follow protection
            if (currentUser.getId().equals(userId)) {
                logger.warn("User {} is trying to follow themselves", email);
                return ResponseEntity.badRequest()
                        .body("Cannot follow yourself");
            }

            Follow follow = followService
                    .followUser(currentUser.getId(), userId);

            Map<String, Object> response = new HashMap<>();
            response.put("following", true);

            // ✅ IMPORTANT: follow can be NULL (already following)
            if (follow == null) {
                response.put("message", "Already following");
                return ResponseEntity.ok(response);
            }

            response.put("message", "User followed successfully");
            response.put("follower", UserMapper.toDTO(follow.getFollower()));
            response.put("following_user", UserMapper.toDTO(follow.getFollowing()));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            logger.error("Error while following user", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    /*
     * =========================================================
     * UNFOLLOW USER
     * DELETE /api/users/{userId}/unfollow
     * =========================================================
     */

    @DeleteMapping("/{userId}/unfollow")
    public ResponseEntity<?> unfollowUser(
            @PathVariable Long userId,
            Authentication auth) {

        try {
            if (auth == null || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You must be logged in");
            }

            String email = auth.getName();
            Users currentUser = userService.findByEmail(email)
                    .orElse(null);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            // ✅ Idempotent unfollow
            followService.unfollowUser(currentUser.getId(), userId);

            return ResponseEntity.ok(
                    Map.of("following", false,
                            "message", "Unfollowed successfully"));

        } catch (Exception e) {
            logger.error("Error while unfollowing user", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    /*
     * =========================================================
     * CHECK FOLLOW STATUS
     * GET /api/users/{userId}/is-following
     * =========================================================
     */

    @GetMapping("/{userId}/is-following")
    public ResponseEntity<?> isFollowing(
            @PathVariable Long userId,
            Authentication auth) {

        try {
            if (auth == null || !auth.isAuthenticated()
                    || "anonymousUser".equals(auth.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You must be logged in");
            }

            String email = auth.getName();
            Users currentUser = userService.findByEmail(email)
                    .orElse(null);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found");
            }

            boolean isFollowing = followService.isFollowing(currentUser.getId(), userId);

            return ResponseEntity.ok(
                    Map.of("following", isFollowing));

        } catch (Exception e) {
            logger.error("Error while checking follow status", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    /*
     * =========================================================
     * FOLLOWERS & FOLLOWING LISTS
     * =========================================================
     */

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
        try {
            List<Follow> followers = followService.getFollowers(userId);
            return ResponseEntity.ok(
                    Map.of(
                            "count", followers.size(),
                            "followers", UserMapper.followersToDTOs(followers)));
        } catch (Exception e) {
            logger.error("Error while getting followers", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId) {
        try {
            List<Follow> following = followService.getFollowing(userId);
            return ResponseEntity.ok(
                    Map.of(
                            "count", following.size(),
                            "following", UserMapper.followingToDTOs(following)));
        } catch (Exception e) {
            logger.error("Error while getting following", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    /*
     * =========================================================
     * COUNTS
     * =========================================================
     */

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> getFollowersCount(@PathVariable Long userId) {
        try {
            long count = followService.getFollowersCount(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            logger.error("Error while getting followers count", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<?> getFollowingCount(@PathVariable Long userId) {
        try {
            long count = followService.getFollowingCount(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            logger.error("Error while getting following count", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }

    /*
     * =========================================================
     * USER PROFILE
     * =========================================================
     */

    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {
        try {
            Users user = userService.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            long followersCount = followService.getFollowersCount(userId);
            long followingCount = followService.getFollowingCount(userId);

            return ResponseEntity.ok(
                    Map.of(
                            "user", UserMapper.toDTO(user),
                            "followers_count", followersCount,
                            "following_count", followingCount));
        } catch (Exception e) {
            logger.error("Error while getting user profile", e);
            return ResponseEntity.badRequest()
                    .body("Error: " + e.getMessage());
        }
    }
}
