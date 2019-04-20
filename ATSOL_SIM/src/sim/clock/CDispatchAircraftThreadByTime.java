package sim.clock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import elements.IElementControlledByClock;
import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.AVehiclePlan;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.table.ITableAble;
import sim.CAtsolSimMain;

/**
 * 
 * Dtails....
 * 
 * 
 * <p>
 * The naming convention
 *  <li>I...... : Interface class</li>
 *  <li>A...... : Abstract class</li>
 *  <li>C...... : Concrete class</li>
 *  
 *  <li>doSomething  : Do it method </li>
 *  <li>runSomething : Run it method </li>
 *  <li>getSomething : Getter </li>
 *  <li>setSomething : Set attribute</li>
 *
 *  <li>i...... : Instance variable </li>
 *  <li>l...... : Local variable </li>
 *  <li>s...... : Static variable </li>
 *  <li>a...... : Argument </li>
 *  <li>n...... : ENUM </li>
 *
 *  <li>VARIABLE_NAME : Constant variable </li>
 * </ul>
 * </p>
 * 
 * 
 * @date : Mar 30, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 30, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CDispatchAircraftThreadByTime implements IElementControlledByClock{

	private List<ITableAble>	iAircraftList =  CAtsolSimMain.getInstance().getAircraftTable().getElementList();
	private boolean				iIsPrepared    = true;
	
	private ArrayList<CVehicleAndPlanAndTime> iVehicleAndPlanAndTimes = new ArrayList<CVehicleAndPlanAndTime>();
	
	public static CDispatchAircraftThreadByTime iInstance = new CDispatchAircraftThreadByTime();
	
	private CDispatchAircraftThreadByTime() {
	}
	
	public void initializeDispatch() {
		
		iVehicleAndPlanAndTimes.clear();
		// Create Plan List
		for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
			List<AVehiclePlan> lFlightPlanList = ((CAircraft) iAircraftList.get(loopAC)).getPlanList();
			for(int loopPlan = 0; loopPlan < lFlightPlanList.size(); loopPlan++) {

				// get a FlightPlan
				CFlightPlan lFlightPlan = (CFlightPlan) lFlightPlanList.get(loopPlan);				
				iVehicleAndPlanAndTimes.add(new CVehicleAndPlanAndTime(((AVehicle) iAircraftList.get(loopAC)), (AVehiclePlan)lFlightPlan, lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis()));
			}
		}

		Collections.sort(iVehicleAndPlanAndTimes);

	}
	
	
	
	@SuppressWarnings("unused")
	@Override
	public void incrementTime(Calendar aCurrentTIme, int aIncrementStepInSec) {
		// TODO Auto-generated method stub
		iIsPrepared = false;
		
		int lSearchIndex = 0;
		while(iVehicleAndPlanAndTimes.size()>0) {
			
			CAircraft lAircraft = (CAircraft) iVehicleAndPlanAndTimes.get(lSearchIndex).getVehicle();
			CFlightPlan lFlightPlan = (CFlightPlan) iVehicleAndPlanAndTimes.get(lSearchIndex).getVechiPlan();
			
			// verify this aircraft is operating now
			if(CAtsolSimMain.getInstance().getSimClock().haseObject(lAircraft)) {
				if(lSearchIndex== iVehicleAndPlanAndTimes.size()-1) {
					break;
				}
				lSearchIndex++;
				continue;
			}
			
			
			// Verify Start point is airport or waypoint
			String lAirportOrWaypoint = null;
			try{
				CAirport lAirport = (CAirport) lFlightPlan.getNode(0);
				lAirportOrWaypoint = "Airport";
			}catch(ClassCastException e) {				
				CWaypoint lWaypoint = (CWaypoint) lFlightPlan.getNode(0);
				lAirportOrWaypoint = "Waypoint";
			}catch(NullPointerException e){
				e.printStackTrace();
			}
			
			
			// Check Time
			long lSTD = lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis();
			
			// If Start point is Airport
			if(lSTD <= aCurrentTIme.getTimeInMillis()+1800000 && lAirportOrWaypoint.equalsIgnoreCase("Airport")) {
				// set a Flight plan to currentFlightPlan
				lAircraft.setCurrentPlan(lFlightPlan);
				// and remove this Plan from list
				lAircraft.getPlanList().remove(lFlightPlan);
				
				// Add to Clock as Observable
				CAtsolSimMain.getInstance().getSimClock().createLinkBetweenTimeAndElement(lAircraft);
				
				// Remove from List
				iVehicleAndPlanAndTimes.remove(lSearchIndex);
				lSearchIndex = 0;
				continue;
			}
			
			// If Start point is Airport
			if(lSTD <= aCurrentTIme.getTimeInMillis() && lAirportOrWaypoint.equalsIgnoreCase("Waypoint")) {
				// set a Flight plan to currentFlightPlan
				lAircraft.setCurrentPlan(lFlightPlan);
				// and remove this Plan from list
				lAircraft.getPlanList().remove(lFlightPlan);

				// Add to Clock as Observable
				CAtsolSimMain.getInstance().getSimClock().createLinkBetweenTimeAndElement(lAircraft);
				
				// Remove from List
				iVehicleAndPlanAndTimes.remove(lSearchIndex);
				lSearchIndex = 0;
				continue;
			}
			
			if(lSearchIndex== iVehicleAndPlanAndTimes.size()-1) {
				break;
			}
			
			
			lSearchIndex++;
			
		}
		

		
		iIsPrepared = true;
	}

	@Override
	public boolean isPreparedForNextIncrement() {
		// TODO Auto-generated method stub
		return iIsPrepared;
	}


	public static CDispatchAircraftThreadByTime getInstance() {
		return iInstance;
	}
	
	
	public long getSimStartTime() {
		Iterator iter = iVehicleAndPlanAndTimes.iterator();
		while(iter.hasNext()) {
			CVehicleAndPlanAndTime a = (CVehicleAndPlanAndTime) iter.next();
			if(a.getSTD()!=0) {
				return a.getSTD();				
			}
		}
		return 0;
	}
	
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */

	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */

	/*
	================================================================
	
							The Others
	
	================================================================
	 */
	
