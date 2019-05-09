package elements.mobile.human;

import java.util.List;

import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.state.CAircraftGroundConflictStopMoveState;
import elements.mobile.vehicle.state.CAircraftNothingMoveState;
import elements.mobile.vehicle.state.CAircraftTaxiingMoveState;
import elements.mobile.vehicle.state.EAircraftMovementMode;
import elements.property.EMode;
import elements.util.geo.CCoordination;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import sim.gui.control.CAtsolSimGuiControl;

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
 * @date : 2019. 4. 29.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 4. 29. : Coded by S. J. Yun.
 *
 *
 */

public class CGroundConflictDetectionAndResolution {
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
	public static void groundConflictDetectionAndResolution(CAircraft lAircraft, List<CAircraft> lOtherACList) {
		lOtherACList.remove(lAircraft);
		if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {
			
//			// Ignore already stopping
//			if(lAircraft.getMoveState() instanceof CAircraftGroundConflictStopMoveState) continue;
			
			// Detect and resolution Conflict
			for(CAircraft loopOther : lOtherACList) {
				if((loopOther.getMoveState() instanceof CAircraftNothingMoveState)) continue;
				Polygon lthisACSafety = lAircraft.getSafetyPolygonInform();
				Polygon lthisACShape = lAircraft.getShapePolygonInform();
				Polygon lthisACSafetyFront = lAircraft.getSafetyFrontPolygonInform();
				Polygon lotherACSafety = loopOther.getSafetyPolygonInform();
				Polygon lotherACShape = loopOther.getShapePolygonInform();
//				System.out.println();
//				System.out.println();
				
				// Detect Conflict
				Shape lConflictSafetyAndSafetyInfo;
				Shape lConflictSafetyAndShapeInfo;
				Shape lConflictFrontAndSafetyInfo;
				boolean lConflictSafetyAndSafety=false;
				boolean lConflictSafetyAndShape=false;
				boolean lConflictFrontToSafety = false;
				synchronized (lthisACSafety) {
					synchronized (lotherACShape) {
						synchronized (lthisACShape) {
							synchronized (lotherACSafety) {
								synchronized (lthisACSafetyFront) {
									
									lConflictSafetyAndSafetyInfo = Shape.intersect(lthisACSafety, lotherACSafety);
									lConflictSafetyAndShapeInfo  = Shape.intersect(lthisACSafety, lotherACShape);
									lConflictFrontAndSafetyInfo  = Shape.intersect(lthisACSafetyFront, lotherACSafety);
									
									
									lConflictSafetyAndSafety = lConflictSafetyAndSafetyInfo.getBoundsInLocal().isEmpty() == false;
									lConflictSafetyAndShape  = lConflictSafetyAndShapeInfo.getBoundsInLocal().isEmpty() == false;
									lConflictFrontToSafety   = lConflictFrontAndSafetyInfo.getBoundsInLocal().isEmpty() == false;
									
									if(lConflictSafetyAndSafety) {
										System.out.println("Conflict Detection : " + lAircraft + "-" + loopOther + " (Safety -> Safety)");
									}
									if(lConflictSafetyAndShape) {
										System.out.println("Conflict Detection : " + lAircraft + "-" + loopOther + " (Safety -> Shape)");
									}
									if(lConflictFrontToSafety) {
										System.out.println("Conflict Detection : " + lAircraft + "-" + loopOther + " (Front -> Safety(the lowest priority situation))");
									}
								}
							}
						}
					}
				}

				
//				if(lAircraft.getCurrentFlightPlan().toString().equalsIgnoreCase("AAR124")) {
//					System.out.println(lAircraft.getRoutingInfo());
//					System.out.println(lAircraft.getCurrentFlightPlan().getNodeList());
//					System.out.println();
//				}
				
				
				/*
				 * Valid Priority
				 */ 

				// When Conflict resolved, continue Taxiing
				if(!lConflictSafetyAndSafety && !lConflictSafetyAndShape) {
					if(lAircraft.getConflictVehicle()!=null && lAircraft.getConflictVehicle().equals(loopOther)) {
						lAircraft.setConflictVehicle(null);
						
//						if(lAircraft.getMovementMode() == EAircraftMovementMode.PUSHBACK) {
//							lAircraft.setMoveState(new CAircraft());
//						}else if(lAircraft.getMovementMode() == EAircraftMovementMode.TAXIING) {
						lAircraft.setMoveState(new CAircraftTaxiingMoveState());
//						}
						
							
						continue;
					}
					continue;
				}
					
					
					
				// Ignore when other aircraft has this aircraft as conflict vehicle					
				if(loopOther.getConflictVehicle()!=null&&loopOther.getConflictVehicle().equals(lAircraft)) continue;
				

				// Ignore if leading Aircraft
//				if(lAircraft.getLeadingVehicle()!=null&&lAircraft.getLeadingVehicle().equals(loopOther)) continue;

				// When the other aircraft's routing has my link
				// I have a priority
				if(loopOther.getRoutingInfoLink().size()>0 && loopOther.getRoutingInfoLink().contains(lAircraft.getCurrentLink()) && loopOther.getMovementMode() != EAircraftMovementMode.PUSHBACK) continue;
				
				
				// Departure Priority
				// If other aircraft is arrival,
				// I have a priority
				if(loopOther.getMode() == EMode.ARR) continue;

				
				// ignore already Stopping
				if(lAircraft.getMoveState() instanceof CAircraftGroundConflictStopMoveState) {
					continue;
				}
				
				
				// Low Priority (Safety to Shape)
				if(lConflictSafetyAndShape) {
					// set conflict vehicle
					lAircraft.setConflictVehicle(loopOther);

					// Set Status							
					
					lAircraft.setMoveState(new CAircraftGroundConflictStopMoveState());
					
					continue;
				}
				
				//Low Priority (Safety to Shape)
				if(lConflictFrontToSafety) {
					// set conflict vehicle
					lAircraft.setConflictVehicle(loopOther);

					// Set Status								
					lAircraft.setMoveState(new CAircraftGroundConflictStopMoveState());
					
					continue;
				}
				

				/*
				 * Set Conflict behavior
				 */ 




				// Drawing
				GraphicsContext gc = CAtsolSimGuiControl.iInstance.getSimCanvas().getGraphicsContext2D();
				double x = lConflictSafetyAndShapeInfo.getBoundsInLocal().getMaxX();
				double y = lConflictSafetyAndShapeInfo.getBoundsInLocal().getMaxY();					
				CCoordination p = CAtsolSimGuiControl.iInstance.changeCoordinatesInCanvas(x, y);
				gc.setStroke(Color.RED);
				gc.strokeOval(p.getXCoordination()-2, p.getYCoordination()-2, 4, 4);
//				System.err.println("Require : Link Conflict Detection Priority");
				

			}//for(CAircraft loopOther : lOtherACList) {
		} // if(!(lAircraft.getMoveState() instanceof CAircraftNothingMoveState)) {
		
		
		
		
		
		
		
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






