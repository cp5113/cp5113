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

import algorithm.routing.DijkstraAlgorithm;
import elements.AElement;
import elements.IElementControlledByClock;
import elements.airspace.CAirRoute;
import elements.airspace.CWaypoint;
import elements.area.ASector;
import elements.facility.AFacility;
import elements.facility.CAirport;
import elements.facility.CRunway;
import elements.facility.CSpot;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.mobile.human.AATCController;
import elements.mobile.human.CApproachController;
import elements.mobile.human.CGroundController;
import elements.mobile.human.CLocalController;
import elements.mobile.human.EGender;
import elements.mobile.human.ESkill;
import elements.mobile.vehicle.CAircraft;
import elements.mobile.vehicle.CFlightPlan;
import elements.network.ANode;
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
public class CControllerTable extends ATable {

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
			if(aFileArrayList.get(loopFile).getName().contains("Controller.csv")) {
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
		
		DijkstraAlgorithm lDijkstraAlgorithm;
		
		
		if(lDataList.isEmpty()) return;
		
		/*
		 * Create Table
		 */
		for(String key : lDataList.keySet()) {
			
			// Extract Data - flight Plan
			HashMap<String,String> lData = lDataList.get(key);
			
			// Extract Properties
			String lName = lData.get("Name");
			String lAge = lData.get("Age");
			String lExperienceDay = lData.get("ExperienceDay");
			String lSkill = lData.get("Skill");
			String lGender = lData.get("Gender");
			String lControllerType = lData.get("ControllerType");
			String lFacilityID = lData.get("FacilityID");
			String lSpecificLink = lData.get("SpecificLink");
			String[] lSpecificLinkList = null;
			try{
				lSpecificLinkList = lSpecificLink.split("/");
			}catch(Exception e) {
				lSpecificLinkList = null;
			}
			
			// Create Object
			AATCController lController = null; 
			CAirport lAirport = null;
			switch(lControllerType) {
			case "GroundController":
				
				// Create Controller
				lController = new CGroundController(lName, parseDouble(lAge).intValue(), parseDouble(lExperienceDay).intValue(), ESkill.valueOf(lSkill), EGender.valueOf(lGender));
				
				// Add to Airport
				lAirport = (CAirport) CAtsolSimMain.getInstance().getiAirportTable().getElementTable().get(lFacilityID);
				if(lAirport==null) {
					System.out.println("No facility in Airport or Airspace for the controller " + lController.getName());
					continue;
				}
				
				lAirport.getGroundControllerList().add((CGroundController) lController);
				
				// set Airport to controller
				lController.addOwnedFacilty(lAirport);
				
				// Set TaxiwayLink to controller
				for(int i=0; i< lAirport.getTaxiwayLinkList().size(); i++) {
					if(lSpecificLinkList== null || lSpecificLinkList[0].equalsIgnoreCase("")) {						
						lController.addFacility(lAirport.getTaxiwayLinkList().get(i));
						lAirport.getTaxiwayLinkList().get(i).setATCController(lController);
						lAirport.getTaxiwayLinkList().get(i).setATCControllerToChildren(lController);
					}else {
						for(int loopSpecific = 0; loopSpecific < lSpecificLinkList.length; loopSpecific++) {
							if(lSpecificLinkList[loopSpecific].equalsIgnoreCase(lAirport.getTaxiwayLinkList().get(i).getName())) {
								lController.addFacility(lAirport.getTaxiwayLinkList().get(i));
								lAirport.getTaxiwayLinkList().get(i).setATCController(lController);
								lAirport.getTaxiwayLinkList().get(i).setATCControllerToChildren(lController);
								
							}
						}
					}
				}
				
				// Set Routing Algorithm
				lDijkstraAlgorithm = new DijkstraAlgorithm(lAirport.getTaxiwayLinkList(), lAirport.getTaxiwayNodeList());
				lController.setRoutingAlgorithm(lDijkstraAlgorithm);
						
				
				break;
			case "LocalController":
				
				// Create Controller
				lController = new CLocalController(lName, parseDouble(lAge).intValue(), parseDouble(lExperienceDay).intValue(), ESkill.valueOf(lSkill), EGender.valueOf(lGender));
				
				// Add to Airport
				lAirport = (CAirport) CAtsolSimMain.getInstance().getiAirportTable().getElementTable().get(lFacilityID);
				if(lAirport==null) {
					System.out.println("No facility in Airport or Airspace for the controller " + lController.getName());
					continue;
				}
				lAirport.getLocalControllerList().add((CLocalController) lController);
				
				// set Airport to controller
				lController.addOwnedFacilty(lAirport);
				
				
				// Set Runway to controller
				for(CRunway loopRwy : lAirport.getRunwayList()) {
					if(lSpecificLinkList== null || lSpecificLinkList[0].equalsIgnoreCase("")) {						
						lController.addFacility(loopRwy);
						loopRwy.setATCController(lController);
						loopRwy.setATCControllerToChildren(lController);
					}else {
						for(int loopSpecific = 0; loopSpecific < lSpecificLinkList.length; loopSpecific++) {
							if(lSpecificLinkList[loopSpecific].equalsIgnoreCase(loopRwy.getName())) {
								lController.addFacility(loopRwy);
								loopRwy.setATCController(lController);
								loopRwy.setATCControllerToChildren(lController);
							}
						}
					}
				}
				// Set Routing Algorithm
				lDijkstraAlgorithm = new DijkstraAlgorithm(lAirport.getTaxiwayLinkList(), lAirport.getTaxiwayNodeList());
				lController.setRoutingAlgorithm(lDijkstraAlgorithm);

				break;
			case "RampController":

				break;
			case "ApproachController":
				// Create Controller
				lController = new CApproachController(lName, parseDouble(lAge).intValue(), parseDouble(lExperienceDay).intValue(), ESkill.valueOf(lSkill), EGender.valueOf(lGender));
				
				// Add to Sector
				for(int i = 0; i < CAtsolSimMain.getInstance().getAirspaceTable().getElementList().size(); i++) {
					ASector lSector = (ASector) CAtsolSimMain.getInstance().getAirspaceTable().getElementList().get(i);
					if(lSector.getControllerListString().contains(lName)) {
						lSector.addControllerList(lController);
					}else {
						System.out.println("No airspace for the controller " + lController.getName() + " / " + lSector);
						continue;
					}
				}
				
				// set Airport to controller
				lController.addOwnedFacilty(lAirport);			
							
				break;
			case "ArrivalController":

				break;
			case "DepartureController":

				break;
			case "AreaController":

				break;
			default:
				break;
			}
			
			// add to table			
			addElement(lController);
			
			
		} // for(String key : lDataList.keySet()) 
	}
	
	public INode searchNodeInAirportAndWaypoint(String aNode) {
		INode lOutputNode = null;
		
		INode lAirportNode = (INode) CAtsolSimMain.getInstance().getiAirportTable().getElementTable().get(aNode);
		INode lWaypointNode = (INode) CAtsolSimMain.getInstance().getWaypointTable().getElementTable().get(aNode);
		if(lAirportNode != null) {
			lOutputNode = lAirportNode;
		}else if(lWaypointNode !=null){
			lOutputNode = lWaypointNode;
		}
		
		
		return lOutputNode;
	}
	
	
	public static void main(String args[]) {
		CControllerTable my = new CControllerTable();
		ArrayList<File> aFile = new ArrayList<File>();
		aFile.add(new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\controller\\Controller.csv"));
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






