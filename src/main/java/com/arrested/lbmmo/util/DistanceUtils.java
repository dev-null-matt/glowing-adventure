package com.arrested.lbmmo.util;

public class DistanceUtils {

	/**
	 * Calculates the distance between two positions using the great sphere haversine function for
	 * shortest path distance.
	 * 
	 * @param lat1 The latitude of the first point
	 * @param long1 The longitude of the first point
	 * @param lat2 The latitude of the second point
	 * @param long2 The longitude of the second point
	 * 
	 * @return The distance between the two points
	 */
	public static double distance( double lat1, double long1, double lat2, double long2 ) {

		double radius = 6371 * 1000; // Radius in meters of the Earth (avg, approx)

		double deltaLong = Math.toRadians( long2 - long1 );
		double deltaLat = Math.toRadians( lat2 - lat1 );

		double a = Math.sin( deltaLat / 2 ) * Math.sin( deltaLat / 2 ) + Math.sin( deltaLong / 2 ) * Math.sin( deltaLong / 2 ) * Math.cos( Math.toRadians( lat1 ) )
						* Math.cos( Math.toRadians( lat2 ) );

		double c = 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1 - a ) );

		return ( (double) radius ) * c;
	}
}
