/**
 * ATSOL_SIM
 * elements.mobile
 * CGroundController.java
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
 *  <li>n...... : ENUM </li>
 *
 *  <li>VARIABLE_NAME : Constant variable </li>
 * </ul>
 * </p>
 * 
 * 
 * @date : May 10, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 10, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.Calendar;

import elements.mobile.vehicle.CAircraft;
import sim.clock.CSimClockOberserver;
import sim.clock.ISimClockOberserver;

/**
 * @author S. J. Yun
 *
 */
public class CGroundController extends AATCController {


	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	public CGroundController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName, aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}
	/** (non-Javadoc)
	 * @see elements.mobile.human.IATCController#controlAircraft()
	 */
	@Override
	public void controlAircraft() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handOffAircraft(IATCController aToController, CAircraft aAircraft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handOnAircraft(IATCController aFromController, CAircraft aAircraft) {
		// TODO Auto-generated method stub
		System.out.println("I Got Aircraft \"" + aAircraft +"\", from Controller \"" + aFromController + "\"");
		System.out.println();
		addAircraft(aAircraft);
		
	}


	@Override
	public void waitUntilClockStatusIsChanged() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyToClockImDone() {
		// TODO Auto-generated method stub
		iSimClockObserver.pubSaidImDone();
	}

	@Override
	public void addClock(ISimClockOberserver aSimclock) {
		// TODO Auto-generated method stub
		iSimClockObserver = aSimclock;
	}
	@Override
	public void run() {
		
		iPreviousTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
		notifyToClockImDone();
		
		while(((CSimClockOberserver) iSimClockObserver).isRunning()) {
			iCurrentTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
			while(iPreviousTimeInMilliSecond == iCurrentTimeInMilliSecond) {
				iCurrentTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
//			System.out.println("Ground Controller is Working.. Sleep 3 secs");
//			

//			System.out.println("Ground controller is done");
			notifyToClockImDone();
			iPreviousTimeInMilliSecond = iCurrentTimeInMilliSecond;
		}
	}
	@Override
	public void removeClock() {
		// TODO Auto-generated method stub
		iSimClockObserver = null;
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






