/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraftPerformance.java
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

import elements.mobile.vehicle.CAircraft;

/**
 * @author S. J. Yun
 *
 */
public class CAircraftPerformance extends AVehiclePerformance {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private CAircraftType iOwnerAircraftType;	
	private double TaxiingSpeedMax;
	private double TaxiingSpeedNorm;
	private double AccelerationOnGroundMax;
	private double DecelerationOnGroundMax;
	private double AccelerationOnRunwayMax;
	private double DecelerationOnRunwayMax;
	private double ExitSpeedNorm;
	private double TakeoffSpeedNorm;
	private double ClimbSpeed1000Norm;
	
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
	protected CAircraftPerformance(CAircraftType aOwnerAircraftType, 
			double aTaxiingSpeedMax, double aTaxiingSpeedNorm, double aAccelerationOnGroundMax,
			double aDecelerationOnGroundMax, double aAccelerationOnRunwayMax, double aDecelerationOnRunwayMax,
			double aExitSpeedNorm, double aTakeoffSpeedNorm, double aClimbSpeed1000Norm) {
		super();
		iOwnerAircraftType = aOwnerAircraftType;
		TaxiingSpeedMax = aTaxiingSpeedMax;
		TaxiingSpeedNorm = aTaxiingSpeedNorm;
		AccelerationOnGroundMax = aAccelerationOnGroundMax;
		DecelerationOnGroundMax = aDecelerationOnGroundMax;
		AccelerationOnRunwayMax = aAccelerationOnRunwayMax;
		DecelerationOnRunwayMax = aDecelerationOnRunwayMax;
		ExitSpeedNorm = aExitSpeedNorm;
		TakeoffSpeedNorm = aTakeoffSpeedNorm;
		ClimbSpeed1000Norm = aClimbSpeed1000Norm;
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
	public CAircraftPerformance( 
			double aTaxiingSpeedMax, double aTaxiingSpeedNorm, double aAccelerationOnGroundMax,
			double aDecelerationOnGroundMax, double aAccelerationOnRunwayMax, double aDecelerationOnRunwayMax,
			double aExitSpeedNorm, double aTakeoffSpeedNorm, double aClimbSpeed1000Norm) {
		super();		
		TaxiingSpeedMax = aTaxiingSpeedMax;
		TaxiingSpeedNorm = aTaxiingSpeedNorm;
		AccelerationOnGroundMax = aAccelerationOnGroundMax;
		DecelerationOnGroundMax = aDecelerationOnGroundMax;
		AccelerationOnRunwayMax = aAccelerationOnRunwayMax;
		DecelerationOnRunwayMax = aDecelerationOnRunwayMax;
		ExitSpeedNorm = aExitSpeedNorm;
		TakeoffSpeedNorm = aTakeoffSpeedNorm;
		ClimbSpeed1000Norm = aClimbSpeed1000Norm;
	}

	
			
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public CAircraftType getOwnerAircraftType() {
		return iOwnerAircraftType;
	}
	public double getTaxiingSpeedMax() {
		return TaxiingSpeedMax;
	}
	public double getTaxiingSpeedNorm() {
		return TaxiingSpeedNorm;
	}
	public double getAccelerationOnGroundMax() {
		return AccelerationOnGroundMax;
	}
	public double getDecelerationOnGroundMax() {
		return DecelerationOnGroundMax;
	}
	public double getAccelerationOnRunwayMax() {
		return AccelerationOnRunwayMax;
	}
	public double getDecelerationOnRunwayMax() {
		return DecelerationOnRunwayMax;
	}
	public double getExitSpeedNorm() {
		return ExitSpeedNorm;
	}
	public double getTakeoffSpeedNorm() {
		return TakeoffSpeedNorm;
	}
	public double getClimbSpeed1000Norm() {
		return ClimbSpeed1000Norm;
	}

	public void setOwnerAircraftType(CAircraftType aAircraftType) {
		iOwnerAircraftType = aAircraftType;
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






