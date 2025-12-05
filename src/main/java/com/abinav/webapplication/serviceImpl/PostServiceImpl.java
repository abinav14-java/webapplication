package com.abinav.webapplication.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.PostDTO;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.LikeRepository;
import com.abinav.webapplication.repository.CommentRepository;
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

    @Override
    public PostDTO getPostDTOById(Long postId, String currentUserEmail) throws Exception {
        Post post = getPostById(postId);
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setUserEmail(post.getUser().getEmail());
        dto.setCaption(post.getCaption());
        dto.setImageUrl(post.getImageUrl());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setLikeCount(likeRepository.countByPost(post));
        dto.setCommentCount(commentRepository.countByPost(post));
        try {
            dto.setLikedByCurrentUser(likeRepository.existsByUserAndPost(
                userRepository.findByEmail(currentUserEmail).orElse(null),
                post
            ));
        } catch (Exception e) {
            dto.setLikedByCurrentUser(false);
        }
        return dto;
    }

    @Override
    public List<PostDTO> getUserPosts(String email) throws Exception {
        Users user = userRepository.findByEmail(email)
            .orElseThrow(() -> new Exception("User not found"));
        
        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);
        return convertToDTO(posts, email);
    }

    @Override
    public List<PostDTO> getAllPosts(String currentUserEmail) throws Exception {
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return convertToDTO(posts, currentUserEmail);
    }

    private List<PostDTO> convertToDTO(List<Post> posts, String currentUserEmail) throws Exception {
        return posts.stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setUsername(post.getUser().getUsername());
            dto.setUserEmail(post.getUser().getEmail());
            dto.setCaption(post.getCaption());
            dto.setImageUrl(post.getImageUrl());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setLikeCount(likeRepository.countByPost(post));
            dto.setCommentCount(commentRepository.countByPost(post));
            try {
                dto.setLikedByCurrentUser(likeRepository.existsByUserAndPost(
                    userRepository.findByEmail(currentUserEmail).orElse(null), 
                    post
                ));
            } catch (Exception e) {
                dto.setLikedByCurrentUser(false);
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
