/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraft.java
 */
package elements.mobile.vehicle;

import api.CPushBackPauseTimeAPI;
import elements.airspace.CWaypoint;
import elements.area.ASector;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CTaxiwayNode;
import elements.mobile.human.AATCController;
import elements.mobile.human.CApproachController;
import elements.mobile.human.CGroundController;
import elements.mobile.human.CLocalController;
import elements.mobile.human.IATCController;
import elements.mobile.vehicle.state.CAircraftApproachMoveState;
import elements.mobile.vehicle.state.CAircraftLandingMoveState;
import elements.mobile.vehicle.state.CAircraftLineUpMoveState;
import elements.mobile.vehicle.state.CAircraftNothingMoveState;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.mobile.vehicle.state.CAircraftTerminationMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ANode;
import elements.operator.CAirline;
import elements.property.EMode;
import elements.table.ITableAble;
import elements.util.geo.CCoordination;
import sim.CAtsolSimMain;
import sim.clock.CSimClockOberserver;
import sim.clock.ISimClockOberserver;

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
	protected		EAircraftMovementStatus			iMovementStatus;
	protected		EAircraftMovementMode			iMovementMode;
	protected	long							iPushbackPauseTimeInMilliSeconds = 1;//4 * 60 * 1000;
	protected   long							iPushbackPausedTimeInMilliSeconds = 0;
	protected	CTaxiwayNode					iDirectionTaxwayNodeAfterPushBack;
	protected 	CTaxiwayNode					iRunwayEntryPoint;
	protected 	CTaxiwayNode					iRunwayEntryPointReference;
	
	protected	double							iTargetExitSpeed;

	double iTouchdownDistance = -999;
	long	iRunwayEntryTime = -9L;

	
	CTaxiwayNode iExitTaxiwayNode = null;
	
	
	
	
	
	@Override
	public String toString() {
		return iRegistration;
		
	}
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */

	public synchronized CTaxiwayNode getExitTaxiwayNode() {
		return iExitTaxiwayNode;
	}

	public synchronized double getTargetExitSpeed() {
		return iTargetExitSpeed;
	}

	public synchronized void setTargetExitSpeed(double aTargetExitSpeed) {
		iTargetExitSpeed = aTargetExitSpeed;
	}

	public synchronized void setExitTaxiwayNode(CTaxiwayNode aExitTaxiwayNode) {
		iExitTaxiwayNode = aExitTaxiwayNode;
	}

	public synchronized long getRunwayEntryTime() {
		return iRunwayEntryTime;
	}

	public synchronized void setRunwayEntryTime(long aRunwayEntryTime) {
		iRunwayEntryTime = aRunwayEntryTime;
	}

	public synchronized double getTouchdownDistance() {
		return iTouchdownDistance;
	}

	public synchronized void setTouchdownDistance(double aTouchdownDistance) {
		iTouchdownDistance = aTouchdownDistance;
	}

	public double calculateDistanceBtwNodes(ANode a, ANode b) {
		double output = 0;
		output = Math.sqrt((a.getCoordination().getXCoordination() - b.getCoordination().getXCoordination()) *(a.getCoordination().getXCoordination() - b.getCoordination().getXCoordination()) +
				(a.getCoordination().getYCoordination() - b.getCoordination().getYCoordination()) *(a.getCoordination().getYCoordination() - b.getCoordination().getYCoordination())); 
		return output;
	}
	
	public double calculateDistanceBtwCoordination(CCoordination a, CCoordination b) {
		double output = 0;
		
		ANode aa = new ANode() {
			@Override
			public void setATCControllerToChildren(IATCController aController) {
			}
		};
		
		
		ANode bb = new ANode() {
			public void setATCControllerToChildren(IATCController aController) {
			}
		};
		
		aa.setCoordination(a);
		bb.setCoordination(b);
		
		output = calculateDistanceBtwNodes(aa, bb);
		return output;
		
	}

	public synchronized String getRegistration() {
		return iRegistration;
	}



	public synchronized CTaxiwayNode getRunwayEntryPointReference() {
		return iRunwayEntryPointReference;
	}



	public synchronized void setRunwayEntryPointReference(CTaxiwayNode aRunwayEntryPointReference) {
		iRunwayEntryPointReference = aRunwayEntryPointReference;
	}



	public synchronized CTaxiwayNode getRunwayEntryPoint() {
		return iRunwayEntryPoint;
	}



	public synchronized void setRunwayEntryPoint(CTaxiwayNode aRunwayEntryPoint) {
		iRunwayEntryPoint = aRunwayEntryPoint;
	}



	public synchronized CTaxiwayNode getDirectionTaxwayNodeAfterPushBack() {
		return iDirectionTaxwayNodeAfterPushBack;
	}



	public synchronized void setDirectionTaxwayNodeAfterPushBack(CTaxiwayNode aDirectionTaxwayNodeAfterPushBack) {
		iDirectionTaxwayNodeAfterPushBack = aDirectionTaxwayNodeAfterPushBack;
	}



	public synchronized long getPushbackPauseTimeInMilliSeconds() {
		CPushBackPauseTimeAPI userAPI = new CPushBackPauseTimeAPI();
		long userPushback = userAPI.calculatePushbackPauseTimeInMilliseconds(iCurrentTimeInMilliSecond, (CAircraft)this);
		if(userPushback>=0) {
			iPushbackPauseTimeInMilliSeconds = userPushback;
		}
		return iPushbackPauseTimeInMilliSeconds;
	}


	public synchronized long getPushbackPausedTimeInMilliSeconds() {
		return iPushbackPausedTimeInMilliSeconds;
	}


	public synchronized void addPushbackPausedTimeInMilliSeconds(long aPushbackPausedTimeInMilliSeconds) {
		iPushbackPausedTimeInMilliSeconds += aPushbackPausedTimeInMilliSeconds;
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
		this.getCurrentPosition().setXCoordination(lCurrentPlan.getNode(0).getCoordination().getXCoordination());
		this.getCurrentPosition().setYCoordination(lCurrentPlan.getNode(0).getCoordination().getYCoordination());
		
		
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
			CWaypoint lWaypoint = (CWaypoint) lCurrentPlan.getOriginationNode();
			AATCController lCandidate = null;
			for(ITableAble loopAirspace : CAtsolSimMain.getInstance().getAirspaceTable().getElementList()) {
				boolean isInside = ((ASector)loopAirspace).isInside(lWaypoint.getCoordination());
				if(isInside) {
					lCandidate =  ((ASector)loopAirspace).getControllerList().get(0);
					break;
				}
			}
			iATCController = lCandidate;
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
			
		
			
	
			
			
////			System.out.println(iCurrentTimeInMilliSecond + " : " + ((CFlightPlan)this.getCurrentPlan()).getCallsign() + "'s Controller Status is " + ((AATCController)this.iATCController).isThreadContinueableAtInitialState());
//			// Wait Controller's Instruction
//			synchronized (iThreadLockerOwner) {
//				while(!((AATCController)this.iATCController).isThreadContinueableAtInitialState()) {
//					try {						
////						System.out.println(iCurrentTimeInMilliSecond + " : " + ((CFlightPlan)this.getCurrentPlan()).getCallsign() + " is waiting..");
//						iThreadLockerOwner.wait();
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}					
//				}				
//			}
////			System.out.println(iCurrentTimeInMilliSecond + " : " + ((CFlightPlan)this.getCurrentPlan()).getCallsign() + " waiting is done..");
			
			
			// Do Move Aircraft
			if(iNextEventTime<0 || iNextEventTime<=iCurrentTimeInMilliSecond) {
				iNextEventTime = -9999;
				
				
				// Request Pushback
				if((this.getMoveState() instanceof CAircraftNothingMoveState) && this.getMode() == EMode.DEP &&
					 iATCController instanceof CGroundController && lCurrentPlan.getScheduleTimeList().get(0).getTimeInMillis()<=iCurrentTimeInMilliSecond) {
					((CGroundController)iATCController).requestPushBack((CAircraft) this);
				}
				
				// Request Taxi after Pushback
				if((this.getMoveState() instanceof CAircraftTaxiingMoveState) && 
				   this.getMovementMode() == EAircraftMovementMode.PUSHBACK	&&this.getMode() == EMode.DEP &&
				    this.getPushbackPausedTimeInMilliSeconds()>=this.getPushbackPauseTimeInMilliSeconds()) {
					((CGroundController)iATCController).requestTaxiToRunway((CAircraft) this);
					
				}
				
				// Request Lineup Clearance
				if((this.getMoveState() instanceof CAircraftTaxiingMoveState) && 
					this.getATCController() instanceof CLocalController &&
					this.getMovementMode() == EAircraftMovementMode.TAXIING	&& this.getMode() == EMode.DEP &&					
					this.calculateRemainingRouteDistance(this.getDepartureRunway().findEnteringNodeForDeparture()) < 150) {					
					((CLocalController)iATCController).requestLineUp((CAircraft) this);
				}
				
				// Request Takeoff Clearance
				if(this.getMoveState() instanceof CAircraftLineUpMoveState &&
				   this.getCurrentVelocity().getVelocity()<2) {
					this.setMovementMode(EAircraftMovementMode.TAKEOFF);
					this.setMovementStatus(EAircraftMovementStatus.TAKEOFF);
					((CLocalController)iATCController).requestTakeoff((CAircraft) this);
				}
				
				
				// Terminate Aircraft After Takeoff
				if(this.getMoveState() instanceof CAircraftTerminationMoveState) {
					this.getCurrentPosition().setXYCoordination(-99999999999.0, -999999999.0);
					this.setCurrentVelocity(0);
					this.iATCController.handOffAircraft(null, (CAircraft)this);
					this.iATCController = null;
					this.iConflictVehicle = null;
					this.iLeadingVehicle  = null;
					iSimClockObserver.deleteFromClock(this);
					notifyToClockImDone();					
					break;
				}
				
				
				
				// Request Landing to Approach 
				if(!(this.getMoveState() instanceof CAircraftLandingMoveState) &&
				   !(this.getMoveState() instanceof CAircraftApproachMoveState) &&
						this.getMode() == EMode.ARR &&
						iATCController instanceof CApproachController) {	
					this.setMovementStatus(EAircraftMovementStatus.APPROACHING);
					this.setMovementMode(EAircraftMovementMode.APPROACHING);
					((CApproachController)iATCController).requestLanding((CAircraft) this);					
				}
				
				
				
				// Handoff to Local controller Request Landing to LocalController
				if(this.getMovementMode()== EAircraftMovementMode.APPROACHING &&
				   !(this.iATCController instanceof CLocalController)) {
					
					double lRemainingDistance = this.calculateDistanceBtwCoordination(this.getCurrentPosition(), this.getCurrentFlightPlan().getNode(0).getCoordination());
					if(lRemainingDistance< 8*1852) {					
						CRunway a = this.getArrivalRunway();
						this.iATCController.handOffAircraft(this.getArrivalRunway().getATCController(), (CAircraft)this);
						((CLocalController)iATCController).requestLanding((CAircraft) this);			
					}
				}
				
				
				
				// Request Taxi to Spot
				if(this.getMovementMode()==EAircraftMovementMode.LANDING &&
						this.getMovementStatus() == EAircraftMovementStatus.LANDING_DECEL&&
						this.getExitTaxiwayNode()!=null &&
						this.getCurrentFlightPlan().getNode(this.getCurrentFlightPlan().getNodeList().size()-1) instanceof CAirport) {
					((CLocalController)iATCController).requestTaxiToSpot((CAircraft) this);
				}
				
				
				
				
				
				
				
				// Move This Aircraft
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






