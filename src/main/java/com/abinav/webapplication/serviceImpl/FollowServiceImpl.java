package com.abinav.webapplication.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abinav.webapplication.model.Follow;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.FollowRepository;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.FollowService;

@Service
public class FollowServiceImpl implements FollowService {

        @Autowired
        private FollowRepository followRepository;

        @Autowired
        private UserRepository userRepository;

        /*
         * =========================================================
         * EMAIL-BASED METHODS
         * =========================================================
         */

        @Override
        public void followUser(String followerEmail, String followingEmail) throws Exception {
                if (followerEmail.equals(followingEmail)) {
                        throw new Exception("Cannot follow yourself");
                }

                Users follower = userRepository.findByEmail(followerEmail)
                                .orElseThrow(() -> new Exception("Follower user not found"));

                Users following = userRepository.findByEmail(followingEmail)
                                .orElseThrow(() -> new Exception("User to follow not found"));

                boolean alreadyFollowing = followRepository.existsByFollowerAndFollowing(follower, following);

                if (alreadyFollowing) {
                        return; // ✅ IDEMPOTENT
                }

                Follow follow = new Follow();
                follow.setFollower(follower);
                follow.setFollowing(following);
                followRepository.save(follow);
        }

        @Override
        public void unfollowUser(String followerEmail, String followingEmail) throws Exception {
                Users follower = userRepository.findByEmail(followerEmail)
                                .orElseThrow(() -> new Exception("Follower user not found"));

                Users following = userRepository.findByEmail(followingEmail)
                                .orElseThrow(() -> new Exception("User to unfollow not found"));

                followRepository.findByFollowerAndFollowing(follower, following)
                                .ifPresent(followRepository::delete); // ✅ IDEMPOTENT
        }

        @Override
        public boolean isFollowing(String followerEmail, String followingEmail) throws Exception {
                Users follower = userRepository.findByEmail(followerEmail)
                                .orElseThrow(() -> new Exception("User not found"));

                Users following = userRepository.findByEmail(followingEmail)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.existsByFollowerAndFollowing(follower, following);
        }

        @Override
        public long getFollowerCount(String userEmail) throws Exception {
                Users user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.countByFollowing(user);
        }

        @Override
        public long getFollowingCount(String userEmail) throws Exception {
                Users user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.countByFollower(user);
        }

        @Override
        public List<Users> getFollowers(String userEmail) throws Exception {
                Users user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.findByFollowing(user)
                                .stream()
                                .map(Follow::getFollower)
                                .collect(Collectors.toList());
        }

        @Override
        public List<Users> getFollowing(String userEmail) throws Exception {
                Users user = userRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.findByFollower(user)
                                .stream()
                                .map(Follow::getFollowing)
                                .collect(Collectors.toList());
        }

        /*
         * =========================================================
         * ID-BASED METHODS (USED BY CONTROLLERS)
         * =========================================================
         */

        public Follow followUser(Long followerId, Long followingId) throws Exception {
                if (followerId.equals(followingId)) {
                        throw new Exception("Cannot follow yourself");
                }

                Users follower = userRepository.findById(followerId)
                                .orElseThrow(() -> new Exception("Follower user not found"));

                Users following = userRepository.findById(followingId)
                                .orElseThrow(() -> new Exception("User to follow not found"));

                boolean alreadyFollowing = followRepository.existsByFollowerAndFollowing(follower, following);

                if (alreadyFollowing) {
                        return null; // ✅ IDEMPOTENT
                }

                Follow follow = new Follow();
                follow.setFollower(follower);
                follow.setFollowing(following);
                return followRepository.save(follow);
        }

        public void unfollowUser(Long followerId, Long followingId) throws Exception {
                Users follower = userRepository.findById(followerId)
                                .orElseThrow(() -> new Exception("Follower user not found"));

                Users following = userRepository.findById(followingId)
                                .orElseThrow(() -> new Exception("User to unfollow not found"));

                followRepository.findByFollowerAndFollowing(follower, following)
                                .ifPresent(followRepository::delete); // ✅ IDEMPOTENT
        }

        public boolean isFollowing(Long followerId, Long followingId) throws Exception {
                Users follower = userRepository.findById(followerId)
                                .orElseThrow(() -> new Exception("User not found"));

                Users following = userRepository.findById(followingId)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.existsByFollowerAndFollowing(follower, following);
        }

        public long getFollowersCount(Long userId) throws Exception {
                Users user = userRepository.findById(userId)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.countByFollowing(user);
        }

        public long getFollowingCount(Long userId) throws Exception {
                Users user = userRepository.findById(userId)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.countByFollower(user);
        }

        public List<Follow> getFollowers(Long userId) throws Exception {
                Users user = userRepository.findById(userId)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.findByFollowing(user);
        }

        public List<Follow> getFollowing(Long userId) throws Exception {
                Users user = userRepository.findById(userId)
                                .orElseThrow(() -> new Exception("User not found"));

                return followRepository.findByFollower(user);
        }
}
