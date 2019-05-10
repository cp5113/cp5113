package elements.mobile.vehicle.state;

import java.util.Calendar;

import api.CLandingPerformanceAPI;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.human.CGroundController;
import elements.mobile.human.CLocalController;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ALink;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import sim.clock.CSimClockOberserver;

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
 * @date : 2019. 5. 1.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 5. 1. : Coded by S. J. Yun.
 *
 *
 */

public class CAircraftOnRunwayAfterLandingMoveState implements IVehicleMoveState {

	@Override
	public void doMove(long aIncrementTimeStep, long aCurrentTime, AVehicle aThisVechicle) {
		// Initial variable
		double deltaTOrigin = 0.01;
		double deltaT       = deltaTOrigin;
				
		long 	lCurrentTimeFirstTime 	= (long)((long)(aCurrentTime/aIncrementTimeStep)*aIncrementTimeStep);
		long 	lTargetTime   			= lCurrentTimeFirstTime+aIncrementTimeStep;
		long 	lCurrentTime 			= aCurrentTime;
		double 	lAmountTime 			= ((double)lCurrentTime-(double)lCurrentTimeFirstTime)/(double)aIncrementTimeStep; 
		
		// Get Initial Information
		CAircraft   			lAircraft   		= (CAircraft) aThisVechicle;
		CAircraftPerformance 	lPerformance   		= (CAircraftPerformance) ((CAircraftType)lAircraft.getVehcleType()).getPerformance();		
		CFlightPlan 			lFlightPlan 		= (CFlightPlan) lAircraft.getCurrentPlan();		
		CRunway					lRunway				= lAircraft.getArrivalRunway();
		CCoordination			lThresholdCoord 	= lRunway.getTaxiwayNodeList().get(0).getCoordination();
		CLandingPerformanceAPI 	lLandingPerformance = new CLandingPerformanceAPI();
		double          		lAccelerationMax    = lPerformance.getDecelerationOnGroundMax();
		
		if(lAircraft.toString().equalsIgnoreCase("HL7798")) {
//			System.out.println("CAircraftOnRunwayAfterLandingMoveState : Debug Here");
		}
		
		// Move While until amountTime reach incrementTimeStep
		while(lAmountTime*1000<aIncrementTimeStep) {
			
			// get Current Position
			double lXCurrent 	= lAircraft.getCurrentPosition().getXCoordination();
			double lYCurrent 	= lAircraft.getCurrentPosition().getYCoordination();
			double lXOrigin 	= lAircraft.getCurrentPosition().getXCoordination();
			double lYOrigin 	= lAircraft.getCurrentPosition().getYCoordination();
			double lSpeedCurrent = lAircraft.getCurrentVelocity().getVelocity();
			
			// Extract Target Position == destination node
			double lXTarget 	= lFlightPlan.getNode(0).getCoordination().getXCoordination();//lFlightPlan.getNode(0).getCoordination().getXCoordination();
			double lYTarget 	= lFlightPlan.getNode(0).getCoordination().getYCoordination();//lFlightPlan.getNode(0).getCoordination().getYCoordination();
			
			// Calculate cos and sin 
			double lCos   =  (lXTarget-lXCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			double lSin   =  (lYTarget-lYCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			if(Double.isNaN(lCos)) lCos=1;
			if(Double.isNaN(lSin)) lSin=0;
			
			/*
			 *  Find Target Exit Taxiway
			 *  Search Stoppable Node
			 */
			if(lAircraft.getExitTaxiwayNode() == null) {
				for(int loopRemainingNode = 0; loopRemainingNode < lFlightPlan.getNodeList().size()-1; loopRemainingNode++) {
					// Find Taxiway Nearest to Spot
					CTaxiwayNode lNode 			= (CTaxiwayNode) lFlightPlan.getNode(loopRemainingNode);
					CTaxiwayNode lNodeAfterExit = null;
					CTaxiwayLink lLinkExit 		= null;
					
					double lMinDist 			= 99999999.0;
					for(ALink loopLink : lNode.getOwnerLinkList()) {
						int indexNodeA = loopLink.getNodeList().indexOf(lNode);
						int indexNodeB;
						if(indexNodeA == loopLink.getNodeList().size()-1) {
							indexNodeB = 0;  
						}else {
							indexNodeB = 1;
						}


						CTaxiwayNode theOtherSideNode = (CTaxiwayNode) loopLink.getNodeList().get(indexNodeB);
						double tempDist = lAircraft.calculateDistanceBtwCoordination(theOtherSideNode.getCoordination(), lFlightPlan.getArrivalSpot().getCoordination());
						if(tempDist < lMinDist && !lRunway.getTaxiwayNodeList().contains(theOtherSideNode)) {
							lLinkExit 		= (CTaxiwayLink) loopLink;
							lNodeAfterExit 	= theOtherSideNode;
							lMinDist 		= tempDist;
						}
					}



					// Extract Exit Speed
					double lExitSpeed = lLinkExit.getSpeedLimitMps();
					if(lExitSpeed <=0) {
						lExitSpeed = 15 * 0.514444; // 15 kts == 7.71667 m/s
					}


					// Calculate Remaining Distance
					double lRemainingDist = lAircraft.calculateDistanceBtwCoordination(lAircraft.getCurrentPosition(),lNode.getCoordination());

					// Verify Remaining Distance is valid
					double lStoppableDist = lAircraft.calculateStoppingDistance(lAircraft.getCurrentVelocity().getVelocity(),lAircraft.getTargetExitSpeed(),lAccelerationMax);;


					if(lRemainingDist>=lStoppableDist) {
						
						// Reconstruct Flight Plan
						int lIndexDelete = lFlightPlan.getNodeList().indexOf(lNode);
						for(int delete = lFlightPlan.getNodeList().size()-2; delete> lIndexDelete; delete--) {
							lFlightPlan.removePlanItem(lFlightPlan.getNode(delete));
						}
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(0);
						lFlightPlan.insertPlanItem(lFlightPlan.getNodeList().size()-1, lNodeAfterExit, cal, new CAltitude(0.0, EGEOUnit.FEET));;
						
						
						// Set Exit Taxiway
						lAircraft.setExitTaxiwayNode(lNode);
						lAircraft.setTargetExitSpeed(lExitSpeed);
						
						break; // Target Exit Taxiway
					}
				} // for(int loopRemainingNode = 0; loopRemainingNode < lFlightPlan.getNodeList().size()-1; loopRemainingNode++) {
			}//if(lAircraft.getExitTaxiwayNode() == null) {
//			System.out.println();
			
			if(lFlightPlan.getNodeList().size()<2) {
				System.err.println("CAircraftOnRunwayAfterLandingMoveState : No Flight Plan After Landing");
			}
			
			
			/*
			 * Move Aircraft
			 */
			while(true) {
				
				
				
				// Calculate Acceleration
				double lRemainingDistance	= lAircraft.calculateDistanceBtwCoordination(lAircraft.getCurrentPosition(), lAircraft.getExitTaxiwayNode().getCoordination());				
				double lStoppableDist 		= lAircraft.calculateStoppingDistance(lAircraft.getCurrentVelocity().getVelocity(),lAircraft.getTargetExitSpeed(),lAccelerationMax);
				
				
				double lAccelCurrent = 0;	
				if(lStoppableDist + lStoppableDist*0.1>=lRemainingDistance)  {
					lAccelCurrent = -lAccelerationMax/2;
				}
				
				
				// Divide Speed and Acceleration
				double lSpeedCurrentX = lSpeedCurrent * lCos;
				double lSpeedCurrentY = lSpeedCurrent * lSin;
				double lAccelCurrentX = lAccelCurrent * lCos;
				double lAccelCurrentY = lAccelCurrent * lSin;
				
				
				
				double lSpeedNextX	  = lSpeedCurrentX + lAccelCurrentX * deltaT;
				double lSpeedNextY	  = lSpeedCurrentY + lAccelCurrentY * deltaT;
				
				// Calculate Next Position = S0 + v0t + 1/2at^2
				double lXNext		  = lXCurrent + lSpeedCurrentX * deltaT + 1/2 * lAccelCurrentX * deltaT * deltaT;
				double lYNext		  = lYCurrent + lSpeedCurrentY * deltaT + 1/2 * lAccelCurrentY * deltaT * deltaT;
				
				
				// Verify next Point overshoot next node
				if(!SValidateRangeChecker.validDataInRange(lXNext,lXOrigin,lXTarget) || !SValidateRangeChecker.validDataInRange(lYNext,lYOrigin,lYTarget)) {
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

				// Update Time
				lAmountTime += deltaT;
				
				
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
				
				
				
				// 
//				System.out.println(aCurrentTime - lAircraft.getRunwayEntryTime());
//				System.out.println(CSimClockOberserver.getInstance().getCurrentTIme().getTimeInMillis());
//				System.out.println();
//				double lAccelCurrent =  
			}
			
			
			
			// Verify Next Node or not
			if(lFlightPlan.getNode(0).getCoordination().getXCoordination() == lXCurrent && lFlightPlan.getNode(0).getCoordination().getYCoordination() == lYCurrent) {
				
				if(lFlightPlan.getNode(0).equals(lAircraft.getExitTaxiwayNode())) {
					// When reach end of taxiway link
					// Remove this aircraft from taxiway Link shcedule
					lAircraft.getCurrentNode().getVehicleWillUseList().remove(lAircraft.getCurrentNode().getVehicleWillUseList().indexOf(lAircraft));
				
					lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
					lAircraft.removeRoutingInfo(0);

					// Update Current Location
					lAircraft.setCurrentLink(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
					lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
					
					

					
					
					// Clear Runway
					lFlightPlan.getArrivalRunway().getRunwayOccupyingList().remove(lAircraft);
					lFlightPlan.getArrivalRunway().getArrivalAircraftList().remove(lAircraft);					
					for(CTaxiwayLink loopLink : lFlightPlan.getArrivalRunway().getTaxiwayLink()) {
						loopLink.removeFromOccupyingSchedule(lAircraft);
					}
					for(CTaxiwayNode loopNode : lFlightPlan.getArrivalRunway().getTaxiwayNodeList()) {
						loopNode.getVehicleWillUseList().remove(lAircraft);
					}
					
					// Change to Taxiing Mode
					lAircraft.setMoveState(new CAircraftTaxiingMoveState());
					lAircraft.setMovementMode(EAircraftMovementMode.TAXIING);
					lAircraft.setMovementStatus(EAircraftMovementStatus.TAXIING_CONST);
					lAircraft.doMoveVehicle((long) (lAmountTime * 1000));
					return;
				}
				
				// When reach end of taxiway link
				// Remove this aircraft from taxiway Link shcedule
				if(lAircraft.getCurrentNode().getVehicleWillUseList().contains(lAircraft)) {
					lAircraft.getCurrentNode().getVehicleWillUseList().remove(lAircraft.getCurrentNode().getVehicleWillUseList().indexOf(lAircraft));
				}
			
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
				if(lAircraft.getRoutingInfo()!=null && lAircraft.getRoutingInfo().size()>0) {
					lAircraft.removeRoutingInfo(0);
				}
//				System.out.println();
				// Update Current Location
				if(lAircraft.getRoutingInfo()!=null) {
					lAircraft.setCurrentLink(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
				}
				lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
				
				
				
			}
			
			

		}// while(lAmountTime*1000<aIncrementTimeStep)
		
		
		
		
		
		
		
		
		
		
		
		

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
}






