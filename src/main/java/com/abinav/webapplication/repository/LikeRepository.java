package com.abinav.webapplication.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.abinav.webapplication.model.Like;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.Users;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(Users user, Post post);
    List<Like> findByPost(Post post);
    long countByPost(Post post);
    boolean existsByUserAndPost(Users user, Post post);
}
