/**
 * ATSOL_SIM
 * elements.facility
 * AFacility.java
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
 * @date : May 10, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 10, 2017 : Coded by S. J. Yun.
 *
 *
 */

import elements.AElement;
import elements.mobile.human.IATCController;

/**
 * @author S. J. Yun
 *
 */
abstract public class AFacility extends AElement{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/	
	
	protected	static	int				iFacilityCount 		= 0;
	protected IATCController			iATCController;
	

	
	
	
	public AFacility(){
		
		iFacilityCount++;
	}
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	public int getFacilityCount(){		
		return iFacilityCount;
	}

	public IATCController getATCController() {
		return iATCController;
	}
	public void setATCController(IATCController aController) {
		iATCController = aController;
	}
	public abstract void setATCControllerToChildren(IATCController aController);
	
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






