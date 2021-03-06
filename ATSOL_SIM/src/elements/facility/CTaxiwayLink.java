/**
 * ATSOL_SIM
 * elements.facility
 * CTaxiwayLink.java
 */
package elements.facility;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import elements.mobile.human.IATCController;
import elements.network.ALink;
import elements.network.ANode;
import elements.table.CAirportTable;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.scene.paint.Color;
import sim.CAtsolSimMain;
import sim.gui.CDrawingInform;
import sim.gui.EShape;
import sim.gui.IDrawingObject;
import sim.gui.control.CAtsolSimGuiControl;

/**
 * @author S. J. Yun
 *
 */
public class CTaxiwayLink extends ALink implements IDrawingObject{


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
	@Override
	public CDrawingInform getDrawingInform() {
		
		List<CCoordination> lCoordinationList = Collections.synchronizedList(new ArrayList<CCoordination>()); 
		
		ANode node2 = null;
		for(int loopNode = 0; loopNode < iNodeList.size()-1;loopNode++) {
			ANode node1 = iNodeList.get(loopNode);
			node2 = iNodeList.get(loopNode+1);
			
			CCoordination center = new CCoordination((node1.getCoordination().getXCoordination()+node2.getCoordination().getXCoordination())/2,(node1.getCoordination().getYCoordination()+node2.getCoordination().getYCoordination())/2);
			
			lCoordinationList.add(node1.getCoordination());
			lCoordinationList.add(center);			
		}
		lCoordinationList.add(node2.getCoordination());
		
		CDrawingInform  lDrawingInform = new CDrawingInform(lCoordinationList, new CAltitude(0,EGEOUnit.FEET), EShape.LINE, Color.BLACK,true,10.0);
		return lDrawingInform;			
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






