/* *********************************************************************** *
 * project: org.matsim.*
 * Trb09Analysis
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
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
package playground.dgrether.analysis;

import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.population.BasicPopulationWriter;
import org.matsim.api.basic.v01.population.PlanElement;
import org.matsim.api.core.v01.ScenarioImpl;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.core.config.Config;
import org.matsim.core.population.LegImpl;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.population.PlanImpl;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.scenario.ScenarioLoader;

import playground.dgrether.DgPaths;


public class Trb09Preprocessing {

	private static final Logger log = Logger.getLogger(Trb09Preprocessing.class);
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			String runNumber1 = "860";
//			String runNumber1 = "732";
//			String runNumber2 = "733";
		
			String runid1 = "run" + runNumber1;
			
			String runiddot1 = runid1 + ".";
			
			String netfile = DgPaths.RUNBASE + runid1 + "/" + runiddot1 + "output_network.xml.gz";
			String plans1file = DgPaths.RUNBASE + runid1 + "/" + runiddot1 + "output_plans.xml.gz";
			String plans1fileOut = DgPaths.RUNBASE + runid1 + "/" + runiddot1 + "output_plans_wo_routes.xml.gz";

			ScenarioImpl sc = new ScenarioImpl();
			Config conf = sc.getConfig();
			conf.network().setInputFile(netfile);
			conf.plans().setInputFile(plans1file);
			ScenarioLoader loader = new ScenarioLoader(sc);
			loader.loadScenario();
			PopulationImpl pop = sc.getPopulation();
			for (PersonImpl p : pop.getPersons().values()){
				for (PlanImpl plan : p.getPlans()) {
					for (PlanElement pe : plan.getPlanElements()){
						if (pe instanceof Leg) {
							LegImpl l = (LegImpl)pe;
							l.setRoute(null);
						}
					}
				}
			}
			
			BasicPopulationWriter writer = new BasicPopulationWriter(pop);
			writer.write(plans1fileOut);
			log.debug("ya esta ;-)");
			
			
			
			
	}
	

}
