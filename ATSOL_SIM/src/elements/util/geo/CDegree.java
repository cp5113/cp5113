/**
 * ATSOL_SIM
 * elements.util.geo
 * CDegree.java
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
public class CDegree {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private double		iDegree;
	private EUnit		iUnit = EUnit.DEGREE;
	private EVariation	iVariation;

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public CDegree (double aDegree){
		iDegree = aDegree;
	}

	/**
	 * getiDegree
	 * 
	 * Do What
	 * 
	 * @return iDegree double
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public double getDegree() {
		return iDegree;
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
	 * setiDegree
	 * 
	 * Do What
	 * 
	 * @param aIDegree the iDegree to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public void setDegree(double aIDegree) {
		iDegree = aIDegree;
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

	/**
	 * getiVariation
	 * 
	 * Do What
	 * 
	 * @return iVariation EVariation
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public EVariation getVariation() {
		return iVariation;
	}

	/**
	 * setiVariation
	 * 
	 * Do What
	 * 
	 * @param aIVariation the iVariation to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public void setVariation(EVariation aIVariation) {
		iVariation = aIVariation;
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






