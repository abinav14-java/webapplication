package com.abinav.webapplication.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.abinav.webapplication.model.Post;
import com.abinav.webapplication.model.Users;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserOrderByCreatedAtDesc(Users user);
    List<Post> findAllByOrderByCreatedAtDesc();
}
