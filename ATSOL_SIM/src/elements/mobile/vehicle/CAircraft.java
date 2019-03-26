/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraft.java
 */
package elements.mobile.vehicle;

import java.util.ArrayList;
import java.util.Calendar;

import elements.IElementControlledByClock;
import elements.operator.CAirline;
import elements.property.CAircraftType;
import elements.table.ITableAble;
import sim.gui.CDrawingInform;
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
public class CAircraft extends AVehicle implements ITableAble, IDrawingObject, IElementControlledByClock{
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
	public CDrawingInform getDrawingInform() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void incrementTime(Calendar aCurrentTIme, int aIncrementStepInSec) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean isPreparedForNextIncrement() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	/*
	================================================================
	
							The Others
	
	================================================================
	 */
}






