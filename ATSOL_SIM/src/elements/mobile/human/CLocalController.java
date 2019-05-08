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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.sun.glass.ui.Pixels.Format;

import api.CCaptureDistanceAPI;
import api.CRunwaySeparationAPI;
import elements.AElement;
import elements.COccupyingInform;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.CAircraftLineUpMoveState;
import elements.mobile.vehicle.state.CAircraftNothingMoveState;
import elements.mobile.vehicle.state.CAircraftTakeoffMoveState;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ANode;
import elements.network.INode;
import elements.property.EMode;
import elements.util.geo.CAltitude;
import elements.util.geo.EGEOUnit;
import sim.clock.ISimClockOberserver;
import util.performance.CLineupPerformance;
import util.performance.CUnUniformModelPerformance;

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
	long iPreviouseDepartureTimeInMilliseconds;
	CAircraft iPreviousDepartureAircraft;
	long iPreviouseArrivalTimeInMilliseconds;
	CAircraft iPreviousArrivalAircraft;
	
	public CLocalController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// First Come First Serve
		for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
			CAircraft lAircraft = iAircraftList.get(loopAC);
			CFlightPlan lFlightPlan = (CFlightPlan) lAircraft.getCurrentPlan();
			
			// Conflict Detection			
			if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {
				// Create Search aircraft List
				ArrayList<CAircraft> lOtherACList = new ArrayList<CAircraft>();
				lOtherACList.addAll(iAircraftList);
				
				CGroundConflictDetectionAndResolution.groundConflictDetectionAndResolution(lAircraft, lOtherACList);
				
			} // if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {


		}	// for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {				
		
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
		for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
			CAircraft lAircraft = iAircraftList.get(loopAC);
			CFlightPlan lFlightPlan = (CFlightPlan) lAircraft.getCurrentPlan();


			// Conflict Detection
			ArrayList<CAircraft> lOtherACList = new ArrayList<CAircraft>();
			if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState) && !(lAircraft.getMoveState() instanceof CAircraftTakeoffMoveState)) {

				//				// Ignore already stopping
				//				if(lAircraft.getMoveState() instanceof CAircraftGroundConflictStopMoveState) continue;

				// Create Search aircraft List
				lOtherACList.addAll(iAircraftList);
				CGroundConflictDetectionAndResolution.groundConflictDetectionAndResolution(lAircraft, lOtherACList);
			} // if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {


		}//for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
	}
	
	
	
	
	
	
	
	@Override
	public synchronized void initializeAircraft(CAircraft aAircraft) {
		// TODO Auto-generated method stub
		System.err.println("You must create initializeAircraft Method of LocalController");
		
	}

	public synchronized void requestLineUp(CAircraft aAircraft) {
		
		
		// Find Runway
		CRunway lRunway 			= aAircraft.getDepartureRunway();
		CFlightPlan lFlightPlan 	= aAircraft.getCurrentFlightPlan();
		CTaxiwayNode lRunwayEntry 	= aAircraft.getRunwayEntryPoint();
		
		
		// Create Routing
		if(aAircraft.getRunwayEntryPointReference() == null) {
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
		}
		
		
		
		
		// Verification
		if(!iFacilityControlledList.contains(lRunway)) {
			System.err.println("Error in Request Line up in LocalController : Runway is not same");
		}
		
		/**
		 * Do not Lineup
		 */

		// Runway Safety Control
		
		// When runway has Aircraft
//		if(lRunway.getRunwayOccupyingList().size()>0) {
//			for(CAircraft loopAC : lRunway.getRunwayOccupyingList()) {
//				double lDistanceToOppositeThreshold = loopAC.calculateDistanceBtwCoordination(loopAC.getCurrentPosition(), lRunway.getTaxiwayNodeList().get(lRunway.getTaxiwayNodeList().size()-1).getCoordination());
//				if (lDistanceToOppositeThreshold<lRunway.getDistance()/3 * 2 && aAircraft.getLeadingVehicle().equals(loopAC)) {					
//					// when the other aircraft가 활주로 중간을 넘었을때
//					break;
//				}else {
//					System.out.println("Linup Ignore : Runway is occupiyed at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTimeInMilliSecond)));
//					return;
//				}
//			}			
//		}
		
		if(lRunway.getRunwayOccupyingList().size()>0) {
			for(int loopAC = lRunway.getRunwayOccupyingList().size()-1; loopAC >=0 ; loopAC--) {
				CAircraft prevAircraft = lRunway.getRunwayOccupyingList().get(loopAC);
				double lDistanceToOppositeThreshold = prevAircraft.calculateDistanceBtwCoordination(prevAircraft.getCurrentPosition(), lRunway.getTaxiwayNodeList().get(lRunway.getTaxiwayNodeList().size()-1).getCoordination());
				if (lDistanceToOppositeThreshold<lRunway.getDistance()/3 * 2) {					
					// when the other aircraft가 활주로 중간을 넘었을때
					break;
				}else {
					System.out.println("Linup Ignore : Runway is occupiyed at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTimeInMilliSecond)));
					return;
				}
			}			
		}
		

		// Departure - Departure Separation

		// Capture Distance(Arrival) on Same runway
		long lReadyForTakeOffTime 				= new CLineupPerformance().estimateLineUpTime(aAircraft, 0.01, iCurrentTimeInMilliSecond) + calculateLinupInstructionTime(aAircraft);
//		long lDepartureSeparationInMilliSeconds = new CRunwaySeparation().assignDepartureSparationTimeInMilliSeconds(iPreviousDepartureAircraft, aAircraft);
		CCaptureDistanceAPI lcaptureDistanceAPI = new CCaptureDistanceAPI();
		for(CAircraft loopAC : lRunway.getArrivalAircraftList()) {
			// Ignore after Landing
			if(loopAC.getMovementMode() != EAircraftMovementMode.APPROACHING) {
				continue;
			}
			
			long 	lELDT 									= loopAC.calculateELDT(iCurrentTimeInMilliSecond, lRunway);		
			double 	lDistanceToThreshold 					= loopAC.calculateDistanceBtwCoordination(loopAC.getCurrentPosition(), lRunway.getTaxiwayNodeList().get(0).getCoordination());
//			double  lTimeRemainsToThresHold 				= lDistanceToThreshold/loopAC.getCurrentVelocity().getVelocity();
//			double  lTimeRemainsToThresholdInMilliSeconds 	= (long) (lTimeRemainsToThresHold*1000) + iCurrentTimeInMilliSecond;
			
			double 	lCaptureDistanceInTime 					= lcaptureDistanceAPI.assignCaptureDistanceToLineup(lRunway, this, loopAC)/loopAC.getCurrentVelocity().getVelocity();
			double  lCaptureDistanceInTimeInMilliSeconds 	= (long)(lCaptureDistanceInTime*1000);
			
			// Arrival Aircraft capture the runway, do not line up
			if(lReadyForTakeOffTime+lCaptureDistanceInTimeInMilliSeconds > lELDT) {				
//				System.out.println("Linup Ignore : Landing Aircraft Capture the runway. Landing will be at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lELDT)));
				return;
			}
			
		
		}
		
		
		 
		
		
		
		
		/**
		 *  Do Lineup
		 */
		
		// Calculate Instruction Time
		long instructionTime = calculateLinupInstructionTime(aAircraft);
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		this.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		
		// Set Aircraft MoveState
		aAircraft.setMovementMode(EAircraftMovementMode.LINEUP);
		aAircraft.setMoveState(new CAircraftLineUpMoveState());
		
		// Runway Control
		lRunway.getDepartureAircraftList().add(aAircraft);
		lRunway.getRunwayOccupyingList().add(aAircraft);
		
	}

	public synchronized void requestTakeoff(CAircraft aAircraft) {

		// Verify Runway is Cleared
		CRunway lRunway = aAircraft.getDepartureRunway();
		if(lRunway.getRunwayOccupyingList().size()-1 >= 1) {
			return; // the other aircraft occupy this runway
		}
		
		
		// Departure Separation
		long lDepartureSeparation = new CRunwaySeparationAPI().assignDepartureSparationTimeInMilliSeconds(iPreviousDepartureAircraft, aAircraft);
		if(iPreviouseDepartureTimeInMilliseconds + lDepartureSeparation > iCurrentTimeInMilliSecond) {
			return; //Departure Separation
		}
		
		
		// Calculate Instruction Time
		long instructionTime = calculateTakeOffInstructionTime(aAircraft);
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		this.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		iPreviouseDepartureTimeInMilliseconds 	= iCurrentTimeInMilliSecond + instructionTime;
		iPreviousDepartureAircraft 				= aAircraft;
		
		// Issue Takeoff Clearance Clearance
		((CTaxiwayNode)(aAircraft.getCurrentFlightPlan().getNode(0))).getVehicleWillUseList().remove(aAircraft);
		aAircraft.setMovementMode(EAircraftMovementMode.TAKEOFF);
		aAircraft.setMovementStatus(EAircraftMovementStatus.TAKEOFF);
		aAircraft.setMoveState(new CAircraftTakeoffMoveState());
				
	}
	
	
	
	
	
	public synchronized void requestLanding(CAircraft aAircraft) {
		long instructionTime = calculateLandingInstructionTime(aAircraft);
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
		this.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);
	}
	
	
	
	
	
	
	
	
	public synchronized void requestTaxiToSpot(CAircraft aAircraft) {
		CFlightPlan lFlightPlan = (CFlightPlan) aAircraft.getCurrentPlan();
		
		// Find Route
		List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
		for(INode loopNode : lFlightPlan.getNodeList()) {
			if(loopNode instanceof CAirport) {
				break;
			}
			lOD.add((CTaxiwayNode) loopNode);
		}
		lOD.add(lFlightPlan.getArrivalSpot().getTaxiwayNode());
		
		// Find Route
		LinkedList<CTaxiwayNode> lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);				
		aAircraft.setRoutingInfo(lRouteList);

		
	
		
		
		
		// To Ignore Ground Controller's work
		LinkedList<CTaxiwayNode> lRouteListOnlyLocalController = new LinkedList<CTaxiwayNode>();
		for(CTaxiwayNode loopNode : lRouteList) {
			if(loopNode.getATCController() instanceof CGroundController) {
				break;
			}
			lRouteListOnlyLocalController.add(loopNode);
		}
		
		// Calculate Instruction and Read back Time				
		long lTaxiInstructionTimeMilliSec = calculateTaxiInstructionTime(lRouteListOnlyLocalController,aAircraft);
		
		
		
		/*
		 * verify Opposite direction traffic 
		 */				
		// Calculate Estimated Taxiing Time 
		List<COccupyingInform> lTaxiwayUsageSchedule = CUnUniformModelPerformance.estimateTaxiingTime(aAircraft,0.01,iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
			if(lTaxiwayUsageSchedule.get(loopSche).getLink().isOppositDirectionElementInTimeWindow(lTaxiwayUsageSchedule.get(loopSche).getStartTime(), 
					lTaxiwayUsageSchedule.get(loopSche).getEndTime(), lTaxiwayUsageSchedule.get(loopSche).getDestination())) {
				System.err.println("In LocalController : You shall make re-routing to avoid opposit direction");
				break;
			}
				
		}
		
		// Set Route to Aircraft		
		aAircraft.setRoutingInfo(lRouteList);
		
		// Check Crossing Runway 
		aAircraft.getCrossingRunwayList().clear();
		aAircraft.getCrossingRunwayNodeList().clear();
		for(CTaxiwayNode loopR : lRouteList) {
			if(loopR.getRunway() !=null && !loopR.getRunway().equals(lFlightPlan.getArrivalRunway())){
				aAircraft.getCrossingRunwayList().add(loopR.getRunway());
				aAircraft.getCrossingRunwayNodeList().add(loopR);
			}
		}
		
		
		// Notify Taxischedule to taxiway link
		for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
			lTaxiwayUsageSchedule.get(loopSche).getLink().addToOccupyingSchedule(lTaxiwayUsageSchedule.get(loopSche));
		}
		
		
		// Create Plan List			
		for(int loopPlan = lFlightPlan.getNodeList().size()-1; loopPlan >= 0; loopPlan--) {
			lFlightPlan.removePlanItem(lFlightPlan.getNodeList().get(loopPlan));
		}
		
		for(int loopR = lRouteList.size()-1; loopR>=0; loopR--) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			lFlightPlan.insertPlanItem(0, lRouteList.get(loopR), cal, new CAltitude(0, EGEOUnit.FEET));
		}
		
		// Reconstruct Flight Plan and Aircraft Status		
		aAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
		aAircraft.setCurrentLink(aAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
		
		
		// Set Event Time to Aircraft and This(Controller)
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		this.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		
	}

	public void requestCrossingRunway(CAircraft aCAircraft) {
		System.out.println();
		
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






