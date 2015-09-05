package com.arrested.lbmmo.service;

import org.springframework.stereotype.Component;

import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.persistence.entity.Waypoint;
import com.arrested.lbmmo.util.DistanceUtils;

@Component
public class DistanceCalculationService {

	public double distanceToObjective(Waypoint position, Objective objective) {
		return DistanceUtils.distance(objective.getWaypoint().getLatitude(), objective.getWaypoint().getLongitude(), position.getLatitude(), position.getLongitude());
	}
}
