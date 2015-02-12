package com.arrested.lbmmo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.arrested.lbmmo.persistence.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByUsername(String username);
	public List<User> findByEmail(String email);
}
