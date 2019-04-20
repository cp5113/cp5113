/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraftType.java
 */
package elements.property;
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
 * @date : May 15, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 15, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;

import elements.table.ITableAble;

/**
 * @author S. J. Yun
 *
 */
public class CAircraftType extends AVehicleType implements ITableAble {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	
	public CAircraftType() {
		
	}

	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Mar 25, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 25, 2019 : Coded by S. J. Yun.
	 */
	public CAircraftType(String aAircraftType, 
			CAircraftPerformance aAircraftPerformance, String aRange) {
		super();
		
		iType = aAircraftType;		
		iPerformance = aAircraftPerformance;
		iRange = aRange;
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






