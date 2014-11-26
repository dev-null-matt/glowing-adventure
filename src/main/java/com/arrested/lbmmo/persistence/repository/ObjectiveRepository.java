package com.arrested.lbmmo.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arrested.lbmmo.persistence.entity.Objective;

public interface ObjectiveRepository extends JpaRepository<Objective, Long>{

	public List<Objective> findByQuestIdAndQuestStep(long questId, int questStep);
}
