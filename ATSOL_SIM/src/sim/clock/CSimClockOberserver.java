package sim.clock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import elements.AElement;
import elements.IElementControlledByClock;
import elements.IElementObservableClock;
import elements.table.ATable;
import elements.table.ITableAble;
import sim.CAtsolSimMain;
import sim.gui.control.CAtsolSimGuiControl;

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
 * @date : Mar 28, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 28, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CSimClockOberserver implements ISimClockObserverable, ISimClockOberserver, Runnable {


	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private Calendar iCurrentTIme = Calendar.getInstance();
	private Calendar iStartTIme = Calendar.getInstance();
	private Calendar iEndTIme = Calendar.getInstance();
	private int iIncrementStepInMiliSec = 1000;
	private ArrayList<IElementObservableClock> 	iObserverableList 		= new ArrayList<IElementObservableClock>();    // for SJ (Aircraft move independent on time)
	private ArrayList<Integer>					iObserverableDoneList 	= new ArrayList<Integer>();	
	private ArrayList<IElementControlledByClock> iObserverList   = new ArrayList<IElementControlledByClock>();  // for API user
	
	private ArrayList<Thread>					iThreadList      = new ArrayList<Thread>();
	private static CSimClockOberserver iInstance = new CSimClockOberserver();
	private boolean iRunning;
	
	private int iCountPublicationIsDone = 0;
	private boolean iPause = false; 
	
	private CSimClockOberserver() {
		
	}
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	public void createThreadOfElements() {
		for(int loopObservable = 0; loopObservable < iObserverableList.size(); loopObservable++) {			
			Thread lThread = new Thread((Runnable) iObserverableList.get(loopObservable),iObserverableList.get(loopObservable).toString());
			iThreadList.add(lThread);
			lThread.start();
			
		}
	}
	private void createThreadOfElement(IElementObservableClock aObservable) {
		Thread lThread = new Thread((Runnable) aObservable,aObservable.toString());
		iThreadList.add(lThread);
		lThread.start();		
	}
	
	public void createThreadOfElements(IElementObservableClock aObservableObject) {
		try {
			Thread lThread = new Thread((Runnable) aObservableObject);
			iThreadList.add(lThread);
			lThread.start();
		}catch(Exception e) {
			System.out.println("Error : Creating Thread of Element of " + ((AElement) aObservableObject).toString());
		}
	}
	
	
	
	
	public void terminateThreadOfElements() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iRunning = false;
		iThreadList.clear();
		
		for(int loopList = 0; loopList<iObserverableList.size();loopList++) {
			iObserverableList.get(loopList).removeClock();			
		}
		iObserverableList.clear();
		iObserverList.clear();
		iObserverableDoneList.clear();
		iCountPublicationIsDone = 0;
	}
	
	
	public void createLinkBetweenTimeAndElements() {
		
		// All IElementObservableClock Object
//		List<ATable> lAllTable = CAtsolSimMain.getInstance().getAllTableList();
//		for(int loopList = 0; loopList < lAllTable.size(); loopList++) {
//			List<ITableAble> lElementList = lAllTable.get(loopList).getElementList();
//			for(int loopElement = 0; loopElement < lElementList.size(); loopElement++) {
//				try {	
//					addInClock((IElementObservableClock) lElementList.get(loopElement));
//					((IElementObservableClock) lElementList.get(loopElement)).addClock(iInstance);
//				}catch(Exception e) {
//					// when the object is not instance of IElementObservableClock
//				}
//			}
//		}
		
		// Controller Only
		List<ITableAble> lControllerTable = CAtsolSimMain.getInstance().getControllerTable().getElementList();
		for(int loopList = 0; loopList < lControllerTable.size(); loopList++) {

			addInClock((IElementObservableClock) lControllerTable.get(loopList));
			((IElementObservableClock) lControllerTable.get(loopList)).addClock(iInstance);

		}
	}
	
	public void createLinkBetweenTimeAndElement(IElementObservableClock aObservableObject) {

		addInClock(aObservableObject);
		aObservableObject.addClock(iInstance);
		createThreadOfElement(aObservableObject);
	}

	
	
	/** (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		iRunning = true;

		// Initialize CControlAircraftThreadByTime to enhance number of Thread
		CDispatchAircraftThreadByTime lControlAircraftThreadByTime = CDispatchAircraftThreadByTime.getInstance();
		this.addInClock(lControlAircraftThreadByTime);

		
		//		iCountPublicationIsDone = 0;

		
		// Run Clock
 		iCountPublicationIsDone= 0;
		while(iCurrentTIme.getTimeInMillis() <= iEndTIme.getTimeInMillis() && iRunning ) {			
			
			// Wait until Observable Element is done (e.g., Aircraft, Controller...)
			while(iCountPublicationIsDone < iObserverableList.size() && iRunning) {		
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			iCountPublicationIsDone = 0;			
			


			// Pause
			if(iPause) {
				while(iRunning) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!iPause) {
						iPause = true;
						break;
					}
				}
			}
			
			// Add Current Time and Observable Element will running			
			iCurrentTIme.add(Calendar.MILLISECOND, iIncrementStepInMiliSec);
			// Display Current Time
			System.out.println("Current Time : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTIme.getTimeInMillis())));
//			System.out.println("Current Time : " + iCurrentTIme.getTimeInMillis());
			
			
			// Notify to Observer
			notifyTimeIncrementToElement();
			
			
			
			
			// Draw Map
			CAtsolSimGuiControl.getInstance().drawDrawingObjectList();

//			
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			

		}
		if(!iPause) {
			iRunning = false;
		}
		
	}
	
	public static CSimClockOberserver getInstance() {
		return iInstance;				
	}
	
	@Override
	public void addInClock(IElementObservableClock aElementObservableClock) {
		// TODO Auto-generated method stub
		iObserverableList.add(aElementObservableClock);
		iObserverableDoneList.add(0);
	}

	@Override
	public synchronized void deleteFromClock(int aIndex) {
		// TODO Auto-generated method stub
		iObserverableList.remove(aIndex);
		iObserverableDoneList.remove(aIndex);
	}
	@Override
	public synchronized void deleteFromClock(IElementObservableClock aElementObservableClock) {
		// TODO Auto-generated method stub
		int index =  iObserverableList.indexOf(aElementObservableClock);
		iObserverableList.remove(aElementObservableClock);
		iObserverableDoneList.add(index);
	}
		

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return iRunning;
	}

	@Override
	public void setRunning(boolean aIsRunning) {
		// TODO Auto-generated method stub
		iRunning = aIsRunning;
	}

	@Override
	public void stopClock() {
		// TODO Auto-generated method stub
		iRunning = false;
		terminateThreadOfElements();
		
	}

	@Override
	public synchronized void pubSaidImDone(String aWho) {
		// TODO Auto-generated method stub		
		iCountPublicationIsDone += 1;
//		System.out.println(aWho + " said to Clock im done");
//		System.out.println("Done Element counts : " + iCountPublicationIsDone);
//		System.out.println("Elements in Clock  : " + iObserverableList.size());
	}
	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */

	@Override
	public synchronized  void addInClock(IElementControlledByClock aObserver) {
		// TODO Auto-generated method stub
		iObserverList.add(aObserver);
	}

	@Override
	public synchronized void deleteFromClock(IElementControlledByClock aObserver) {
		// TODO Auto-generated method stub
		iObserverList.remove(aObserver);
	}

	@Override
	public void notifyTimeIncrementToElement() {
		// TODO Auto-generated method stub
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
	public Calendar getCurrentTIme() {
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
	
	public boolean haseObject(IElementObservableClock aObject) {
		
		return iObserverableList.contains(aObject);
	}
	/*
	================================================================
	
							The Others
	
	================================================================
	 */

	public void stepFoward() {
		iPause = false;
	}

	public synchronized void setPause(boolean aB) {
		if(iPause == true) {
			iPause=false;
		}else {
			iPause=true;
		}
//		iPause = aB;
	}
}






