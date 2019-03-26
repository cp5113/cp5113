/**
 * ATSOL_SIM
 * elements.table
 * CAirportTable.java
 */
package elements.table;
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
 * @date : Mar 21, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Mar 21, 2019 : Coded by S. J. Yun.
 *
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import elements.AElement;
import elements.IElementControlledByClock;
import elements.airspace.CAirRoute;
import elements.airspace.CWaypoint;
import elements.facility.CAirport;
import elements.facility.CSpot;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.INode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.CDegree;
import elements.util.geo.EGEOUnit;
import elements.util.geo.EUnit;
import sim.CAtsolSimMain;
import util.file.CListUtil;
import util.file.SReadCSV;

/**
 * @author S. J. Yun
 *
 */
public class CAircraftTable extends ATable {

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
	
	/** (non-Javadoc)
	 * @see elements.table.ATable#readCSV(java.io.File)
	 */
	@Override
	public void createTable(ArrayList<File> aFileArrayList) {
		// TODO Auto-generated method stub
		
 		for(int loopFile = 0; loopFile< aFileArrayList.size() ; loopFile++) {
			if(aFileArrayList.get(loopFile).getName().contains("FlightSchedule.csv")) {
				createAircraftTypeTable(aFileArrayList.get(loopFile));
			}
		}
				
	}
	
	
	

	private void createAircraftTypeTable(File aFile) {

		SimpleDateFormat lSimpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/*
		 * / Read CSV
		 */
		HashMap<String, HashMap<String, String>> lDataList =  SReadCSV.readCSVHashMap(aFile, CAirport.class, ",");
		if(lDataList.isEmpty()) return;
		
		/*
		 * Create Table
		 */
		for(String key : lDataList.keySet()) {
			
			// Extract Data - flight Plan
			HashMap<String,String> lData = lDataList.get(key);
			
			// Extract Properties
			String lCallsign 				= lData.get("Callsign");
			String lAircraftType 			= lData.get("AircraftType");
			String lRange 					= lData.get("Range");
			String lRegistration 			= lData.get("Registration");
			String lOrigin 					= lData.get("Origin");
			String lDestination 			= lData.get("Destination");
			String lScheduleTimeDeparture 	= lData.get("ScheduleTimeDeparture");
			String lScheduleTimeArrival 	= lData.get("ScheduleTimeArrival");
			String lSpotArrival 			= lData.get("SpotArrival");
			String lSpotDeparture			= lData.get("SpotDeparture");
			String lDepartureRunway 		= lData.get("DepartureRunway");
			String lArrivalRunway 			= lData.get("ArrivalRunway");
			String lAirline 				= lData.get("Airline");
			String lRoute 					= lData.get("Route");
			String lCrusingAltitude			= lData.get("CrusingAltitude");
			
			// Search Aircraft or create Aircraft
			CAircraft lAircraft	= (CAircraft) getElementTable().get(lRegistration);
			if(lAircraft == null) {
				lAircraft = new CAircraft();
				lAircraft.setRegistration(lRegistration);				
				CAircraftType lACTypeInTable = (CAircraftType) CAtsolSimMain.getInstance().getiAircraftTypeTable().getElementTable().get(lAircraftType + "/" +lRange);
				if(lACTypeInTable == null) {
					System.out.println("Aircraft Type "+ lAircraftType +"/Range is not defined");
					System.out.println("Input Data is "+ lAircraftType +"/" + lRange);
				}
				lAircraft.setVehcleType(lACTypeInTable);
			}

			// Search Node List
			INode lOriginNode = searchNodeInAirportAndWaypoint(lOrigin);
			INode lDestinNode = searchNodeInAirportAndWaypoint(lDestination);
			
			// Search Node in Route
			CAirRoute lRouteConnected = (CAirRoute) CAtsolSimMain.getInstance().getiAirRouteTable().getElementTable().get(lRoute);			
			List<INode> lNodeList;
			try {
				lNodeList= lRouteConnected.getNodeList();
			}
			catch(Exception e) {
				System.out.println(lRoute + " is not defined in Route.csv");
				continue;
			}
			
			
			// Create Flight plan 
			CFlightPlan lFlightPlan = new CFlightPlan(lAircraft,lOriginNode,lDestinNode);
			
			// Set Properties to FlightPlan
			lFlightPlan.setCallsign(lCallsign);
			lFlightPlan.setCrusingAltitude(new CAltitude(parseDouble(lCrusingAltitude), EGEOUnit.FEET));
			
			// Add Node list into Flight Plan			
			for(int loopNode = 0; loopNode < lNodeList.size(); loopNode++) {
				if(loopNode==0) {
					Calendar lCal                       = Calendar.getInstance();
					try {
						lCal.setTime(lSimpleDateFormat.parse(lScheduleTimeDeparture));
					} catch (ParseException e) {
						lCal.setTimeInMillis(0);
					}
					lFlightPlan.addPlanItem(lNodeList.get(loopNode), lCal);
				}else if(loopNode==lNodeList.size()-1) {
					Calendar lCal                       = Calendar.getInstance();
					try {
						lCal.setTime(lSimpleDateFormat.parse(lScheduleTimeArrival));
					} catch (ParseException e) {
						lCal.setTimeInMillis(0);
					}
					lFlightPlan.addPlanItem(lNodeList.get(loopNode), lCal);
				}else {
					lFlightPlan.addPlanItem(lNodeList.get(loopNode),null);
				}				
				
				if(lNodeList.get(loopNode) instanceof CWaypoint && (lFlightPlan.getOriginationNode() instanceof CWaypoint || lFlightPlan.getDestinationNode() instanceof CWaypoint)) {
					lFlightPlan.setAltitude(loopNode, new CAltitude(parseDouble(lCrusingAltitude), EGEOUnit.FEET));
				}
				lAircraft.getPlanList().add(lFlightPlan);
			}
			// add to table			
			addElement(lAircraft);
			
			
		} // for(String key : lDataList.keySet()) 
	}
	
	public INode searchNodeInAirportAndWaypoint(String aNode) {
		INode lOutputNode = null;
		
		INode lAirportNode = (INode) CAtsolSimMain.getInstance().getiAirportTable().getElementTable().get(aNode);
		INode lWaypointNode = (INode) CAtsolSimMain.getInstance().getiWaypointTable().getElementTable().get(aNode);
		if(lAirportNode != null) {
			lOutputNode = lAirportNode;
		}else if(lWaypointNode !=null){
			lOutputNode = lWaypointNode;
		}
		
		
		return lOutputNode;
	}
	
	
	public static void main(String args[]) {
		CAircraftTable my = new CAircraftTable();
		ArrayList<File> aFile = new ArrayList<File>();
		aFile.add(new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\schedule\\FlightSchedule.csv"));
		my.createTable(aFile);
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






