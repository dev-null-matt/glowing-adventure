package com.arrested.lbmmo.service;

import org.springframework.stereotype.Component;

import com.arrested.lbmmo.persistence.entity.Objective;
import com.arrested.lbmmo.util.DistanceUtils;
import com.arrested.lbmmo.ws.bean.request.PositionBean;

@Component
public class DistanceCalculationService {

	public double distanceToObjective(PositionBean position, Objective objective) {
		return DistanceUtils.distance(objective.getWaypoint().getLatitude(), objective.getWaypoint().getLongitude(), position.getLatitude(), position.getLongitude());
	}
}
