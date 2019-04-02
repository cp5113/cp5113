/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * AVehicle.java
 */
package elements.mobile.vehicle;
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
import java.util.List;

import elements.IElementObservableClock;
import elements.mobile.AMobile;
import elements.mobile.human.IATCController;
import elements.operator.AOperator;
import elements.property.AVehiclePerformance;
import elements.property.AVehicleType;
import elements.table.ITableAble;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.EGEOUnit;
import javafx.scene.paint.Color;
import sim.clock.ISimClockOberserver;
import sim.gui.CDrawingInform;
import sim.gui.EShape;
import sim.gui.IDrawingObject;

/**
 * @author S. J. Yun
 *
 */
public abstract class AVehicle extends AMobile implements ITableAble, IDrawingObject, IElementObservableClock, Runnable{
	
	// Strategy Pattern과 Observer Pattern을 공부해야 함
	// S : http://hyeonstorage.tistory.com/146
	// S : http://flowarc.tistory.com/entry/1-Strategy-Pattern
	// O : http://flowarc.tistory.com/entry/%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%98%B5%EC%A0%80%EB%B2%84-%ED%8C%A8%ED%84%B4Observer-Pattern
	
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	protected		AVehicleType			iVehcleType;
	
	protected		CVehicleStatus			iCurrentStatus;
	protected		CVehicleStatus			iPreviousStatus;
	protected		CVehicleStatus			iNextStatus;
	
	protected		IATCController			iATCController;

	
	protected		List<AVehiclePlan>		iPlanList 				= Collections.synchronizedList(new ArrayList<AVehiclePlan>());
	protected		AVehiclePlan			iCurrentPlan			;
	
	protected		CCoordination			iCurrentPostion		= new CCoordination(-9999999, -9999999, EGEOUnit.METER);
	protected		CCoordination			iPreviousPostion 	= new CCoordination(-9999999, -9999999, EGEOUnit.METER);
	protected		CCoordination			iNextPostion  		= new CCoordination(-9999999, -9999999, EGEOUnit.METER);
	protected		CAltitude				iCurrentAltitude 	= new CAltitude(0, EGEOUnit.FEET);
	
	
	protected 	CDrawingInform				iDrawingInform = new CDrawingInform(iCurrentPostion,iCurrentAltitude,EShape.DOT,Color.RED,true);
	
	
	
	protected		AOperator				iOperator;
	protected		AVehiclePerformance		iPerformance;
	
	protected ISimClockOberserver iSimClockObserver;
	
	
	
	public CVehicleStatus getCurrentStatus() {
		return iCurrentStatus;
	}
	public void setCurrentStatus(CVehicleStatus aCurrentStatus) {
		iCurrentStatus = aCurrentStatus;
	}
	public CVehicleStatus getPreviousStatus() {
		return iPreviousStatus;
	}
	public void setPreviousStatus(CVehicleStatus aPreviousStatus) {
		iPreviousStatus = aPreviousStatus;
	}
	public CVehicleStatus getNextStatus() {
		return iNextStatus;
	}
	public void setNextStatus(CVehicleStatus aNextStatus) {
		iNextStatus = aNextStatus;
	}
	public List<AVehiclePlan> getPlanList() {
		return iPlanList;
	}
	public void setPlanList(List<AVehiclePlan> aPlanList) {
		iPlanList = aPlanList;
	}
	public CCoordination getCurrentPostion() {
		return iCurrentPostion;
	}
	public void setCurrentPostion(CCoordination aCurrentPostion) {
		iCurrentPostion = aCurrentPostion;
	}
	public CCoordination getPreviousPostion() {
		return iPreviousPostion;
	}
	public void setPreviousPostion(CCoordination aPreviousPostion) {
		iPreviousPostion = aPreviousPostion;
	}
	public CCoordination getNextPostion() {
		return iNextPostion;
	}
	public void setNextPostion(CCoordination aNextPostion) {
		iNextPostion = aNextPostion;
	}
	public AOperator getOperator() {
		return iOperator;
	}
	public void setOperator(AOperator aOperator) {
		iOperator = aOperator;
	}
	public AVehiclePerformance getPerformance() {
		return iPerformance;
	}
	public void setPerformance(AVehiclePerformance aPerformance) {
		iPerformance = aPerformance;
	}
	public AVehiclePlan getCurrentPlan() {
		return iCurrentPlan;
	}
	public void setCurrentPlan(AVehiclePlan aCurrentPlan) {
		iCurrentPlan = aCurrentPlan;
	}
	public AVehicleType getVehcleType() {
		return iVehcleType;
	}
	public void setVehcleType(AVehicleType aVehcleType) {
		iVehcleType = aVehcleType;
	}
	
	
	@Override
	public CDrawingInform getDrawingInform() {
		// TODO Auto-generated method stub
		return iDrawingInform;
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






