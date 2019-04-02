/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CFlightPlan.java
 */
package elements.mobile.vehicle;
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
 * @date : May 13, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 13, 2017 : Coded by S. J. Yun.
 *
 *
 */

import elements.facility.CRunway;
import elements.facility.CSpot;
import elements.network.INode;
import sim.clock.CVehicleAndPlanAndTime;

/**
 * @author S. J. Yun
 *
 */
public class CFlightPlan extends AVehiclePlan{
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private String iCallsign; 
	private CRunway iDepartureRunway;
	private CRunway iArrivalRunway;
	private CSpot	iArrivalSpot;
	private CSpot   iDepartureSpot;
	
	
	public CFlightPlan(AVehicle aOwner, INode aOrigination, INode aDestination) {
		super(aOwner, aOrigination, aDestination);
		// TODO Auto-generated constructor stub
	}
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public String getCallsign() {
		return iCallsign;
	}
	public void setCallsign(String aCallsign) {
		iCallsign = aCallsign;
	}

	public CRunway getDepartureRunway() {
		return iDepartureRunway;
	}

	public void setDepartureRunway(CRunway aDepartureRunway) {
		iDepartureRunway = aDepartureRunway;
	}

	public CRunway getArrivalRunway() {
		return iArrivalRunway;
	}

	public void setArrivalRunway(CRunway aArrivalRunway) {
		iArrivalRunway = aArrivalRunway;
	}

	public CSpot getArrivalSpot() {
		return iArrivalSpot;
	}

	public void setArrivalSpot(CSpot aArrivalSpot) {
		iArrivalSpot = aArrivalSpot;
	}

	public CSpot getDepartureSpot() {
		return iDepartureSpot;
	}

	public void setDepartureSpot(CSpot aDepartureSpot) {
		iDepartureSpot = aDepartureSpot;
	}

	@Override
	public String toString() {
		return iCallsign;
		
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






