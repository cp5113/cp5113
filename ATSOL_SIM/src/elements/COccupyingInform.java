package elements;

import elements.network.ALink;
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
 * @date : Apr 9, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 9, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class COccupyingInform implements Comparable<COccupyingInform>{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private ALink iThisLink;
	private long iStartTime = -999;
	private long iEndTime = -999;
	private AElement iOccupyingObject = null;
	
	private ANode iDestination = null;
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Apr 9, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Apr 9, 2019 : Coded by S. J. Yun.
	 */
	public COccupyingInform(ALink aThisLink, long aStartTime, long aEndTime, AElement aOccupyingObject, ANode aDestination) {
		super();
		iThisLink = aThisLink;
		iStartTime = aStartTime;
		iEndTime = aEndTime;
		iOccupyingObject = aOccupyingObject;
		iDestination	= aDestination;
	}
	
	
	public synchronized long getStartTime() {
		return iStartTime;
	}


	public synchronized void setStartTime(long aStartTime) {
		iStartTime = aStartTime;
	}


	public synchronized long getEndTime() {
		return iEndTime;
	}


	public synchronized void setEndTime(long aEndTime) {
		iEndTime = aEndTime;
	}


	public synchronized AElement getOccupyingObject() {
		return iOccupyingObject;
	}


	public synchronized void setOccupyingObject(AElement aOccupyingObject) {
		iOccupyingObject = aOccupyingObject;
	}


	public synchronized ANode getDestination() {
		return iDestination;
	}


	public synchronized void setDestination(ANode aDestination) {
		iDestination = aDestination;
	}


	@Override
	public int compareTo(COccupyingInform aO) {
		if (this.getStartTime() <aO.getStartTime()) {
			return -1;
		}else if(this.getStartTime() >aO.getStartTime()) {
			return 1;
		}
		return 0;
	}
	
	
	public synchronized ALink getLink() {
		return iThisLink;
	}


	public synchronized void setLink(ALink aThisLink) {
		iThisLink = aThisLink;
	}


	public String toString() {
		return iThisLink.getName() + " : " + iStartTime + "~" + iEndTime + " by " + iOccupyingObject + " moving to "+ iDestination;
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






