package elements.property;
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
 * @date : Apr 3, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 3, 2019 : Coded by S. J. Yun.
 *
 *
 */

public enum EADG {
	NaN,A,B,C,D,E,F;
private int value;
	

	/**
	 * getValue
	 * 
	 * Do What
	 * 
	 * @return value int
	 * 
	 * @date : Apr 3, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Apr 3, 2019 : Coded by S. J. Yun.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * setValue
	 * 
	 * Do What
	 * 
	 * @param aValue the value to set
	 * 
	 * @date : Apr 3, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Apr 3, 2019 : Coded by S. J. Yun.
	 */
	public void setValue(int aValue) {
		this.value = aValue;
	}}






