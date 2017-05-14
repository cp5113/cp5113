/**
 * ATSOL_SIM
 * elements.util
 * CCoordination.java
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

import math.basic.BasicMath;

/**
 * @author S. J. Yun
 *
 */
public class CCoordination {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private	double 	iXCoordination,iYCoordination;
	
	private EGEOUnit	iUnit	= EGEOUnit.TM;


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
	public CCoordination(double aIXCoordination, double aIYCoordination) {
		super();
		iXCoordination = aIXCoordination;
		iYCoordination = aIYCoordination;
	}
	
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
	public CCoordination(double aXCoordination, double aYCoordination,EGEOUnit aUnit) {
		super();
		iXCoordination 	= aXCoordination;
		iYCoordination 	= aYCoordination;
		iUnit			= aUnit;
	}
	
	

	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */
	
	/**
	 * calculateDistance
	 * 
	 * To calculate TM distance (based on euclidean distance)
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public static double calculateDistance(CCoordination aPt1, CCoordination aPt2) {

		// Initialize output
		double lDistance = Double.MAX_VALUE;
		
				
		lDistance = Math.sqrt(	BasicMath.power((aPt1.getXCoordination() - aPt2.getXCoordination()),2) +
				  				BasicMath.power((aPt1.getYCoordination() - aPt2.getYCoordination()),2)
				  				);
		

		return lDistance;
	}
	
	/**
	 * calculateDistance
	 * 
	 * To calculate TM distance (based on euclidean distance)
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public double calculateDistance(CCoordination aAnotherPt) {

		// Initialize output
		double lDistance = Double.MAX_VALUE;
		
				
		lDistance = Math.sqrt(	BasicMath.power((getXCoordination() - aAnotherPt.getXCoordination()),2) +
				  				BasicMath.power((getYCoordination() - aAnotherPt.getYCoordination()),2)
				  				);
		

		return lDistance;
	}

	/**
	 * getiXCoodination
	 * 
	 * Do What
	 * 
	 * @return iXCoodination double
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public double getXCoordination() {
		return iXCoordination;
	}

	/**
	 * getiYCoordination
	 * 
	 * Do What
	 * 
	 * @return iYCoordination double
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public double getYCoordination() {
		return iYCoordination;
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
	public EGEOUnit getUnit() {
		return iUnit;
	}

	/**
	 * setiXCoodination
	 * 
	 * Do What
	 * 
	 * @param aIXCoodination the iXCoodination to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setXCoordination(double aXCoordination) {
		iXCoordination = aXCoordination;
	}

	/**
	 * setiYCoordination
	 * 
	 * Do What
	 * 
	 * @param aIYCoordination the iYCoordination to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setYCoordination(double aIYCoordination) {
		iYCoordination = aIYCoordination;
	}

	/**
	 * setiYCoordination
	 * 
	 * Do What
	 * 
	 * @param aIYCoordination the iYCoordination to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setXYCoordination(double aXCoordination, double aYCoordination) {
		setXCoordination(aXCoordination);
		setYCoordination(aYCoordination);
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
	@SuppressWarnings("unused")
	private void setUnit(EGEOUnit aIUnit) {
		iUnit = aIUnit;
	}
	

	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






