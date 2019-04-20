/**
 * ATSOL_SIM
 * elements.facility
 * CAirport.java
 */
package elements.facility;
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

import java.util.List;

import algorithm.routing.DijkstraAlgorithm;
import algorithm.routing.IRoutingAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import elements.mobile.human.CGroundController;
import elements.mobile.human.CLocalController;
import elements.mobile.human.IATCController;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.network.INode;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.CDegree;

/**
 * @author S. J. Yun
 *
 */
/**
 * @author S. J. Yun
 *
 */
public class CAirport extends AFacility  implements INode {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	
	// Basic Information Group
	private ELocation  		iLocation	= ELocation.GROUND;
	
	private String			iOperator;
	
	private String			iAirportICAO; //4 letters
	private String			iAirportIATA; //3 letters
	
	private	CCoordination	iARP;
	private CAltitude		iElevation;
	private CDegree			iVariation;
	
	
	// "Has" Relationship - Facilities and Network	
	private List<CRunway>						iRunwayList 		= Collections.synchronizedList(new ArrayList<CRunway>());
	private List<CTaxiwayLink> 					iTaxiwayLinkList	= Collections.synchronizedList(new ArrayList<CTaxiwayLink>());
	private List<CTaxiwayNode> 					iTaxiwayNodeList 	= Collections.synchronizedList(new ArrayList<CTaxiwayNode>());
	private List<CSpot> 						iSpotList		 	= Collections.synchronizedList(new ArrayList<CSpot>());
	private List<CApron>	 					iApronList		 	= Collections.synchronizedList(new ArrayList<CApron>());
	
	// "Has" Relationship - Operator
	private List<CLocalController>	 			iLocalControllerList		 = Collections.synchronizedList(new ArrayList<CLocalController>());
	private List<CGroundController> 			iGroundControllerList		 = Collections.synchronizedList(new ArrayList<CGroundController>());

	private	double								iLonggestLinkLength		= 0;
	
	private List<CAircraft>						iDepartureAircraftList      = Collections.synchronizedList(new LinkedList<CAircraft>());
	private List<CAircraft>						iArrivalAircraftList        = Collections.synchronizedList(new LinkedList<CAircraft>());
	private List<CAircraft>						iAircraftList				=  Collections.synchronizedList(new LinkedList<CAircraft>());
	
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public synchronized void addDepartureList(CAircraft aAircraft) {
		iDepartureAircraftList.add(aAircraft);
		sortDepartureACList();
		
		// Reconstruct All List
		iAircraftList.clear();
		iAircraftList.addAll(iDepartureAircraftList);
		iAircraftList.addAll(iArrivalAircraftList);

	}
	public synchronized void addArrivalList(CAircraft aAircraft) {
		iArrivalAircraftList.add(aAircraft);
		sortArrivalACList();
		// Reconstruct All List
		iAircraftList.clear();
		iAircraftList.addAll(iDepartureAircraftList);
		iAircraftList.addAll(iArrivalAircraftList);
	}
	public synchronized void removeDepartureList(CAircraft aAircraft) {
		iDepartureAircraftList.remove(aAircraft);		
		sortDepartureACList();
		// Reconstruct All List
		iAircraftList.clear();
		iAircraftList.addAll(iDepartureAircraftList);
		iAircraftList.addAll(iArrivalAircraftList);
	}
	public synchronized void removeArrivalList(CAircraft aAircraft) {
		iArrivalAircraftList.remove(aAircraft);		
		sortArrivalACList();
		// Reconstruct All List
		iAircraftList.clear();
		iAircraftList.addAll(iDepartureAircraftList);
		iAircraftList.addAll(iArrivalAircraftList);
	}
	
