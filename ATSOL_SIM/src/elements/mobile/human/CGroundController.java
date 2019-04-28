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
import elements.COccupyingInform;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CSpot;
import elements.facility.CTaxiwayNode;
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
			ArrayList<CAircraft> lOtherACList = new ArrayList<CAircraft>();
			if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {
				
//				// Ignore already stopping
//				if(lAircraft.getMoveState() instanceof CAircraftGroundConflictStopMoveState) continue;
				
				// Create Search aircraft List
				lOtherACList.addAll(iAircraftList);
				try {
					lOtherACList.remove(lAircraft);
				}catch(Exception e) {
					System.out.println();
				}
				
				// Detect and resolution Conflict
				for(CAircraft loopOther : lOtherACList) {
					if((loopOther.getMoveState() instanceof CAircraftNothingMoveState)) continue;
					Polygon lthisACSafety = lAircraft.getSafetyPolygonInform();
					Polygon lthisACShape = lAircraft.getShapePolygonInform();
					Polygon lotherACSafety = loopOther.getSafetyPolygonInform();
					Polygon lotherACShape = loopOther.getShapePolygonInform();
//					System.out.println();
//					System.out.println();
					
					// Detect Conflict
					Shape lConflictSafetyAndSafetyInfo;
					Shape lConflictSafetyAndShapeInfo;
					boolean lConflictSafetyAndSafety=false;
					boolean lConflictSafetyAndShape=false;;
					synchronized (lthisACSafety) {
						synchronized (lotherACShape) {
							synchronized (lthisACShape) {
								synchronized (lotherACSafety) {
									lConflictSafetyAndSafetyInfo = Shape.intersect(lthisACSafety, lotherACSafety);
									lConflictSafetyAndShapeInfo  = Shape.intersect(lthisACSafety, lotherACShape);									
									lConflictSafetyAndSafety = lConflictSafetyAndSafetyInfo.getBoundsInLocal().isEmpty() == false;
									lConflictSafetyAndShape  = lConflictSafetyAndShapeInfo.getBoundsInLocal().isEmpty() == false;
									if(lConflictSafetyAndSafety) {
										System.out.println("Conflict Detection : " + lAircraft + "-" + loopOther + " (Safety -> Safety)");
									}
									if(lConflictSafetyAndShape) {
										System.out.println("Conflict Detection : " + lAircraft + "-" + loopOther + " (Safety -> Shape)");
									}
								}
							}
						}
					}

					
					
					/*
					 * Valid Priority
					 */ 

					// When Conflict resolved, continue Taxiing
					if(!lConflictSafetyAndSafety && !lConflictSafetyAndShape) {
						if(lAircraft.getConflictVehicle()!=null) {
							lAircraft.setConflictVehicle(null);
							lAircraft.setMoveState(new CAircraftTaxiingMoveState());	
							continue;
						}
						continue;
					}
						
						
						
					// Ignore when other aircraft has this aircraft as conflict vehicle					
					if(loopOther.getConflictVehicle()!=null&&loopOther.getConflictVehicle().equals(lAircraft)) continue;
					

					// Ignore if leading Aircraft
					if(lAircraft.getLeadingVehicle()!=null&&lAircraft.getLeadingVehicle().equals(loopOther)) continue;

					// When the other aircraft's routing has my link
					// I have a priority
					if(loopOther.getRoutingInfoLink().contains(lAircraft.getCurrentLink())) continue;

					// Departure Priority
					// If other aircraft is arrival,
					// I have a priority
					if(loopOther.getMode() == EMode.ARR) continue;

					
					
					// Low Priority
					if(lConflictSafetyAndShape) {
						// set conflict vehicle
						lAircraft.setConflictVehicle(loopOther);

						// Set Status								
						lAircraft.setMoveState(new CAircraftGroundConflictStopMoveState());
					}
					
					

					/*
					 * Set Conflict behavior
					 */ 




					// Drawing
					GraphicsContext gc = CAtsolSimGuiControl.iInstance.getSimCanvas().getGraphicsContext2D();
					double x = lConflictSafetyAndShapeInfo.getBoundsInLocal().getMaxX();
					double y = lConflictSafetyAndShapeInfo.getBoundsInLocal().getMaxY();					
					CCoordination p = CAtsolSimGuiControl.iInstance.changeCoordinatesInCanvas(x, y);
					gc.setStroke(Color.RED);
					gc.strokeOval(p.getXCoordination()-2, p.getYCoordination()-2, 4, 4);
					System.err.println("Require : Link Conflict Detection Priority");
					

				}//for(CAircraft loopOther : lOtherACList) {
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
				assignRunwayDeparture(aAircraft, lFlightPlan);
				// Find Runway entring Node
				ANode lRunwayEnteringNode = aAircraft.getDepartureRunway().findEnteringNodeForDeparture();
				aAircraft.setRunwayEntryPoint((CTaxiwayNode) lRunwayEnteringNode);
				
				// Calculate pre-Routing to decide pushback direction
				List<CTaxiwayNode> lOD = new ArrayList<CTaxiwayNode>();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add((CTaxiwayNode) lRunwayEnteringNode); // Destination Node				
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
				
				
				// Set Route from SPOT to Pushback Node
				lOD.clear();
				lOD.add(((CSpot) lFlightPlan.getNode(0)).getTaxiwayNode()); // CurrentNode
				lOD.add(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().get(((CAirport)lFlightPlan.getOriginationNode()).getTaxiwayNodeList().indexOf(lPushbackNode))); // Destination Node				
				lRouteList =  (LinkedList<CTaxiwayNode>) iRoutingAlgorithm.findShortedPath(lOD);
				long lPushbackInstructionTimeMilliSec = calculatePushbackInstructionTime();
				
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

				
				// Set Aircraft Status
				aAircraft.setMovementMode(EAircraftMovementMode.PUSHBACK);
				aAircraft.setMoveState(new CAircraftTaxiingMoveState());
				
				// Set Event Time to Aircraft and This(Controller)				
				aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
				this.setNextEventTime(iCurrentTimeInMilliSecond + lPushbackInstructionTimeMilliSec);
				

				
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
		long lTaxiInstructionTimeMilliSec = calculateTaxiInstructionTime(lRouteList);
		
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
				assignRunwayDeparture(lAircraft, lFlightPlan);
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
				long lPushbackInstructionTimeMilliSec = calculatePushbackInstructionTime();
				
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
	
	
	
	public synchronized void assignRunwayDeparture(CAircraft aAircraft, CFlightPlan aFlightPlan) {
		
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
			aAircraft.setDepartureRunway(lCandidateRunway);
			
			
			// Run User API
			CAssignRunwayAPI lUserAPI = new CAssignRunwayAPI();
			lUserAPI.assignRunway(iCurrentTimeInMilliSecond, aAircraft, aFlightPlan, lAirport);
			
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
		long lTaxiInstructionTimeMilliSec = calculateTaxiInstructionTime(lRouteList);
		
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
			aAircraft.setCurrentNode(lSpotAssigned.getTaxiwayNode());
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







