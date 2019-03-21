package sim.clock;
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
 * @date : Mar 21, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 * : https://flowarc.tistory.com/entry/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%98%B5%EC%A0%80%EB%B2%84-%ED%8C%A8%ED%84%B4Observer-Pattern 
 * @version : 
 * Mar 21, 2019 : Coded by S. J. Yun.
 *
 *
 */

import elements.IElementControlledByClock;

public interface ISimClock{
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
	

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	public void addInClock(IElementControlledByClock aObserver);
	public void deleteFromClock(IElementControlledByClock aObserver);
	public void notifyTimeIncrementToElement();	
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






