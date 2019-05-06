package util.performance;

import java.util.ArrayList;
import java.util.List;

import elements.COccupyingInform;
import elements.facility.CAirport;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.mobile.vehicle.state.SValidateRangeChecker;
import elements.network.ANode;
import elements.network.INode;
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
 * @date : Apr 9, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 9, 2019 : Coded by S. J. Yun.
 *
 *
 */



public class CApproachAircraftPerformance {
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
	/**
	 * Calculate Estimated Taxiing Time using Un-Uniform Model
	 */
	public static long estimateApproachLegFlightTime(CAircraft aAircraft, double deltaT, long eventStartT){
		double deltaTOrigin = deltaT;
				
				
		// 	Get base information
		CAircraftType lACType = (CAircraftType)aAircraft.getVehcleType();
		CFlightPlan   lFlightPlan = aAircraft.getCurrentFlightPlan();
		
		
		double  lSpeedCurrent 	= aAircraft.getCurrentVelocity().getVelocity();
		
		double lTotalDistance   = 0.0;
		

		// Calculate Remaining Distance
		CCoordination lCurrentPos = aAircraft.getCurrentPosition();
		for(INode loopNode : lFlightPlan.getNodeList()) {
			CCoordination lTargetPos = loopNode.getCoordination();
			
			// Calculate
			double tempDist  = aAircraft.calculateDistanceBtwCoordination(lCurrentPos, lTargetPos);
			lTotalDistance += tempDist;
			
			// update
			lCurrentPos = lTargetPos;
			
			if(loopNode.equals(lFlightPlan.getArrivalRunway().getTaxiwayNodeList().get(0))){
				break;
			}
		}
		
		
		
		
		// Calculate Time with Constant Speed
		double lFlightTimeInSecond = lTotalDistance/lSpeedCurrent;
		long   lFlightTimeInMilliSecond = (long)(lFlightTimeInSecond * 1000);
		
		
		
		
		return lFlightTimeInMilliSecond + eventStartT;//(long)(lTimeDuration*1000) + eventStartT;
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
}


// Simulation
//// Initialize
//double  lTimeDuration = 0;
//double lTotalDistance = 0;		
//double lXCurrent = aAircraft.getCurrentPosition().getXCoordination();
//double lYCurrent = aAircraft.getCurrentPosition().getYCoordination();
//double lXOrigin  = aAircraft.getCurrentPosition().getXCoordination();
//double lYOrigin  = aAircraft.getCurrentPosition().getYCoordination();
//
//

// Calculate		
//int count = 0;
//for(INode loopNode : lFlightPlan.getNodeList()) {
//	if(loopNode instanceof CAirport) {
//		break;
//	}
//	
//
//
//	while(true) {
//		
//		
//		// Extract Target Position == destination node
//		double lXTarget = loopNode.getCoordination().getXCoordination();//lFlightPlan.getNode(0).getCoordination().getXCoordination();
//		double lYTarget = loopNode.getCoordination().getYCoordination();//lFlightPlan.getNode(0).getCoordination().getYCoordination();
//
//		
//		// Calculate cos and sin 
//		double lCos   =  (lXTarget-lXCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
//		double lSin   =  (lYTarget-lYCurrent)  /  Math.sqrt((lXTarget-lXCurrent)*(lXTarget-lXCurrent) + (lYTarget-lYCurrent) * (lYTarget-lYCurrent));
//		if(Double.isNaN(lCos)) lCos=1;
//		if(Double.isNaN(lSin)) lSin=0;
//		
//		
//		// Divide Speed and Acceleration
//		double lSpeedCurrentX = lSpeedCurrent * lCos;
//		double lSpeedCurrentY = lSpeedCurrent * lSin;
//		double lAccelCurrentX = 0;
//		double lAccelCurrentY = 0;
//		
//		// Calcualte Vx, Vy = V0 + at
//		double lSpeedNextX	  = lSpeedCurrentX + lAccelCurrentX * deltaT;
//		double lSpeedNextY	  = lSpeedCurrentY + lAccelCurrentY * deltaT;
//		
//		// Calculate Next Position = S0 + v0t + 1/2at^2
//		double lXNext		  = lXCurrent + lSpeedCurrentX * deltaT + 1/2 * lAccelCurrentX * deltaT * deltaT;
//		double lYNext		  = lYCurrent + lSpeedCurrentY * deltaT + 1/2 * lAccelCurrentY * deltaT * deltaT;
//		
//		
//		// Verify next Point overshoot next node
////		if(!SValidateRangeChecker.validDataInRange(lXNext,lXOrigin,lXTarget) || !SValidateRangeChecker.validDataInRange(lYNext,lYOrigin,lYTarget)) {					
////			// Reduce Delta T
////			if(deltaT * 0.1 <=0.00001) {
////				
////			}else {
////				deltaT *=0.1;
////				continue;
////			}
////		}
//		
//		
//		// Calculate Distance
//		lTotalDistance += Math.sqrt((lXCurrent - lXNext) * (lXCurrent - lXNext) + (lYCurrent - lYNext) * (lYCurrent - lYNext)); 
//		
//		// Update Current X, Y
//		lXCurrent = lXNext;
//		lYCurrent = lYNext;
//		
//		
//		// update duration
//		lTimeDuration += deltaT;
//		
//		
//		// When deltaT is less than 0.001sec == 1milliseconds
//		// The Corner Point
//		// Make Resolution 1m
//		if(Math.abs(lXTarget-lXNext) < 10 && Math.abs(lYTarget-lYNext)< 10) {
//			
//			
//			lXCurrent = lXTarget;
//			lYCurrent = lYTarget;
//			
//			// Restore Delta t to original
//			deltaT = deltaTOrigin;
//			
//			// Escape Loop
//			break;
//		}
//		
//	}	//while(true) {	
//	
//
//	
//	
//	// Update Origin
//	lXOrigin = loopNode.getCoordination().getXCoordination();
//	lYOrigin = loopNode.getCoordination().getYCoordination();
//	
//	
//}
//
//
//


