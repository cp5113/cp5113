/**
 * ATSOL_SIM
 * elements
 * AElement.java
 */
package elements;
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

import java.util.Calendar;

import elements.facility.ELocation;
import elements.table.ITableAble;

/**
 * @author S. J. Yun
 *
 */
/**
 * @author S. J. Yun
 *
 */
public abstract class AElement implements ITableAble{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	protected Object		iThreadLockerThis  = new Object();
	protected Object		iThreadLockerOwner;
	
	protected	String					iName;
	protected 	int						iID;
	protected 	String					iType		= this.getClass().getSimpleName();//this.getClass().getSimpleName().substring(1, this.getClass().getSimpleName().length()-1);
	protected   String					iNameGroup;
	protected	AElement				iOwnerObject;
	protected ELocation				    iLocation;		
	protected 	int						iCapacity;
	
	protected	Calendar				iPreviousTime;
	protected	Calendar				iCurrentTime;

	protected	long				iPreviousTimeInMilliSecond;
	protected	long				iCurrentTimeInMilliSecond;

	protected 	boolean				iIsWorking;
	
	protected boolean				iThreadContinueableAtInitialState = false;
	/**
	 * getiName
	 * 
	 * Do What
	 * 
	 * @return iName String
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized String getName() {
		return iName;
	}
	/**
	 * getiID
	 * 
	 * Do What
	 * 
	 * @return iID int
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized int getID() {
		return iID;
	}
	/**
	 * getiType
	 * 
	 * Do What
	 * 
	 * @return iType String
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized String getType() {
		return iType;
	}
	/**
	 * setiName
	 * 
	 * Do What
	 * 
	 * @param aIName the iName to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setName(String aIName) {
		iName = aIName;
	}
	/**
	 * setiID
	 * 
	 * Do What
	 * 
	 * @param aIID the iID to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setID(int aIID) {
		iID = aIID;
	}
	/**
	 * setiType
	 * 
	 * Do What
	 * 
	 * @param aIType the iType to set
	 * 
	 * @date : May 12, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 12, 2017 : Coded by S. J. Yun.
	 */
	public synchronized void setType(String aIType) {
		iType = aIType;
	}
	
	
	public synchronized String toString(){
		return iName;
	}
	public synchronized String getNameGroup() {
		return iNameGroup;
	}
	public synchronized void setNameGroup(String aNameGroup) {
		iNameGroup = aNameGroup;
	}
	public synchronized AElement getOwnerObject() {
		return iOwnerObject;
	}
	public synchronized void setOwnerObject(AElement aOwnerObject) {
		iOwnerObject = aOwnerObject;
	}
	public ELocation getLocation(){		
		return iLocation;
	}
	public int getCapacity() {
		return iCapacity;
	}
	public boolean isIsWorking() {
		return iIsWorking;
	}
	public void setIsWorking(boolean aIsWorking) {
		iIsWorking = aIsWorking;
	}
	public synchronized Object getThreadLockerThis() {
		return iThreadLockerThis;
	}
	public synchronized void setThreadLockerThis(Object aThreadLockerThis) {
		iThreadLockerThis = aThreadLockerThis;
	}
	public synchronized Object getThreadLockerOwner() {
		return iThreadLockerOwner;
	}
	public synchronized void setThreadLockerOwner(Object aThreadLockerOwner) {
		iThreadLockerOwner = aThreadLockerOwner;
	}
	public synchronized boolean isThreadContinueableAtInitialState() {
		return iThreadContinueableAtInitialState;
	}
	public synchronized void setThreadContinueableAtInitialState(boolean aThreadContinueableAtInitialState) {
		iThreadContinueableAtInitialState = aThreadContinueableAtInitialState;
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






