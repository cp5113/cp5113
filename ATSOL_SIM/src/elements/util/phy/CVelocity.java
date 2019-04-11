/**
 * ATSOL_SIM
 * elements.util.phy
 * CVelocity.java
 */
package elements.util.phy;
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

/**
 * @author S. J. Yun
 *
 */
public class CVelocity {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private double				iVelocity;
	private EVelocityUnit		iUnit = EVelocityUnit.METER_PER_SEC;
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Apr 8, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Apr 8, 2019 : Coded by S. J. Yun.
	 */
	public CVelocity(double aVelocity, EVelocityUnit aUnit) {
		super();
		iVelocity = aVelocity;
		iUnit = aUnit;
	}
	public synchronized double getVelocity() {
		return iVelocity;
	}
	public synchronized void setVelocity(double aVelocity) {
		iVelocity = aVelocity;
	}
	public synchronized EVelocityUnit getUnit() {
		return iUnit;
	}
	public synchronized void setUnit(EVelocityUnit aUnit) {
		iUnit = aUnit;
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






