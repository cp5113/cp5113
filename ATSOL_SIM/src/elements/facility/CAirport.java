/**
 * ATSOL_SIM
 * elements.facility
 * CAirport.java
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

import java.util.ArrayList;

import elements.mobile.human.CGroundController;
import elements.mobile.human.CLocalController;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.CDegree;

/**
 * @author S. J. Yun
 *
 */
public class CAirport extends AFacility {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	
	// Basic Information Group
	private ELocation  		iLocation	= ELocation.GROUND;
	
	private String			iOwner;
	
	private String			iAirportICAO; //4 letters
	private String			iAirportIATA; //3 letters
	
	private	CCoordination	iARP;
	private CAltitude		iElevation;
	private CDegree			iVariation;
	
	
	// "Has" Relationship - Facilities and Network
	
	private ArrayList<CRunway>						iRunwayList 		= new ArrayList<CRunway>();
	private ArrayList<CTaxiwayLink> 				iTaxiwayLinkList	= new ArrayList<CTaxiwayLink>();
	private ArrayList<CTaxiwayNode> 				iTaxiwayNodeList 	= new ArrayList<CTaxiwayNode>();
	private ArrayList<CGate> 						iGateList		 	= new ArrayList<CGate>();
	private ArrayList<CApron>	 					iApronList		 	= new ArrayList<CApron>();
	
	// "Has" Relationship - Operator
	private ArrayList<CLocalController>	 			iLocalControllerList		 = new ArrayList<CLocalController>();
	private ArrayList<CGroundController> 			iGroundControllerList		 = new ArrayList<CGroundController>();

	
	
	
	
	
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






