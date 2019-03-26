/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * APlan.java
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
 * @date : May 13, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 13, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import elements.network.INode;
import elements.util.geo.CAltitude;
import elements.util.geo.EGEOUnit;

/**
 * @author S. J. Yun
 *
 */
public class AVehiclePlan {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private AVehicle			iOwner					= null;
	private INode				iCurrentNode				;
	private INode				iOriginationNode			;
	private INode				iDestinationNode			;
	private List<INode>			iNodeList				= Collections.synchronizedList(new ArrayList<INode>());
	private List<Calendar>		iScheduleTimeList		= Collections.synchronizedList(new ArrayList<Calendar>());
	private List<Calendar>		iEstimateTimeList		= Collections.synchronizedList(new ArrayList<Calendar>());
	private List<Calendar>		iActualTimeList			= Collections.synchronizedList(new ArrayList<Calendar>());
	private List<CAltitude>		iAltitudeList			= Collections.synchronizedList(new ArrayList<CAltitude>());
	
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	public AVehiclePlan(AVehicle aOwner, INode aOrigination, INode aDestination) {
		iOwner = aOwner;
		iOriginationNode = aOrigination;
		iDestinationNode = aDestination;

	}
	
	public void addPlanItem(INode aNode, Calendar aScheduleTime) {
		iNodeList.add(aNode);
		iScheduleTimeList.add(aScheduleTime);
		iEstimateTimeList.add(null);
		iActualTimeList.add(null);
		iAltitudeList.add(new CAltitude(0, EGEOUnit.FEET));
	}
	public void addPlanItem(INode aNode, Calendar aScheduleTime, CAltitude aAltitude) {
		iNodeList.add(aNode);
		iScheduleTimeList.add(aScheduleTime);
		iEstimateTimeList.add(null);
		iActualTimeList.add(null);
		iAltitudeList.add(aAltitude);
	}
	
	public void removePlanItem(INode aNode) {
		int indexRemove = iNodeList.indexOf(aNode);
		iNodeList.remove(indexRemove);
		iScheduleTimeList.remove(indexRemove);
		iEstimateTimeList.remove(indexRemove);
		iActualTimeList.remove(indexRemove);
		iAltitudeList.remove(indexRemove);
	}
	
	public void insertPlanItem(int beforeIndex, INode aNode, Calendar aScheduleTime, CAltitude aAltitude) {
		iNodeList.add(beforeIndex, aNode);
		iScheduleTimeList.add(beforeIndex,aScheduleTime);
		iEstimateTimeList.add(beforeIndex,null);
		iActualTimeList.add(beforeIndex,null);
		iAltitudeList.add(beforeIndex,aAltitude);	
	}
	public void insertPlanItem(INode nodeBefor, INode aNode, Calendar aScheduleTime, CAltitude aAltitude) {
		int beforeIndex = iNodeList.indexOf(nodeBefor);
		iNodeList.add(beforeIndex, aNode);
		iScheduleTimeList.add(beforeIndex,aScheduleTime);
		iEstimateTimeList.add(beforeIndex,null);
		iActualTimeList.add(beforeIndex,null);
		iAltitudeList.add(beforeIndex,aAltitude);
	}
	
	public int size() {
		return iNodeList.size();
	}
	public INode getNode(int a) {
		return iNodeList.get(a);
	}
	
	@Override
	public String toString() {		
		return iOwner.getName();
	}
	public void setAltitude(int index, CAltitude aAltitude) {
		iAltitudeList.get(index).setAltitude(aAltitude.getAltitude());
	}
	public void setAltitude(int index, double aAltitude) {
		iAltitudeList.get(index).setAltitude(aAltitude);
	}

	public AVehicle getOwner() {
		return iOwner;
	}

	public INode getCurrentNode() {
		return iCurrentNode;
	}

	public INode getOriginationNode() {
		return iOriginationNode;
	}

	public INode getDestinationNode() {
		return iDestinationNode;
	}

	public List<INode> getNodeList() {
		return iNodeList;
	}

	public List<Calendar> getScheduleTimeList() {
		return iScheduleTimeList;
	}

	public List<Calendar> getEstimateTimeList() {
		return iEstimateTimeList;
	}

	public List<Calendar> getActualTimeList() {
		return iActualTimeList;
	}

	public List<CAltitude> getAltitudeList() {
		return iAltitudeList;
	}

	public void setCurrentNode(INode aCurrentNode) {
		iCurrentNode = aCurrentNode;
	}

	public void setScheduleTimeList(List<Calendar> aScheduleTimeList) {
		iScheduleTimeList = aScheduleTimeList;
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






