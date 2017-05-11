/**
 * ATSOL_SIM
 * elements.facility
 * AFacility.java
 */
package elements.facility;
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
 * @date : May 10, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 10, 2017 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
abstract public class AFacility {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private	String 			iName;
	private	int				iFacilityID = CElementFacilityCounter.getNumberOfElements();				
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public AFacility(){
		iFacilityID += 1; 
	}
	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */
	
	public int getFacilityID(){
		return iFacilityID;
	}

	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






