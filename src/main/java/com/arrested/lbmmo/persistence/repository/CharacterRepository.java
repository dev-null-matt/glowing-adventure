package com.arrested.lbmmo.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arrested.lbmmo.persistence.entity.Character;

public interface CharacterRepository extends JpaRepository<Character, Long> {

	public Set<Character> findByNameIgnoreCaseAndUserId(String name, long userId);
}
