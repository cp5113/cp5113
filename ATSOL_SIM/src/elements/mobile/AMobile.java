/**
 * ATSOL_SIM
 * elements.mobile
 * AMobile.java
 */
package elements.mobile;

import elements.CElementCounter;

/**
 * 
 * The Mobile abstract class
 * 
 * <li> Human </li>
 * <li> Vehicle </li>
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
 *
 *  <li>VARIABLE_NAME : Constant variable </li>
 * </ul>
 * </p>
 * 
 * 
 * @date : May 9, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 9, 2017 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
public abstract class AMobile {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	private int iElementID = CElementCounter.getNumberOfElements();;
	
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	/**
	 * getID
	 * 
	 * Getting Object ID in Simulation
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public int getElementID(){
		return iElementID;
		
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