	private	synchronized void sortDepartureACList() {
		Collections.sort(iDepartureAircraftList, new Comparator<CAircraft>() {
			@Override
			public int compare(CAircraft aArg0, CAircraft aArg1) {
				if(aArg0.getCurrentPlan().getSTDinMilliSec() < aArg1.getCurrentPlan().getSTDinMilliSec()) {
					return -1;
				}else if(aArg0.getCurrentPlan().getSTDinMilliSec() > aArg1.getCurrentPlan().getSTDinMilliSec()) {
					return 1;
				}
				return 0;
			}
		});

	}
	private synchronized void sortArrivalACList() {
		Collections.sort(iArrivalAircraftList, new Comparator<CAircraft>() {
			@Override
			public int compare(CAircraft aArg0, CAircraft aArg1) {
				if(aArg0.getCurrentPlan().getSTAinMilliSec() < aArg1.getCurrentPlan().getSTAinMilliSec()) {
					return -1;
				}else if(aArg0.getCurrentPlan().getSTAinMilliSec() > aArg1.getCurrentPlan().getSTAinMilliSec()) {
					return 1;
				}
				return 0;
			}
		});
	}
	public synchronized ELocation getLocation() {
		return iLocation;
	}
	public synchronized double getLonggestLinkLength() {
		return iLonggestLinkLength;
	}
	public synchronized void setLonggestLinkLength(double aLonggestLinkLength) {
		iLonggestLinkLength = aLonggestLinkLength;
	}
	public synchronized void setLocation(ELocation aLocation) {
		iLocation = aLocation;
	}
	public synchronized String getOperator() {
		return iOperator;
	}
	public synchronized void setOperator(String aOwner) {
		iOperator = aOwner;
	}
	public synchronized String getAirportICAO() {
		return iAirportICAO;
	}
	public synchronized void setAirportICAO(String aAirportICAO) {
		iAirportICAO = aAirportICAO;
	}
	public synchronized String getAirportIATA() {
		return iAirportIATA;
	}
	public synchronized void setAirportIATA(String aAirportIATA) {
		iAirportIATA = aAirportIATA;
	}
	public synchronized CCoordination getARP() {
		return iARP;
	}
	public synchronized void setARP(CCoordination aARP) {
		iARP = aARP;
	}
	public synchronized CAltitude getElevation() {
		return iElevation;
	}
	public synchronized void setElevation(CAltitude aElevation) {
		iElevation = aElevation;
	}
	public synchronized CDegree getVariation() {
		return iVariation;
	}
	public synchronized void setVariation(CDegree aVariation) {
		iVariation = aVariation;
	}
	public synchronized List<CRunway> getRunwayList() {
		return iRunwayList;
	}
	public synchronized void setRunwayList(List<CRunway> aRunwayList) {
		iRunwayList = aRunwayList;
	}
	public synchronized List<CTaxiwayLink> getTaxiwayLinkList() {
		return iTaxiwayLinkList;
	}
	public synchronized void setTaxiwayLinkList(List<CTaxiwayLink> aTaxiwayLinkList) {
		iTaxiwayLinkList = aTaxiwayLinkList;
	}
	public synchronized List<CTaxiwayNode> getTaxiwayNodeList() {
		return iTaxiwayNodeList;
	}
	public synchronized void setTaxiwayNodeList(List<CTaxiwayNode> aTaxiwayNodeList) {
		iTaxiwayNodeList = aTaxiwayNodeList;
	}
	public synchronized List<CSpot> getSpotList() {
		return iSpotList;
	}
	public synchronized void setSpotList(List<CSpot> aSpotList) {
		iSpotList = aSpotList;
	}
	public synchronized List<CApron> getApronList() {
		return iApronList;
	}
	public synchronized void setApronList(List<CApron> aApronList) {
		iApronList = aApronList;
	}
	public synchronized List<CLocalController> getLocalControllerList() {
		return iLocalControllerList;
	}
	public synchronized void setLocalControllerList(List<CLocalController> aLocalControllerList) {
		iLocalControllerList = aLocalControllerList;
	}
	public synchronized List<CGroundController> getGroundControllerList() {
		return iGroundControllerList;
	}
	public synchronized void setGroundControllerList(List<CGroundController> aGroundControllerList) {
		iGroundControllerList = aGroundControllerList;
	}

	public String toString() {
		return iAirportICAO;
	}
	@Override
	public CCoordination getCoordination() {
		// TODO Auto-generated method stub
		return iARP;
	}
	@Override
	public void setATCControllerToChildren(IATCController aController) {
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






