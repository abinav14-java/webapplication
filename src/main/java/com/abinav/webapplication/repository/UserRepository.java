package com.abinav.webapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abinav.webapplication.model.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email) throws Exception;

	Optional<Users> findByEmail(String email) throws Exception;

}
