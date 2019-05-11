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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import algorithm.routing.IRoutingAlgorithm;
import api.CATCInstructionTimeAPI;
import api.CAssignSpotAPI;
import elements.IElementControlledByClock;
import elements.IElementObservableClock;
import elements.facility.AFacility;
import elements.facility.CAirport;
import elements.facility.CSpot;
import elements.mobile.vehicle.AVehiclePlan;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
import elements.property.CAircraftPerformance;
import elements.table.ITableAble;
import sim.CAtsolSimMain;
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
	
	protected List<CAircraft> iRequestEventAircraftList = Collections.synchronizedList(new ArrayList<CAircraft>());
	protected List<AFacility> iOwnedFacilty = Collections.synchronizedList(new ArrayList<AFacility>());
	

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
	
	protected long calculateTaxiInstructionTime(List<? extends ANode> aRoute,CAircraft aAircraft) {
		long instructionTimeMilliSeconds = 0;
		try {
			LinkedHashSet<String> lRouteUniqueName = new LinkedHashSet<String>();
			for(int loopR = 1; loopR < aRoute.size(); loopR++) {
				lRouteUniqueName.add(aRoute.get(loopR).getNameGroup());
			}

			instructionTimeMilliSeconds = (lRouteUniqueName.size()*60/150 + 8*60/150);
		}catch(Exception e) {
			instructionTimeMilliSeconds = 0;
		}
		
		 
		long api = new CATCInstructionTimeAPI().calculateTaxiInstructionTime(aRoute, aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}
		
		return instructionTimeMilliSeconds;
	}
	
	
	protected long calculatePushbackInstructionTime(CAircraft aAircraft) {
		long instructionTimeMilliSeconds = 0;
		// Incheon Ground, korean Air 1124 Ready for push back
		// Korean air 1124 push back approved heading west
		// push back approved heading west, korean air 1124 
		
		// 24 words
		// 150 words per minute (Medium speed ICAO 2006b 2-1)
		instructionTimeMilliSeconds = (24*60/150)*1000;
		
		
		long api = new CATCInstructionTimeAPI().calculatePushbackInstructionTime(aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}
		
		return instructionTimeMilliSeconds;
	}
	
	
	protected long calculateFrequencyChangeInstructionTime(CAircraft aAircraft) {
		long instructionTimeMilliSeconds = 0;
		// Korean air 1124, Incheon Ground Contact Incheon Tower 118.05 good day
		// Contact Incehon Tower 118.05, Korean air 1124, good day 

		// 21 words
		// 150 words per minute (Medium speed ICAO 2006b 2-1)		
		instructionTimeMilliSeconds = (21*60/150+ 3)*1000;
		
		long api = new CATCInstructionTimeAPI().calculateFrequencyChangeInstructionTime(aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}		
		return instructionTimeMilliSeconds;
	}
	
	
	protected long calculateLinupInstructionTime(CAircraft aAircraft) {
		long instructionTimeMilliSeconds = 0;
		// Korean air 1124, Runway 33L Line up and Wait
		// Runway 33L, Line up and Wait, Korean air 1124 

		// 16 words
		// 150 words per minute (Medium speed ICAO 2006b 2-1)		
		instructionTimeMilliSeconds = (16*60/150+ 3)*1000;
		
		long api = new CATCInstructionTimeAPI().calculateLinupInstructionTime(aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}
		
		return instructionTimeMilliSeconds;
	}

	protected long calculateTakeOffInstructionTime(CAircraft aAircraft) {
		long instructionTimeMilliSeconds = 0;
		// Korean air 1124, Ruwnay 33L Wind 230 at 5 Cleared for Takeoff  
		// Runway 33L Cleared for Takeoff Korean air 1124, 
		// 22 words
		// 150 words per minute (Medium speed ICAO 2006b 2-1)		
		instructionTimeMilliSeconds = (22*60/150 + 3)*1000;
		
		long api = new CATCInstructionTimeAPI().calculateTakeOffInstructionTime(aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}
		
		return instructionTimeMilliSeconds;
	}	
	
	protected long calculateLandingInstructionTime(CAircraft aAircraft) {
		long instructionTimeMilliSeconds = 0;
		// Korean air 1124, Ruwnay 33L Wind 230 at 5 Cleared to Land  
		// Runway 33L Cleared to Land Korean air 1124, 
		// 22 words
		// 150 words per minute (Medium speed ICAO 2006b 2-1)		
		instructionTimeMilliSeconds = (22*60/150 + 3)*1000;
		
		long api = new CATCInstructionTimeAPI().calculateLandingInstructionTime(aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}
		
		return instructionTimeMilliSeconds;
	}	
	
	protected long calculateRunwayCrossingInstructionTime(CAircraft aAircraft, CLocalController aCLocalController) {
		
		long instructionTimeMilliSeconds = 0;
		// Korean air 1124, Cross runway 33L
		// Cross runway 33L Korean air 1124 
		// 12 words
		// 150 words per minute (Medium speed ICAO 2006b 2-1)		
		instructionTimeMilliSeconds = (12*60/150 + 3)*1000;
		
		long api = new CATCInstructionTimeAPI().calculateLandingInstructionTime(aAircraft, this);
		if(api>=0) {
			instructionTimeMilliSeconds = api;
		}
		
		return instructionTimeMilliSeconds;
	}
	
	
	
	public synchronized void setRoutingAlgorithm(IRoutingAlgorithm aRoutingAlgoritm) {
		iRoutingAlgorithm = aRoutingAlgoritm;
	}
	
	
	protected void addAircraft(CAircraft aAircraft) {
		synchronized (iAircraftList) {
			if(!iAircraftList.contains(aAircraft)) {
				iAircraftList.add(aAircraft);
			}
		}
	}
	public CAircraft getAircraft(int aIndex){
		synchronized (iAircraftList) {
		return iAircraftList.get(aIndex);
		}
	}
	
	public List<CAircraft> getAircraftList(){
		synchronized (iAircraftList) {
		return iAircraftList;
		}
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


	public AFacility getOwnedFacilty(int aIndex) {
		return iOwnedFacilty.get(aIndex);
	}
	public List<AFacility> getOwnedFacilty() {
		return iOwnedFacilty;
	}

	public void addOwnedFacilty(AFacility aOwnedFacilty) {
		iOwnedFacilty.add(aOwnedFacilty);
	}

	
	@Override
	public synchronized void handOffAircraft(IATCController aToController, CAircraft aAircraft) {
		

		// Remove From This Aircraft
		if(iAircraftList.contains(aAircraft)) {
			iAircraftList.remove(iAircraftList.indexOf(aAircraft));
		}
		
		// Hand off
		aAircraft.setATCController(aToController);
		
		
		// Communication
		aAircraft.setNextEventTime(iCurrentTimeInMilliSecond + calculateFrequencyChangeInstructionTime(aAircraft));
		this.setNextEventTime(iCurrentTimeInMilliSecond + calculateFrequencyChangeInstructionTime(aAircraft));
		
		// Add to the other controller
		if(aToController != null) {
			aToController.handOnAircraft(this, aAircraft);
		}

		// Thread
		// Write
		try {
			CAtsolSimMain.getOutputFileATCWriter().write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTimeInMilliSecond)) + "," + 
					this.toString()+ "," + 
					aAircraft.getCurrentFlightPlan().getCallsign()+ "," + 
					"FreqencyChange"+ "," + 
					(long)(calculateFrequencyChangeInstructionTime(aAircraft)/1000) + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	


	@Override
	public synchronized void handOnAircraft(IATCController aFromController, CAircraft aAircraft) {
		
		// Hand on AC		
		addAircraft(aAircraft);
		aAircraft.setATCController(this);
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
	
	
	public synchronized List<CAircraft> getRequestEventAircraftList() {
		return iRequestEventAircraftList;
	}


	@Override
	public void notifyToClockImDone() {
		
		iSimClockObserver.pubSaidImDone("Controller");
	}

	@Override
	public void addClock(ISimClockOberserver aSimclock) {
		
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
			controlAircraft();
			if(iNextEventTime<0 || iNextEventTime<=iCurrentTimeInMilliSecond) {
				iNextEventTime = -9999;				
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
		
		iSimClockObserver = null;
	}
	
	
	@Override
	public synchronized void waitUntilClockStatusIsChanged() {
		
		
	}
	
	
	
	
	
	protected CSpot assignArrivalSpot(CAircraft aAircraft) {
		// Assign Arrival Spot
		CFlightPlan lFlightPlan = (CFlightPlan)aAircraft.getCurrentPlan();
		CSpot lCandidateSpot    = lFlightPlan.getArrivalSpot() ;
		CAirport lAirport = (CAirport)aAircraft.getCurrentFlightPlan().getDestinationNode();
		
		// Find Next flight Spot	
		if(lCandidateSpot == null) {
			for(AVehiclePlan loopPlan : aAircraft.getPlanList()) {
				if(((CFlightPlan)loopPlan).getDepartureSpot() != null) {
					lCandidateSpot = ((CFlightPlan)loopPlan).getDepartureSpot(); 
				}
			}
		}	
		
		if(lCandidateSpot!=null && (!lCandidateSpot.getACTypeADG().get(((CAircraftPerformance) (aAircraft.getPerformance())).getADG().toString())
				|| (!lCandidateSpot.getVehicleWillUseList().contains(aAircraft) && lCandidateSpot.getVehicleWillUseList().size()>0) )) {
			lCandidateSpot = null;
		}
		
		
		
		//  random select
		if(lCandidateSpot==null) {
			lAirport = (CAirport)aAircraft.getCurrentFlightPlan().getDestinationNode();
			// Search All Feasible Spot			
			Collections.shuffle(lAirport.getSpotList(),new Random(92545153));
			
			for(CSpot loopSpot : lAirport.getSpotList()) {
				if(loopSpot.getVehicleWillUseList().size()==0 && loopSpot.getACTypeADG().get(((CAircraftPerformance) (aAircraft.getPerformance())).getADG().toString())) {
					lCandidateSpot = loopSpot;
					break;
				}
				
			}			
			
			if(lCandidateSpot == null) {
				System.err.println("AATCController assignArrivalSpot : No Spot");
				System.err.println("AATCController assignArrivalSpot : No Spot");
			}
		}
		
		
		
		
		
		
		// API
		CSpot lSpotAPI = new CAssignSpotAPI().assignSpot(aAircraft, lAirport);
		if(lSpotAPI!=null) {
			lCandidateSpot = lSpotAPI;
		}
		
		
		// Write
		try {
			CAtsolSimMain.getOutputFileATCWriter().write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTimeInMilliSecond)) + "," + 
					this.toString()+ "," + 
					aAircraft.getCurrentFlightPlan().getCallsign()+ "," + 
					"AssignSpot(NonWork)->" + lCandidateSpot+ "," + 
					(long)(0) + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lCandidateSpot;
	}


	
	
	
	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






