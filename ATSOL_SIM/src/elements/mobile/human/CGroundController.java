/**
 * ATSOL_SIM
 * elements.mobile
 * CGroundController.java
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
 * @date : May 10, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 10, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import elements.COccupyingInform;
import elements.facility.CAirport;
import elements.facility.CSpot;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.network.ALink;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EMode;
import elements.util.geo.CAltitude;
import elements.util.geo.EGEOUnit;
import sim.CAtsolSimMain;
import util.performance.CUnUniformModelPerformance;

/**
 * @author S. J. Yun
 *
 */
public class CGroundController extends AATCController {


	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	public CGroundController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}
	/** (non-Javadoc)
	 * @see elements.mobile.human.IATCController#controlAircraft()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void controlAircraft() {
		// TODO Auto-generated method stub
//		System.out.println(this.iAircraftList);
		
		
		
		// Testing Aircraft whether it need to be Controlled or not
		// First Come First Serve
		for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
			CAircraft lAircraft = iAircraftList.get(loopAC);
			CFlightPlan lFlightPlan = (CFlightPlan) lAircraft.getCurrentPlan();
			long lTimeSTDThis = lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis();
			
			
			// If Departure and STD is same with current Time
			// Do Push-back
			if(!(lAircraft.getMoveState() instanceof CAircraftTaxiingMoveState) && lAircraft.getMode() == EMode.DEP && iCurrentTimeInMilliSecond>lTimeSTDThis && iNextEventTime<0) {
				
		
				// Find Route
				List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().get(258)); // Destination Node				
				LinkedList<CTaxiwayNode> lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);				
				lAircraft.setRoutingInfo(lRouteList);

				// Calculate Instruction and Read back Time				
				long lTaxiInstructionTimeMilliSec = calculateTaxiInstructionTime(lRouteList);
				
				/*
				 * verify Opposite direction traffic 
				 */				
				// Calculate Estimated Taxiing Time 
				List<COccupyingInform> lTaxiwayUsageSchedule = CUnUniformModelPerformance.estimateTaxiingTime(lAircraft,0.01,iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
				for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
					if(lTaxiwayUsageSchedule.get(loopSche).getLink().isOppositDirectionElementInTimeWindow(lTaxiwayUsageSchedule.get(loopSche).getStartTime(), 
							lTaxiwayUsageSchedule.get(loopSche).getEndTime(), lTaxiwayUsageSchedule.get(loopSche).getDestination())) {
						System.err.println("In CGroundController : You shall make re-routing to avoid opposit direction");
						break;
					}
						
				}
				
				// Set Route to Aircraft
				lRouteList.remove(0);				
				lAircraft.setRoutingInfo(lRouteList);
				
				// Notify Taxischedule to taxiway link
				for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
					lTaxiwayUsageSchedule.get(loopSche).getLink().addToOccupyingSchedule(lTaxiwayUsageSchedule.get(loopSche));
				}
				
				
				// Create Plan List				
				for(int loopR = lRouteList.size()-1; loopR>=0; loopR--) {
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(0);
					lFlightPlan.insertPlanItem(1, lRouteList.get(loopR), cal, new CAltitude(0, EGEOUnit.FEET));
				}
				
				// Reconstruct Flight Plan and Aircraft Status
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0)); // Remove Spot
				lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
				lAircraft.setCurrentLink(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));

				
				// Set Aircraft State
				lAircraft.setMoveState(new CAircraftTaxiingMoveState());
				
				// Set Event Time to Aircraft and This(Controller)
				lAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
				this.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
				
				
				
//				System.out.println();
				
				
				// Break loop to prevent that controller consider the other aircraft
				break;
				
				
			}
			
			
			
			
		}
		
		
		

		

	}
	@Override
	public synchronized void initializeAircraft(CAircraft aAircraft) {

		// Get Plan and Airport
		CFlightPlan lFlightPlan = (CFlightPlan)aAircraft.getCurrentPlan();
		CAirport lAirportControl= (CAirport)iOwnedFacilty;
		
		// Verify Arrival or departure		
		if(lFlightPlan.getOriginationAirport().equalsIgnoreCase(lAirportControl.getAirportICAO())) { // Departure
			// Validate Departure Spot
			CSpot lSpotAssigned = assignSpot(aAircraft, lFlightPlan.getDepartureSpot());
			
			// Set Departure Spot
			lFlightPlan.insertPlanItem(0, lSpotAssigned, lFlightPlan.getScheduleTimeList().get(0), new CAltitude(0,EGEOUnit.FEET));
			
			// Remove Departure Airport
			lFlightPlan.removePlanItem(lFlightPlan.getNode(1));
			
			// Set Aircraft Current Position
			aAircraft.getCurrentPostion().setXYCoordination(lSpotAssigned.getTaxiwayNode().getCoordination().getXCoordination(), lSpotAssigned.getTaxiwayNode().getCoordination().getYCoordination());
			
			// Set Aircraft Mode
			aAircraft.setMode(EMode.DEP);
			
			
		}else { // Arrival
			// Set Aircraft Mode
			aAircraft.setMode(EMode.ARR);
			
			System.err.println("Ground Controller : Hand on and Assign Spot and reconstruct Flight Plan is not developed");
		}
		
		

		
		
	}


	public CSpot assignSpot(CAircraft aAircraft, CSpot aOriginSpot) {
		// Set Output
		CSpot lAssignSpotResult = null;
		
		CAirport lAirportControlled= (CAirport)iOwnedFacilty;
		CAircraftPerformance lPerformance = (CAircraftPerformance) ((CAircraftType)aAircraft.getVehcleType()).getPerformance();
		
		// Validate CSpot is available wingspan and empty
		if(aOriginSpot.getACTypeADG().get(lPerformance.getADG().toString()) && !aOriginSpot.isIsOccuping()) {
			lAssignSpotResult = aOriginSpot; // 
		}else { // if not
			
			// Search All Feasible Spot			
			Collections.shuffle(lAirportControlled.getSpotList(),new Random(92545153));

			Iterator<CSpot> iter = lAirportControlled.getSpotList().iterator();
			while(iter.hasNext()) {
				CSpot lSpotCheck = iter.next();
				if(lSpotCheck.getACTypeADG().get(lPerformance.getADG().toString()) && !lSpotCheck.isIsOccuping()) {						
					lAssignSpotResult = lSpotCheck;
					break;
				}
			}
			
		}
		
		
		return lAssignSpotResult; 
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






