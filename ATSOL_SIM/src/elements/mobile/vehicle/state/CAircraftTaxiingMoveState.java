package elements.mobile.vehicle.state;

import elements.AElement;
import elements.facility.CRunway;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.util.geo.CCoordination;

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
		CAircraftPerformance lPerformance    = ((CAircraftType)lAircraft.getVehcleType()).getAircraftPerformance(); 
		CFlightPlan 	lFlightPlan 		 = (CFlightPlan) lAircraft.getCurrentPlan();
		double			lAccelMax			 = lPerformance.getAccelerationOnGroundMax();
		
		// ignore when no flight Plan ( == AC is at Arrival Spot)
		if(lFlightPlan.getNodeList().size()==0) {
			return;
		}
		
		// Move While until amountTime reach incrementTimeStep
		while(lAmountTime*1000<aIncrementTimeStep) {
			// get Current Position
			double lXCurrent = lAircraft.getCurrentPostion().getXCoordination();
			double lYCurrent = lAircraft.getCurrentPostion().getYCoordination();
			double lXOrigin = lAircraft.getCurrentPostion().getXCoordination();
			double lYOrigin = lAircraft.getCurrentPostion().getYCoordination();
			
			// Extract Target Position == destination node
			double lXTarget = lFlightPlan.getNode(0).getCoordination().getXCoordination();
			double lYTarget = lFlightPlan.getNode(0).getCoordination().getYCoordination();
			// Verify Runway
			if(lFlightPlan.getNode(0) instanceof CRunway) {
				return;
			}
			
			
			// Calculate cos and sin 
			double lCos   =  (lXTarget-lXCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			double lSin   =  (lYTarget-lYCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			
			
			// Get Taxiway Link Information to get Taxiing Speed Limit
			CTaxiwayLink lTaxiwayLink = null; 
			try {
				lTaxiwayLink = (CTaxiwayLink)(lAircraft.getRoutingLinkInfoUsingNode((ANode) lFlightPlan.getNode(0)));
			}catch(Exception e) {
				System.err.println("CAircraftTaxiingMoveState : It seems that the aircraft enter Runway");
			}
			// Set Target Taxiing Speed
			double lSpeedTarget = 10;
			if(lTaxiwayLink.getSpeedLimitKts()>0 && lTaxiwayLink.getSpeedLimitKts()<=lPerformance.getTaxiingSpeedMax()) {
				lSpeedTarget = lTaxiwayLink.getSpeedLimitKts();
			}else {
				lSpeedTarget = lPerformance.getTaxiingSpeedNorm();
			}
			
			double lSpeedCurrent = lAircraft.getCurrentVelocity().getVelocity();
			
			/*
			 * Calculate Next Position using Un-Uniform Model
			 */
			while(true) {
				// Calculate Acceleration Speed 
				double lAccelCurrent  = (lAccelMax/lSpeedTarget) * (lSpeedTarget - lSpeedCurrent);

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
				if(!validDataInRange(lXNext,lXOrigin,lXTarget) || !validDataInRange(lYNext,lYOrigin,lYTarget)) {
					
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
				lAircraft.getCurrentPostion().setXYCoordination(lXCurrent, lYCurrent);
				lAircraft.getCurrentVelocity().setVelocity(lSpeedCurrent);
				
				// Update Time
				lAmountTime += deltaT;
//				System.out.println(lAmountTime);
//				System.out.println(lXTarget + "," + lYTarget);
//				System.out.println(lXCurrent + "," + lYCurrent);
//				System.out.println();
				
				// When deltaT is less than 0.001sec == 1milliseconds
				// The Corner Point
				// Make Resolution 1m
				if(Math.abs(lXTarget-lXNext) < 0.01 || deltaT<=0.0001) {
					lXCurrent = lXTarget;
					lYCurrent = lYTarget;
					lAircraft.getCurrentPostion().setXYCoordination(lXCurrent, lYCurrent);
					lAircraft.getCurrentVelocity().setVelocity(lSpeedCurrent);
					
					// Restore Delta t to original
					deltaT = deltaTOrigin;
					
					// Escape Loop
					break;
				}
				
				// Escape when amount T over incrementTimeStep
				if(lAmountTime*1000 > aIncrementTimeStep) {
					break;
				}
				
			} //while(true) {
			
			
			// Verify Next Node or not
			if(lFlightPlan.getNode(0).getCoordination().getXCoordination() == lXCurrent && lFlightPlan.getNode(0).getCoordination().getYCoordination() == lYCurrent) {
				// When reach end of taxiway link
				// Remove this aircraft from taxiway Link shcedule
				lTaxiwayLink.removeFromOccupyingSchedule(lAircraft);
				
				// When reach end of taxiway link
				// Remove this node from flight plan				
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
//				System.out.println();
			}
			
		} //while(lAmountTime<aIncrementTimeStep) {
		
		
	
	}
	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */
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
	
							The Others
	
	================================================================
	 */
}






