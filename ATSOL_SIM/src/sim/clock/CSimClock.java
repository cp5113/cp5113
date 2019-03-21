package sim.clock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import elements.IElementControlledByClock;

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
 * @date : Mar 21, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 21, 2019 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
public class CSimClock implements ISimClock, Runnable{
	
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private Calendar iCurrentTIme;
	private Calendar iStartTIme;
	private Calendar iEndTIme;	
	private int iIncrementStepInMiliSec = 100;
	private ArrayList<IElementControlledByClock> iObserverList;
	private static CSimClock iInstance = new CSimClock();
	
	
	
	private CSimClock() {
		// TODO Auto-generated constructor stub
	}
	
	
	

	
	@Override
	public void addInClock(IElementControlledByClock aObserver) {
		// TODO Auto-generated method stub
			iObserverList.add(aObserver);
	}

	@Override
	public void deleteFromClock(IElementControlledByClock aObserver) {
		// TODO Auto-generated method stub
		iObserverList.remove(aObserver);

	}

	@Override
	public void notifyTimeIncrementToElement() {
		
		// Verify that All Elements are ready
		int lCountReadyObserver = 0;
		for(IElementControlledByClock lObserver : iObserverList) {
			if(lObserver.isPreparedForNextIncrement()) lCountReadyObserver ++;
		}
		
		// Nofity to all Elements
		if(lCountReadyObserver == iObserverList.size()) {
			for(IElementControlledByClock lObserver : iObserverList) {
				lObserver.incrementTime(iCurrentTIme, iIncrementStepInMiliSec);
			}
		}

	}



	
	
	
	
	


	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	/**
	 * getCurrentTIme
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized Calendar getCurrentTIme() {
		return iCurrentTIme;
	}

	/**
	 * setCurrentTIme
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized void setCurrentTIme(Calendar aCurrentTIme) {
		iCurrentTIme = aCurrentTIme;
	}

	/**
	 * getStartTIme
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized Calendar getStartTIme() {
		return iStartTIme;
	}

	/**
	 * setStartTIme
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized void setStartTIme(Calendar aStartTIme) {
		iStartTIme = aStartTIme;
	}

	/**
	 * getEndTIme
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized Calendar getEndTIme() {
		return iEndTIme;
	}

	/**
	 * setEndTIme
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized void setEndTIme(Calendar aEndTIme) {
		iEndTIme = aEndTIme;
	}

	/**
	 * getIncrementStepInSec
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized int getIncrementStepInMiliSec() {
		return iIncrementStepInMiliSec;
	}

	/**
	 * setIncrementStepInSec
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized void setIncrementStepInSec(int aIncrementStepInMiliSec) {
		iIncrementStepInMiliSec = aIncrementStepInMiliSec;
	}

	/**
	 * getObserverList
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized ArrayList<IElementControlledByClock> getObserverList() {
		return iObserverList;
	}

	/**
	 * setObserverList
	 * 
	 * Do What
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public synchronized void setObserverList(ArrayList<IElementControlledByClock> aObserverList) {
		iObserverList = aObserverList;
	}

	
	/**
	 * getInstance
	 * 
	 * Create Clock Object
	 * 
	 * @date : Mar 21, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 21, 2019 : Coded by S. J. Yun.
	 */
	public static CSimClock getInstance() {
		if(iInstance == null) {
			iInstance = new CSimClock();
		}
		return iInstance;
	}





	/** (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		// Run Clock
		while(iCurrentTIme.getTimeInMillis() <= iEndTIme.getTimeInMillis()) {
			iCurrentTIme.add(Calendar.MILLISECOND, iIncrementStepInMiliSec);;
			System.out.println("Current Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTIme.getTimeInMillis())));
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
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






