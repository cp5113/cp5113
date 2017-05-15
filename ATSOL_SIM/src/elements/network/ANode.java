/**
 * ATSOL_SIM
 * network
 * ANode.java
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
 * @date : May 11, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 11, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;

import elements.AElement;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;

/**
 * @author S. J. Yun
 *
 */
public abstract class ANode extends AElement{
	/*
	================================================================

			           Initializing Section

	================================================================
	 */
	
	protected	boolean					iIsOccuping	=	false;
	
	
	protected	final	int				CAPACITY	= 1;
	
	protected 	CAltitude				iAltitude;
	protected 	CCoordination			iCoordination;
	protected 	ArrayList<ANode>		iAdjacentNode = new ArrayList<ANode>();
	
	
	
	
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
	public ANode(String aIName, CCoordination aiCoordination) {
		setName(aIName);		
		iCoordination = aiCoordination;
	}



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
	public ANode(String aIName, double aXCoordination, double aYCoordination) {
		super();
		super.setName(aIName);	
		iCoordination = new CCoordination(aXCoordination, aYCoordination);
		
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






