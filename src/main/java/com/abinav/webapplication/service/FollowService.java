package com.abinav.webapplication.service;

import java.util.List;
import com.abinav.webapplication.model.Users;

public interface FollowService {
    void followUser(String followerEmail, String followingEmail) throws Exception;

    void unfollowUser(String followerEmail, String followingEmail) throws Exception;

    boolean isFollowing(String followerEmail, String followingEmail) throws Exception;

    long getFollowerCount(String userEmail) throws Exception;

    long getFollowingCount(String userEmail) throws Exception;

    List<Users> getFollowers(String userEmail) throws Exception;

    List<Users> getFollowing(String userEmail) throws Exception;
}
