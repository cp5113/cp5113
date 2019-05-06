/**
 * ATSOL_SIM
 * elements.mobile
 * CApproachController.java
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

import java.awt.Graphics2D;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

import api.CAssignRunwayAPI;
import api.CAssignSpotAPI;
import api.CRunwaySeparationAPI;
import api.CRunwaySequenceAPI;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CSpot;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.CAircraftApproachMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.network.ANode;
import elements.network.INode;
import elements.property.CAircraftPerformance;
import elements.property.EMode;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sim.clock.ISimClockOberserver;
import sim.gui.control.CAtsolSimGuiControl;
import util.performance.CApproachAircraftPerformance;

/**
 * @author S. J. Yun
 *
 */
public class CApproachController extends AATCController {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	public CApproachController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}

	/** (non-Javadoc)
	 * @see elements.mobile.human.IATCController#controlAircraft()
	 */
	@Override
	public synchronized void controlAircraft() {

	}
	@Override
	public void initializeAircraft(CAircraft aAircraft) {

		// Get Plan and LastNode
		CFlightPlan lFlightPlan = (CFlightPlan)aAircraft.getCurrentPlan();
		INode       lLastNode  = lFlightPlan.getNode(lFlightPlan.getNodeList().size()-1);
		
		// Verify Arrival or departure
		if(lLastNode instanceof CAirport) {
			// Set Mode
			aAircraft.setMode(EMode.ARR);
			

			// Set Current Position
			ANode lCurrentNode = (ANode) lFlightPlan.getNode(0);			
			aAircraft.setCurrentNode((ANode) lCurrentNode);
			aAircraft.getCurrentPosition().setXYCoordination(lCurrentNode.getCoordination().getXCoordination(), lCurrentNode.getCoordination().getYCoordination());
			
			// Remove Current Node
			lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
			
			
		}else {
//			aAircraft.setMode(EMode.ETC);
			System.err.println("CApproachController : You shall develope Crossing State or other things");
		}
		
		
	
	}


		
	public void requestLanding(CAircraft aAircraft) {
		
		
		// Assign Arrival Spot
		CFlightPlan lFlightPlan = (CFlightPlan) aAircraft.getCurrentPlan();

		
		
		// Assign Runway
		CRunway lRunway = assignArrivalRunway(aAircraft);
		
		
		// Assign Runway
		if(aAircraft.getArrivalRunway()==null) {			
			aAircraft.setArrivalRunway(lRunway);			
		}
		if(!lRunway.getArrivalAircraftList().contains(aAircraft)) {
			lRunway.getArrivalAircraftList().add(aAircraft);
		}
		
		
		// Add FinalApproachPoint into flight Plan
		INode lNode = aAircraft.getCurrentFlightPlan().getNode(0);
		if(!lNode.toString().contains("_FAF")) {
			addFinalApproachPoint(aAircraft, lRunway);
		}
		
		
		// Estimate Time
		long lETA = CApproachAircraftPerformance.estimateApproachLegFlightTime(aAircraft, 0.01, iCurrentTimeInMilliSecond)+3000; // add 3 seconds
		aAircraft.setETA(lETA);
		
		// Run Resequence Algorithm API
		new CRunwaySequenceAPI().resequenceArrival(lRunway.getArrivalAircraftList());
		
		
		// Separation Check
		int lSequenceNumber = lRunway.getArrivalAircraftList().indexOf(aAircraft);
		if(lSequenceNumber>0) {
			CAircraft 	lPreviousAircraft 		= lRunway.getArrivalAircraftList().get(lSequenceNumber-1);
			if(lPreviousAircraft.getMovementMode() != EAircraftMovementMode.LANDING) {
				lPreviousAircraft.setETA(CApproachAircraftPerformance.estimateApproachLegFlightTime(lPreviousAircraft, 0.01, iCurrentTimeInMilliSecond)+3000);; // Add 3 seconds
				long		lPreviousAircraftETA	= lPreviousAircraft.getETA();
			
				long 		lETAGap					=  lETA-lPreviousAircraftETA;
				double      lETAGapDistance			= aAircraft.getCurrentVelocity().getVelocity() * ((double)lETAGap / 1000.0);
			
				
				if(lETAGap<0 || lETAGapDistance < new CRunwaySeparationAPI ().assignArrivalSparationInMeter(lPreviousAircraft, aAircraft)) {
					
					// Do not  Approach
					return;
				}
			}
		}
		
		
		// Set Aircraft MoveState
		CSpot lCandidateSpot 	= assignArrivalSpot(aAircraft);
		lFlightPlan.setArrivalSpot(lCandidateSpot);
		lCandidateSpot.getVehicleWillUseList().add(aAircraft);
		aAircraft.setMoveState(new CAircraftApproachMoveState());
		
	}
	
	
	public CRunway assignArrivalRunway(CAircraft aAircraft) {
		CAirport lAirport = (CAirport)aAircraft.getCurrentFlightPlan().getDestinationNode();
		
		if(aAircraft.getCurrentFlightPlan().getArrivalRunway() != null) {
			return  aAircraft.getCurrentFlightPlan().getArrivalRunway();
		}
		
		int lNumOfQ = Integer.MAX_VALUE;
		CRunway lChoosenRwy = null;
		for(CRunway loopRwy : lAirport.getRunwayList()) {
			int lNumOfQtemp = loopRwy.getArrivalAircraftList().size() + loopRwy.getDepartureAircraftList().size(); 
			if(lNumOfQ>=lNumOfQtemp && loopRwy.isArrival()) {
				lNumOfQ= lNumOfQtemp;
				lChoosenRwy = loopRwy;;
			}
		}
		
		CRunway lAPIRunway = new CAssignRunwayAPI().assignRunway(iCurrentTimeInMilliSecond, aAircraft, aAircraft.getCurrentFlightPlan(), lAirport);
		if(lAPIRunway !=null) {
			lChoosenRwy = lAPIRunway;
		}
		
		
		
		return lChoosenRwy;
	}
	
	
	
	
	
	
	public void addFinalApproachPoint(CAircraft aAircraft, CRunway aRunway) {
		
		// Get Flight Plan
		CFlightPlan lFlightPlan = aAircraft.getCurrentFlightPlan();
		
		// Get Threshold info
		CTaxiwayNode lRunwayThreshold1 = (CTaxiwayNode) aRunway.getTaxiwayNodeList().get(1);
		CTaxiwayNode lRunwayThreshold2 = (CTaxiwayNode) aRunway.getTaxiwayNodeList().get(0);
		
		
		// Find Cos and Sin
		double lDist =Math.sqrt((lRunwayThreshold1.getCoordination().getXCoordination() - lRunwayThreshold2.getCoordination().getXCoordination()) * (lRunwayThreshold1.getCoordination().getXCoordination() - lRunwayThreshold2.getCoordination().getXCoordination())
				      + (lRunwayThreshold1.getCoordination().getYCoordination() - lRunwayThreshold2.getCoordination().getYCoordination()) * (lRunwayThreshold1.getCoordination().getYCoordination() - lRunwayThreshold2.getCoordination().getYCoordination()) );
		
		double lCos  = (lRunwayThreshold2.getCoordination().getXCoordination()-lRunwayThreshold1.getCoordination().getXCoordination())/lDist;
		double lSin  = (lRunwayThreshold2.getCoordination().getYCoordination()-lRunwayThreshold1.getCoordination().getYCoordination())/lDist;
		
		// Calculate FAF coordination
		double lFAFx = (5*1852)*lCos + lRunwayThreshold2.getCoordination().getXCoordination(); // 5nm
		double lFAFy = (5*1852)*lSin + lRunwayThreshold2.getCoordination().getYCoordination(); // 5nm
		
		// Create dummy Node
		ANode lFAFDummyNode = new ANode() {			
			@Override
			public void setATCControllerToChildren(IATCController aController) {
			}
		};
		lFAFDummyNode.setCoordination(new CCoordination(lFAFx, lFAFy, EGEOUnit.METER));
		lFAFDummyNode.setName(aRunway + "_FAF");
		
		
		
		GraphicsContext gc = CAtsolSimGuiControl.getInstance().getSimCanvas().getGraphicsContext2D();
		CCoordination p = CAtsolSimGuiControl.iInstance.changeCoordinatesInCanvas(lFAFx, lFAFy);
		gc.setStroke(Color.RED);
		gc.strokeOval(p.getXCoordination()-5, p.getYCoordination()-5, 10, 10);
		
		
		// Add to flight plan
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(0);		
		lFlightPlan.insertPlanItem(0, lRunwayThreshold2, cal1, new CAltitude(0, EGEOUnit.FEET));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		lFlightPlan.insertPlanItem(0,lFAFDummyNode, cal, new CAltitude(0, EGEOUnit.FEET));
		
		
		
	}
	
	

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
}







