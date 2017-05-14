/**
 * ATSOL_SIM
 * network
 * ALink.java
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
import elements.util.geo.CDegree;

/**
 * @author S. J. Yun
 *
 */
public abstract class ALink extends AElement {
	/*
	================================================================

			           Initializing Section

	================================================================
	 */

	private boolean					iIsOccuping	=	false;
	
	
	private int						iCapacity	=	Integer.MAX_VALUE;
		
	
	private ANode					iOrigin,iDestination;
	private ArrayList<ANode>		iNodeList 				= new ArrayList<ANode>();
	private ArrayList<ALink>		iAdjacentLink 			= new ArrayList<ALink>();
	private	double					iDistance				=	0.0;
	private CDegree					iHeading;
	private CDegree					iOppositeHeading;
	
	
	
	
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






