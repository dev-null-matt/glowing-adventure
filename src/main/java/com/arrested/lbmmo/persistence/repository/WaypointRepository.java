package com.arrested.lbmmo.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.arrested.lbmmo.persistence.entity.Waypoint;

public interface WaypointRepository extends JpaRepository<Waypoint, Long> {

}
