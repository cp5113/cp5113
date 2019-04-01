package test.simclock;

import java.util.Calendar;

import elements.IElementControlledByClock;
import sim.clock.CSimClock;

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
 * @date : Mar 30, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 30, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class SimClockObservabbleTestDrive {
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
	
	public static void main(String args[]){
		SimClockObservabbleTestDrive mymain = new SimClockObservabbleTestDrive();
		mymain.run();
		
		
	}
	
	public void run() {
		CSimClock clock =CSimClock.getInstance();


		clock.setStartTimeInMilliSeconds(0);
		clock.setEndTimeInMilliSeconds(3600000);

		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());		
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());		
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());		
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());		
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());		
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());
		clock.addInClock(new ACObserver());		
		
		
		
		Thread clockThread = new Thread(clock);
		clockThread.start();
	}

	
	public class ACObserver implements IElementControlledByClock{
		
		public ACObserver() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void incrementTime(Calendar aCurrentTIme, int aIncrementStepInSec) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public boolean isPreparedForNextIncrement() {
			// TODO Auto-generated method stub
			return true;
		}
		
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






