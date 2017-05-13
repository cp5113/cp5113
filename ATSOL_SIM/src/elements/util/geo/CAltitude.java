/**
 * ATSOL_SIM
 * elements.util.geo
 * CAltitude.java
 */
package elements.util.geo;
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
 * @date : May 12, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 12, 2017 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
public class CAltitude {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private	double 	iAltitude;
	
	private EUnit	iUnit	= EUnit.FEET;


	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	/**
	 * getiAltitude
	 * 
	 * Do What
	 * 
	 * @return iAltitude double
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public double getAltitude() {
		return iAltitude;
	}

	/**
	 * getiUnit
	 * 
	 * Do What
	 * 
	 * @return iUnit EUnit
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public EUnit getUnit() {
		return iUnit;
	}

	/**
	 * setiAltitude
	 * 
	 * Do What
	 * 
	 * @param aIAltitude the iAltitude to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setAltitude(double aIAltitude) {
		iAltitude = aIAltitude;
	}

	/**
	 * setiUnit
	 * 
	 * Do What
	 * 
	 * @param aIUnit the iUnit to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public void setUnit(EUnit aIUnit) {
		iUnit = aIUnit;
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






