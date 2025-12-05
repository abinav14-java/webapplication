package com.abinav.webapplication.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.abinav.webapplication.model.Comment;
import com.abinav.webapplication.model.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
    long countByPost(Post post);
}
