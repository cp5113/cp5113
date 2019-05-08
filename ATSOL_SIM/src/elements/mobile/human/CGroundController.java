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

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import api.CAssignRunwayAPI;
import api.CAssignSpotAPI;
import api.CPushbackDirectionAPI;
import api.CPushbackInstructionIssueAPI;
import api.CTurnaroundTime;
import elements.COccupyingInform;
import elements.facility.AFacility;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CSpot;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.CAircraftGroundConflictStopMoveState;
import elements.mobile.vehicle.state.CAircraftNothingMoveState;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ALink;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EMode;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import sim.CAtsolSimMain;
import sim.gui.control.CAtsolSimGuiControl;
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


	protected List<CAircraft> iPushbackAircraftList = Collections.synchronizedList(new ArrayList<CAircraft>());



	public CGroundController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}
	/** (non-Javadoc)
	 * @see elements.mobile.human.IATCController#controlAircraft()
	 */
	@Override
	public synchronized void controlAircraft() {
		// First Come First Serve
		for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
			CAircraft lAircraft = iAircraftList.get(loopAC);
			CFlightPlan lFlightPlan = (CFlightPlan) lAircraft.getCurrentPlan();
			long lTimeSTDThis = lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis();


			// Handoff to Local Controller
			if(lAircraft.getDepartureRunway()!=null
					&& lAircraft.getMovementMode() == EAircraftMovementMode.TAXIING
					&& lAircraft.calculateRemainingRouteDistance(lAircraft.getRunwayEntryPoint())<300) {
				handOffAircraft(lAircraft.getDepartureRunway().getATCController(), lAircraft);				
			}
			if((CAircraft)lAircraft.getLeadingVehicle() !=null 
					&& ((CAircraft)lAircraft.getLeadingVehicle()).getATCController() instanceof CLocalController) {
				handOffAircraft(lAircraft.getDepartureRunway().getATCController(), lAircraft);					
			}

			if(iCurrentTimeInMilliSecond >= 1407023571000L) {
				//				System.out.println();
			}


			// Conflict Detection			
			if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {
				// Create Search aircraft List
				ArrayList<CAircraft> lOtherACList = new ArrayList<CAircraft>();
				lOtherACList.addAll(iAircraftList);


				CGroundConflictDetectionAndResolution.groundConflictDetectionAndResolution(lAircraft, lOtherACList);

			} // if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {


		}	// for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {				

	} // public synchronized void controlAircraft() {


	public synchronized void requestPushBack(CAircraft aAircraft) {
		if (iNextEventTime<0) { // Block aircraft during ATC to the other aircraft
			CFlightPlan lFlightPlan = (CFlightPlan) aAircraft.getCurrentPlan();
			long lTimeSTDThis = lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis();




			// Pushback
			if(!(aAircraft.getMoveState() instanceof CAircraftTaxiingMoveState) && aAircraft.getMode() == EMode.DEP && iCurrentTimeInMilliSecond>lTimeSTDThis && iNextEventTime<0) {

				// Assign Runway for Departure
				CRunway lAssignedRunway = assignRunwayDeparture(aAircraft, lFlightPlan);
				aAircraft.setDepartureRunway(lAssignedRunway);

				// Find Runway entring Node
				ANode lRunwayEnteringNode = aAircraft.getDepartureRunway().findEnteringNodeForDeparture();
				aAircraft.setRunwayEntryPoint((CTaxiwayNode) lRunwayEnteringNode);

				// Calculate pre-Routing to decide pushback direction
				List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add((CTaxiwayNode) lRunwayEnteringNode); // Destination Node				
				LinkedList<CTaxiwayNode> lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);
				aAircraft.setPreDefinedTaxiRoute(lRouteList);


				// Decide Pushback Direction
				ANode lPushbackNode = null;
				ALink lPushbackLink = null;
				for(ANode loopRN : lRouteList) {
					if(loopRN.getOwnerLinkList().size()>=3) {
						ANode lOriginalNode = lRouteList.get(lRouteList.indexOf(loopRN)-1);
						ANode lOriginalDirectionNode = lRouteList.get(lRouteList.indexOf(loopRN)+1);
						for(ALink loopRL : loopRN.getOwnerLinkList()) {
							if(!loopRL.getNodeList().contains(lOriginalNode) &&!loopRL.getNodeList().contains(lOriginalDirectionNode)) {
								for(ANode loopNodeList : loopRL.getNodeList()) {
									if(!loopNodeList.equals(loopRN)) {
										lPushbackLink = loopRL;
										lPushbackNode = loopNodeList;
										aAircraft.setDirectionTaxwayNodeAfterPushBack((CTaxiwayNode) loopRN);
										break;
									}
								}
							}
							if(lPushbackLink!=null) {
								break;
							}
						}

					}
					if(lPushbackLink!=null) {
						break;
					}
				}

				// API : Pushback Direction
				CTaxiwayNode lPushbackDirectionAPI = new CPushbackDirectionAPI().assingPushbackDirection(aAircraft, iCurrentTimeInMilliSecond);
				if(lPushbackDirectionAPI != null) {
					lPushbackNode = lPushbackDirectionAPI;
				}


				// Pushback Priority Check
				for(CAircraft loopPushbackAC : iPushbackAircraftList) {
					double lDist = aAircraft.calculateDistanceBtwCoordination(aAircraft.getCurrentPosition(), loopPushbackAC.getCurrentPosition());

					LinkedList<CTaxiwayNode> lotherACPreRoute = loopPushbackAC.getPreDefinedTaxiRoute();

					// Search and Decision
					double lTempDistance = 0;
					for(int i = 0; i<lotherACPreRoute.size()-1;i++) {

						// Skip Spot
						if(loopPushbackAC.getCurrentFlightPlan().getDepartureSpot().getTaxiwayNode().equals(lotherACPreRoute.get(i+1))){
							lTempDistance += loopPushbackAC.calculateDistanceBtwNodes(lotherACPreRoute.get(i),lotherACPreRoute.get(i+2));
							i++;
							continue;
						}
						//						System.out.println(lotherACPreRoute.get(i));
						lTempDistance += loopPushbackAC.calculateDistanceBtwNodes(lotherACPreRoute.get(i),lotherACPreRoute.get(i+1));
						if(lTempDistance>350) {
							break;
						}

						// when my pushback point is in the other aircraft's path
						if(lotherACPreRoute.get(i).equals(lPushbackNode) || lotherACPreRoute.get(i+1).equals(lPushbackNode)) {
							// No pushback
							//							System.out.println(aAircraft + "Ignored Pushback Request by " + loopPushbackAC);
							return;
						}

						// when my pushback point is in the other aircraft's path
						double lTempDistanceThisAC = 0;
						for(int j = 0; j<aAircraft.getPreDefinedTaxiRoute().size()-1; j++) {
							lTempDistanceThisAC += aAircraft.calculateDistanceBtwNodes(aAircraft.getPreDefinedTaxiRoute().get(j),aAircraft.getPreDefinedTaxiRoute().get(j+1));
							if(lTempDistanceThisAC>350) {
								break;
							}
							//							System.out.println(aAircraft.getPreDefinedTaxiRoute().get(j) + " with  " + lotherACPreRoute.get(i) + " or " + lotherACPreRoute.get(i+1));
							if(aAircraft.getPreDefinedTaxiRoute().get(j).equals(lotherACPreRoute.get(i)) || aAircraft.getPreDefinedTaxiRoute().get(j).equals(lotherACPreRoute.get(i+1))) {
								// No pushback
								//								System.out.println(aAircraft + "Ignored Pushback Request by " + loopPushbackAC);
								return;
							}
						}

					}

				}

				// Pushback Priority Check version 2 (avoid Taxiing Aircraft)
				for(AVehicle loopBlockingAC : aAircraft.getPreDefinedTaxiRoute().get(1).getVehicleWillUseList()) {
					double lReminingDistance = loopBlockingAC.calculateRemainingRouteDistance(aAircraft.getPreDefinedTaxiRoute().get(1));
					if(lReminingDistance<300 && ((CAircraft)loopBlockingAC).getMovementMode() == EAircraftMovementMode.TAXIING) {
						return;
					}
				}
				for(AVehicle loopBlockingAC : aAircraft.getPreDefinedTaxiRoute().get(2).getVehicleWillUseList()) {
					double lReminingDistance = loopBlockingAC.calculateRemainingRouteDistance(aAircraft.getPreDefinedTaxiRoute().get(2));
					if(lReminingDistance<100 && ((CAircraft)loopBlockingAC).getMovementMode() == EAircraftMovementMode.TAXIING) {
						return;
					}
				}
				for(AVehicle loopBlockingAC : lPushbackNode.getVehicleWillUseList()) {
					double lReminingDistance = loopBlockingAC.calculateRemainingRouteDistance(aAircraft.getPreDefinedTaxiRoute().get(1));
					if(lReminingDistance<150 && ((CAircraft)loopBlockingAC).getMovementMode() == EAircraftMovementMode.TAXIING) {
						return;
					}
				}







				// API : Pushback Issue
				if(!new CPushbackInstructionIssueAPI().issuePushbackInstruction(aAircraft, iCurrentTimeInMilliSecond,iPushbackAircraftList)) {
					return;
				}



				// Set Route from SPOT to Pushback Node
				lOD.clear();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().get(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().indexOf(lPushbackNode))); // Destination Node				
				lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);
				long lPushbackInstructionTimeMilliSec = calculatePushbackInstructionTime(aAircraft);

				// Set Route to Aircraft
				lRouteList.remove(0);				
				aAircraft.setRoutingInfo(lRouteList);

				// Notify Taxischedule to taxiway link
				List<COccupyingInform> lTaxiwayUsageSchedule = CUnUniformModelPerformance.estimateTaxiingTime(aAircraft,0.01,iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
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
				aAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
				aAircraft.setCurrentLink(aAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
				System.out.println("Pushback Route : " + lFlightPlan.getNodeList());

				// Set Aircraft Status
				aAircraft.setMovementMode(EAircraftMovementMode.PUSHBACK);
				aAircraft.setMoveState(new CAircraftTaxiingMoveState());

				// Set Event Time to Aircraft and This(Controller)				
				aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
				this.setNextEventTime(iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
				iPushbackAircraftList.add(aAircraft);

			}

		}
	}

	public synchronized void requestTaxiToRunway(CAircraft aAircraft) {
		CFlightPlan lFlightPlan = (CFlightPlan) aAircraft.getCurrentPlan();

		// Find Route
		List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
		lOD.add(((CTaxiwayNode) lFlightPlan.getNode(0))); // CurrentNode
		lOD.add(aAircraft.getDirectionTaxwayNodeAfterPushBack()); // Direction after pushback
		lOD.add((CTaxiwayNode) aAircraft.getRunwayEntryPoint()); // Destination Node				
		LinkedList<CTaxiwayNode> lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);				
		aAircraft.setRoutingInfo(lRouteList);

		// Calculate Instruction and Read back Time				
		long lTaxiInstructionTimeMilliSec = calculateTaxiInstructionTime(lRouteList,aAircraft);

		/*
		 * verify Opposite direction traffic 
		 */				
		// Calculate Estimated Taxiing Time 
		List<COccupyingInform> lTaxiwayUsageSchedule = CUnUniformModelPerformance.estimateTaxiingTime(aAircraft,0.01,iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
			if(lTaxiwayUsageSchedule.get(loopSche).getLink().isOppositDirectionElementInTimeWindow(lTaxiwayUsageSchedule.get(loopSche).getStartTime(), 
					lTaxiwayUsageSchedule.get(loopSche).getEndTime(), lTaxiwayUsageSchedule.get(loopSche).getDestination())) {
				System.err.println("In CGroundController : You shall make re-routing to avoid opposit direction");
				break;
			}

		}

		// Set Route to Aircraft
		lRouteList.remove(0);				
		aAircraft.setRoutingInfo(lRouteList);

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

		aAircraft.getCurrentNode().getVehicleWillUseList().remove(aAircraft);
		aAircraft.getCurrentLink().removeFromOccupyingSchedule(aAircraft);

		// Reconstruct Flight Plan and Aircraft Status
		lFlightPlan.removePlanItem(lFlightPlan.getNode(0)); // Remove Spot
		aAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
		aAircraft.setCurrentLink(aAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));


		// Set Aircraft State
		aAircraft.setMovementMode(EAircraftMovementMode.TAXIING);
		aAircraft.setMoveState(new CAircraftTaxiingMoveState());

		// Set Event Time to Aircraft and This(Controller)
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		this.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		iPushbackAircraftList.remove(aAircraft);
		lFlightPlan.getDepartureSpot().getVehicleWillUseList().remove(aAircraft);
	}







	public synchronized void controlAircraftBackup() {
		// TODO Auto-generated method stub
		//		System.out.println(this.iAircraftList);



		// Testing Aircraft whether it need to be Controlled or not
		// First Come First Serve
		for(int loopAC = 0; loopAC < iAircraftList.size(); loopAC++) {
			CAircraft lAircraft = iAircraftList.get(loopAC);
			CFlightPlan lFlightPlan = (CFlightPlan) lAircraft.getCurrentPlan();
			long lTimeSTDThis = lFlightPlan.getScheduleTimeList().get(0).getTimeInMillis();


			// Pushback
			if(!(lAircraft.getMoveState() instanceof CAircraftTaxiingMoveState) && lAircraft.getMode() == EMode.DEP && iCurrentTimeInMilliSecond>lTimeSTDThis && iNextEventTime<0) {

				// Assign Runway for Departure
				CRunway lAssignedRunway = assignRunwayDeparture(lAircraft, lFlightPlan);
				lAircraft.setDepartureRunway(lAssignedRunway);


				// Find Runway entring Node
				ANode lRunwayEnteringNode = lAircraft.getDepartureRunway().findEnteringNodeForDeparture();

				// Calculate pre-Routing to decide pushback direction
				List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().get(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().indexOf(lRunwayEnteringNode))); // Destination Node				
				LinkedList<CTaxiwayNode> lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);

				// Decide Pushback Direction
				ANode lPushbackNode = null;
				ALink lPushbackLink = null;
				for(ANode loopRN : lRouteList) {
					if(loopRN.getOwnerLinkList().size()>=3) {
						ANode lOriginalNode = lRouteList.get(lRouteList.indexOf(loopRN)-1);
						ANode lOriginalDirectionNode = lRouteList.get(lRouteList.indexOf(loopRN)+1);
						for(ALink loopRL : loopRN.getOwnerLinkList()) {
							if(!loopRL.getNodeList().contains(lOriginalNode) &&!loopRL.getNodeList().contains(lOriginalDirectionNode)) {
								for(ANode loopNodeList : loopRL.getNodeList()) {
									if(!loopNodeList.equals(loopRN)) {
										lPushbackLink = loopRL;
										lPushbackNode = loopNodeList;
										break;
									}
								}
							}
							if(lPushbackLink!=null) {
								break;
							}
						}

					}
					if(lPushbackLink!=null) {
						break;
					}
				}


				// Set Route from SPOT to Pushback Node
				lOD.clear();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().get(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().indexOf(lPushbackNode))); // Destination Node				
				lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);
				long lPushbackInstructionTimeMilliSec = calculatePushbackInstructionTime(lAircraft);

				// Set Route to Aircraft
				lRouteList.remove(0);				
				lAircraft.setRoutingInfo(lRouteList);

				// Notify Taxischedule to taxiway link
				List<COccupyingInform> lTaxiwayUsageSchedule = CUnUniformModelPerformance.estimateTaxiingTime(lAircraft,0.01,iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
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


				// Set Aircraft Status
				lAircraft.setMovementMode(EAircraftMovementMode.PUSHBACK);
				lAircraft.setMoveState(new CAircraftTaxiingMoveState());

				// Set Event Time to Aircraft and This(Controller)				
				lAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
				this.setNextEventTime(iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);



			}







			// Taxiing Departure
			if(!(lAircraft.getMoveState() instanceof CAircraftTaxiingMoveState) && lAircraft.getMode() == EMode.DEP && iCurrentTimeInMilliSecond>lTimeSTDThis && iNextEventTime<0) {				
				// Issue Taxiing Instruction (Including Communication Time)
				issueTaxiingInstruction(lAircraft, lFlightPlan, lFlightPlan.getDepartureRunway().findEnteringNodeForDeparture());
				// Break loop to prevent that controller consider the other aircraft
				break;
			}




		}






	}



	public synchronized CRunway assignRunwayDeparture(CAircraft aAircraft, CFlightPlan aFlightPlan) {

		// When the aircraft doesn't have departure runway
		if( aFlightPlan.getDepartureRunway() == null && aAircraft.getCurrentNode().getOwnerObject() instanceof CAirport) {
			// Get airport info
			CAirport lAirport = (CAirport) aAircraft.getCurrentNode().getOwnerObject();

			// Search Runway for Departure minimum Q
			CRunway lCandidateRunway = null;
			int 	lRunwayQ      = 99999;
			for(CRunway loopRunway : lAirport.getRunwayList()) {
				if(loopRunway.isDeparture()) {
					if(loopRunway.getDepartureAircraftList().size() + loopRunway.getArrivalAircraftList().size() < lRunwayQ) {
						lRunwayQ = loopRunway.getDepartureAircraftList().size() + loopRunway.getArrivalAircraftList().size();
						lCandidateRunway = loopRunway;
					}
				}
			} //for(CRunway loopRunway : lAirport.getRunwayList()) {



			// Run User API
			CAssignRunwayAPI lUserAPI = new CAssignRunwayAPI();
			CRunway lRunwayFromAPI = lUserAPI.assignRunway(iCurrentTimeInMilliSecond, aAircraft, aFlightPlan, lAirport);
			if(lRunwayFromAPI!=null) {
				lCandidateRunway = lRunwayFromAPI;
			}

			return lCandidateRunway;
		}else if(aFlightPlan.getDepartureRunway() != null){
			return aFlightPlan.getDepartureRunway();
		}else {
			return null;
		}

	}

	public synchronized void issueTaxiingInstruction(CAircraft aAircraft, CFlightPlan aFlightPlan, ANode aDestinationNode) {
		// Find Route
		List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
		lOD.add(((CSpot) aFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
		lOD.add(((CAirport)aFlightPlan.getOriginationNode()).getTaxiwayNodeList().get(258)); // Destination Node				
		LinkedList<CTaxiwayNode> lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);				
		aAircraft.setRoutingInfo(lRouteList);

		// Calculate Instruction and Read back Time				
		long lTaxiInstructionTimeMilliSec = calculateTaxiInstructionTime(lRouteList,aAircraft);

		/*
		 * verify Opposite direction traffic 
		 */				
		// Calculate Estimated Taxiing Time 
		List<COccupyingInform> lTaxiwayUsageSchedule = CUnUniformModelPerformance.estimateTaxiingTime(aAircraft,0.01,iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
			if(lTaxiwayUsageSchedule.get(loopSche).getLink().isOppositDirectionElementInTimeWindow(lTaxiwayUsageSchedule.get(loopSche).getStartTime(), 
					lTaxiwayUsageSchedule.get(loopSche).getEndTime(), lTaxiwayUsageSchedule.get(loopSche).getDestination())) {
				System.err.println("In CGroundController : You shall make re-routing to avoid opposit direction");
				break;
			}

		}

		// Set Route to Aircraft
		lRouteList.remove(0);				
		aAircraft.setRoutingInfo(lRouteList);

		// Notify Taxischedule to taxiway link
		for(int loopSche = 0; loopSche < lTaxiwayUsageSchedule.size(); loopSche++) {
			lTaxiwayUsageSchedule.get(loopSche).getLink().addToOccupyingSchedule(lTaxiwayUsageSchedule.get(loopSche));
		}


		// Create Plan List				
		for(int loopR = lRouteList.size()-1; loopR>=0; loopR--) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			aFlightPlan.insertPlanItem(1, lRouteList.get(loopR), cal, new CAltitude(0, EGEOUnit.FEET));
		}

		// Reconstruct Flight Plan and Aircraft Status
		aFlightPlan.removePlanItem(aFlightPlan.getNode(0)); // Remove Spot
		aAircraft.setCurrentNode((ANode) aFlightPlan.getNode(0));
		aAircraft.setCurrentLink(aAircraft.getRoutingLinkInfoUsingNode((ANode) aFlightPlan.getNode(0)));


		// Set Aircraft State
		aAircraft.setMovementMode(EAircraftMovementMode.TAXIING);
		aAircraft.setMoveState(new CAircraftTaxiingMoveState());

		// Set Event Time to Aircraft and This(Controller)
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
		this.setNextEventTime(iCurrentTimeInMilliSecond + lTaxiInstructionTimeMilliSec);
	}









	@Override
	public synchronized void initializeAircraft(CAircraft aAircraft) {

		// Get Plan and Airport
		CFlightPlan lFlightPlan = (CFlightPlan)aAircraft.getCurrentPlan();

		CAirport lAirportControl = null;
		for(AFacility loopFa : iOwnedFacilty) {
			if(loopFa instanceof CAirport) {
				lAirportControl= (CAirport)loopFa;
			}
		}


		// Verify Arrival or departure		
		if(lFlightPlan.getOriginationAirport().equalsIgnoreCase(lAirportControl.getAirportICAO())) { // Departure
			// Validate Departure Spot
			CSpot lSpotAssigned = assignSpot(aAircraft, lFlightPlan.getDepartureSpot());			
			lSpotAssigned.getVehicleWillUseList().add(aAircraft);

			// Set Departure Spot
			lFlightPlan.insertPlanItem(0, lSpotAssigned, lFlightPlan.getScheduleTimeList().get(0), new CAltitude(0,EGEOUnit.FEET));

			// Remove Departure Airport
			lFlightPlan.removePlanItem(lFlightPlan.getNode(1));

			// Set Aircraft Current Position
			aAircraft.getCurrentPosition().setXYCoordination(lSpotAssigned.getTaxiwayNode().getCoordination().getXCoordination(), lSpotAssigned.getTaxiwayNode().getCoordination().getYCoordination());
			aAircraft.setCurrentNode(lSpotAssigned.getTaxiwayNode());
			// Set Aircraft Mode
			aAircraft.setMode(EMode.DEP);

			// Set Departure Turnaround Time
			aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + new CTurnaroundTime().getBeforeDepartureTurnaroundTimeInSeconds(aAircraft, lAirportControl)*1000); 

		}else if(aAircraft.getMode() == EMode.ARR) { // Arrival

			// When Local controller toss this aircraft (Arrival)
			// Taxi instruction from current node to spot
			long instructionTime = calculateTaxiInstructionTime(aAircraft.getRoutingInfo(), aAircraft);

			// Set Communication
			if(aAircraft.getNextEventTime()<=0) {
				aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);	
			}else {
				aAircraft.setNextEventTime(aAircraft.getNextEventTime() + instructionTime);
			}
			if(this.getNextEventTime()<=0) {
				this.setNextEventTime(iCurrentTimeInMilliSecond + instructionTime);	
			}else {
				this.setNextEventTime(this.getNextEventTime() + instructionTime);
			}


		}else {
			System.err.println("Ground Controller : Hand on and Assign Spot and reconstruct Flight Plan is not developed");
		}





	}


	public CSpot assignSpot(CAircraft aAircraft, CSpot aOriginSpot) {
		// Set Output
		CSpot lAssignSpotResult = null;

		CAirport lAirportControlled = null;
		for(AFacility loopFa : iOwnedFacilty) {
			if(loopFa instanceof CAirport) {
				lAirportControlled= (CAirport)loopFa;
			}
		}
		CAircraftPerformance lPerformance = (CAircraftPerformance) ((CAircraftType)aAircraft.getVehcleType()).getPerformance();

		// Validate CSpot is available wingspan and empty
		if(aOriginSpot.getACTypeADG().get(lPerformance.getADG().toString()) && aOriginSpot.getVehicleWillUseList().size()==0) {
			lAssignSpotResult = aOriginSpot; // 
		}else { // if not

			// Search All Feasible Spot			
			Collections.shuffle(lAirportControlled.getSpotList(),new Random(92545153));

			Iterator<CSpot> iter = lAirportControlled.getSpotList().iterator();
			while(iter.hasNext()) {
				CSpot lSpotCheck = iter.next();
				if(lSpotCheck.getACTypeADG().get(lPerformance.getADG().toString()) && aOriginSpot.getVehicleWillUseList().size()==0) {						
					lAssignSpotResult = lSpotCheck;
					break;
				}
			}

			CSpot lSpotAPI = new CAssignSpotAPI().assignSpot(aAircraft, (CAirport)iOwnedFacilty);
			if(lSpotAPI!=null) {
				lAssignSpotResult = lSpotAPI;
			}
		}


		return lAssignSpotResult; 
	}


	public List<CAircraft> getPushbackAircraftList(){
		return iPushbackAircraftList;
	}
	public void requestRecheckTaxiToRunway(CAircraft aCAircraft) {


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







