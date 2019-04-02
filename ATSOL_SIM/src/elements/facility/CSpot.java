/**
 * ATSOL_SIM
 * elements.facility
 * CGate.java
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
public class CSpot extends ANode{


	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private CTaxiwayNode iTaxiwayNode;
	
	
	
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
	public CSpot(String aIName, CCoordination aAiCoordination) {
		super(aIName, aAiCoordination);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Mar 22, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 22, 2019 : Coded by S. J. Yun.
	 */
	public CSpot(CTaxiwayNode aTaxiwayNode) {		
		iTaxiwayNode = aTaxiwayNode;
	}

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */





	

	public synchronized CTaxiwayNode getTaxiwayNode() {
		return iTaxiwayNode;
	}



	public synchronized void setTaxiwayNode(CTaxiwayNode aTaxiwayNode) {
		iTaxiwayNode = aTaxiwayNode;
	}

	@Override
	public void setATCControllerToChildren(IATCController aController) {
		// TODO Auto-generated method stub
		
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






