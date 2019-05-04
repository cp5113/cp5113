package util.performance;

import elements.facility.CTaxiwayLink;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.mobile.vehicle.state.SValidateRangeChecker;
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
 * @date : 2019. 5. 3.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 5. 3. : Coded by S. J. Yun.
 *
 *
 */

public class CLineupPerformance {
	/*
	================================================================

			           Initializing Section

	================================================================
	 */
	public long estimateLineUpTime(CAircraft aAircraft, double deltaT, long eventStartT) {
		// Initial variable				
		double lAmountTime = 0;

		// Get Initial Information
		CAircraft   	lAircraft   		 = (CAircraft) aAircraft;
		CAircraftPerformance lPerformance    = (CAircraftPerformance) ((CAircraftType)lAircraft.getVehcleType()).getPerformance();
		
		double			lAccelMax			 = lPerformance.getAccelerationOnGroundMax();
		double			lDecelMax			 = lPerformance.getDecelerationOnGroundMax();
		

		// Calculate remaining Distance to Destination Node
		double lRemainingDistance = lAircraft.calculateRemainingRouteDistance();				
		double lLineupRefLength = lAircraft.calculateDistanceBtwNodes(lAircraft.getRunwayEntryPoint(),lAircraft.getRunwayEntryPointReference());
		lRemainingDistance = lAircraft.getVehcleType().getLength() - (lLineupRefLength - lRemainingDistance);
		
		double lMovingDistance = 0;
		double lSpeedCurrent   = lAircraft.getCurrentVelocity().getVelocity();
		while(lRemainingDistance-lMovingDistance>0.1) {
		

				// Set Target Taxiing Speed
				double lSpeedTarget = 10.0*0.514444; // 10 KNOTS				


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


				// Calculate Next Position = S0 + v0t + 1/2at^2
				lMovingDistance       = lMovingDistance + lSpeedCurrent * deltaT + 1/2 * lAccelCurrent * deltaT * deltaT;				


				// Update Current Data				
				lSpeedCurrent 	= lSpeedCurrent + lAccelCurrent*deltaT; 
				
				
				// Update Time
				lAmountTime += deltaT;
				

		} //while(lAmountTime<aIncrementTimeStep) {

		//			System.out.println("AC : " + lAircraft);
		//			System.out.println("CurrentSpeed : " + lAircraft.getCurrentVelocity().getVelocity());		
		//			System.out.println("Remaining : " + lAircraft.getRoutingRemainingDistance() + "m");

		
		
		return (long)(lAmountTime*1000) + eventStartT;
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






