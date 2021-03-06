/**
 * ATSOL_SIM
 * elements.mobile
 * CAreaController.java
 */
package elements.mobile.human;
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
 *
 *  <li>VARIABLE_NAME : Constant variable </li>
 * </ul>
 * </p>
 * 
 * 
 * @date : May 9, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 9, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.Calendar;

import elements.mobile.vehicle.CAircraft;
import sim.clock.ISimClockOberserver;

/**
 * @author S. J. Yun
 *
 */
public class CAreaController extends AATCController{


	/*
	================================================================

			           Initializing Section

	================================================================
	 */

	/*
	================================================================

						Methods Section

	================================================================
	 */


	/*
	================================================================

							The Others

	================================================================
	 */

	public CAreaController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}


	/** (non-Javadoc)
	 * @see elements.mobile.human.IATCController#controlAircraft()
	 */
	@Override
	public synchronized void controlAircraft() {
		// TODO Auto-generated method stub
		System.out.println(this.iAircraftList);
		System.out.println();
	}
	@Override
	public void initializeAircraft(CAircraft aAircraft) {
		// TODO Auto-generated method stub
		
	}


}



