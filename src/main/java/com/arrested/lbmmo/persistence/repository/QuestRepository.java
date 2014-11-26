package com.arrested.lbmmo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arrested.lbmmo.persistence.entity.Quest;

public interface QuestRepository extends JpaRepository<Quest, Long> {

}
