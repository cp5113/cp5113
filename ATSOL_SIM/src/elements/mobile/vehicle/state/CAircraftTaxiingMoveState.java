package elements.mobile.vehicle.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

import elements.AElement;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EMode;
import elements.util.geo.CCoordination;
import javafx.scene.paint.Color;
import sim.CAtsolSimMain;
import sim.gui.control.CAtsolSimGuiControl;

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
 * @date : Apr 8, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 8, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CAircraftTaxiingMoveState implements IVehicleMoveState {


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
	@SuppressWarnings("unused")
	@Override
	public synchronized void doMove(long aIncrementTimeStep, long aCurrentTime, AVehicle aThisVechicle) {
		
		// Initial variable
		double deltaTOrigin = 0.01;
		double deltaT       = deltaTOrigin;
		double lAmountTime = 0;
		
		// Get Initial Information
		CAircraft   	lAircraft   		 = (CAircraft) aThisVechicle;
		CAircraftPerformance lPerformance    = (CAircraftPerformance) ((CAircraftType)lAircraft.getVehcleType()).getPerformance(); 
		CFlightPlan 	lFlightPlan 		 = (CFlightPlan) lAircraft.getCurrentPlan();
		double			lAccelMax			 = lPerformance.getAccelerationOnGroundMax();
		double			lDecelMax			 = lPerformance.getDecelerationOnGroundMax();
		CCoordination	lDestinationCoord    = lAircraft.getRoutingInfo().get(lAircraft.getRoutingInfo().size()-1).getCoordination();
		
		
//		if(lAircraft.toString().equalsIgnoreCase("HL7506")) {
//			if(lAircraft.getMovementMode()==EAircraftMovementMode.PUSHBACK &&
//					lAircraft.getRoutingInfo().size()>4) {
//				if(lAircraft.getRoutingInfo().size()>4) {
//			System.out.println(lAircraft.getRoutingInfo());
//			System.out.println(lFlightPlan.getNodeList());
//			System.out.println();
//			}
//		}
		
		// ignore when no flight Plan ( == AC is at Arrival Spot)
		if(lFlightPlan.getNodeList().size()==0) {
			return;
		}
		
		
		
			
		
		
		
		
		// Move While until amountTime reach incrementTimeStep
		while(lAmountTime*1000<aIncrementTimeStep) {
			// get Current Position
			double lXCurrent = lAircraft.getCurrentPosition().getXCoordination();
			double lYCurrent = lAircraft.getCurrentPosition().getYCoordination();
			double lXOrigin = lAircraft.getCurrentPosition().getXCoordination();
			double lYOrigin = lAircraft.getCurrentPosition().getYCoordination();
			
			// Extract Target Position == destination node
			double lXTarget = lAircraft.getRoutingInfo().get(0).getCoordination().getXCoordination();//lFlightPlan.getNode(0).getCoordination().getXCoordination();
			double lYTarget = lAircraft.getRoutingInfo().get(0).getCoordination().getYCoordination();//lFlightPlan.getNode(0).getCoordination().getYCoordination();

			
			
			// Calculate cos and sin 
			double lCos   =  (lXTarget-lXCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			double lSin   =  (lYTarget-lYCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			if(Double.isNaN(lCos)) lCos=1;
			if(Double.isNaN(lSin)) lSin=0;
			
			// Get Taxiway Link Information to get Taxiing Speed Limit
			CTaxiwayLink lTaxiwayLink = null; 
//			System.out.println(lAircraft.getRoutingInfo().size());
//			System.out.println(lAircraft.getRoutingInfoLink().size());
			try {
				lTaxiwayLink = (CTaxiwayLink)(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
			}catch(Exception e) {
				System.err.println("CAircraftTaxiingMoveState : It seems that the aircraft enter Runway");
			}
			// Set Target Taxiing Speed
			double lSpeedTarget = 10;
			if(lTaxiwayLink.getSpeedLimitMps()>0 && lTaxiwayLink.getSpeedLimitMps()<=lPerformance.getTaxiingSpeedMax()) {
				lSpeedTarget = lTaxiwayLink.getSpeedLimitMps();
			}if(lAircraft.getMovementMode()==EAircraftMovementMode.PUSHBACK){
				lSpeedTarget = 3.0*0.514444;
			}else {
				lSpeedTarget = lPerformance.getTaxiingSpeedNorm();
			}
			
			double lSpeedCurrent = lAircraft.getCurrentVelocity().getVelocity();
			double lTrafficDensity = lTaxiwayLink.getNumOfElementUsingLink();
			/*
			 * Calculate Next Position 
			 */
			while(true) {
				// Calculate remaining Distance to Destination Node
				double lRemainingDistance = lAircraft.calculateRemainingRouteDistance();
				if(lAircraft.getMode()==EMode.DEP && lAircraft.getMovementMode()==EAircraftMovementMode.TAXIING) {
					lRemainingDistance = lAircraft.calculateRemainingRouteDistance(lAircraft.getRunwayEntryPoint());
					lRemainingDistance = lRemainingDistance - lFlightPlan.getDepartureRunway().getRunwaySafetyWidth();
				}
				double lStoppingDistanceCurrentSpeed = lAircraft.calculateStoppingDistance(lSpeedCurrent,lDecelMax);
				double lStoppingDistanceMaximumSpeed = lAircraft.calculateStoppingDistance(lSpeedTarget,lDecelMax);
				
				
				// Find Leading Aircraft
				double lDistanceFromLeadingAC = findLeadingAircraft(lAircraft);
				boolean lFollowingMode = false;
				
//				if(lDistanceFromLeadingAC<1000) {
//					System.out.println(calculateStoppingDistance(lSpeedCurrent,lDecelMax));
//					System.out.println();
//				}
//				if(lAircraft.getCurrentNode().getName().equalsIgnoreCase("N_8_1")) {
//					System.out.println();
//				}
				
				
				// Calculate Acceleration Speed
				double lAccelCurrent = 0;
				if(lRemainingDistance <= lStoppingDistanceCurrentSpeed) {					
					// Constant Deceleration Model					
					lAccelCurrent = -lDecelMax/2;
					
				}else {
					// Un-Uniform Model
					lAccelCurrent  = (lAccelMax/lSpeedTarget) * (lSpeedTarget - lSpeedCurrent);
					if(Math.abs(lSpeedTarget - lSpeedCurrent)<0.01) {
						lAccelCurrent = 0;
					}
					if(lSpeedCurrent>0 && lSpeedTarget == 0) {
						lAccelCurrent = -lDecelMax/2;;
					}
				}
				
				// Hybrid Car-following Model
				if(!Double.isNaN(lDistanceFromLeadingAC) && lDistanceFromLeadingAC<=1000) {   //lStoppingDistanceMaximumSpeed*2
					// Calculate Target Speed
					double lHeadwayJam = lAircraft.getVehcleType().getSafetyDistanceLength()*2;					
					double lCarFollowingVTarget =  Math.max(0, lSpeedTarget * (1- lHeadwayJam/lDistanceFromLeadingAC));
					if(lCarFollowingVTarget<0.001) {
						lSpeedTarget         = 0;
						lCarFollowingVTarget = 0;
					}
					double lCarFollowingA       = (lCarFollowingVTarget-lSpeedCurrent);
				
					// outlier control
					if(lCarFollowingA<-lDecelMax/2) {
						lCarFollowingA= -lDecelMax/2;
					}
					
					lAccelCurrent = lCarFollowingA;
					lFollowingMode = true;
//					if(lAccelCurrent<-0.01) {
//						
////						if(lAircraft.getLeadingVehicle() != null) {
////							System.out.println("AC : " + lAircraft);
////							System.out.println("Gap Destination : " + Math.abs(lAircraft.calculateRemainingRouteDistance() -	lAircraft.getLeadingVehicle().calculateRemainingRouteDistance()));
////							System.out.println("lDistanceFromLeadingAC : " + lDistanceFromLeadingAC);
////							System.out.println("Stopping Distance : " + (calculateStoppingDistance(lSpeedCurrent,lDecelMax) + calculateStoppingDistance(lSpeedCurrent,lDecelMax)*deltaT));
////							System.out.println();
////						}						
//					}
					
					
				}
				// Control deceleration speed using hybrid logic
				if(lSpeedCurrent>0 && lStoppingDistanceCurrentSpeed + lStoppingDistanceCurrentSpeed*deltaT >= lDistanceFromLeadingAC) {
					lAccelCurrent = -lDecelMax/2;
					lFollowingMode = true;
				}
				
//				if(lAircraft.getLeadingVehicle() != null) {				
////				if(Math.abs(lAircraft.calculateRemainingRouteDistance() -	lAircraft.getLeadingVehicle().calculateRemainingRouteDistance())*3< lDistanceFromLeadingAC ) {
//					System.out.println("AC : " + lAircraft);
//					System.out.println("Gap Destination : " + Math.abs(lAircraft.calculateRemainingRouteDistance() -	lAircraft.getLeadingVehicle().calculateRemainingRouteDistance()));
//					System.out.println("lDistanceFromLeadingAC : " + lDistanceFromLeadingAC);
//					System.out.println("Stopping Distance : " + (calculateStoppingDistance(lSpeedCurrent,lDecelMax) + calculateStoppingDistance(lSpeedCurrent,lDecelMax)*deltaT));
//					System.out.println();
//					System.out.println();
////				}
//				}
				
				
				
				// Make Integer
				if(lAccelCurrent>=0 && lSpeedTarget - lSpeedCurrent<=0.0001) {
					lSpeedCurrent = lSpeedTarget;
					lAccelCurrent = 0;				
				}else if(lAccelCurrent<=0 && (lSpeedCurrent<=0.01 || lRemainingDistance<=0.01)) {
					lSpeedCurrent = 0;
					lAccelCurrent = 0;
				}
//				else if(lSpeedTarget<lSpeedCurrent) {
//					lAccelCurrent = lSpeedCurrent-lSpeedTarget;
//					lSpeedCurrent = lSpeedTarget;					
//				}
				
				// Color and Status change
				if(lAircraft.getMovementMode()==EAircraftMovementMode.TAXIING) {
					if(lAccelCurrent>0) {
						lAircraft.getDrawingInform().setColor(Color.BLUE);
						lAircraft.setMovementStatus(EAircraftMovementStatus.TAXIING_ACCEL);
					}else if(lAccelCurrent<0.01 && lAccelCurrent>-0.01) {
						lAircraft.getDrawingInform().setColor(Color.GREEN);
						if(lSpeedCurrent<0.01 && lSpeedCurrent>-0.01) {
							lAircraft.setMovementStatus(EAircraftMovementStatus.TAXIING_STOP);
						}else {
							lAircraft.setMovementStatus(EAircraftMovementStatus.TAXIING_CONST);
						}
					}else {
						if(lFollowingMode) {
							lAircraft.setMovementStatus(EAircraftMovementStatus.TAXIING_DECEL_FOLLOWING);
						}else {
							lAircraft.setMovementStatus(EAircraftMovementStatus.TAXIING_DECEL);
						}
						lAircraft.getDrawingInform().setColor(Color.RED);
					}
				}else if(lAircraft.getMovementMode()==EAircraftMovementMode.PUSHBACK) {
					if(lAccelCurrent>0) {
						lAircraft.getDrawingInform().setColor(Color.BLUE);
						lAircraft.setMovementStatus(EAircraftMovementStatus.PUSHBACK);
					}else if(lAccelCurrent<0.01 && lAccelCurrent>-0.01) {
						lAircraft.getDrawingInform().setColor(Color.GREEN);
						if(lSpeedCurrent<0.01 && lSpeedCurrent>-0.01) {							
						}else {
							lAircraft.setMovementStatus(EAircraftMovementStatus.PUSHBACK);
						}
					}else {
						lAircraft.setMovementStatus(EAircraftMovementStatus.PUSHBACK_DECEL);
						lAircraft.getDrawingInform().setColor(Color.RED);
					}
				}

				
				// Pushback Puase
				if(lAircraft.getMovementMode() == EAircraftMovementMode.PUSHBACK &&						
						lRemainingDistance < 1) {
					lAircraft.setMovementStatus(EAircraftMovementStatus.PUSHBACK_HOLD);
					lAircraft.addPushbackPausedTimeInMilliSeconds((long)(deltaT*1000));					
				}

				
				
				// Divide Speed and Acceleration
				double lSpeedCurrentX = lSpeedCurrent * lCos;
				double lSpeedCurrentY = lSpeedCurrent * lSin;
				double lAccelCurrentX = lAccelCurrent * lCos;
				double lAccelCurrentY = lAccelCurrent * lSin;
				
				// Calcualte Vx, Vy = V0 + at
				double lSpeedNextX	  = lSpeedCurrentX + lAccelCurrentX * deltaT;
				double lSpeedNextY	  = lSpeedCurrentY + lAccelCurrentY * deltaT;
				
				// Calculate Next Position = S0 + v0t + 1/2at^2
				double lXNext		  = lXCurrent + lSpeedCurrentX * deltaT + 1/2 * lAccelCurrentX * deltaT * deltaT;
				double lYNext		  = lYCurrent + lSpeedCurrentY * deltaT + 1/2 * lAccelCurrentY * deltaT * deltaT;
				
				
				// Verify next Point overshoot next node
				if(!SValidateRangeChecker.validDataInRange(lXNext,lXOrigin,lXTarget) || !SValidateRangeChecker.validDataInRange(lYNext,lYOrigin,lYTarget)) {
					
//					System.out.println("Damn it");
					
					// Reduce Delta T
					if(deltaT * 0.1 <=0.00001) {
						
					}else {
						deltaT *=0.1;
						continue;
					}
				}
				
				
				// Update Current Data				
				lXCurrent     	= lXNext;
				lYCurrent		= lYNext;	

				
				lSpeedCurrent 	= Math.sqrt(lSpeedNextX *lSpeedNextX + lSpeedNextY * lSpeedNextY); 
				lAircraft.getCurrentPosition().setXYCoordination(lXCurrent, lYCurrent);
				lAircraft.setCurrentVelocity(lSpeedCurrent);
				
//				CAtsolSimGuiControl.getInstance().drawDrawingObjectList();
				
				// Update Time
				lAmountTime += deltaT;
//				System.out.println(lAmountTime);
//				System.out.println(lXTarget + "," + lYTarget);
//				System.out.println(lXCurrent + "," + lYCurrent);
//				System.out.println();
			
				
				
				// When deltaT is less than 0.001sec == 1milliseconds
				// The Corner Point
				// Make Resolution 1m
				if(Math.abs(lXTarget-lXNext) < 0.1 && Math.abs(lYTarget-lYNext)< 0.1) {
					
					
					lXCurrent = lXTarget;
					lYCurrent = lYTarget;
					lAircraft.getCurrentPosition().setXYCoordination(lXCurrent, lYCurrent);
					lAircraft.setCurrentVelocity(lSpeedCurrent);
					
					// Restore Delta t to original
					deltaT = deltaTOrigin;
					
					// Escape Loop
					break;
				}
				
				// Escape when amount T over incrementTimeStep
				if(lAmountTime*1000 >= aIncrementTimeStep) {
					break;
				}
			} //while(true) {
			
			
			// Verify Next Node or not
			if(lAircraft.getRoutingInfo().size()>1 && lFlightPlan.getNode(0).getCoordination().getXCoordination() == lXCurrent && lFlightPlan.getNode(0).getCoordination().getYCoordination() == lYCurrent) {
			
				
				// When reach end of taxiway link
				// Remove this aircraft from taxiway Link shcedule
				//				if(lAircraft.getMovementMode() != EAircraftMovementMode.PUSHBACK) {
				lTaxiwayLink.removeFromOccupyingSchedule(lAircraft);
				//				}

				lAircraft.getCurrentNode().getVehicleWillUseList().remove(lAircraft.getCurrentNode().getVehicleWillUseList().indexOf(lAircraft));
				// When reach end of taxiway link
				// Remove this node from flight plan
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
				lAircraft.removeRoutingInfo(0);

				// Update Current Location
				lAircraft.setCurrentLink(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
				lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));

			
			}
			
		} //while(lAmountTime<aIncrementTimeStep) {
		
//		System.out.println("AC : " + lAircraft);
//		System.out.println("CurrentSpeed : " + lAircraft.getCurrentVelocity().getVelocity());		
//		System.out.println("Remaining : " + lAircraft.getRoutingRemainingDistance() + "m");

	}
	
	
	
	



	private double findLeadingAircraft(CAircraft aAircraft) {
		
		// Initialize leading aircraft list
		ArrayList<ACuseSameNode> lSameNodeACList = new ArrayList<ACuseSameNode>();
		ArrayList<AVehicle>      lSameNodeACListV = new ArrayList<AVehicle>();
		double lRemainingDistance = 999999999999.0;
		
//		if(aAircraft.toString().equalsIgnoreCase("HL9606")) {
//			System.out.println();
//		}
		// Search next nodes
		
		for(int loopNodeIndex = 0; loopNodeIndex < aAircraft.getRoutingInfo().size(); loopNodeIndex++) {
			
			ANode loopNode = aAircraft.getRoutingInfo().get(loopNodeIndex);
			// Calculate remaining distance
			double lRemainingDist = aAircraft.calculateRemainingRouteDistance(loopNode);	
			
			Iterator<AVehicle> iterV = loopNode.getVehicleWillUseList().iterator();
			int maxCountUnLimit = loopNode.getVehicleWillUseList().size();
			int countUnlimit = 0;
			while(iterV.hasNext() && countUnlimit<maxCountUnLimit) {
				countUnlimit++;
				AVehicle loopV = null;
				try {
					loopV = iterV.next();
				}catch(ConcurrentModificationException e) { // Prevent access List(Synchronized) on Multi-threading
					continue;
				}catch(Exception e) {
					if(loopV == null && countUnlimit > 1) { // Prevent access List(Synchronized) on Multi-threading
						break;
					}
				}
				
				// Ignore Self object
				if(loopV.equals(aAircraft)) continue;
				
				
				// Ignore already have
				if(lSameNodeACListV.contains(loopV)) continue;
				
				
				// Ignore Landing and Takeoff
				if(((CAircraft)loopV).getMovementMode() == EAircraftMovementMode.APPROACHING || ((CAircraft)loopV).getMovementMode() == EAircraftMovementMode.LANDING || ((CAircraft)loopV).getMovementMode() == EAircraftMovementMode.TAKEOFF) {
					continue;
				}
				
				// The AC
				// Remaining distance is less than this aircraft
				double lLeaderableACRemainingDistance =loopV.calculateRemainingRouteDistance(loopNode); 
				if(lLeaderableACRemainingDistance < lRemainingDist) {
					
					
					if(loopV.getCurrentLink().equals(aAircraft.getCurrentLink())){ // When on same taxiway
						lSameNodeACList.add(new ACuseSameNode((CAircraft) loopV, lLeaderableACRemainingDistance, lRemainingDist-lLeaderableACRemainingDistance, loopNode));
					}else { // When on different taxiway
						lSameNodeACList.add(new ACuseSameNode((CAircraft) loopV, lLeaderableACRemainingDistance, lRemainingDist+lLeaderableACRemainingDistance, loopNode));
					}
//					lSameNodeACList.add(new ACuseSameNode((CAircraft) loopV, lLeaderableACRemainingDistance, lRemainingDist-lLeaderableACRemainingDistance, loopNode));
					lSameNodeACListV.add(loopV);					
				}				
				
			} // for(AVehicle loopV : loopNode.getVehicleWillUseList()) {
			
			
			
			// When the leadable aircraft are found in nearest node
			// this algorithm will be stop
			if(lSameNodeACListV.size()>0) {
				break; 
			}
			
			
			// until longest distance			
			if(((CAirport)loopNode.getOwnerObject()).getLonggestLinkLength() <= lRemainingDist) {
				break;
			}
		}
		if(lSameNodeACList.size()==0) {
			aAircraft.setLeadingVehicle(null);
			return lRemainingDistance;
		}
		Collections.sort(lSameNodeACList);
		
		/* Analyze Leading or not
		* Assume Aircraft A and B
		* A is 100m away from node D on link w
		* B is 100m away from node D on link K
		* if both aircraft's speed are 0kts(stop) and A is leading aircraft,
		* the car following algorithm shall issue stop instruction to both aircraft because the aircraft's remaining distance are same(my car-following model works based on remaining distance) 
		* However, A and B is separated perfectly
		* So, the distance between A and B should be calculated to analyze who is leading or nothing aircraft.
		*
		* when Distance between aircraft is 200m,
		* let assume Maximum speed stopping distance of both aircraft are 50m,
		* the aircraft are no related aircraft
		* 
		* when distance between aircraft is 200m,
		* maximum speed stopping distance of both aircraft are 50m
		* and aircraft A located at 50m from conflict node
		* and aircraft B located at 150m from conflict node
		* then Aircraft A is leading aircraft of aircraft B 
		* 
		* 
		
		* In the other hand, C is 50m away from node D on link w(Same as aircraft A)
		*  The aircraft C must be leading aircraft for aircraft A. (C is in maximum speed stopping distance 100m)
		*/ 
		
		// Calculate Maximum Stopping Distance		
		double lMaximumSpeedThisAC = aAircraft.getCurrentLink().getSpeedLimitMps();
		if(lMaximumSpeedThisAC==0) {
			lMaximumSpeedThisAC = ((CAircraftPerformance)aAircraft.getPerformance()).getTaxiingSpeedNorm();
		}
		double lMaximumStoppingDistanceThisAC = aAircraft.calculateStoppingDistance(lMaximumSpeedThisAC,((CAircraftPerformance)aAircraft.getPerformance()).getDecelerationOnGroundMax());
		
		for(ACuseSameNode loopSameNodeAC : lSameNodeACList) {
			
			// When Same Taxiway Link
			// Leading/Following 1
			if(loopSameNodeAC.iAircraft.getCurrentLink().equals(aAircraft.getCurrentLink())) {				
				if (loopSameNodeAC.iDistanceBtwAircraft <= aAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceThisAC*2 + lMaximumStoppingDistanceThisAC*0.1) {
					if(loopSameNodeAC.iAircraft.getLeadingVehicle()==null || !loopSameNodeAC.iAircraft.getLeadingVehicle().equals(aAircraft)) {	
					aAircraft.setLeadingVehicle(loopSameNodeAC.iAircraft);
					return loopSameNodeAC.iDistanceBtwAircraft;	
					}
				}				
			}
			
			// When Different Taxiway Link
			if(!loopSameNodeAC.iAircraft.getCurrentLink().equals(aAircraft.getCurrentLink())) {

				// Calculate Maximum Speed Stopping Distance
				double lMaximumSpeedOtherAC = loopSameNodeAC.iAircraft.getCurrentLink().getSpeedLimitMps();
				if(lMaximumSpeedOtherAC==0) {
					lMaximumSpeedOtherAC = ((CAircraftPerformance)loopSameNodeAC.iAircraft.getPerformance()).getTaxiingSpeedNorm();
				}
				double lMaximumStoppingDistanceOtherAC = aAircraft.calculateStoppingDistance(lMaximumSpeedOtherAC, ((CAircraftPerformance)loopSameNodeAC.iAircraft.getPerformance()).getDecelerationOnGroundMax());

				// Calculate Leaderable Aircraft's Remaining Distance to Conflict Node
				double lLeaderableACRemainingDistanceToConflictNode =  loopSameNodeAC.iAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode);

				// Leading/Following 2-1
				if(lLeaderableACRemainingDistanceToConflictNode <=  loopSameNodeAC.iAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceOtherAC + lMaximumStoppingDistanceOtherAC*0.1
						&& !aAircraft.getRoutingInfoLink().contains(loopSameNodeAC.iAircraft.getCurrentLink())) {
					// Reduce dimension to 1-dimension
					double lConvertDimensiondistancebetweenAC = aAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode)-lLeaderableACRemainingDistanceToConflictNode;
					if(lConvertDimensiondistancebetweenAC>=0) {
						if(loopSameNodeAC.iAircraft.getLeadingVehicle()==null || !loopSameNodeAC.iAircraft.getLeadingVehicle().equals(aAircraft)) {	
							aAircraft.setLeadingVehicle(loopSameNodeAC.iAircraft);
							return lConvertDimensiondistancebetweenAC;
						}
					}
				}
			
				// Leading/Following 2-2(Chasing)
				if(aAircraft.getRoutingInfoLink().contains(loopSameNodeAC.iAircraft.getCurrentLink())) {
					loopSameNodeAC.iDistanceBtwAircraft = aAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode)-loopSameNodeAC.iAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode);
//					System.out.println(loopSameNodeAC.iDistanceBtwAircraft);
//					System.out.println(aAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceThisAC*2 + lMaximumStoppingDistanceThisAC*0.1);
					if(loopSameNodeAC.iDistanceBtwAircraft <=  aAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceThisAC*2 + lMaximumStoppingDistanceThisAC*0.1) {

						if(loopSameNodeAC.iAircraft.getLeadingVehicle()==null || !loopSameNodeAC.iAircraft.getLeadingVehicle().equals(aAircraft)) {	
							aAircraft.setLeadingVehicle(loopSameNodeAC.iAircraft);
							return loopSameNodeAC.iDistanceBtwAircraft;	
						}

					}
				}

				
			
				
				
//				double lMaximumSpeedOtherAC = loopSameNodeAC.iAircraft.getCurrentLink().getSpeedLimitMps();
//				if(lMaximumSpeedOtherAC==0) {
//					lMaximumSpeedOtherAC = ((CAircraftPerformance)loopSameNodeAC.iAircraft.getPerformance()).getTaxiingSpeedNorm();
//				}
//				double lMaximumStoppingDistanceOtherAC = calculateStoppingDistance(lMaximumSpeedOtherAC, ((CAircraftPerformance)loopSameNodeAC.iAircraft.getPerformance()).getDecelerationOnGroundMax());
////				System.out.println(loopSameNodeAC.iAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode));
////				System.out.println(loopSameNodeAC.iAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceOtherAC + lMaximumStoppingDistanceOtherAC*0.1);				
////				System.out.println(aAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode));
//				double lLeaderableACRemainingDistanceToConflictNode =  loopSameNodeAC.iAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode);
//				if(lLeaderableACRemainingDistanceToConflictNode <=  loopSameNodeAC.iAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceOtherAC + lMaximumStoppingDistanceOtherAC*0.1) {
//					
//					if(loopSameNodeAC.iAircraft.getLeadingVehicle()==null || !loopSameNodeAC.iAircraft.getLeadingVehicle().equals(aAircraft)) {						
//						double lConvertDimensiondistancebetweenAC = aAircraft.calculateRemainingRouteDistance(loopSameNodeAC.iNode)-lLeaderableACRemainingDistanceToConflictNode;
//						
//						if(lConvertDimensiondistancebetweenAC>=0) {
//							aAircraft.setLeadingVehicle(loopSameNodeAC.iAircraft);
//							return lConvertDimensiondistancebetweenAC;
//						}
//					}
//				}else if(loopSameNodeAC.iDistanceBtwAircraft <= aAircraft.getVehcleType().getSafetyDistanceLength() + lMaximumStoppingDistanceThisAC + lMaximumStoppingDistanceThisAC*0.1) {
//					aAircraft.setLeadingVehicle(loopSameNodeAC.iAircraft);
//					return loopSameNodeAC.iDistanceBtwAircraft;	
//				}
			}
			

		}
		
		// Remove Leader Aircraft		
		if(aAircraft.getLeadingVehicle()!=null) {
			aAircraft.setLeadingVehicle(null);
		}
		
		return lRemainingDistance;
		
		
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
	class ACuseSameNode implements Comparable<ACuseSameNode>{
		CAircraft iAircraft;
		Double	  iDistanceFromConflictNode;
		Double    iDistanceBtwAircraft;
		
		ANode	  iNode;
		public ACuseSameNode(CAircraft aAircraft, Double aDistanceFromConflictNode, Double aDistanceBtwAircraft,ANode aNode) {
			super();
			iAircraft = aAircraft;
			iDistanceFromConflictNode = aDistanceFromConflictNode;
			iDistanceBtwAircraft = aDistanceBtwAircraft;
			iNode = aNode;
		}
		public String toString() {
			return "\"" + iAircraft + "\" going to " + iNode + " (Gap btw ac : " + iDistanceBtwAircraft + "m)";
		}
		@Override
		public int compareTo(ACuseSameNode aO) {
	        if (this.iDistanceBtwAircraft< aO.iDistanceBtwAircraft) {
	            return -1;
	        } else if (this.iDistanceBtwAircraft> aO.iDistanceBtwAircraft) {
	            return 1;
	        }
	        return 0;
		}
		
	}
}