//	public static void main(String args[]) {
//		CControlAircraftThreadByTime myMain = CControlAircraftThreadByTime.getInstance();
//		
//	}
}



//
///*
// * Find Aircraft which has Flight Plan start at 
// * 	if Start at Wpt : CurrentTime
// *  it Start at Apt : CurrentTime - 1800 seconds (1800000 milliseconds)
//*/  
//for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
//	List<AVehiclePlan> lFlightPlanList = ((CAircraft) iAircraftList.get(loopAC)).getPlanList();
//	for(int loopPlan = 0; loopPlan < lFlightPlanList.size(); loopPlan++) {
//		
//		// get a FlightPlan
//		CFlightPlan lFlightPlan = (CFlightPlan) lFlightPlanList.get(loopPlan);
//		
//		
//		// Check Airport or Waypoint
//		String lAirportOrWaypoint = null;
//		try{
//			CAirport lAirport = (CAirport) lFlightPlan.getNode(0);
//			lAirportOrWaypoint = "Airport";
//		}catch(ClassCastException e) {				
//			CWaypoint lWaypoint = (CWaypoint) lFlightPlan.getNode(0);
//			lAirportOrWaypoint = "Waypoint";
//		}catch(NullPointerException e){
//			e.printStackTrace();
//		}
//		
//		
//		
//		// Check Time
//		long lSTD = lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis();
//		
//		// If Start point is Airport
//		if(lSTD >= aCurrentTIme.getTimeInMillis()+1800000 && lAirportOrWaypoint.equalsIgnoreCase("Airport")) {
//			// Get Aircraft
//			CAircraft lAircraft =((CAircraft) iAircraftList.get(loopAC)); 
//			
//			// verify this aircraft is operating now
//			if(!CAtsolSimMain.getInstance().getSimClock().haseObject(lAircraft)) {
//				// set a Flight plan to currentFlightPlan
//				lAircraft.setCurrentPlan(lFlightPlan);
//
//				// Add to Clock as Observable
//				CAtsolSimMain.getInstance().getSimClock().createLinkBetweenTimeAndElement(lAircraft);
//			}
//			
//			
//		}//if(lSTD >= aCurrentTIme.getTimeInMillis()+1800000 && lAirportOrWaypoint.equalsIgnoreCase("Airport")) {
//		
//		// If start point is waypoint
//		
//		
//		
//		
//		
//	}//for(int loopPlan = 0; loopPlan < lFlightPlanList.size(); loopPlan++) {
//}//for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {


