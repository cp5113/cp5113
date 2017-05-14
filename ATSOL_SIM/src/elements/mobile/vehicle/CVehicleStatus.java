/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CVehicleStatus.java
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

import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.CDegree;
import elements.util.phy.CVelocity;

/**
 * @author S. J. Yun
 *
 */
public class CVehicleStatus {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private	CCoordination			iCoordinates;
	private	CAltitude				iAltitude;	
	
	private	CDegree					iHeading;
	private CVelocity				iVelocity;
	private CVelocity				iAcceleration;
	private	CAltitude				iVerticalSpeeed;
	
	
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






