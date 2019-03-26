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
import java.util.HashMap;

import elements.AElement;
import elements.facility.CAirport;
import elements.property.CAircraftType;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;

/**
 * @author S. J. Yun
 *
 */
public abstract class ANode extends AElement implements INode{
	/*
	================================================================

			           Initializing Section

	================================================================
	 */
	
	protected	boolean					iIsOccuping	=	false;
	
	
	protected	int				        CAPACITY	= 1;   
	protected 	CAltitude				iAltitude;
	protected 	CCoordination			iCoordination;
	protected   Double					iLatitude;
	protected   Double					iLogitude;
	protected 	ArrayList<ANode>		iAdjacentNode = new ArrayList<ANode>();
	protected 	HashMap<String,Boolean> iACTypeAPC = new HashMap<String, Boolean>(); 
	protected	ANode					iChildren;
	
	
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
	
	protected ANode() {
		
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



	public synchronized HashMap<String, Boolean> getACTypeAPC() {
		return iACTypeAPC;
	}



	public synchronized void setACTypeAPC(HashMap<String, Boolean> aACTypeAPC) {
		iACTypeAPC = aACTypeAPC;
	}



	public synchronized boolean isIsOccuping() {
		return iIsOccuping;
	}



	public synchronized void setIsOccuping(boolean aIsOccuping) {
		iIsOccuping = aIsOccuping;
	}



	public synchronized int getCAPACITY() {
		return CAPACITY;
	}



	public synchronized void setCAPACITY(int aCAPACITY) {
		CAPACITY = aCAPACITY;
	}




	public synchronized CAltitude getAltitude() {
		return iAltitude;
	}



	public synchronized void setAltitude(CAltitude aAltitude) {
		iAltitude = aAltitude;
	}



	public synchronized CCoordination getCoordination() {
		return iCoordination;
	}



	public synchronized void setCoordination(CCoordination aCoordination) {
		iCoordination = aCoordination;
	}



	public synchronized Double getLatitude() {
		return iLatitude;
	}



	public synchronized void setLatitude(Double aLatitude) {
		iLatitude = aLatitude;
	}



	public synchronized Double getLogitude() {
		return iLogitude;
	}



	public synchronized void setLogitude(Double aLogitude) {
		iLogitude = aLogitude;
	}


	public synchronized ArrayList<ANode> getAdjacentNode() {
		return iAdjacentNode;
	}



	public synchronized void setAdjacentNode(ArrayList<ANode> aAdjacentNode) {
		iAdjacentNode = aAdjacentNode;
	}

	public synchronized ANode getChildren() {
		return iChildren;
	}

	public synchronized void setChildren(ANode aChildren) {
		iChildren = aChildren;
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






