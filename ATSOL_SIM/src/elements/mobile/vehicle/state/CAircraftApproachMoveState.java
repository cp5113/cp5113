package elements.mobile.vehicle.state;

import elements.facility.CAirport;
import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;

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
 * @date : 2019. 4. 30.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 4. 30. : Coded by S. J. Yun.
 *
 *
 */

public class CAircraftApproachMoveState implements IVehicleMoveState {

	@Override
	public void doMove(long aIncrementTimeStep, long aCurrentTime, AVehicle aThisVechicle) {
		double deltaT = 0.01;
		double deltaTOrigin = deltaT;
		double lAmountTime = 0;
		
		
		// 	Get base information
		CAircraft  lAircraft = (CAircraft) aThisVechicle;
		CAircraftType lACType = (CAircraftType)lAircraft.getVehcleType();
		CFlightPlan   lFlightPlan = lAircraft.getCurrentFlightPlan();
		
			
		double  lSpeedCurrent 		= lAircraft.getCurrentVelocity().getVelocity();
		

		
		
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
			double lCos   =  (lXTarget-lXCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			double lSin   =  (lYTarget-lYCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
			if(Double.isNaN(lCos)) lCos=1;
			if(Double.isNaN(lSin)) lSin=0;
			
			
			/*
			 * Calculate Next Position 
			 */
			while(true) {

				// Set Acceleration
				double lAccelCurrent = 0;
				
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
				
			} // while(true) {
			
			
			// Verify Next Node or not
			if(lFlightPlan.getNode(0).getCoordination().getXCoordination() == lXCurrent && lFlightPlan.getNode(0).getCoordination().getYCoordination() == lYCurrent) {
				// When reach end of a Node
				// Remove this node from flight plan				
				lAircraft.setCurrentNode((ANode) lFlightPlan.getNode(0));
				lFlightPlan.removePlanItem(lFlightPlan.getNode(0));
				if(lFlightPlan.getNode(0) instanceof CAirport) {
					lAircraft.setMovementMode(EAircraftMovementMode.LANDING);
					lAircraft.setMovementStatus(EAircraftMovementStatus.LANDING);
					lAircraft.setMoveState(new CAircraftLandingMoveState());
					lAircraft.setRunwayEntryTime((long)(lAmountTime*1000));
					lAircraft.doMoveVehicle((long)(lAmountTime*1000));
					return;
				}
			}
			
			
			
			
			
		} // while(lAmountTime*1000<aIncrementTimeStep) {
		

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






