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
 * @date : Apr 3, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 3, 2019 : Coded by S. J. Yun.
 *
 *
 */

import elements.facility.CRunway;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.mobile.vehicle.state.EAircraftMovementStatus;
import elements.network.ALink;
import elements.network.ANode;
import elements.network.INode;
import elements.util.geo.CCoordination;
import elements.util.phy.CVelocity;

public interface IAircraft {
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
	public CCoordination 	getCurrentPosition();
	public ANode     		getCurrentNode();
	public ALink     		getCurrentLink();
	public double			getCurrentVelocityMps();
	public double			getCurrentAltitudeFeet();
	public CFlightPlan		getCurrentFlightPlan();
	public CFlightPlan		getNextFlightPlan();
	
	
	public CRunway			getDepartureRunway();
	public CRunway			getArrivalRunway();
	public void				setDepartureRunway(CRunway aDepartureRunway);
	public void 			setArrivalRunway(CRunway aArrivalRunway);
	
	
	
	
	

	public EAircraftMovementStatus getMovementStatus();

	public void setMovementStatus(EAircraftMovementStatus aMovementStatus);
	
	
	public EAircraftMovementMode getMovementMode();

	public void setMovementMode(EAircraftMovementMode aMovementMode);
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






