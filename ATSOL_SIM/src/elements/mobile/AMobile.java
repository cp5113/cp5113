/**
 * ATSOL_SIM
 * elements.mobile
 * AMobile.java
 */
package elements.mobile;

import elements.AElement;

/**
 * 
 * The Mobile abstract class
 * 
 * <li> Human </li>
 * <li> Vehicle </li>
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

/**
 * @author S. J. Yun
 *
 */
public abstract class AMobile extends AElement {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/	
	
	protected  long iNextEventTime = -99999999;
	
	public AMobile(){
	
	}
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	
	
	public String getName(){
		return iName;
	}
	
	public synchronized long getNextEventTime() {
		return iNextEventTime;
	}

	public synchronized void setNextEventTime(long aNextEventTime) {
		iNextEventTime = aNextEventTime;
	}

	public void setName(String aName){
		iName = aName;
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






