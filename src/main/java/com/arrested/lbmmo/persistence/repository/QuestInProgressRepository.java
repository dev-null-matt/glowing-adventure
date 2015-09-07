package com.arrested.lbmmo.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arrested.lbmmo.persistence.entity.QuestInProgress;

public interface QuestInProgressRepository extends JpaRepository<QuestInProgress,Long>{

	@Query(value="select * from quest_in_progress where completed_date is null and character_id = ?1", nativeQuery=true)
	public Set<QuestInProgress> findIncompleteMissions(long characterId);
	
	@Query(value="select * from quest_in_progress where completed_date is not null and character_id = ?1", nativeQuery=true)
	public Set<QuestInProgress> findCompleteMissions(long characterId);
}
