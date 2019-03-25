package util.file;

import java.util.Iterator;
import java.util.List;

import elements.AElement;
import elements.facility.CTaxiwayNode;

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
 * @date : Mar 23, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 23, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class CListUtil {
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
	public static AElement searchElementInListUsingName(List<?> aObject, String aSearchString) {
		AElement lOutputObject = null;
		
		Iterator<AElement> iter = (Iterator<AElement>) aObject.iterator();		
		while(iter.hasNext()) {
			lOutputObject =iter.next();
			if(lOutputObject.getName().equalsIgnoreCase(aSearchString)) {
				 break;
			}
		}
		
		
		return lOutputObject;
		
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






