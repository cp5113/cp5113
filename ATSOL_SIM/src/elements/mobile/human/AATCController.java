/**
 * ATSOL_SIM
 * elements.mobile
 * AATCController.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import algorithm.routing.IRoutingAlgorithm;
import elements.IElementControlledByClock;
import elements.IElementObservableClock;
import elements.facility.AFacility;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
import elements.table.ITableAble;
import sim.clock.CSimClockOberserver;
import sim.clock.ISimClockOberserver;

/**
 * @author S. J. Yun
 *
 */
public abstract class AATCController extends AHuman implements IATCController, ITableAble, IElementObservableClock, Runnable{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	
	protected ISimClockOberserver iSimClockObserver;
	protected List<CAircraft> iAircraftList = Collections.synchronizedList(new ArrayList<CAircraft>());
	protected List<AFacility> iFacilityControlledList= Collections.synchronizedList(new ArrayList<AFacility>());
	
	protected AFacility		  iOwnedFacilty;

	protected IRoutingAlgorithm iRoutingAlgorithm;
	
	
	public AATCController(String aName, int aAge, int aExperienceDay, ESkill aNSkill, EGender aNGender) {
		super(aName,aAge, aExperienceDay, aNSkill, aNGender);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Mar 27, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 27, 2019 : Coded by S. J. Yun.
	 */
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	
	/**
	 * Calculate Taxi Instruction Time
	 * Using Taxiway Node List
	 * 1500milli added to call Callsign
	 * doubled for readback
	**/
	
	protected long calculateTaxiInstructionTime(List<? extends ANode> aRoute) {
		long instructionTimeMilliSeconds = 0;
		try {
			LinkedHashSet<String> lRouteUniqueName = new LinkedHashSet<String>();
			for(int loopR = 1; loopR < aRoute.size(); loopR++) {
				lRouteUniqueName.add(aRoute.get(loopR).getNameGroup());
			}

			instructionTimeMilliSeconds = (lRouteUniqueName.size()*500 + 1500);
		}catch(Exception e) {
			instructionTimeMilliSeconds = 0;
		}
		return instructionTimeMilliSeconds;
	}
	
	public synchronized void setRoutingAlgorithm(IRoutingAlgorithm aRoutingAlgoritm) {
		iRoutingAlgorithm = aRoutingAlgoritm;
	}
	
	
	protected void addAircraft(CAircraft aAircraft) {
		iAircraftList.add(aAircraft);
	}
	public CAircraft getAircraft(int aIndex){
		return iAircraftList.get(aIndex);
	}
	
	public List<CAircraft> getAircraftList(){
		return iAircraftList;
	}
	
	public void addFacility(AFacility aFacility) {
		iFacilityControlledList.add(aFacility);
	}
	public AFacility getFacilityControlled(int aIndex){
		return iFacilityControlledList.get(aIndex);
	}
	

	public List<AFacility> getFacilityControlledList() {
		return iFacilityControlledList;
	}


	public void setFacilityControlledList(List<AFacility> aFacilityControlledList) {
		iFacilityControlledList = aFacilityControlledList;
	}


	public AFacility getOwnedFacilty() {
		return iOwnedFacilty;
	}


	public void setOwnedFacilty(AFacility aOwnedFacilty) {
		iOwnedFacilty = aOwnedFacilty;
	}

	
	@Override
	public synchronized void handOffAircraft(IATCController aToController, CAircraft aAircraft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void handOnAircraft(IATCController aFromController, CAircraft aAircraft) {
		// TODO Auto-generated method stub
		
		// Hand on AC
//		System.out.println("I Got Aircraft \"" + aAircraft +"\", from Controller \"" + aFromController + "\"");		
		addAircraft(aAircraft);
		aAircraft.setThreadLockerOwner(this.iThreadLockerThis);
		
		// Initialize AC
		initializeAircraft(aAircraft);		
		
		
		// Release initialize Lock
//		System.out.println("Notify");		
		synchronized (aAircraft.getThreadLockerThis()) {
			aAircraft.setThreadContinueableAtInitialState(true);
			aAircraft.getThreadLockerThis().notify();
		}
		
//		System.out.println("Notified");
	}
	
	
	
	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */
	
	
	@Override
	public void notifyToClockImDone() {
		// TODO Auto-generated method stub
		iSimClockObserver.pubSaidImDone("Controller");
	}

	@Override
	public void addClock(ISimClockOberserver aSimclock) {
		// TODO Auto-generated method stub
		iSimClockObserver = aSimclock;
	}
	@Override
	public void run() {

		// Initialize previous time
		iPreviousTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
		
		// notify to clock I'm done!
		notifyToClockImDone();
		
		
		// Start Thread - run until clock is done
		while(((CSimClockOberserver) iSimClockObserver).isRunning()) {

			
			
			// Get current time of Clock
			iCurrentTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
			
			// Waiting until Clock is changed
			while(iPreviousTimeInMilliSecond == iCurrentTimeInMilliSecond && ((CSimClockOberserver) iSimClockObserver).isRunning()) {
				iCurrentTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			iThreadContinueableAtInitialState = false; // to lock aircraft under controlled
			
			
			// Do Control Aircraft when controller is not speaking to Aircraft
			if(iNextEventTime<0 || iNextEventTime<=iCurrentTimeInMilliSecond) {
				iNextEventTime = -9999;
				controlAircraft();
			}
			
			
			// Notify to all aircraft in this controller			
			synchronized (iThreadLockerThis) {
//				System.out.println(iCurrentTimeInMilliSecond + " : Controller Notifying..");
				iThreadContinueableAtInitialState =true;
				iThreadLockerThis.notifyAll();				
//				System.out.println(iCurrentTimeInMilliSecond + " : Controller Notifying is done..");				
//				iThreadContinueableAtInitialState =false;
			}
			
			// Notify my work is done to Clock
			notifyToClockImDone();
			
			
			// Previous time  is set up for next time "Waiting until Clock is changed"
			iPreviousTimeInMilliSecond = iCurrentTimeInMilliSecond;
		}
	}
	@Override
	public void removeClock() {
		// TODO Auto-generated method stub
		iSimClockObserver = null;
	}
	
	
	@Override
	public synchronized void waitUntilClockStatusIsChanged() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






