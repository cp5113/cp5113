package api.inf;

import java.util.List;

import elements.mobile.human.AATCController;
import elements.mobile.vehicle.CAircraft;
import elements.network.ANode;

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
 * @date : 2019. 4. 29.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 4. 29. : Coded by S. J. Yun.
 *
 *
 */

public interface IATCInstructionTime {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public long calculateTakeOffInstructionTime(CAircraft aAircraft, AATCController aController);
	public long calculateTaxiInstructionTime(List<? extends ANode> aRoute, CAircraft aAircraft, AATCController aController);
	public long calculatePushbackInstructionTime(CAircraft aAircraft, AATCController aController);
	public long calculateFrequencyChangeInstructionTime(CAircraft aAircraft, AATCController aController);
	public long calculateLinupInstructionTime(CAircraft aAircraft, AATCController aController);
	
	/*================================================================
	
						Listeners Section
	
	================================================================
	 */

	/*
	================================================================
	
							The Others
	
	================================================================
	 */

}






