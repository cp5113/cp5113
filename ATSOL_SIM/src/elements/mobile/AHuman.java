/**
 * ATSOL_SIM
 * elements.mobile
 * CHuman.java
 */
package elements.mobile;
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
 *
 *  <li>VARIABLE_NAME : Constant variable </li>
 * </ul>
 * </p>
 * 
 * 
 * @date : May 9, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 9, 2017 : Coded by S. J. Yun.
 *
 *
 */

/**
 * @author S. J. Yun
 *
 */
public abstract class AHuman extends AMobile{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	private int			iAge;
	private int			iExperienceDay;
	
	private ESkill 		nSkill;
	private EGender		nGender;

	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */

	/**
	 * getAge
	 * 
	 * Do What
	 * 
	 * @return iAge int
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public int getAge() {
		return iAge;
	}

	/**
	 * getExperienceDay
	 * 
	 * Do What
	 * 
	 * @return iExperienceDay int
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public int getExperienceDay() {
		return iExperienceDay;
	}

	
	/**
	 * getSkill
	 * 
	 * Do What
	 * 
	 * @return nSkill Enum
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public ESkill getSkill() {
		return nSkill;
	}
	

	
	/**
	 * setSkill
	 * 
	 * Do What
	 * 
	 * @param aSkill the ESkill to set
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public void setSkill(ESkill aSkill) {
		nSkill = aSkill;
	}
	
	
	
	/**
	 * setAge
	 * 
	 * Do What
	 * 
	 * @param aIAge the iAge to set
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public void setAge(int aIAge) {
		iAge = aIAge;
	}

	/**
	 * setExperienceDay
	 * 
	 * Do What
	 * 
	 * @param aIExperienceDay the iExperienceDay to set
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public void setExperienceDay(int aIExperienceDay) {
		iExperienceDay = aIExperienceDay;
	}

	/**
	 * getnGender
	 * 
	 * Do What
	 * 
	 * @return nGender EGender
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public EGender getGender() {
		return nGender;
	}

	/**
	 * setnGender
	 * 
	 * Do What
	 * 
	 * @param aNGender the nGender to set
	 * 
	 * @date : May 9, 2017
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * May 9, 2017 : Coded by S. J. Yun.
	 */
	public void setGender(EGender aNGender) {
		nGender = aNGender;
	};
	
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






