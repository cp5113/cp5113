/**
 * ATSOL_SIM
 * elements.network
 * AArea.java
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
 * @date : May 12, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 12, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.LinkedList;

import elements.AElement;
import elements.util.geo.CAltitude;

/**
 * @author S. J. Yun
 *
 */
public abstract class AArea extends AElement{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	protected 	LinkedList<ANode> 	iVertix = new LinkedList<ANode>();
	protected	CAltitude			iLowLimit;
	protected	CAltitude			iUpperLimit;
	
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public int countNumberOfObjectInArea(){
		return 0;
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






