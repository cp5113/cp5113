package elements.airspace;

import elements.AElement;
import elements.facility.AFacility;
import elements.facility.ELocation;
import elements.network.ANode;
import elements.table.ITableAble;
import elements.util.geo.CCoordination;

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
 * @date : Mar 26, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 26, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CWaypoint extends ANode implements ITableAble {
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
	 * @date : Mar 26, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 26, 2019 : Coded by S. J. Yun.
	 */
	public CWaypoint(String aName,CCoordination aCoordination) {
		super();
		iName		 = aName;
		iCoordination = aCoordination;
		iLocation	  = ELocation.AIRSPACE;
				
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






