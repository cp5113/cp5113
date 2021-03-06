package api;

import api.inf.IPushbackDirection;
import elements.facility.CAirport;
import elements.facility.CTaxiwayNode;
import elements.mobile.human.AATCController;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;

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
 * @date : 2019. 5. 6.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 5. 6. : Coded by S. J. Yun.
 *
 *
 */

public class CPushbackDirectionAPI implements IPushbackDirection {

	@Override
	public CTaxiwayNode assingPushbackDirection(CAircraft aAircraft, long aCurrentTimeInMilliSeconds) {
		if(aAircraft.getCurrentFlightPlan().getDepartureSpot().toString().equals("103")) {
			CFlightPlan lFlightPlan = aAircraft.getCurrentFlightPlan();
			CAirport	lAirport    = (CAirport) lFlightPlan.getDepartureSpot().getOwnerObject();
			
			CTaxiwayNode lOutput = null;
			for(CTaxiwayNode loopNode : lAirport.getTaxiwayNodeList()) {
				if(loopNode.toString().equalsIgnoreCase("AS_16_1")) {
					lOutput = loopNode;
					return lOutput;
				}
			}
		}
		
		return null;
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






