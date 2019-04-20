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

/**
 * @author S. J. Yun
 *
 */
public class CRunway extends ALink{
	
	private List<CTaxiwayLink> iTaxiwayLink = new ArrayList<CTaxiwayLink>();
	private boolean iIsArrival = false;
	private boolean iIsDeparture = false;
	private List<Double> iDistanceList = new ArrayList<Double>();
	
	private List<CAircraft> iDepartureAircraftList= new ArrayList<CAircraft>();
	private List<CAircraft> iArrivalAircraftList= new ArrayList<CAircraft>();
	
	@Override
	public void setATCControllerToChildren(IATCController aController) {
		// TODO Auto-generated method stub
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



	public synchronized boolean isIsArrival() {
		return iIsArrival;
	}



	public synchronized void setIsArrival(boolean aIsArrival) {
		iIsArrival = aIsArrival;
	}



	public synchronized boolean isIsDeparture() {
		return iIsDeparture;
	}



	public synchronized void setIsDeparture(boolean aIsDeparture) {
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






