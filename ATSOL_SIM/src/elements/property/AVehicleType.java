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
 * @date : Mar 26, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 26, 2019 : Coded by S. J. Yun.
 *
 *
 */

public abstract class AVehicleType {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	protected String					iType;	
	protected AVehiclePerformance		iPerformance;
	protected String					iRange;
	
	private double iLength =39.5;
	private double iWidth  = 34.32;
	private double iSafetyDistanceWidth  = 34.32 + 4.5;
	private double iSafetyDistanceLength = 39.5 + 10.0;

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	public String getVehicleType() {
		return iType;
	}
	public synchronized double getWidth() {
		return iWidth;
	}
	public synchronized void setWidth(double aWidth) {
		iWidth = aWidth;
	}
	public void setVehicleType(String aAircraftType) {
		iType = aAircraftType;
	}
	public AVehiclePerformance getPerformance() {
		return iPerformance;
	}
	public void setVehicletPerformance(AVehiclePerformance aVehiclePerformance) {
		iPerformance = aVehiclePerformance;
	}
	public String toString() {
		return iType + "/" + iRange;
	}
	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */
	public synchronized double getLength() {
		return iLength;
	}
	public synchronized void setLength(double aLength) {
		iLength = aLength;
	}
	public synchronized double getSafetyDistanceWidth() {
		return iSafetyDistanceWidth;
	}
	public synchronized void setSafetyDistanceWidth(double aSafetyDistanceWidth) {
		iSafetyDistanceWidth = aSafetyDistanceWidth;
	}
	public synchronized double getSafetyDistanceLength() {
		return iSafetyDistanceLength;
	}
	public synchronized void setSafetyDistanceLength(double aSafetyDistanceLength) {
		iSafetyDistanceLength = aSafetyDistanceLength;
	}
	public synchronized void setPerformance(AVehiclePerformance aPerformance) {
		iPerformance = aPerformance;
	}

	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






