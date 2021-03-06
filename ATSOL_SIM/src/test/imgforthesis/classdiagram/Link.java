package test.imgforthesis.classdiagram;
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
 * @date : 2019. 5. 13.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 5. 13. : Coded by S. J. Yun.
 *
 *
 */

public class Link {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	public String Name;
	public Node StartNode;
	public Node EndNode;
	public double Distance;
	public double AltitudeMin;
	public double AltitudeMax;
	public boolean IsOccupying;
	
	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	String getName() {
		return "";
	}
	
	Node getStartNode() {
		return null;
	}
	Node getEndNode() {
		return null;
	}
	double getDistance() {
		return 0;
	}
	
	boolean isOccupying() {
		return true;
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







