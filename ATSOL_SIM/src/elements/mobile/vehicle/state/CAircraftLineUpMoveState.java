package elements.mobile.vehicle.state;

import elements.facility.CTaxiwayLink;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EMode;
import elements.util.geo.CCoordination;
import javafx.scene.paint.Color;

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
 * @date : Apr 23, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 23, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CAircraftLineUpMoveState implements IVehicleMoveState{

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
		// ignore when no flight Plan ( == AC is at Arrival Spot)
		if(lFlightPlan.getNodeList().size()==0) {
			return;
		}
//		if(lAircraft.toString().equalsIgnoreCase("B6317")) {
//			System.out.println(lAircraft.getRoutingInfo());
//			System.out.println(lAircraft.getCurrentFlightPlan().getNodeList());
//			System.out.println(lAircraft.getConflictVehicle());
//			System.out.println(lAircraft.getCurrentVelocity().getVelocity());
//			System.out.println();
//		}

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

			// Set Target Taxiing Speed
			double lSpeedTarget = 10.0*0.514444; // 10 KNOTS
			double lSpeedCurrent = lAircraft.getCurrentVelocity().getVelocity();


			// Get Taxiway Link Information to get Taxiing Speed Limit
			CTaxiwayLink lTaxiwayLink = null; 
			try {
				lTaxiwayLink = (CTaxiwayLink)(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
			}catch(Exception e) {
				System.err.println("CAircraftTaxiingMoveState : It seems that the aircraft enter Runway");
			}
			
			
			/*
			 * Calculate Next Position 
			 */
			while(true) {
				// Calculate remaining Distance to Destination Node
				double lRemainingDistance = lAircraft.calculateRemainingRouteDistance();
				if(lAircraft.getMode()==EMode.DEP && lAircraft.getMovementMode()==EAircraftMovementMode.LINEUP && lAircraft.getCurrentNode().equals(lAircraft.getRunwayEntryPointReference())) {
					double lLineupRefLength = lAircraft.calculateDistanceBtwNodes(lAircraft.getRunwayEntryPoint(),lAircraft.getRunwayEntryPointReference());
					lRemainingDistance = lAircraft.getVehcleType().getLength() - (lLineupRefLength - lRemainingDistance);
				}
				
				
				
				double lStoppingDistanceCurrentSpeed = lAircraft.calculateStoppingDistance(lSpeedCurrent,lDecelMax);
				

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



				// Make Integer
				if(lAccelCurrent>=0 && lSpeedTarget - lSpeedCurrent<=0.0001) {
					lSpeedCurrent = lSpeedTarget;
					lAccelCurrent = 0;				
				}else if(lAccelCurrent<=0 && (lSpeedCurrent<=0.01 || lRemainingDistance<=0.01)) {
					lSpeedCurrent = 0;
					lAccelCurrent = 0;
				}


				// Color and Status change
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

					//						System.out.println("Damn it");

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

				//					CAtsolSimGuiControl.getInstance().drawDrawingObjectList();

				// Update Time
				lAmountTime += deltaT;
				//					System.out.println(lAmountTime);
				//					System.out.println(lXTarget + "," + lYTarget);
				//					System.out.println(lXCurrent + "," + lYCurrent);
				//					System.out.println();



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
				//					if(lAircraft.getMovementMode() != EAircraftMovementMode.PUSHBACK) {
				lTaxiwayLink.removeFromOccupyingSchedule(lAircraft);
				lAircraft.getCurrentNode().getVehicleWillUseList().remove(lAircraft.getCurrentNode().getVehicleWillUseList().indexOf(lAircraft));
				//					}

				// When reach end of taxiway link
				// Remove this node from flight plan
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
				lAircraft.removeRoutingInfo(0);

				// Update Current Location
				lAircraft.setCurrentLink(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
				lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));



			}

		} //while(lAmountTime<aIncrementTimeStep) {

		//			System.out.println("AC : " + lAircraft);
		//			System.out.println("CurrentSpeed : " + lAircraft.getCurrentVelocity().getVelocity());		
		//			System.out.println("Remaining : " + lAircraft.getRoutingRemainingDistance() + "m");

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






