/* *********************************************************************** *
 * project: org.matsim.*
 * FakeTravelTimeCost.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package playground.dressler.ea_flow;

import org.matsim.core.network.LinkImpl;
import org.matsim.core.router.util.TravelMinCost;
import org.matsim.core.router.util.TravelTime;

/**
 * always returns traveltime = 0 to avoid time expansion
 * travelcost is the actual freespeed time, rounded down to integers 
 */
public class FakeTravelTimeCost implements TravelMinCost, TravelTime {
		
	public FakeTravelTimeCost() {
				
	}
	
	public double getLinkTravelCost(LinkImpl link, double time) {
		return Math.round((link.getLength() / link.getFreespeed(0)));
	}

	public double getLinkTravelTime(LinkImpl link, double time) {
		return 0; 
	}

	public double getLinkMinimumTravelCost(LinkImpl link) {
		return Math.round((link.getLength() / link.getFreespeed(0)));
	}

}
