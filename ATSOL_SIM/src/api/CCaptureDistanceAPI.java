package api;

import api.inf.ICaptureDistance;
import elements.facility.CRunway;
import elements.mobile.human.IATCController;
import elements.mobile.vehicle.CAircraft;

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

public class CCaptureDistanceAPI implements ICaptureDistance {

	@Override
	public double assignCaptureDistanceToLineup(CRunway aRunway, IATCController aController, CAircraft aFollowingAircraft) {
		return 3*1852;
	}
	
	
	public double assignCaptureDistanceToCrossing(CRunway aRunway, IATCController aController, CAircraft aFollowingAircraft) {
		return 2*1852;
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






