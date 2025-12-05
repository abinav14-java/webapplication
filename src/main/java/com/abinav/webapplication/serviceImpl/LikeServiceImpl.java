package com.abinav.webapplication.serviceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abinav.webapplication.model.Like;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.LikeRepository;
import com.abinav.webapplication.repository.PostRepository;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.LikeService;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Like toggleLike(Long postId, String userEmail) throws Exception {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return null; // Indicates like was removed
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setPost(post);
            return likeRepository.save(newLike);
        }
    }

    @Override
    public long getLikeCount(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        return likeRepository.countByPost(post);
    }

    @Override
    public boolean isLikedByUser(Long postId, String userEmail) throws Exception {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        return likeRepository.existsByUserAndPost(user, post);
    }

    @Override
    public void deleteLike(Long postId, String userEmail) throws Exception {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        Optional<Like> like = likeRepository.findByUserAndPost(user, post);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        }
    }

    // New methods for controller
    public Like likePost(Long postId, Long userId) throws Exception {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
        
        if (existingLike.isPresent()) {
            return existingLike.get();
        }
        
        Like newLike = new Like();
        newLike.setUser(user);
        newLike.setPost(post);
        return likeRepository.save(newLike);
    }

    public void unlikePost(Long postId, Long userId) throws Exception {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        Optional<Like> like = likeRepository.findByUserAndPost(user, post);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        }
    }

    public long getLikesCount(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        return likeRepository.countByPost(post);
    }

    public List<Like> getPostLikes(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        return likeRepository.findByPost(post);
    }

    public boolean isLikedByUser(Long postId, Long userId) throws Exception {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        return likeRepository.existsByUserAndPost(user, post);
    }
}