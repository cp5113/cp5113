/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraft.java
 */
package elements.mobile.vehicle;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import api.CAircraftMoveStateAPI;
import api.CChangeAircraftMoveState;
import api.CPushBackPauseTimeAPI;
import api.CTurnaroundTime;
import elements.airspace.CWaypoint;
import elements.area.ASector;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CSpot;
import elements.facility.CTaxiwayNode;
import elements.mobile.human.AATCController;
import elements.mobile.human.CApproachController;
import elements.mobile.human.CGroundController;
import elements.mobile.human.CLocalController;
import elements.mobile.human.IATCController;
import elements.mobile.vehicle.state.CAircraftApproachMoveState;
import elements.mobile.vehicle.state.CAircraftGroundConflictStopMoveState;
import elements.mobile.vehicle.state.CAircraftLandingMoveState;
import elements.mobile.vehicle.state.CAircraftLineUpMoveState;
import elements.mobile.vehicle.state.CAircraftNothingMoveState;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.mobile.vehicle.state.CAircraftTerminationMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ANode;
import elements.operator.CAirline;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EMode;
import elements.table.ITableAble;
import elements.util.geo.CCoordination;
import sim.CAtsolSimMain;
import sim.clock.CDispatchAircraftThreadByTime;
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
	
	private ArrayList<CTaxiwayNode> iCrossingRunwayNodeList = new ArrayList<CTaxiwayNode>();
	private ArrayList<CRunway> 		iCrossingRunwayList 	= new ArrayList<CRunway>();
	private double					iTimeFromThresholdInSeconds		= -9999;
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
	
	long 	iETA             = -9L;
	
	CTaxiwayNode iExitTaxiwayNode = null;
	
	
	
	
	
	public synchronized double getTimeFromThreshold() {
		return iTimeFromThresholdInSeconds;
	}

	public synchronized void setTimeFromThreshold(double aTimeFromThreshold) {
		iTimeFromThresholdInSeconds = aTimeFromThreshold;
	}

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

	public synchronized ArrayList<CTaxiwayNode> getCrossingRunwayNodeList() {
		return iCrossingRunwayNodeList;
	}

	public synchronized ArrayList<CRunway> getCrossingRunwayList() {
		return iCrossingRunwayList;
	}

	public synchronized long getETA() {
		return iETA;
	}

	public synchronized void setETA(long aETA) {
		iETA = aETA;
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
		iImDone = 1;
	}






	@Override
	public void addClock(ISimClockOberserver aSimclock) {
		// TODO Auto-generated method stub
		iSimClockObserver = aSimclock;
	}



	@Override
	public void run() {
		iImDone = 0;
		
		// Initialize status of this aircraft
		CFlightPlan lCurrentPlan = (CFlightPlan) this.iCurrentPlan;
//		this.getCurrentPosition().setXCoordination(lCurrentPlan.getNode(0).getCoordination().getXCoordination());
//		this.getCurrentPosition().setYCoordination(lCurrentPlan.getNode(0).getCoordination().getYCoordination());
		
		
		// Set API Aircraft State Change
		CChangeAircraftMoveState lAircraftMoveStateChangable = new CChangeAircraftMoveState();
		
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
			System.out.println(((CFlightPlan)iCurrentPlan).getCallsign() + " is initialized by controller(" + iATCController + ")");
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
			iImDone = 0;
		
			
			// Debugging
			
			
			
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
			
			if(this.toString().equalsIgnoreCase("BMAL") &&
					iCurrentTimeInMilliSecond >= 1336858404000L) {
//				System.err.println("AAircraft : Exit Why???");
//				System.out.println();
			}
			
			
			
			// Do Move Aircraft
			if(iNextEventTime<0 || iNextEventTime<=iCurrentTimeInMilliSecond) {
				iNextEventTime = -9999;
				
				// Aircraft State API
				if(lAircraftMoveStateChangable.changeAircraftMoveStatebyAPI((CAircraft) this)) {
					this.setMoveState(new CAircraftMoveStateAPI());
				}
				
				
				// Request Pushback
				if((this.getMoveState() instanceof CAircraftNothingMoveState) && this.getMode() == EMode.DEP &&
					 iATCController instanceof CGroundController && lCurrentPlan.getScheduleTimeList().get(0).getTimeInMillis()<=iCurrentTimeInMilliSecond) {
					((CGroundController)iATCController).requestPushBack((CAircraft) this);
				}
				
				// Request Taxi after Pushback
				if((this.getMoveState() instanceof CAircraftTaxiingMoveState) && 
				   this.getMovementMode() == EAircraftMovementMode.PUSHBACK	&&this.getMode() == EMode.DEP &&
				    this.getPushbackPausedTimeInMilliSeconds()>=this.getPushbackPauseTimeInMilliSeconds()) {
					System.out.println(this + " : Request Taxi after pushback");
					((CGroundController)iATCController).requestTaxiToRunway((CAircraft) this);
				
					
				}
	
				// Request Re Route
				if(this.getMovementMode() == EAircraftMovementMode.TAXIING &&
						this.iLeadingVehicle != null &&
						((CAircraft)this.iLeadingVehicle).getMovementMode() == EAircraftMovementMode.PUSHBACK &&
						iATCController instanceof CGroundController) {
					((CGroundController)iATCController).requestRecheckTaxiToRunway((CAircraft) this);
					if(this.getCurrentFlightPlan().getNode(0) instanceof CSpot) {
						System.err.println("AAircraft  : After Rerouting, Spot is alive" );
					}
				}
				
				
				
				// Request Lineup Clearance
				if((this.getMoveState() instanceof CAircraftTaxiingMoveState) && 
					this.getATCController() instanceof CLocalController &&
					this.getMovementMode() == EAircraftMovementMode.TAXIING	&& this.getMode() == EMode.DEP &&					
					this.calculateRemainingRouteDistance(this.getDepartureRunway().findEnteringNodeForDeparture()) < 150 &&
					this.getLeadingVehicle() == null) {					
					((CLocalController)iATCController).requestLineUp((CAircraft) this);
				}
				
				// Request Takeoff Clearance
				if(this.getMoveState() instanceof CAircraftLineUpMoveState &&
				   this.getCurrentVelocity().getVelocity()<2) {					
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
//					notifyToClockImDone();		
					// Escape thread
					break;
				}
				
				
				
				// Request Landing to Approach 
				if(!(this.getMoveState() instanceof CAircraftLandingMoveState) &&
				   !(this.getMoveState() instanceof CAircraftApproachMoveState) &&
						this.getMode() == EMode.ARR &&
						iATCController instanceof CApproachController) {	
					this.setMovementStatus(EAircraftMovementStatus.APPROACHING);
					this.setMovementMode(EAircraftMovementMode.APPROACHING);
					
					CAircraftType		lACType		=	(CAircraftType) this.getVehcleType();
					double  			lNormSpeed	= ((CAircraftPerformance)lACType.getPerformance()).getVat();
					double 				lSTD 		= 5.14444; // 10 kts
					lNormSpeed 						= lNormSpeed + (lSTD*this.getRandomNumber() - lSTD/2);
					this.setCurrentVelocity(lNormSpeed);
					
					
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
				
				
				
				// Request Taxi to Spot to LocalController
				if(this.getMovementMode()==EAircraftMovementMode.LANDING &&
						this.getMovementStatus() == EAircraftMovementStatus.LANDING_DECEL&&
						this.getExitTaxiwayNode()!=null &&
						this.getCurrentFlightPlan().getNode(this.getCurrentFlightPlan().getNodeList().size()-1) instanceof CAirport) {
					((CLocalController)iATCController).requestTaxiToSpot((CAircraft) this);
				}
				
				
				// Crossing Runway
				if(this.getMode() == EMode.ARR && 
					this.getMovementMode() == EAircraftMovementMode.TAXIING &&
					this.getCrossingRunwayList().size()>0 &&
					iATCController instanceof CLocalController && 
					this.calculateRemainingRouteDistance(this.getCrossingRunwayNodeList().get(0)) <= this.getCrossingRunwayList().get(0).getRunwaySafetyWidth()+50) {
					((CLocalController)iATCController).requestCrossingRunway((CAircraft) this);					
					
					
				}
					
				
				
				// Hand off Local to Ground
				if(this.getMovementMode()==EAircraftMovementMode.TAXIING &&
						this.getCurrentLink().getATCController() != null &&
						!this.getATCController().equals(this.getCurrentLink().getATCController())){
					
					// Add Taxiing Instruction
					iATCController.handOffAircraft(this.getCurrentLink().getATCController(), (CAircraft) this);
										
				}
				
				
				// After Arrival at Gate
				if(this.getMode() == EMode.ARR && 
					this.getMovementMode() == EAircraftMovementMode.TAXIING &&
					this.getRoutingInfo().size()==1 &&
					this.calculateDistanceBtwCoordination(this.getCurrentPosition(), this.getCurrentFlightPlan().getArrivalSpot().getTaxiwayNode().getCoordination()) == 0){
					
					// Handoff from Controller
					((AATCController)iATCController).getAircraftList().remove(this);
					iATCController = null;
					
					// Change State
					this.setMoveState(new CAircraftNothingMoveState());
					this.setMode(EMode.ETC);
					this.setMovementMode(EAircraftMovementMode.NOTHING);
					this.setMovementStatus(EAircraftMovementStatus.NOTHING);
					
					if(!this.getCurrentFlightPlan().getArrivalSpot().getVehicleWillUseList().contains(this)) {
						this.getCurrentFlightPlan().getArrivalSpot().getVehicleWillUseList().add(this);
					}
					// Set Turnaround Time
					iNextEventTime = iCurrentTimeInMilliSecond + new CTurnaroundTime().getAfterArrivalTurnaroundTimeInSeconds((CAircraft) this, (CAirport)this.getCurrentNode().getOwnerObject()) * 1000;
					

				}
				
				
				

						
				// Escape or Connect next Flight
				if(iATCController == null &&
						this.getMode() == EMode.ETC && 
						this.getMovementMode() == EAircraftMovementMode.NOTHING &&
						this.getMovementStatus() == EAircraftMovementStatus.NOTHING&&
						this.getMoveState() instanceof CAircraftNothingMoveState &&
						iNextEventTime<=iCurrentTimeInMilliSecond){
					
					
					
					// Escape Thread
					// Notify to Clock
//					notifyToClockImDone();
					((CSimClockOberserver) iSimClockObserver).deleteFromClock(this);
					iPreviousTimeInMilliSecond = iCurrentTimeInMilliSecond;
					
					
					// Escape aircraft Data
					if(this.getPlanList().size()==0) {
						this.getCurrentPosition().setXYCoordination(-99999999999.0, -999999999.0);
						this.setCurrentVelocity(0);
						// Clear Spot and Node
						this.getCurrentNode().setIsOccuping(false);
						this.getCurrentNode().getVehicleWillUseList().remove(this);					
						((CTaxiwayNode)this.getCurrentNode()).getSpot().setIsOccuping(false);
						((CTaxiwayNode)this.getCurrentNode()).getSpot().getVehicleWillUseList().remove(this);
						
						// Erase Safety Area						;
						this.setCurrentVelocity(this.getCurrentVelocity().getVelocity()*0.0);
					}
					
					break;
				}
				
				
				
				
			};//if(iNextEventTime<0 || iNextEventTime<=iCurrentTimeInMilliSecond) {
			
			
			
			
			
			
			
			// Debugging After runway Exit, aircraft move state is not changed
			if(this.getCurrentNode() != null &&
					this.getCurrentNode() instanceof CTaxiwayNode &&
					((CTaxiwayNode)this.getCurrentNode()).getRunway() == null &&
					this.getMode() == EMode.ARR &&
							!(this.getMoveState() instanceof CAircraftNothingMoveState) &&
					!(this.getMoveState() instanceof CAircraftTaxiingMoveState) && 
					!(this.getMoveState() instanceof CAircraftGroundConflictStopMoveState)) {
				System.err.println("AAircraft : After runway Exit, aircraft move state is not changed");
				System.out.println("");
			}
			
			// Debugging Flight Plan(0) has Spot information during Departure Taxiing
			if(this.getMode() == EMode.DEP &&
					this.getMoveState() instanceof CAircraftTaxiingMoveState &&
					this.getCurrentFlightPlan().getNode(0) instanceof CSpot) {
				System.err.println("AAircraft : Flight Plan(0) has Spot information during Departure Taxiing");
				System.out.println();
			}
			
		
			
			// Move This Aircraft
			doMoveVehicle();
			
			// Write to file
			if(!(this.getMoveState() instanceof CAircraftNothingMoveState)) {
				try {				
					CAtsolSimMain.getOutputFileTrajectoryWriter().write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(iCurrentTimeInMilliSecond)) + "," + 
							this.getCurrentFlightPlan().getCallsign() + "," + 
							this.toString() + "," + 
							this.getMode()  + "," + 
							this.getMovementMode()  + "," + 
							this.getMovementStatus() + "," +
							this.getMoveState().getClass().getSimpleName() + "," + 
							this.getCurrentLink() + "," + 
							this.getCurrentNode() + "," + 
							this.getCurrentPosition() + "," + 
							this.getCurrentAltitude().getAltitude()  + "," + 
							this.getCurrentVelocity().getVelocity() + "\n");
				} catch (IOException e1) {
					System.err.println("AAircraft : Writing Error");
				}
			}
						
			// Debugging
			if(iCurrentTimeInMilliSecond != ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis()) {
				System.out.println("AC Current Time : " + iCurrentTimeInMilliSecond + " / Clock : " + ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis());
				try {
					throw new Exception();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("AC Current Time : " + iCurrentTimeInMilliSecond + " / Clock : " + ((CSimClockOberserver) iSimClockObserver).getCurrentTIme().getTimeInMillis());
					e.printStackTrace();
				}
			}
			
			
			// Notify to Clock
			notifyToClockImDone();
			iPreviousTimeInMilliSecond = iCurrentTimeInMilliSecond;
		} // while( ((CSimClockOberserver) iSimClockObserver).isRunning()) {
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






