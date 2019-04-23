/**
 * ATSOL_SIM
 * elements.facility
 * CRunway.java
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

import java.util.ArrayList;
import java.util.List;

import elements.mobile.human.IATCController;
import elements.mobile.vehicle.CAircraft;
import elements.network.ALink;
import elements.network.ANode;

/**
 * @author S. J. Yun
 *
 */
public class CRunway extends ALink{
	
	private List<CTaxiwayLink> iTaxiwayLink = new ArrayList<CTaxiwayLink>();
	private List<CTaxiwayNode> iTaxiwayNode = new ArrayList<CTaxiwayNode>();
	private boolean iIsArrival = false;
	private boolean iIsDeparture = false;
	private List<Double> iDistanceList = new ArrayList<Double>();
	
	private List<CAircraft> iDepartureAircraftList= new ArrayList<CAircraft>();
	private List<CAircraft> iArrivalAircraftList= new ArrayList<CAircraft>();
	
	private double iRunwaySafetyWidth = 100; 
	
	@Override
	public void setATCControllerToChildren(IATCController aController) {
		
		iATCController = aController;
	
	}
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	

	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Apr 17, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Apr 17, 2019 : Coded by S. J. Yun.
	 */
	public CRunway(String aName, boolean aIsArrival, boolean aIsDeparture) {
		super();
		iName = aName;
		iIsArrival = aIsArrival;
		iIsDeparture = aIsDeparture;
	}

	public ANode findEnteringNodeForDeparture() {
		double x1 = iTaxiwayLink.get(0).getNodeList().get(0).getCoordination().getXCoordination();
		double y1 = iTaxiwayLink.get(0).getNodeList().get(0).getCoordination().getYCoordination();
		double x2 = iTaxiwayLink.get(0).getNodeList().get(1).getCoordination().getXCoordination();
		double y2 = iTaxiwayLink.get(0).getNodeList().get(1).getCoordination().getYCoordination();
		
		double xe = iTaxiwayLink.get(iTaxiwayLink.size()-1).getNodeList().get(0).getCoordination().getXCoordination();
		double ye = iTaxiwayLink.get(iTaxiwayLink.size()-1).getNodeList().get(0).getCoordination().getYCoordination();
		
		double distance1e = (x1-xe)*(x1-xe) + (y1-ye)*(y1-ye);
		double distance2e = (x2-xe)*(x2-xe) + (y2-ye)*(y2-ye);
		
		if(distance1e>distance2e) {
			return iTaxiwayLink.get(0).getNodeList().get(0);
		}else if(distance1e<distance2e) {
			return iTaxiwayLink.get(0).getNodeList().get(1);
		}
		
		return null;
	}

	public synchronized boolean isArrival() {
		return iIsArrival;
	}



	public synchronized void setArrival(boolean aIsArrival) {
		iIsArrival = aIsArrival;
	}



	public synchronized boolean isDeparture() {
		return iIsDeparture;
	}



	public synchronized void setDeparture(boolean aIsDeparture) {
		iIsDeparture = aIsDeparture;
	}



	public synchronized List<CTaxiwayLink> getTaxiwayLink() {
		return iTaxiwayLink;
	}



	public synchronized List<Double> getDistanceList() {
		return iDistanceList;
	}



	public synchronized List<CAircraft> getDepartureAircraftList() {
		return iDepartureAircraftList;
	}



	public synchronized List<CAircraft> getArrivalAircraftList() {
		return iArrivalAircraftList;
	}

	public synchronized List<CTaxiwayNode> getTaxiwayNodeList(){
		return iTaxiwayNode;
	}



	public double getRunwaySafetyWidth() {
		
		return iRunwaySafetyWidth;
	}
	public void setRunwaySafetyWidth(double aRunwaySafetyWidth) {
		
		iRunwaySafetyWidth = aRunwaySafetyWidth;
	}

	public void sortNodeList() {
		ArrayList<CTaxiwayNode> tempList = new ArrayList<CTaxiwayNode>();
		for(int loopLink = 0; loopLink < iTaxiwayLink.size()-1; loopLink++) {
			CTaxiwayNode lNode11 = (CTaxiwayNode) iTaxiwayLink.get(loopLink).getNodeList().get(0);
			CTaxiwayNode lNode12 = (CTaxiwayNode) iTaxiwayLink.get(loopLink).getNodeList().get(1);
			CTaxiwayNode lNode21 = (CTaxiwayNode) iTaxiwayLink.get(loopLink+1).getNodeList().get(0);
			CTaxiwayNode lNode22 = (CTaxiwayNode) iTaxiwayLink.get(loopLink+1).getNodeList().get(1);
			
			if(lNode11.equals(lNode21)) {
				// lNode12 lNode11 Lnode 22
				if(!tempList.contains(lNode12)) {					
				tempList.add(lNode12);				
				tempList.add(lNode11);
				}
				tempList.add(lNode22);
			}else if(lNode11.equals(lNode22)) {
				// lNode12 lNode11 Lnode 21
				if(!tempList.contains(lNode12)) {
				tempList.add(lNode12);				
				tempList.add(lNode11);
				}
				tempList.add(lNode21);				
			}else if(lNode12.equals(lNode21)) {
				// lNode11 lNode12 Lnode 22
				if(!tempList.contains(lNode11)) {
				tempList.add(lNode11);				
				tempList.add(lNode12);
				}
				tempList.add(lNode22);
			}else if(lNode12.equals(lNode22)) {
				// lNode11 lNode12 Lnode 21
				if(!tempList.contains(lNode11)) {
				tempList.add(lNode11);
				tempList.add(lNode12);
				}
				tempList.add(lNode21);
			}
			
			
		}
		
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







