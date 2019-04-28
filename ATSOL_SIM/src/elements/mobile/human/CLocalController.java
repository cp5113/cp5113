/**
 * ATSOL_SIM
 * elements.mobile
 * CLocalController.java
 */
package elements.mobile.human;
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
 * @date : May 9, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 9, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import elements.facility.CRunway;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.CAircraftLineUpMoveState;
import elements.mobile.vehicle.state.CAircraftTakeoffMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ANode;
import elements.util.geo.CAltitude;
import elements.util.geo.EGEOUnit;
import sim.clock.ISimClockOberserver;

/**
 * @author S. J. Yun
 *
 */
public class CLocalController extends AATCController {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	
	public CLocalController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	/** (non-Javadoc)
	 * @see elements.mobile.human.IATCController#controlAircraft()
	 */
	@Override
	public synchronized void controlAircraft() {
		// TODO Auto-generated method stub
//		System.out.println(this.getName() + this.iAircraftList);
		
		
	}
	@Override
	public void initializeAircraft(CAircraft aAircraft) {
		// TODO Auto-generated method stub
		System.err.println("You must create initializeAircraft Method of LocalController");
		
	}

	public void requestLineUp(CAircraft aAircraft) {
		
				
		// Find Runway
		CRunway lRunway = aAircraft.getDepartureRunway();
		CFlightPlan lFlightPlan = aAircraft.getCurrentFlightPlan();
		CTaxiwayNode lRunwayEntry = aAircraft.getRunwayEntryPoint();
		// Verification
		if(!iFacilityControlledList.contains(lRunway)) {
			System.err.println("Error in Request Line up in LocalController : Runway is not same");
		}
		
		
		// Runway Safety Control
		if(lRunway.getDepartureAircraftList().size()>0) {
			return;
		}

		
		// Create Routing
		CTaxiwayNode lRunwayEntrySecond = lRunway.getTaxiwayNodeList().get(lRunway.getTaxiwayNodeList().indexOf(lRunwayEntry)+1);
		aAircraft.setRunwayEntryPointReference(lRunwayEntrySecond);
		List<ANode>  lOriginalRoute     = aAircraft.getRoutingInfo(); 
		int 		 lInsertIndex       = aAircraft.getRoutingInfo().indexOf(lRunwayEntry)+1;
		lOriginalRoute.add(lInsertIndex,lRunwayEntrySecond);
		aAircraft.setRoutingInfo(lOriginalRoute);
		
		// Create Flight Plan
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		lFlightPlan.insertPlanItem(lInsertIndex, lOriginalRoute.get(lInsertIndex),cal,new CAltitude(0,EGEOUnit.FEET));
		
		
		
		// Calculate Instruction Time
		long instructionTime = calculateLinupInstructionTime();
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		this.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		
		// Set Aircraft MoveState
		aAircraft.setMovementMode(EAircraftMovementMode.LINEUP);
		aAircraft.setMoveState(new CAircraftLineUpMoveState());
		
		// Runway Control
		lRunway.getDepartureAircraftList().add(aAircraft);
		lRunway.getRunwayOccupyingList().add(aAircraft);
		
	}

	public void requestTakeoff(CAircraft aAircraft) {

		// Verify Runway is Cleared
		CRunway lRunway = aAircraft.getDepartureRunway();
		if(lRunway.getOccupyingList().size()-1 >= 1) {
			return; // the other aircraft occupy this runway
		}
		
		
		// Calculate Instruction Time
		long instructionTime = calculateTakeOffInstructionTime();
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		this.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		
		// Issue Takeoff Clearance Clearance
		aAircraft.setMovementMode(EAircraftMovementMode.TAKEOFF);
		aAircraft.setMoveState(new CAircraftTakeoffMoveState());
				
	}


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
}






