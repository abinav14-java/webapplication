package com.abinav.webapplication.controller;

import com.abinav.webapplication.exception.AuthenticationException;
import com.abinav.webapplication.exception.ResourceNotFoundException;
import com.abinav.webapplication.exception.ValidationException;
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
            Authentication auth) throws Exception {

        // Authentication check
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            logger.warn("Unauthenticated follow attempt to userId: {}", userId);
            throw new AuthenticationException("You must be logged in to follow a user");
        }

        String email = auth.getName();
        Users currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // Self-follow protection
        if (currentUser.getId().equals(userId)) {
            logger.warn("User {} is trying to follow themselves", email);
            throw new ValidationException("Cannot follow yourself");
        }

        Follow follow = followService.followUser(currentUser.getId(), userId);

        Map<String, Object> response = new HashMap<>();
        response.put("following", true);

        // IMPORTANT: follow can be NULL (already following)
        if (follow == null) {
            response.put("message", "Already following");
            return ResponseEntity.ok(response);
        }

        response.put("message", "User followed successfully");
        response.put("follower", UserMapper.toDTO(follow.getFollower()));
        response.put("following_user", UserMapper.toDTO(follow.getFollowing()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
            Authentication auth) throws Exception {

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AuthenticationException("You must be logged in");
        }

        String email = auth.getName();
        Users currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        // ✅ Idempotent unfollow
        followService.unfollowUser(currentUser.getId(), userId);

        return ResponseEntity.ok(
                Map.of("following", false,
                        "message", "Unfollowed successfully"));
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
            Authentication auth) throws Exception {

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new AuthenticationException("You must be logged in");
        }

        String email = auth.getName();
        Users currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        boolean isFollowing = followService.isFollowing(currentUser.getId(), userId);

        return ResponseEntity.ok(
                Map.of("following", isFollowing));
    }

    /*
     * =========================================================
     * FOLLOWERS & FOLLOWING LISTS
     * =========================================================
     */

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) throws Exception {
        List<Follow> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(
                Map.of(
                        "count", followers.size(),
                        "followers", UserMapper.followersToDTOs(followers)));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long userId) throws Exception {
        List<Follow> following = followService.getFollowing(userId);
        return ResponseEntity.ok(
                Map.of(
                        "count", following.size(),
                        "following", UserMapper.followingToDTOs(following)));
    }

    /*
     * =========================================================
     * COUNTS
     * =========================================================
     */

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<?> getFollowersCount(@PathVariable Long userId) throws Exception {
        long count = followService.getFollowersCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<?> getFollowingCount(@PathVariable Long userId) throws Exception {
        long count = followService.getFollowingCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /*
     * =========================================================
     * USER PROFILE
     * =========================================================
     */

    @GetMapping("/{userId}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) throws Exception {
        Users user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        long followersCount = followService.getFollowersCount(userId);
        long followingCount = followService.getFollowingCount(userId);

        return ResponseEntity.ok(
                Map.of(
                        "user", UserMapper.toDTO(user),
                        "followers_count", followersCount,
                        "following_count", followingCount));
    }
}
