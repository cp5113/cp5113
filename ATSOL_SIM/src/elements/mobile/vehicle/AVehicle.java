/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * AVehicle.java
 */
package elements.mobile.vehicle;
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
 * @date : May 12, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 12, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import elements.IElementObservableClock;
import elements.mobile.AMobile;
import elements.mobile.human.IATCController;
import elements.mobile.vehicle.state.CAircraftNothingMoveState;
import elements.mobile.vehicle.state.IVehicleMoveState;
import elements.network.ALink;
import elements.network.ANode;
import elements.operator.AOperator;
import elements.property.AVehiclePerformance;
import elements.property.AVehicleType;
import elements.property.EMode;
import elements.table.ITableAble;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import elements.util.phy.CVelocity;
import elements.util.phy.EVelocityUnit;
import javafx.scene.paint.Color;
import sim.CAtsolSimMain;
import sim.clock.CSimClockOberserver;
import sim.clock.ISimClockOberserver;
import sim.gui.CDrawingInform;
import sim.gui.EShape;
import sim.gui.IDrawingObject;

/**
 * @author S. J. Yun
 *
 */
public abstract class AVehicle extends AMobile implements ITableAble, IDrawingObject, IElementObservableClock, Runnable{
	
	// Strategy Pattern과 Observer Pattern을 공부해야 함
	// S : http://hyeonstorage.tistory.com/146
	// S : http://flowarc.tistory.com/entry/1-Strategy-Pattern
	// O : http://flowarc.tistory.com/entry/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%98%B5%EC%A0%80%EB%B2%84-%ED%8C%A8%ED%84%B4Observer-Pattern
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	protected		AVehicleType			iVehcleType;
	
	protected		CVehicleStatus			iCurrentStatus;
	protected		CVehicleStatus			iPreviousStatus;
	protected		CVehicleStatus			iNextStatus;
	
	protected		IATCController			iATCController;
	
	protected		IVehicleMoveState		iVehicleMoveState = new CAircraftNothingMoveState();

	
	protected		List<AVehiclePlan>		iPlanList 				= Collections.synchronizedList(new ArrayList<AVehiclePlan>());
	protected		AVehiclePlan			iCurrentPlan			;
	
	protected		CCoordination			iCurrentPostion		= new CCoordination(-9999999, -9999999, EGEOUnit.METER);
	protected		CCoordination			iPreviousPostion 	= new CCoordination(-9999999, -9999999, EGEOUnit.METER);
	protected		CCoordination			iNextPostion  		= new CCoordination(-9999999, -9999999, EGEOUnit.METER);
	protected		CAltitude				iCurrentAltitude 	= new CAltitude(0, EGEOUnit.FEET);
	protected		CVelocity				iCurrentVelocity	= new CVelocity(0, EVelocityUnit.METER_PER_SEC);
	
	protected 	CDrawingInform				iDrawingInform = new CDrawingInform(iCurrentPostion,iCurrentAltitude,EShape.DOT,Color.BLUE,true,30.0);
	protected		List<ANode>				iRoutingInfo;
	protected		List<ALink>				iRoutingInfoLink;
	protected		double					iRoutingRemainingDistance = 0.0;
	protected	EMode						iMode;
	
	protected 		ANode					iCurrentNode;
	protected 		ALink					iCurrentLink;
	
	protected		AOperator				iOperator;
	protected		AVehicle				iLeadingVehicle = null;
	
	protected		double					iPushbackPauseTimeInMilliSeconds = 180000; // 3 minutes
	
	protected ISimClockOberserver iSimClockObserver;
	
	
	
