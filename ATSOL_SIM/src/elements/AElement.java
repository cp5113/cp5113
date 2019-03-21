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

/**
 * @author S. J. Yun
 *
 */
public abstract class AElement{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	protected	String					iName;
	protected 	int						iID;
	protected 	String					iType		= this.getClass().getSimpleName();//this.getClass().getSimpleName().substring(1, this.getClass().getSimpleName().length()-1);
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
	public String getName() {
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
	public int getID() {
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
	public String getType() {
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
	public void setName(String aIName) {
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
	public void setID(int aIID) {
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
	public void setiType(String aIType) {
		iType = aIType;
	}
	
	
	public String toString(){
		return iName;
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






