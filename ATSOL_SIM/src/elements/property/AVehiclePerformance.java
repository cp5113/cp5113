/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * AVehiclePerformance.java
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

/**
 * @author S. J. Yun
 *
 */
public abstract class AVehiclePerformance implements Cloneable{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	protected		int				iSoulCapacity;
	protected		double			iFuelCapacity;  // Weight
	protected		double			iCargoCapacity; // Weight
	

	protected		double			iBasicWeight; 
	
	
	protected		String			iProductionCompany;
	protected double TaxiingSpeedMax;
	protected double TaxiingSpeedNorm;
	protected double AccelerationOnGroundMax;
	protected double DecelerationOnGroundMax;
	
	
	public synchronized double getTaxiingSpeedMax() {
		return TaxiingSpeedMax;
	}
	public synchronized void setTaxiingSpeedMax(double aTaxiingSpeedMax) {
		TaxiingSpeedMax = aTaxiingSpeedMax;
	}
	public synchronized double getTaxiingSpeedNorm() {
		return TaxiingSpeedNorm;
	}
	public synchronized void setTaxiingSpeedNorm(double aTaxiingSpeedNorm) {
		TaxiingSpeedNorm = aTaxiingSpeedNorm;
	}
	public synchronized double getAccelerationOnGroundMax() {
		return AccelerationOnGroundMax;
	}
	public synchronized void setAccelerationOnGroundMax(double aAccelerationOnGroundMax) {
		AccelerationOnGroundMax = aAccelerationOnGroundMax;
	}
	public synchronized double getDecelerationOnGroundMax() {
		return DecelerationOnGroundMax;
	}
	public synchronized void setDecelerationOnGroundMax(double aDecelerationOnGroundMax) {
		DecelerationOnGroundMax = aDecelerationOnGroundMax;
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






