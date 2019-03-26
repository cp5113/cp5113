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
public class CSimClock implements ISimClockObserver, Runnable{
	
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private Calendar iCurrentTIme = Calendar.getInstance();
	private Calendar iStartTIme = Calendar.getInstance();
	private Calendar iEndTIme = Calendar.getInstance();
	private int iIncrementStepInMiliSec = 100;
	private ArrayList<IElementControlledByClock> iObserverList = new ArrayList<IElementControlledByClock>();
	private static CSimClock iInstance = new CSimClock();
	private boolean iRunning;
	
	
	private CSimClock() {
		// TODO Auto-generated constructor stub
	}
	
	
	

	



	
	
	
	
	


	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
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
		while(lCountReadyObserver != iObserverList.size()) {
			lCountReadyObserver = 0;
			for(IElementControlledByClock lObserver : iObserverList) {
				if(lObserver.isPreparedForNextIncrement()) lCountReadyObserver ++;
			}
		}
		
		// Notify to all Elements
		for(IElementControlledByClock lObserver : iObserverList) {
			lObserver.incrementTime(iCurrentTIme, iIncrementStepInMiliSec);
		}


	}

	/** (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		iRunning = true;
		// Run Clock

		while(iCurrentTIme.getTimeInMillis() <= iEndTIme.getTimeInMillis() && iRunning) {
			System.out.println("Current Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTIme.getTimeInMillis())));
			iCurrentTIme.add(Calendar.MILLISECOND, iIncrementStepInMiliSec);
			notifyTimeIncrementToElement();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		iRunning = false;
		
	}
	
	public synchronized boolean isRunning() {
		return iRunning;
	}

	private synchronized void setRunning(boolean aIsRunning) {
		iRunning = aIsRunning;
	}
	
	public void stop() {
		setRunning(false);
	}














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
	public synchronized void setStartTimeInMilliSeconds(long aMilliSeconds) {
		iStartTIme.setTimeInMillis(aMilliSeconds);;
		iCurrentTIme.setTimeInMillis(aMilliSeconds);
	}
	public synchronized void setEndTimeInMilliSeconds(long aMilliSeconds) {
		iEndTIme.setTimeInMillis(aMilliSeconds);;		
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















	public void setStartTIme(Calendar aStartT) {
		// TODO Auto-generated method stub
		
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






