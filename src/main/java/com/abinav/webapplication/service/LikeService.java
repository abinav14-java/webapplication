package com.abinav.webapplication.service;

import com.abinav.webapplication.model.Like;

public interface LikeService {
    Like toggleLike(Long postId, String userEmail) throws Exception;
    long getLikeCount(Long postId) throws Exception;
    boolean isLikedByUser(Long postId, String userEmail) throws Exception;
    void deleteLike(Long postId, String userEmail) throws Exception;
}
