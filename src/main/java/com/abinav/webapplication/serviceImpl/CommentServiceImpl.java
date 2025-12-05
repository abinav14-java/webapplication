package com.abinav.webapplication.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.abinav.webapplication.model.Comment;
import com.abinav.webapplication.model.CommentDTO;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.Users;
import com.abinav.webapplication.repository.CommentRepository;
import com.abinav.webapplication.repository.PostRepository;
import com.abinav.webapplication.repository.UserRepository;
import com.abinav.webapplication.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment createComment(Long postId, String userEmail, String content) throws Exception {
        Users user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(content);
        
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long commentId, String content) throws Exception {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new Exception("Comment not found"));
        
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new Exception("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDTO> getPostComments(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        List<Comment> comments = commentRepository.findByPostOrderByCreatedAtDesc(post);
        return comments.stream().map(comment -> {
            CommentDTO dto = new CommentDTO();
            dto.setId(comment.getId());
            dto.setUsername(comment.getUser().getUsername());
            dto.setUserEmail(comment.getUser().getEmail());
            dto.setContent(comment.getContent());
            dto.setCreatedAt(comment.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public long getCommentCount(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        return commentRepository.countByPost(post);
    }

    // New methods for controller (returns Comment objects, not CommentDTO)
    public Comment addComment(Long postId, Long userId, String text) throws Exception {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setContent(text);
        
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Long userId) throws Exception {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new Exception("Comment not found"));
        
        if (!comment.getUser().getId().equals(userId)) {
            throw new Exception("Not authorized to delete this comment");
        }
        
        commentRepository.delete(comment);
    }

    public List<Comment> getCommentsList(Long postId) throws Exception {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new Exception("Post not found"));
        
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    public Comment updateComment(Long commentId, Long userId, String text) throws Exception {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new Exception("Comment not found"));
        
        if (!comment.getUser().getId().equals(userId)) {
            throw new Exception("Not authorized to update this comment");
        }
        
        comment.setContent(text);
        return commentRepository.save(comment);
    }
}
