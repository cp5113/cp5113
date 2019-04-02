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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import elements.IElementControlledByClock;
import elements.IElementObservableClock;
import elements.facility.AFacility;
import elements.mobile.vehicle.CAircraft;
import elements.table.ITableAble;
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
	
	protected AFacility		  iOwnedFacilty;
	
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

	
	
	
	protected void addAircraft(CAircraft aAircraft) {
		iAircraftList.add(aAircraft);
	}
	public CAircraft getAircraft(int aIndex){
		return iAircraftList.get(aIndex);
	}
	
	public List<CAircraft> getAircraftList(){
		return iAircraftList;
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


	public AFacility getOwnedFacilty() {
		return iOwnedFacilty;
	}


	public void setOwnedFacilty(AFacility aOwnedFacilty) {
		iOwnedFacilty = aOwnedFacilty;
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






