/**
 * ATSOL_SIM
 * elements.facility
 * CTaxiwayNode.java
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

import elements.mobile.human.IATCController;
import elements.network.ANode;
import elements.util.geo.CCoordination;

/**
 * @author S. J. Yun
 *
 */
public class CTaxiwayNode extends ANode{

	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	private CSpot iSpot;
	private CRunway iRunway;
	
	
	public CTaxiwayNode(String aIName, CCoordination aAiCoordination) {
		super(aIName, aAiCoordination);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setATCControllerToChildren(IATCController aController) {
		// TODO Auto-generated method stub
		try {
			iSpot.setATCController(aController);
		}catch(Exception e) {
			
		}
		try {
			iRunway.setATCController(aController);
		}catch(Exception e) {
			
		}
		
	}

	public CSpot getSpot() {
		return iSpot;
	}

	public void setSpot(CSpot aSpot) {
		iSpot = aSpot;
		iSpot.setATCController(iATCController);
	}

	public CRunway getRunway() {
		return iRunway;
	}

	public void setRunway(CRunway aRunway) {
		iRunway = aRunway;
		iRunway.setATCController(iATCController);
	}

	
	

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






