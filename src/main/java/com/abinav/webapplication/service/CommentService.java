package com.abinav.webapplication.service;

import java.util.List;
import com.abinav.webapplication.model.Comment;
import com.abinav.webapplication.model.CommentDTO;

public interface CommentService {
    Comment createComment(Long postId, String userEmail, String content) throws Exception;
    Comment updateComment(Long commentId, String content) throws Exception;
    void deleteComment(Long commentId) throws Exception;
    List<CommentDTO> getPostComments(Long postId) throws Exception;
    long getCommentCount(Long postId) throws Exception;
}
