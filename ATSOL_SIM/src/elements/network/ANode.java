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

/**
 * @author S. J. Yun
 *
 */
public abstract class ANode {
	/*
	================================================================

			           Initializing Section

	================================================================
	 */
	
	private	String					iName;
	private int						iID;
	private String					iType;
	
	
	private boolean					iIsOccuping	=	false;
	
	
	private	final	int				iCapacity	= 1;
	
	
	private double					iXCoordinate,iYCoordinate;
	private ArrayList<ANode>		iAdjacentNode = new ArrayList<ANode>();
	
	
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
	public ANode(String aIName, String aIType, double aIXCoordinate, double aIYCoordinate) {
		super();
		iName = aIName;
		iType = aIType;
		iXCoordinate = aIXCoordinate;
		iYCoordinate = aIYCoordinate;
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
	public ANode(String aIName, String aIType, double aIXCoordinate, double aIYCoordinate,
			ArrayList<ANode> aIAdjacentNode) {
		super();
		iName = aIName;
		iType = aIType;
		iXCoordinate = aIXCoordinate;
		iYCoordinate = aIYCoordinate;
		iAdjacentNode = aIAdjacentNode;
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






