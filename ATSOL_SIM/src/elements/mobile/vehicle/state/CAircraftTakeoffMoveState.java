package elements.mobile.vehicle.state;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

import api.CTakeoffPerformanceAPI;
import elements.AElement;
import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ALink;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EMode;
import elements.property.EWTC;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.scene.paint.Color;
import sim.CAtsolSimMain;
import sim.clock.CSimClockOberserver;
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

public class CAircraftTakeoffMoveState implements IVehicleMoveState {


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
		CRunway			lRunway				= lAircraft.getDepartureRunway();
		CTakeoffPerformanceAPI lTakeoffPerformance = new CTakeoffPerformanceAPI();
		

		
//		for(CTaxiwayNode loopNode : ((CAirport)((CTaxiwayNode)lAircraft.getCurrentNode()).getOwnerObject()).getTaxiwayNodeList()) {
//			if(loopNode.getName().equalsIgnoreCase("N_7_1")) {
//				System.out.println();
//			}
//		}
		
		
		// Remove Flight Plan
		ANode lDestination = lRunway.getTaxiwayNodeList().get(lRunway.getTaxiwayNodeList().size()-1);
		CCoordination	lDestinationCoord    = lRunway.getTaxiwayNodeList().get(lRunway.getTaxiwayNodeList().size()-1).getCoordination();
		if(lFlightPlan.getNode(0) instanceof CWaypoint) {
			lDestination =(ANode) lFlightPlan.getNode(0);
			lDestinationCoord = lDestination.getCoordination();			
		}
		

		
		
		
		// Move While until amountTime reach incrementTimeStep
		while(lAmountTime*1000<aIncrementTimeStep) {
			
			// Reconstruct Flight Plan
			double lSpeedTarget = lPerformance.getV2()*0.514444; // kts to m/s			
			double lSpeedCurrent = lAircraft.getCurrentVelocity().getVelocity();

			
			// After Takeoff
			if(lSpeedCurrent>=lSpeedTarget*1.3 && lFlightPlan.getNode(0) instanceof CTaxiwayNode) {
				lDestination       =(ANode) lFlightPlan.getNode(0);				
				lDestinationCoord  = lDestination.getCoordination();
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
				lRunway.removeDepartureAircraftList(lAircraft);
				lRunway.removeFromOccupyingSchedule(lAircraft);;
				lRunway.getRunwayOccupyingList().remove(lAircraft);
			}
			
			// get Current Position
			double lXCurrent = lAircraft.getCurrentPosition().getXCoordination();
			double lYCurrent = lAircraft.getCurrentPosition().getYCoordination();
			double lXOrigin = lAircraft.getCurrentPosition().getXCoordination();
			double lYOrigin = lAircraft.getCurrentPosition().getYCoordination();
			
			// Extract Target Position == destination node
			// Re construct destination Node
			
			double lXTarget = lDestinationCoord.getXCoordination();//lFlightPlan.getNode(0).getCoordination().getXCoordination();
			double lYTarget = lDestinationCoord.getYCoordination();//lFlightPlan.getNode(0).getCoordination().getYCoordination();

			
			
			// Calculate cos and sin 
			double distance = Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));

			double lCos   =  (lXTarget-lXCurrent)  / distance;
			double lSin   =  (lYTarget-lYCurrent)  /  distance;
			if(Double.isNaN(lCos)) lCos=1;
			if(Double.isNaN(lSin)) lSin=0;
		
			

			
			/*
			 * Calculate Next Position 
			 */
			while(true) {
				

				// Un-Uniform Model
				double lAccelCurrent  = lTakeoffPerformance.calculateTargetAcceleration(lPerformance.getWTC(), lSpeedCurrent, lAircraft.getRandomNumber());


				// Color and Status change
				if(lAircraft.getMovementMode()!=EAircraftMovementMode.TAKEOFF) {
						lAircraft.setMovementMode(EAircraftMovementMode.TAKEOFF);						

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
				
				// When deltaT is less than 0.001sec == 1milliseconds
				// The Corner Point
				// Make Resolution 2m
				if(Math.abs(lXTarget-lXNext) < 2 && Math.abs(lYTarget-lYNext)< 2) {
					
					
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
			if(lFlightPlan.getNode(0).getCoordination().getXCoordination() == lXCurrent && lFlightPlan.getNode(0).getCoordination().getYCoordination() == lYCurrent) {

				// Runway control
				if(lFlightPlan.getNode(0).equals(lRunway.getTaxiwayNodeList().get(lRunway.getTaxiwayNodeList().size()-1))) {
					// Runway Control
					lRunway.getDepartureAircraftList().remove(lAircraft);
					lRunway.getRunwayOccupyingList().remove(lAircraft);
				}
				
				
				lAircraft.getCurrentNode().getVehicleWillUseList().remove(lAircraft);
				// When reach end of taxiway link
				// Remove this node from flight plan
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));				
				try{
					lAircraft.removeRoutingInfo(0);
					lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
				}catch(Exception e) {
					System.err.println("CAircraftTakeoffMoveState : You must Create Airborne Routing and Move state");
					lAircraft.setMoveState(new CAircraftTerminationMoveState());
					return;
				}
				
				// Update Current Location
//				lAircraft.setCurrentLink(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
				
				
				
			}
			
		} //while(lAmountTime<aIncrementTimeStep) {
		
//		System.out.println("AC : " + lAircraft);
//		System.out.println("CurrentSpeed : " + lAircraft.getCurrentVelocity().getVelocity());		
//		System.out.println("Remaining : " + lAircraft.getRoutingRemainingDistance() + "m");

	}
	private boolean validDataInRange(double data, double rangeStart, double rangeEnd) {
		rangeStart = Math.abs(rangeStart);
		rangeEnd = Math.abs(rangeEnd);
		data     = Math.abs(data);
		
		double min = 0;
		double max = 0;
		if(rangeStart<=rangeEnd) {
			min = rangeStart;
			max = rangeEnd;
		}else {
			min = rangeEnd;
			max = rangeStart;
		}
		
		
		if(min <= data && data <= max) {
			return true;
		}else {
			return false;
		}
		
		
	}
	
}






