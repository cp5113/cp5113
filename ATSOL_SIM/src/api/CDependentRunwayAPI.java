package api;

import api.inf.IDependentRunway;
import elements.facility.CAirport;
import elements.facility.CRunway;

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
 * @date : 2019. 5. 9.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 5. 9. : Coded by S. J. Yun.
 *
 *
 */

public class CDependentRunwayAPI implements IDependentRunway{

	@Override
	public CRunway getDependentRunway(CRunway aRunway) {
		if(aRunway.getName().equalsIgnoreCase("33R")){
			for(CRunway loopRwy : ((CAirport)aRunway.getOwnerObject()).getRunwayList()) {
				if(loopRwy.getName().equalsIgnoreCase("33L")) {
					return loopRwy;
				}
			}
		}
		if(aRunway.getName().equalsIgnoreCase("33L")){
			for(CRunway loopRwy : ((CAirport)aRunway.getOwnerObject()).getRunwayList()) {
				if(loopRwy.getName().equalsIgnoreCase("33R")) {
					return loopRwy;
				}
			}
		}
		return null;
	}
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






