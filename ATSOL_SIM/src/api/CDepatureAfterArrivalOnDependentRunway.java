package api;

import api.inf.IDepatureAfterArrivalOnDependentRunway;
import elements.facility.CRunway;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.state.CAircraftLandingMoveState;

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
 * @date : 2019. 5. 9.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 5. 9. : Coded by S. J. Yun.
 *
 *
 */

public class CDepatureAfterArrivalOnDependentRunway implements IDepatureAfterArrivalOnDependentRunway{

	public boolean issueTakeoffClearance(CRunway aDependentRunway, CAircraft aDepartureAircraft, CAircraft aArrivalAircraft) {
		
		// Takeoff after touchdown
		if(!(aArrivalAircraft.getMoveState() instanceof CAircraftLandingMoveState)) {
			return false;
		}
		
		
		
		return true;
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






