package api;

import api.inf.IRunwaySeparation;
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

public class CRunwaySeparationAPI implements IRunwaySeparation {

	@Override
	public long assignDepartureSparationTimeInMilliSeconds(CAircraft aPrevAircraft, CAircraft aNextAircraft) {
		// TODO Auto-generated method stub
		return 120*1000;
	}

	@Override
	public long assignArrivalSparationInMeter(CAircraft aPrevAircraft, CAircraft aNextAircraft) {
		// TODO Auto-generated method stub
		return 1852*5; // 5nm
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






