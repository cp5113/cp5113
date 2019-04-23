package util.performance;

import java.util.ArrayList;
import java.util.List;

import elements.COccupyingInform;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
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
 * @date : Apr 9, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 9, 2019 : Coded by S. J. Yun.
 *
 *
 */



public class CUnUniformModelPerformance {
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
	public static List<COccupyingInform> estimateTaxiingTime(CAircraft aAircraft, double deltaT, long eventStartT){
		// 	Get base information
		CAircraftType lACType = (CAircraftType)aAircraft.getVehcleType();
		double  lNormSpeed    = ((CAircraftPerformance)lACType.getPerformance()).getTaxiingSpeedNorm();
		double  lMaxAccel     = ((CAircraftPerformance)lACType.getPerformance()).getAccelerationOnGroundMax();
		double lCurrentV = aAircraft.getCurrentVelocity().getVelocity();
		
		
		// Create Schedule
		List<COccupyingInform> lUseSchedule = new ArrayList<COccupyingInform>();
		
		// Initialize
		double  lTimeDuration = 0;
		double lTotalDistance = 0;
		double lCumDistance   = 0;
		
		for(int loopLink = 0; loopLink < aAircraft.getRoutingInfoLink().size(); loopLink++) {
			CTaxiwayLink lLink = (CTaxiwayLink) aAircraft.getRoutingInfoLink().get(loopLink);
			CTaxiwayNode lStartNode = (CTaxiwayNode) aAircraft.getRoutingInfo().get(loopLink);
			CTaxiwayNode lEndNode = (CTaxiwayNode) aAircraft.getRoutingInfo().get(loopLink+1);
			lTotalDistance += lLink.getDistance();
			
			
			// Create a Schedule
			COccupyingInform lSchedule = new COccupyingInform(lLink,(long)(lTimeDuration*1000)+eventStartT, -9999, 
					                                          aAircraft, lEndNode);
			
			// Verify Taxi Speed
			double lTargetSpeed = 10;
			if(lLink.getSpeedLimitKts()>0 && 
					lLink.getSpeedLimitKts()<=((CAircraftPerformance)lACType.getPerformance()).getTaxiingSpeedMax()) {
				lTargetSpeed = lLink.getSpeedLimitKts();
			}else {
				lTargetSpeed = lNormSpeed;
			}
			
			while(true) {
				
				// Calculate accelereation NonUniform Model
				double lCurrnetAccel = (lMaxAccel/lTargetSpeed) * (lTargetSpeed - lCurrentV);
				
				// Calculate new speed
				double lNewV = lCurrentV + lCurrnetAccel * deltaT;
				
				// Calculate Movement == Cum Distance
				lCumDistance = lCumDistance + lCurrentV * deltaT + (1/2) * lCurrnetAccel * deltaT * deltaT; 
				
				// Update Current Speed
				lCurrentV = lNewV;
				
				// Time Update
				lTimeDuration += deltaT;
				
				// Verify to pass next node
				if (lTotalDistance<=lCumDistance) {
					break;
				}
			} // while(true) {
			
			// Merge to Schedule
			lSchedule.setEndTime((long)(lTimeDuration*1000) + eventStartT);
			lUseSchedule.add(lSchedule);
			
		}// for(int loopLink = 0; loopLink < aAircraft.getRoutingInfoLink().size(); loopLink++) {
		
		
		
		
		return lUseSchedule;
	}
	
	
	
	public static List<COccupyingInform> estimatePushbackTime(CAircraft aAircraft, double deltaT, long eventStartT){
		// 	Get base information
		CAircraftType lACType = (CAircraftType)aAircraft.getVehcleType();
		double  lNormSpeed    = ((CAircraftPerformance)lACType.getPerformance()).getTaxiingSpeedNorm();
		double  lMaxAccel     = ((CAircraftPerformance)lACType.getPerformance()).getAccelerationOnGroundMax();
		double lCurrentV = aAircraft.getCurrentVelocity().getVelocity();
		
		
		// Create Schedule
		List<COccupyingInform> lUseSchedule = new ArrayList<COccupyingInform>();
		
		// Initialize
		double  lTimeDuration = 0;
		double lTotalDistance = 0;
		double lCumDistance   = 0;
		
		for(int loopLink = 0; loopLink < aAircraft.getRoutingInfoLink().size(); loopLink++) {
			CTaxiwayLink lLink = (CTaxiwayLink) aAircraft.getRoutingInfoLink().get(loopLink);
			CTaxiwayNode lStartNode = (CTaxiwayNode) aAircraft.getRoutingInfo().get(loopLink);
			CTaxiwayNode lEndNode = (CTaxiwayNode) aAircraft.getRoutingInfo().get(loopLink+1);
			lTotalDistance += lLink.getDistance();
			
			
			// Create a Schedule
			COccupyingInform lSchedule = new COccupyingInform(lLink,(long)(lTimeDuration*1000)+eventStartT, -9999, 
					                                          aAircraft, lEndNode);
			
			// Verify Taxi Speed
			double lTargetSpeed = 3.0 * 0.514444;
		
			
			while(true) {
				
				// Calculate accelereation NonUniform Model
				double lCurrnetAccel = (lMaxAccel/lTargetSpeed) * (lTargetSpeed - lCurrentV);
				
				// Calculate new speed
				double lNewV = lCurrentV + lCurrnetAccel * deltaT;
				
				// Calculate Movement == Cum Distance
				lCumDistance = lCumDistance + lCurrentV * deltaT + (1/2) * lCurrnetAccel * deltaT * deltaT; 
				
				// Update Current Speed
				lCurrentV = lNewV;
				
				// Time Update
				lTimeDuration += deltaT;
				
				// Verify to pass next node
				if (lTotalDistance<=lCumDistance) {
					break;
				}
			} // while(true) {
			
			// Merge to Schedule
			lSchedule.setEndTime((long)(lTimeDuration*1000) + eventStartT);
			lUseSchedule.add(lSchedule);
			
		}// for(int loopLink = 0; loopLink < aAircraft.getRoutingInfoLink().size(); loopLink++) {
		
		
		
		
		return lUseSchedule;
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