	public synchronized IATCController getATCController() {
		return iATCController;
	}
	public synchronized void setATCController(IATCController aATCController) {
		iATCController = aATCController;
	}
	public synchronized AVehicle getLeadingVehicle() {
		return iLeadingVehicle;
	}
	public synchronized void setLeadingVehicle(AVehicle aLeadingVehicle) {
		iLeadingVehicle = aLeadingVehicle;
	}
	public synchronized double getRoutingRemainingDistance() {		
		calculateRemainingRouteDistance();
		return iRoutingRemainingDistance;
	}
	public synchronized void setRoutingRemainingDistance(double aRoutingRemainingDistance) {		
		iRoutingRemainingDistance = aRoutingRemainingDistance;
	}
	public synchronized List<ANode> getRoutingInfo() {
		return iRoutingInfo;
	}
	public synchronized void setRoutingInfo(List<? extends ANode> aRoutingInfo) {
		
		// clear Node
		if(iRoutingInfo !=null) {
			for(ANode loopRoutingNode : iRoutingInfo) {
				loopRoutingNode.getVehicleWillUseList().remove(this);
			}
		}
		
		// Set Routing Info List (Nodes)
		iRoutingInfo = (List<ANode>) aRoutingInfo;
		iRoutingRemainingDistance = 0.0;
		
		// Set Routing Info List (Links)
		List<ALink> lRouteListLink = new ArrayList<ALink>();
		for(int loopNode = 0; loopNode<aRoutingInfo.size()-1; loopNode++) {
			for(int loopLink = 0; loopLink < aRoutingInfo.get(loopNode).getOwnerLinkList().size(); loopLink++) {
				if(aRoutingInfo.get(loopNode).getOwnerLinkList().get(loopLink).getNodeList().contains(aRoutingInfo.get(loopNode+1))) {
					lRouteListLink.add(aRoutingInfo.get(loopNode).getOwnerLinkList().get(loopLink));
					iRoutingRemainingDistance += lRouteListLink.get(lRouteListLink.size()-1).getDistance();
					break;
				}
			}
		}		

		setRoutingInfoLink(lRouteListLink);
		
		// Let nodes know this aircraft will use the nodes
		for(ANode loopNode : iRoutingInfo) {
			loopNode.getVehicleWillUseList().add(this);
		}
		
	}
	
	public void letNodeKnowsThisAircraftWillComming(){
	
	}
	
	public double calculateRemainingRouteDistance() {
		// Calculate distance from current position to the other node (longest length far from here) 
		double lRemainingDistanceFromHere = 0;
		// Calculate distance from current position to next node
		lRemainingDistanceFromHere += Math.sqrt((this.getCurrentPostion().getXCoordination() - this.getRoutingInfo().get(0).getCoordination().getXCoordination()) * (this.getCurrentPostion().getXCoordination() - this.getRoutingInfo().get(0).getCoordination().getXCoordination()) + 
				(this.getCurrentPostion().getYCoordination() - this.getRoutingInfo().get(0).getCoordination().getYCoordination()) * (this.getCurrentPostion().getYCoordination() - this.getRoutingInfo().get(0).getCoordination().getYCoordination()) );  

		// calculate distance remaining links
		int startIndex = 0;
		if(this.getRoutingInfo().size() != this.getRoutingInfoLink().size()) {
			startIndex = 0;	
		}else {
			startIndex = 1;
		}


		for(int loopLink = startIndex; loopLink < this.getRoutingInfoLink().size(); loopLink++) {
			lRemainingDistanceFromHere += this.getRoutingInfoLink().get(loopLink).getDistance();			
		}
		if(lRemainingDistanceFromHere<500) {
//			System.out.println();
		}
		this.iRoutingRemainingDistance = lRemainingDistanceFromHere;
		return lRemainingDistanceFromHere;
	}
	
	public synchronized double calculateRemainingRouteDistance(ANode aDestinationNode) {
		// Calculate distance from current position to the other node (longest length far from here) 
		double lRemainingDistanceFromHere = 0;
		// Calculate distance from current position to next node
		lRemainingDistanceFromHere += Math.sqrt((this.getCurrentPostion().getXCoordination() - this.getRoutingInfo().get(0).getCoordination().getXCoordination()) * (this.getCurrentPostion().getXCoordination() - this.getRoutingInfo().get(0).getCoordination().getXCoordination()) + 
				(this.getCurrentPostion().getYCoordination() - this.getRoutingInfo().get(0).getCoordination().getYCoordination()) * (this.getCurrentPostion().getYCoordination() - this.getRoutingInfo().get(0).getCoordination().getYCoordination()) );  
		
		if(this.getRoutingInfo().get(0).equals(aDestinationNode)) {
			this.setRoutingRemainingDistance(lRemainingDistanceFromHere);
			return lRemainingDistanceFromHere;
		}
		
		// calculate distance remaining links
		int startIndex = 0;
		if(this.getRoutingInfo().size() != this.getRoutingInfoLink().size()) {
			startIndex = 0;	
		}else {
			startIndex = 1;
		}

		
		for(int loopLink = startIndex; loopLink < this.getRoutingInfoLink().size(); loopLink++) {
			lRemainingDistanceFromHere += this.getRoutingInfoLink().get(loopLink).getDistance();
			if(iRoutingInfoLink.get(loopLink).getNodeList().contains(aDestinationNode)) {
				break;
			}						
		}
		this.setRoutingRemainingDistance(lRemainingDistanceFromHere);
		return lRemainingDistanceFromHere;
	}
	
	public synchronized ALink getRoutingLinkInfoUsingNode(ANode aNode) {
		return iRoutingInfoLink.get(iRoutingInfo.indexOf(aNode));
	}
	
