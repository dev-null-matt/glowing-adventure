package com.arrested.lbmmo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.arrested.lbmmo.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public List<User> findByUsername(String username);
	public List<User> findByEmail(String email);
	
	public List<User> findByUsernameIgnoreCase(String username);
	public List<User> findByEmailIgnoreCase(String email);
	
	@Modifying
	@Query(value = "insert into USER_ACCOUNT (username,password,email,enabled) values (?1, crypt(?3, gen_salt('bf')), ?2,true)", nativeQuery = true)
	public void createAccount(String username, String email, String password);
	
	@Modifying
	@Query(value = "insert into USER_ROLE (user_id,role) values (?1, ?2)", nativeQuery = true)
	public void giveUserRole(long id, String role);
	
	@Modifying
	@Query(value = "update USER_ACCOUNT set password = crypt(?2, gen_salt('bf')) where username = ?1", nativeQuery = true)
	public void setPassword(String username, String password);
}
