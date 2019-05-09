package elements.mobile.vehicle.state;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.prism.image.Coords;

import api.CLandingPerformanceAPI;
import api.CRunwayExitDecisionSpeedAPI;
import api.CTakeoffPerformanceAPI;
import api.CTouchdownDistanceAPI;
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

public class CAircraftLandingMoveState implements IVehicleMoveState {


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
				
		long lCurrentTimeFirstTime = (long)((long)(aCurrentTime/aIncrementTimeStep)*aIncrementTimeStep);
		long lTargetTime   = lCurrentTimeFirstTime+aIncrementTimeStep;
		long lCurrentTime = aCurrentTime;
		double lAmountTime = ((double)lCurrentTime-(double)lCurrentTimeFirstTime)/(double)aIncrementTimeStep; 
		
		

		
		
		
		// Get Initial Information
		CAircraft   	lAircraft   		= (CAircraft) aThisVechicle;
		CAircraftPerformance lPerformance   = (CAircraftPerformance) ((CAircraftType)lAircraft.getVehcleType()).getPerformance();		
		CFlightPlan 	lFlightPlan 		= (CFlightPlan) lAircraft.getCurrentPlan();		
		CRunway			lRunway				= lAircraft.getArrivalRunway();
		CCoordination lThresholdCoord 		= lRunway.getTaxiwayNodeList().get(0).getCoordination();
		CLandingPerformanceAPI lLandingPerformance = new CLandingPerformanceAPI();
		lAircraft.setMovementStatus(EAircraftMovementStatus.LANDING_DECEL);

		
		// Start Time Count
		if (lAircraft.getTimeFromThreshold() <=0) {
			lAircraft.setTimeFromThreshold(0);
		}
		

//		System.out.println("Land Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(aCurrentTime)));
//		System.out.println("ETA       : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(lAircraft.getETA())));
		
		
		// runway Control
		if(!lRunway.getRunwayOccupyingList().contains(lAircraft)) {
			lRunway.getRunwayOccupyingList().add(lAircraft);
		}
		
		
		// Exit Decision Speed
		double lExitDecisionSpeed = new CRunwayExitDecisionSpeedAPI().calculateRunwayExitDecisionSpeed(lAircraft, lRunway);
		if(lExitDecisionSpeed<=0) {
			lExitDecisionSpeed = 20.5778; // m/s == 40kts
		}
		
		
		// Get Touchdown Distance
		double lTouchdownDistance = lAircraft.getTouchdownDistance();
		if (lTouchdownDistance<0) {
			lTouchdownDistance           = new CTouchdownDistanceAPI().calculateTouchdownDistance(lAircraft, lRunway);
			if(lTouchdownDistance<0) {
				// MLAT Analysis
				// 20170602~20170828
				// ARR 34,620 aircraft			
				lTouchdownDistance              = 737.2 + (127.1*lAircraft.getRandomNumber()) - 127.1/2; 
			}
			lAircraft.setTouchdownDistance(lTouchdownDistance);
		}//if (lAircraft.getTouchdownDistance()<0) {

		
		
		// Create Flight Plan
		if(!(lFlightPlan.getNode(0) instanceof CTaxiwayNode)) {
			for(int loopRunwayNode = lRunway.getTaxiwayNodeList().size()-1 ; loopRunwayNode>=1 ; loopRunwayNode--) {
				CTaxiwayNode lNode = lRunway.getTaxiwayNodeList().get(loopRunwayNode);
				lNode.getVehicleWillUseList().add(lAircraft);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(0);
				lFlightPlan.insertPlanItem(0,lNode, cal, new CAltitude(0, EGEOUnit.FEET));			
			}
		}
		
		
	
		
		// Move While until amountTime reach incrementTimeStep
		while(lAmountTime*1000<aIncrementTimeStep) {
			
			// get Current Position
			double lXCurrent = lAircraft.getCurrentPosition().getXCoordination();
			double lYCurrent = lAircraft.getCurrentPosition().getYCoordination();
			double lXOrigin = lAircraft.getCurrentPosition().getXCoordination();
			double lYOrigin = lAircraft.getCurrentPosition().getYCoordination();
			
			// Extract Target Position == destination node
			double lXTarget = lFlightPlan.getNode(0).getCoordination().getXCoordination();//lFlightPlan.getNode(0).getCoordination().getXCoordination();
			double lYTarget = lFlightPlan.getNode(0).getCoordination().getYCoordination();//lFlightPlan.getNode(0).getCoordination().getYCoordination();

			
			
			// Calculate cos and sin 
			double distance = Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));

			double lCos   =  (lXTarget-lXCurrent)  / distance;
			double lSin   =  (lYTarget-lYCurrent)  /  distance;
			if(Double.isNaN(lCos)) lCos=1;
			if(Double.isNaN(lSin)) lSin=0;
		
			double lSpeedCurrent = lAircraft.getCurrentVelocity().getVelocity();


			/*
			 * Calculate Next Position 
			 */
			while(true) {
				

				// Un-Uniform Model after Touch down
				double lAccelCurrent = 0;
//				if( lAircraft.calculateDistanceBtwCoordination(lAircraft.getCurrentPosition(), lThresholdCoord) >= lTouchdownDistance) {
//				System.out.println(((long)(lAmountTime*1000 + lCurrentTimeFirstTime) - (lAircraft.getRunwayEntryTime()))/1000.0);
//				System.out.println(lAmountTime*1000);
//				System.out.println(lCurrentTimeFirstTime);
//				System.out.println(lAircraft.getRunwayEntryTime());
				lAccelCurrent  = lLandingPerformance.calculateTargetAcceleration(lPerformance.getWTC(), lSpeedCurrent, lAircraft.getRandomNumber(),  lAircraft.getTimeFromThreshold());
//				}


				// Color and Status change
				if(lAircraft.getMovementMode()!=EAircraftMovementMode.LANDING) {
						lAircraft.setMovementMode(EAircraftMovementMode.LANDING);						

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
				
				
				// Update Time
				lAmountTime += deltaT;
				lAircraft.setTimeFromThreshold(lAircraft.getTimeFromThreshold() + deltaT);
				
				
				// Verify next Point overshoot next node
				if(!SValidateRangeChecker.validDataInRange(lXNext,lXOrigin,lXTarget) || !SValidateRangeChecker.validDataInRange(lYNext,lYOrigin,lYTarget)) {
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
				if(lAmountTime*1000>=aIncrementTimeStep) {
					break;
				}
				
			
				// Escape when decision speed
				if(lSpeedCurrent <= lExitDecisionSpeed) {				
					lAircraft.setTimeFromThreshold(-9999);
					lAircraft.setMoveState(new CAircraftOnRunwayAfterLandingMoveState());
					lAircraft.doMoveVehicle((long)(lAmountTime*1000));					
					return;
				}
				
			} //while(true) {
			
			
			// Verify Next Node or not
			if(lFlightPlan.getNode(0).getCoordination().getXCoordination() == lXCurrent && lFlightPlan.getNode(0).getCoordination().getYCoordination() == lYCurrent) {
//				System.out.println(((CTaxiwayNode)lFlightPlan.getNode(0)).getVehicleWillUseList());
				((CTaxiwayNode)lFlightPlan.getNode(0)).getVehicleWillUseList().remove(lAircraft);
//				System.out.println(((CTaxiwayNode)lFlightPlan.getNode(0)).getVehicleWillUseList());
				lAircraft.setCurrentNode((CTaxiwayNode)lFlightPlan.getNode(0));
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));				
			
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






