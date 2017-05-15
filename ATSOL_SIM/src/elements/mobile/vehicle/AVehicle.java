/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * AVehicle.java
 */
package elements.mobile.vehicle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import elements.mobile.AMobile;
import elements.operator.AOperator;

/**
 * @author S. J. Yun
 *
 */
public abstract class AVehicle extends AMobile{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	protected		CVehicleStatus			iCurrentStatus;
	protected		CVehicleStatus			iPreviousStatus;
	protected		CVehicleStatus			iNextStatus;
	
	
	protected		AVehiclePlan			iCurrentPlan;
	protected		List<AVehiclePlan>		iPlanList 				= Collections.synchronizedList(new ArrayList<AVehiclePlan>());
	
	
	protected		AOperator				iOperator;
	protected		AVehiclePerformance		iPerformance;
	
	 
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






