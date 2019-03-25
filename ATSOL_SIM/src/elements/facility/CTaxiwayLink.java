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
		
		// TODO Auto-generated method stub
		CAirportTable a = CAtsolSimMain.getInstance().getiAirportTable();
		CAirport b = (CAirport) a.getElementTable().get("RKSI");
		CTaxiwayLink compare = null;
		for(int i = 0; i< b.getTaxiwayLinkList().size(); i++) {
			CTaxiwayLink linkInTable = b.getTaxiwayLinkList().get(i);
//			System.out.println(linkInTable.getNodeList().get(0).getCoordination() + "," +linkInTable.getNodeList().get(1).getCoordination());
			if(this==linkInTable) {
//				if(this.getNodeList().get(0).getCoordination().getXCoordination() != linkInTable.getNodeList().get(0).getCoordination().getXCoordination()) {
//					System.out.println("Wow");
//				}
				
				compare = linkInTable;
				
			}
				
			
//			System.out.println(b.getTaxiwayLinkList().get(i).getNodeList().get(0).getCoordination() + "," +b.getTaxiwayLinkList().get(i).getNodeList().get(1).getCoordination()); 
		}
		
		
		if(this.getNodeList().get(0).getCoordination().getXCoordination() != compare.getNodeList().get(0).getCoordination().getXCoordination()) {
			System.out.println("WW");
		}
		if(this.getNodeList().get(1).getCoordination().getXCoordination() != compare.getNodeList().get(1).getCoordination().getXCoordination()) {
			System.out.println("WW");
		}
		
		if(this.getNodeList().get(0).getCoordination().getYCoordination() != compare.getNodeList().get(0).getCoordination().getYCoordination()) {
			System.out.println("WW");
		}
		if(this.getNodeList().get(1).getCoordination().getYCoordination() != compare.getNodeList().get(1).getCoordination().getYCoordination()) {
			System.out.println("WW");
		}
		
//		System.out.println(this.getNodeList().get(0).getCoordination() + "," +this.getNodeList().get(1).getCoordination());
//		System.out.println(compare.getNodeList().get(0).getCoordination() + "," +compare.getNodeList().get(1).getCoordination());
//		System.out.println(this);
		
		List<CCoordination> lCoordinationList = Collections.synchronizedList(new ArrayList<CCoordination>()); 
		Iterator<ANode> iter = this.getNodeList().iterator();		
		while(iter.hasNext()) {
			CTaxiwayNode lTaxiwayNode = (CTaxiwayNode) iter.next();
			lCoordinationList.add(lTaxiwayNode.getCoordination());
		}
		
		CDrawingInform  lDrawingInform = new CDrawingInform(lCoordinationList, new CAltitude(0,EGEOUnit.FEET), EShape.LINE, Color.BLACK,true);
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






