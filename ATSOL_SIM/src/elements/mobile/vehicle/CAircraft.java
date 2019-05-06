package elements.mobile.vehicle;

import elements.facility.CRunway;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.AAircraft;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ALink;
import elements.network.ANode;
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
 * @date : Apr 3, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 3, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CAircraft extends AAircraft {


	@Override
	public double getCurrentVelocityMps() {

		return iCurrentVelocity.getVelocity();
	}

	@Override
	public double getCurrentAltitudeFeet() {

		return iCurrentAltitude.getAltitude();
	}

	@Override
	public CFlightPlan getCurrentFlightPlan() {

		return (CFlightPlan) iCurrentPlan;
	}

	@Override
	public CFlightPlan getNextFlightPlan() {
		if(iPlanList.size()>1) {
			return (CFlightPlan) iPlanList.get(1);			
		}
		return null;
	}

	@Override
	public void setDepartureRunway(CRunway aDepartureRunway) {
		((CFlightPlan)(iCurrentPlan)).setDepartureRunway(aDepartureRunway);
	}

	@Override
	public void setArrivalRunway(CRunway aArrivalRunway) {
		((CFlightPlan)(iCurrentPlan)).setArrivalRunway(aArrivalRunway);
	}



	@Override
	public CRunway getDepartureRunway() {
		// TODO Auto-generated method stub
		return ((CFlightPlan)(iCurrentPlan)).getDepartureRunway();
	}



	@Override
	public CRunway getArrivalRunway() {
		// TODO Auto-generated method stub
		return ((CFlightPlan)(iCurrentPlan)).getArrivalRunway();
	}


	@Override
	public EAircraftMovementStatus getMovementStatus() {
		return iMovementStatus;
	}


	@Override
	public void setMovementStatus(EAircraftMovementStatus aMovementStatus) {
		iMovementStatus = aMovementStatus;
	}



	@Override
	public EAircraftMovementMode getMovementMode() {
		return iMovementMode;
	}



	@Override
	public void setMovementMode(EAircraftMovementMode aMovementMode) {
	
		iMovementMode = aMovementMode;
	}

	public long calculateELDT(long aCurrentTimeInMilliSeconds,CRunway aRunway) {
		double lDistanceToThreshold = this.calculateDistanceBtwCoordination(this.getCurrentPosition(), aRunway.getTaxiwayNodeList().get(0).getCoordination());
		
		long   lELDT				= iCurrentTimeInMilliSecond + (long)(lDistanceToThreshold/this.getCurrentVelocity().getVelocity() * 1000);
		return lELDT;
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






