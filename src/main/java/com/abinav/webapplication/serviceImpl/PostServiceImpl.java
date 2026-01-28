package com.abinav.webapplication.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.dto.PostDTO;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.CommentRepository;
import com.abinav.webapplication.repository.FollowRepository;
import com.abinav.webapplication.repository.LikeRepository;
import com.abinav.webapplication.repository.PostRepository;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.PostService;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    // 🔥 NEW
    @Autowired
    private FollowRepository followRepository;

    /*
     * =========================================================
     * CRUD
     * =========================================================
     */

    @Override
    public Post createPost(Post post) throws Exception {
        if (post.getUser() == null) {
            throw new Exception("User information is required");
        }
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Long postId, Post updatedPost) throws Exception {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception("Post not found"));

        if (updatedPost.getCaption() != null) {
            post.setCaption(updatedPost.getCaption());
        }
        if (updatedPost.getImageUrl() != null) {
            post.setImageUrl(updatedPost.getImageUrl());
        }

        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception("Post not found"));
        postRepository.delete(post);
    }

    @Override
    public Post getPostById(Long postId) throws Exception {
        return postRepository.findById(postId)
                .orElseThrow(() -> new Exception("Post not found"));
    }

    /*
     * =========================================================
     * SINGLE POST DTO
     * =========================================================
     */

    @Override
    public PostDTO getPostDTOById(Long postId, String currentUserEmail)
            throws Exception {

        Post post = getPostById(postId);

        Users author = post.getUser();

        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setAuthorId(author.getId()); // 🔥 NEW
        dto.setUsername(author.getUsername());
        dto.setUserEmail(author.getEmail());
        dto.setCaption(post.getCaption());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(likeRepository.countByPost(post));
        dto.setCommentCount(commentRepository.countByPost(post));

        // If no user is logged in, return default values
        if (currentUserEmail == null) {
            dto.setLikedByCurrentUser(false);
            dto.setFollowingAuthor(false);
            return dto;
        }

        Users currentUser = userRepository.findByEmail(currentUserEmail)
                .orElse(null);

        if (currentUser == null) {
            dto.setLikedByCurrentUser(false);
            dto.setFollowingAuthor(false);
            return dto;
        }

        // liked
        dto.setLikedByCurrentUser(
                likeRepository.existsByUserAndPost(currentUser, post));

        // 🔥 FOLLOW STATE
        dto.setFollowingAuthor(
                followRepository.existsByFollowerAndFollowing(currentUser, author));

        return dto;
    }

    /*
     * =========================================================
     * USER POSTS
     * =========================================================
     */

    @Override
    public List<PostDTO> getUserPosts(String email) throws Exception {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return convertToDTO(posts, email);
    }

    /*
     * =========================================================
     * FEED POSTS
     * =========================================================
     */

    @Override
    public List<PostDTO> getAllPosts(String currentUserEmail)
            throws Exception {

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        return convertToDTO(posts, currentUserEmail);
    }

    /*
     * =========================================================
     * DTO CONVERSION (CORE LOGIC)
     * =========================================================
     */

    private List<PostDTO> convertToDTO(
            List<Post> posts,
            String currentUserEmail) throws Exception {

        // Allow null currentUserEmail - just return posts with default liked/following
        // state
        Users currentUser = null;
        if (currentUserEmail != null) {
            currentUser = userRepository.findByEmail(currentUserEmail).orElse(null);
        }

        final Users finalCurrentUser = currentUser;

        return posts.stream().map(post -> {

            Users author = post.getUser();

            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setAuthorId(author.getId()); // 🔥 NEW
            dto.setUsername(author.getUsername());
            dto.setUserEmail(author.getEmail());
            dto.setCaption(post.getCaption());
            dto.setImageUrl(post.getImageUrl());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setLikeCount(likeRepository.countByPost(post));
            dto.setCommentCount(commentRepository.countByPost(post));

            // If no current user, default to not liked and not following
            if (finalCurrentUser == null) {
                dto.setLikedByCurrentUser(false);
                dto.setFollowingAuthor(false);
            } else {
                // liked
                dto.setLikedByCurrentUser(
                        likeRepository.existsByUserAndPost(finalCurrentUser, post));

                // 🔥 FOLLOW STATE (KEY FIX)
                dto.setFollowingAuthor(
                        followRepository.existsByFollowerAndFollowing(
                                finalCurrentUser, author));
            }

            return dto;

        }).collect(Collectors.toList());
    }
}
