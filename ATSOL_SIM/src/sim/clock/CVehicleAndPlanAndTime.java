package sim.clock;
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
 * @date : Mar 31, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 31, 2019 : Coded by S. J. Yun.
 *
 *
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import elements.mobile.vehicle.AVehicle;
import elements.mobile.vehicle.AVehiclePlan;

public class CVehicleAndPlanAndTime implements Comparable<CVehicleAndPlanAndTime> {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private AVehicle	iVehicle;
	private	AVehiclePlan	iVechiPlan;
	private	long		iSTD;
	
	
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Mar 31, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 31, 2019 : Coded by S. J. Yun.
	 */
	public CVehicleAndPlanAndTime(AVehicle aVehicle, AVehiclePlan aVechiPlan, long aSTD) {
		super();
		iVehicle = aVehicle;
		iVechiPlan = aVechiPlan;
		iSTD = aSTD;
	}
	
	





	@Override
	public int compareTo(CVehicleAndPlanAndTime aO) {
		// TODO Auto-generated method stub
		if(this.iSTD< aO.getSTD()) {
			return -1;
		}else if(this.iSTD > aO.getSTD()) {
			return 1;
		}
		return 0;
	}
	public AVehicle getVehicle() {
		return iVehicle;
	}
	public void setVehicle(AVehicle aVehicle) {
		iVehicle = aVehicle;
	}
	public AVehiclePlan getVechiPlan() {
		return iVechiPlan;
	}
	public void setVechiPlan(AVehiclePlan aVechiPlan) {
		iVechiPlan = aVechiPlan;
	}
	public long getSTD() {
		return iSTD;
	}
	public void setSTD(long aSTD) {
		iSTD = aSTD;
	}
	
	@Override
	public String toString() {
		return iVechiPlan + " : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iSTD));
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






