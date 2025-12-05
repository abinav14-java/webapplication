package com.abinav.webapplication.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.abinav.webapplication.model.Follow;
import com.abinav.webapplication.model.Users;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(Users follower, Users following);
    List<Follow> findByFollower(Users follower);
    List<Follow> findByFollowing(Users following);
    long countByFollower(Users follower);
    long countByFollowing(Users following);
    boolean existsByFollowerAndFollowing(Users follower, Users following);
}