	public synchronized void removeRoutingInfo(int aNodeIndex) {

		// Let node knows this vehicle departed from this node in route
		iRoutingInfo.get(aNodeIndex).getVehicleWillUseList().remove(this);	
		
		
		
		// Find Connected Link
		int numOfLinkHasNode = 0;
		for(ALink loopLink : iRoutingInfoLink) {
			if(loopLink.getNodeList().contains(iRoutingInfo.get(aNodeIndex))) {
				numOfLinkHasNode++;
			}
		}
		// Remove Routing Link info
		if(numOfLinkHasNode == 2) {
			iRoutingInfoLink.remove(aNodeIndex);
		}
		// Remove Routing Node info
		iRoutingInfo.remove(aNodeIndex);
		

	}
	
	public synchronized List<ALink> getRoutingInfoLink() {
		return iRoutingInfoLink;
	}
	public synchronized void setRoutingInfoLink(List<? extends ALink> aRoutingInfoLink) {
		iRoutingInfoLink = (List<ALink>) aRoutingInfoLink;
	}
	public CVehicleStatus getCurrentStatus() {
		return iCurrentStatus;
	}
	public void setCurrentStatus(CVehicleStatus aCurrentStatus) {
		iCurrentStatus = aCurrentStatus;
	}
	public CVehicleStatus getPreviousStatus() {
		return iPreviousStatus;
	}
	public void setPreviousStatus(CVehicleStatus aPreviousStatus) {
		iPreviousStatus = aPreviousStatus;
	}
	public CVehicleStatus getNextStatus() {
		return iNextStatus;
	}
	public void setNextStatus(CVehicleStatus aNextStatus) {
		iNextStatus = aNextStatus;
	}
	public List<AVehiclePlan> getPlanList() {
		return iPlanList;
	}
	public void setPlanList(List<AVehiclePlan> aPlanList) {
		iPlanList = aPlanList;
	}
	public CCoordination getCurrentPostion() {
		return iCurrentPostion;
	}
	public void setCurrentPostion(CCoordination aCurrentPostion) {
		iCurrentPostion = aCurrentPostion;
	}
	public CCoordination getPreviousPostion() {
		return iPreviousPostion;
	}
	public void setPreviousPostion(CCoordination aPreviousPostion) {
		iPreviousPostion = aPreviousPostion;
	}
	public CCoordination getNextPostion() {
		return iNextPostion;
	}
	public void setNextPostion(CCoordination aNextPostion) {
		iNextPostion = aNextPostion;
	}
	public AOperator getOperator() {
		return iOperator;
	}
	public void setOperator(AOperator aOperator) {
		iOperator = aOperator;
	}
	public AVehiclePerformance getPerformance() {
		return iVehcleType.getPerformance();
	}
	public void setPerformance(AVehiclePerformance aPerformance) {
		iVehcleType.setVehicletPerformance(aPerformance);
	}
	public AVehiclePlan getCurrentPlan() {
		return iCurrentPlan;
	}
	public void setCurrentPlan(AVehiclePlan aCurrentPlan) {
		iCurrentPlan = aCurrentPlan;
	}
	public AVehicleType getVehcleType() {
		return iVehcleType;
	}
	public void setVehcleType(AVehicleType aVehcleType) {
		iVehcleType = aVehcleType;
	}
	
	
	@Override
	public CDrawingInform getDrawingInform() {
		// TODO Auto-generated method stub
		return iDrawingInform;
	}
	public synchronized EMode getMode() {
		return iMode;
	}
	public synchronized void setMode(EMode aMode) {
		iMode = aMode;
	}
	
	
	public void setMoveState(IVehicleMoveState aVehicleMoveState) {
		iVehicleMoveState = aVehicleMoveState;
	}
	
	public IVehicleMoveState  getMoveState() {
		return iVehicleMoveState;
	}
	public void doMoveVehicle() {
		iVehicleMoveState.doMove(CSimClockOberserver.getInstance().getIncrementStepInMiliSec(),iCurrentTimeInMilliSecond, this);
	}
	public synchronized CAltitude getCurrentAltitude() {
		return iCurrentAltitude;
	}
	public synchronized CVelocity getCurrentVelocity() {
		return iCurrentVelocity;
	}
	public synchronized ANode getCurrentNode() {
		return iCurrentNode;
	}
	public synchronized void setCurrentNode(ANode aCurrentNode) {
		iCurrentNode = aCurrentNode;
	}
	public synchronized ALink getCurrentLink() {
		return iCurrentLink;
	}
	public synchronized void setCurrentLink(ALink aCurrentLink) {
		iCurrentLink = aCurrentLink;
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






