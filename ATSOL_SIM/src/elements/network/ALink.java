/**
 * ATSOL_SIM
 * network
 * ALink.java
 */
package elements.network;
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
 * @date : May 11, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 11, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import elements.AElement;
import elements.COccupyingInform;
import elements.facility.AFacility;
import elements.facility.CTaxiwayNode;
import elements.mobile.human.IATCController;
import elements.mobile.vehicle.AVehicle;
import elements.util.geo.CDegree;

/**
 * @author S. J. Yun
 *
 */
public abstract class ALink extends AFacility {
	/*
	================================================================

			           Initializing Section

	================================================================
	 */

	protected 	boolean					iIsOccuping	=	false;
	protected 	int						iCapacity	=	Integer.MAX_VALUE;
		
	
	protected 	ANode					iOrigin,iDestination;
	protected 	ArrayList<ANode>		iNodeList 				= new ArrayList<ANode>();
	protected 	ArrayList<ALink>		iAdjacentLink 			= new ArrayList<ALink>();
	protected	double					iDistance				=	0.0;
	protected 	CDegree					iHeading;
	protected	CDegree					iOppositeHeading;
	protected	ArrayList<AElement>		iOccupyingList			= new ArrayList<AElement>();	
	protected	ArrayList<COccupyingInform>		iOccupyingSchedule	    = new ArrayList<COccupyingInform>();
	
	
	protected double					iSpeedLimitKts          = 15;
	
	/*
	================================================================

						Methods Section

	================================================================
	 */
	public void enteringLink(AElement aElement) {
		iOccupyingList.add(aElement);
	}
	public void exitingLink(AElement aElement) {
		iOccupyingList.remove(aElement);
	}
	public int getNumOfElementUsingLink() {
		return iOccupyingList.size();
	}
	public ArrayList<AElement> getOccupyingList(){
		return iOccupyingList;
	}
	public ArrayList<COccupyingInform>	getOccupyingSchedule(){
		return iOccupyingSchedule;
	}
	public synchronized void addToOccupyingSchedule(long aStartTime, long aEndTime, AElement aElement, ANode aDestinationNode) {
		iOccupyingSchedule.add(new COccupyingInform(this, aStartTime, aEndTime, aElement,aDestinationNode));
		Collections.sort(iOccupyingSchedule);
	}
	public synchronized void removeFromOccupyingSchedule(AElement aElement) {
		for(int i=0; i<iOccupyingSchedule.size();i++) {
			if( iOccupyingSchedule.get(i).getOccupyingObject().equals(aElement)) {
				iOccupyingSchedule.remove(i);				
				break;
			}
		}
	}
	public synchronized boolean isOppositDirectionElementInTimeWindow(long aStartTime, long aEndTime,ANode aDestinationNode) {
		boolean lOutput = false;
		
		
		for(int i=0; i<iOccupyingSchedule.size(); i++) {
		
			if(!aDestinationNode.equals(iOccupyingSchedule.get(i).getDestination())) {
				if(aStartTime >= iOccupyingSchedule.get(i).getStartTime() && aStartTime <=iOccupyingSchedule.get(i).getEndTime()) {
					return true;
				}
				if(aEndTime >= iOccupyingSchedule.get(i).getStartTime() && aEndTime <=iOccupyingSchedule.get(i).getEndTime()) {
					return true;
				}
			}
		}		
		
		return lOutput;
	}
	
	public void addToOccupyingSchedule(COccupyingInform aCOccupyingInform) {
		iOccupyingSchedule.add(new COccupyingInform(this, aCOccupyingInform.getStartTime(), aCOccupyingInform.getEndTime(), aCOccupyingInform.getOccupyingObject(),aCOccupyingInform.getDestination()));
		Collections.sort(iOccupyingSchedule);
	}
	
	
	public synchronized boolean isIsOccuping() {
		return iIsOccuping;
	}
	public synchronized void setIsOccuping(boolean aIsOccuping) {
		iIsOccuping = aIsOccuping;
	}
	public synchronized int getCapacity() {
		return iCapacity;
	}
	public synchronized void setCapacity(int aCapacity) {
		iCapacity = aCapacity;
	}
	public synchronized ANode getOrigin() {
		return iOrigin;
	}
	public synchronized void setOrigin(ANode aOrigin) {
		iOrigin = aOrigin;
	}
	public synchronized ANode getDestination() {
		return iDestination;
	}
	public synchronized void setDestination(ANode aDestination) {
		iDestination = aDestination;
	}
	public synchronized ArrayList<ANode> getNodeList() {
		return iNodeList;
	}
	public synchronized void setNodeList(ArrayList<ANode> aNodeList) {
		iNodeList = aNodeList;
	}
	public synchronized ArrayList<ALink> getAdjacentLink() {
		return iAdjacentLink;
	}
	public synchronized void setAdjacentLink(ArrayList<ALink> aAdjacentLink) {
		iAdjacentLink = aAdjacentLink;
	}
	public synchronized double getDistance() {
		return iDistance;
	}
	public synchronized void setDistance(double aDistance) {
		iDistance = aDistance;
	}
	public synchronized CDegree getHeading() {
		return iHeading;
	}
	public synchronized void setHeading(CDegree aHeading) {
		iHeading = aHeading;
	}
	public synchronized CDegree getOppositeHeading() {
		return iOppositeHeading;
	}
	public synchronized void setOppositeHeading(CDegree aOppositeHeading) {
		iOppositeHeading = aOppositeHeading;
	}
	@Override
	public void setATCControllerToChildren(IATCController aController) {
		// TODO Auto-generated method stub
		Iterator<ANode> iter = this.iNodeList.iterator();
		while(iter.hasNext()) {
			CTaxiwayNode lNode = (CTaxiwayNode) iter.next();
			lNode.setATCController(aController);
			lNode.setATCControllerToChildren(aController);
		}
	}
	public synchronized double getSpeedLimitKts() {
		return iSpeedLimitKts;
	}
	public synchronized void setSpeedLimitKts(double aSpeedLimitKts) {
		iSpeedLimitKts = aSpeedLimitKts;
	}
	public synchronized	double getSpeedLimitMps() {
		return iSpeedLimitKts * 0.514444444;
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






