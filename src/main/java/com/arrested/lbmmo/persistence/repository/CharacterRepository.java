package com.arrested.lbmmo.persistence.repository;

import java.util.Set;

import com.arrested.lbmmo.persistence.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {

	public Set<Character> findByNameAndUserId(String name, long userId);
}
