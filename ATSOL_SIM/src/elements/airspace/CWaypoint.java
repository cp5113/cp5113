package elements.airspace;

import elements.AElement;
import elements.facility.AFacility;
import elements.facility.ELocation;
import elements.mobile.human.IATCController;
import elements.network.ANode;
import elements.table.ITableAble;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.scene.paint.Color;
import sim.gui.CDrawingInform;
import sim.gui.EShape;
import sim.gui.IDrawingObject;

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

public class CWaypoint extends ANode implements ITableAble, IDrawingObject {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	
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
	public CWaypoint(String aName,CCoordination aCoordination) {
		super();
		iName		 = aName;
		iCoordination = aCoordination;
		iLocation	  = ELocation.AIRSPACE;
				
	}

	@Override
	public void setATCControllerToChildren(IATCController aController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CDrawingInform getDrawingInform() {
		CDrawingInform  lDrawingInform = new CDrawingInform(iCoordination, new CAltitude(0,EGEOUnit.FEET), EShape.CIRCLE, Color.BLACK,true,100.0);
		return lDrawingInform;		
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






