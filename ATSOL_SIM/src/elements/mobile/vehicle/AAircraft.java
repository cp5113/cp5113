/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraft.java
 */
package elements.mobile.vehicle;

import java.util.ArrayList;
import java.util.Calendar;

import elements.IElementControlledByClock;
import elements.IElementObservableClock;
import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.mobile.human.AATCController;
import elements.mobile.human.IATCController;
import elements.operator.CAirline;
import elements.property.CAircraftType;
import elements.table.ITableAble;
import javafx.scene.paint.Color;
import sim.clock.CSimClockOberserver;
import sim.clock.ISimClockOberserver;
import sim.gui.CDrawingInform;
import sim.gui.EShape;
import sim.gui.IDrawingObject;

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
 * @date : May 13, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 13, 2017 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
public abstract class AAircraft extends AVehicle implements IAircraft{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private		String							iRegistration;	
	private 	CAirline						iAirline;
	
	
	

	@Override
	public String toString() {
		return iRegistration;
		
	}
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */

	

	public synchronized String getRegistration() {
		return iRegistration;
	}

	public synchronized void setRegistration(String aRegistration) {
		iRegistration = aRegistration;
	}

	public CAirline getAirline() {
		return iAirline;
	}

	public void setAirline(CAirline aAirline) {
		iAirline = aAirline;
	}


	/*
	================================================================
	
						Listeners Section
	
	================================================================
	 */
	

	@Override
	public void waitUntilClockStatusIsChanged() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void notifyToClockImDone() {
		// TODO Auto-generated method stub
		iSimClockObserver.pubSaidImDone(((CFlightPlan)this.getCurrentPlan()).getCallsign().toString() );
		
	}






	@Override
	public void addClock(ISimClockOberserver aSimclock) {
		// TODO Auto-generated method stub
		iSimClockObserver = aSimclock;
	}



	@Override
	public void run() {
		
		// Initialize status of this aircraft
		CFlightPlan lCurrentPlan = (CFlightPlan) this.iCurrentPlan;
		this.getCurrentPostion().setXCoordination(lCurrentPlan.getNode(0).getCoordination().getXCoordination());
		this.getCurrentPostion().setYCoordination(lCurrentPlan.getNode(0).getCoordination().getYCoordination());
		
		
		/*
		 * Find ATC Controller
		 */
		// if Departure from an Airport
		// add to ground/ramp controller
		// and request to set start point as spot
		// 		if not available, change departure spot		
		if(lCurrentPlan.getDepartureSpot()!=null) {
			iATCController = lCurrentPlan.getDepartureSpot().getATCController();
		}else if(lCurrentPlan.getOriginationNode().getClass().getSimpleName().equalsIgnoreCase("CAirport")) {
			if( ((CAirport) lCurrentPlan.getOriginationNode()).getGroundControllerList().size()>0) {
				iATCController = ((CAirport) lCurrentPlan.getOriginationNode()).getGroundControllerList().get(0);
			}else {   // Airport as a Point
				iATCController = ((CAirport) lCurrentPlan.getOriginationNode()).getLocalControllerList().get(0);
			}
		}
		
		// if departure from waypoint
		// add airspace Controller
		if(lCurrentPlan.getOriginationNode().getClass().getSimpleName().equalsIgnoreCase("CWaypoint")) {
			iATCController = ((CWaypoint) lCurrentPlan.getOriginationNode()).getATCController();
		}
		
		/*
		 * Add this A/C to ATC Controller
		 * and Initial Contact
		 */
//		System.out.println();
		iATCController.handOnAircraft(null, (CAircraft)this);
		
		// Lock this aircraft until Controller's initialization
		synchronized (this.iThreadLockerThis) {
			while(!isThreadContinueableAtInitialState()) {
			try {
				System.out.println("Waiting " + iRegistration);
				this.iThreadLockerThis.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			System.out.println(((CFlightPlan)iCurrentPlan).getCallsign() + " is initialized by controller");
		}
		
		

		// Get Previous Time
		iPreviousTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
		
		// notify to clock I'm done!
		notifyToClockImDone();
		
		
		// Wait for Clock is ticking and run own algorithm
		while( ((CSimClockOberserver) iSimClockObserver).isRunning()) {


			
			// Wait until Time is change
			iCurrentTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
			while(iPreviousTimeInMilliSecond == iCurrentTimeInMilliSecond && ((CSimClockOberserver) iSimClockObserver).isRunning()) {
				iCurrentTimeInMilliSecond = ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {

				}
			}
			
		
			
	
			
			
//			System.out.println(iCurrentTimeInMilliSecond + " : " + ((CFlightPlan)this.getCurrentPlan()).getCallsign() + "'s Controller Status is " + ((AATCController)this.iATCController).isThreadContinueableAtInitialState());
			// Wait Controller's Instruction
			synchronized (iThreadLockerOwner) {
				while(!((AATCController)this.iATCController).isThreadContinueableAtInitialState()) {
					try {						
//						System.out.println(iCurrentTimeInMilliSecond + " : " + ((CFlightPlan)this.getCurrentPlan()).getCallsign() + " is waiting..");
						iThreadLockerOwner.wait();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}					
				}				
			}
//			System.out.println(iCurrentTimeInMilliSecond + " : " + ((CFlightPlan)this.getCurrentPlan()).getCallsign() + " waiting is done..");
			
			
			// Do Move Aircraft
			if(iNextEventTime<0 || iNextEventTime<=iCurrentTimeInMilliSecond) {
				iNextEventTime = -9999;
				doMoveVehicle();
			};
			
			
			
			// Debugging
			if(iCurrentTimeInMilliSecond != ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis()) {
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("AC Current Time : " + iCurrentTimeInMilliSecond + " / Clock : " + ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis());
					e.printStackTrace();
				}
			}
			
			
			
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
	
							The Others
	
	================================================================
	 */
}






