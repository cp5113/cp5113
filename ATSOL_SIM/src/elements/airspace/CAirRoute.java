package elements.airspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import elements.AElement;
import elements.facility.AFacility;
import elements.network.INode;
import elements.table.ITableAble;

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

public class CAirRoute extends AElement implements ITableAble{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private List<INode> iNodeList = Collections.synchronizedList(new ArrayList<INode>());
	private int				iCurrentElementCountInRoute = 0;
	private int				iCapacity;
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
	public CAirRoute(List<INode> aNodeList, int aCurrentElementCount, int aCapacity) {
		super();
		iNodeList = aNodeList;
		iCurrentElementCountInRoute = aCurrentElementCount;
		iCapacity = aCapacity;
	}
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
	public CAirRoute(String aName) {
		super();
		iName = aName;
		// TODO Auto-generated constructor stub
	}

	
	

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public List<INode> getNodeList() {
		return iNodeList;
	}
	public void setNodeList(List<INode> aNodeList) {
		iNodeList = aNodeList;
	}
	public int getCurrentElementCountInRoute() {
		return iCurrentElementCountInRoute;
	}
	public void setCurrentElementCountInRoute(int aCurrentElementCountInRoute) {
		iCurrentElementCountInRoute = aCurrentElementCountInRoute;
	}
	public int getCapacity() {
		return iCapacity;
	}
	public void setCapacity(int aCapacity) {
		iCapacity = aCapacity;
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






