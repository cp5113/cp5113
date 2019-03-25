package sim.gui;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.plaf.IconUIResource;

import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import javafx.scene.paint.Color;

public class CDrawingInform {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private List<CCoordination> iCoordinationList = Collections.synchronizedList(new ArrayList<CCoordination>());	
	private CAltitude		iAltitude;
	private EShape			iShape;
	private Color			iColor;
	private boolean			iVisible;
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Mar 23, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 23, 2019 : Coded by S. J. Yun.
	 */
	public CDrawingInform(List<CCoordination> aCoordinationList, CAltitude aAltitude, EShape aShape, Color aColor,boolean aVisible) {
		super();
		iCoordinationList = aCoordinationList;
		iAltitude = aAltitude;
		iShape = aShape;
		iColor = aColor;
		iVisible = aVisible;
	}


	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	
	public synchronized List<CCoordination> getCoordinationList() {
		return iCoordinationList;
	}
	public synchronized void setCoordinationList(List<CCoordination> aCoordinationList) {
		iCoordinationList = aCoordinationList;
	}
	public synchronized CAltitude getAltitude() {
		return iAltitude;
	}
	public synchronized void setAltitude(CAltitude aAltitude) {
		iAltitude = aAltitude;
	}
	public synchronized EShape getShape() {
		return iShape;
	}
	public synchronized void setShape(EShape aShape) {
		iShape = aShape;
	}
	public synchronized Color getColor() {
		return iColor;
	}
	public synchronized void setColor(Color aColor) {
		iColor = aColor;
	}
	public synchronized boolean isVisible() {
		return iVisible;
	}
	public synchronized void setVisible(boolean aVisible) {
		iVisible = aVisible;
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






