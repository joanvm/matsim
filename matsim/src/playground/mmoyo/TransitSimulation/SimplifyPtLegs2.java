package playground.mmoyo.TransitSimulation;

import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.PlanElement;
import org.matsim.core.api.population.Leg;
import java.util.ArrayList;
import java.util.List;
import org.matsim.api.basic.v01.TransportMode;

/**
 * A sequence of PT-Acts and PT-Activities starts with a leg "walk to PT" and ends with a act "exit PT" and a walk leg 
 * This class deletes them and introduces a new empty PT leg instead
 */
public class SimplifyPtLegs2 {
	
	public SimplifyPtLegs2(){
		
	}
	
	/**
	 * Identifies and marks ptActs
	 */
	public void run (Plan plan){
		int i=0;
		List<Integer> ptElements = new ArrayList<Integer>();
		boolean marking =false;
		for (PlanElement pe : plan.getPlanElements()) {
			if (pe instanceof Activity) {
				String actType = ((Activity)pe).getType();
				if (actType.equals("wait pt")) marking= true;
				if (marking)  ptElements.add(i);
				if (actType.equals("exit pt veh")){
					marking = false;
					deleteElements(plan, ptElements);
				}
			}
			i++;
		}
	}

	/**
	 * Deletes marked ptActs and thier legs 
	 */
	private void deleteElements(Plan plan, List<Integer> ptElements){
		Leg firstLeg = (Leg)plan.getPlanElements().get(ptElements.get(0)-1);
		Leg lastLeg = (Leg)plan.getPlanElements().get(ptElements.get(ptElements.size())+1);
		firstLeg.setMode(TransportMode.pt);
		firstLeg.setArrivalTime(lastLeg.getArrivalTime());
		firstLeg.setTravelTime(lastLeg.getArrivalTime() -firstLeg.getDepartureTime());
		for (Integer i : ptElements) {
			plan.removeActivity(i.intValue());
		}
	}
	
}