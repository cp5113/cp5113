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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import elements.AElement;
import elements.IElementControlledByClock;
import elements.facility.CAirport;
import elements.facility.CSpot;
import elements.facility.CTaxiwayLink;
import elements.facility.CTaxiwayNode;
import elements.property.CAircraftPerformance;
import elements.property.CAircraftType;
import elements.property.EADG;
import elements.property.EAPC;
import elements.property.EWTC;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;
import elements.util.geo.CDegree;
import elements.util.geo.EGEOUnit;
import elements.util.geo.EUnit;
import util.file.CListUtil;
import util.file.SReadCSV;

/**
 * @author S. J. Yun
 *
 */
public class CAircraftTypeTable extends ATable {

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
			if(aFileArrayList.get(loopFile).getName().contains("AircraftType.csv")) {
				createAircraftTypeTable(aFileArrayList.get(loopFile));
			}
		}
				
	}
	
	
	

	private void createAircraftTypeTable(File aFile) {
		/*
		 * / Read CSV
		 */
		HashMap<String, HashMap<String, String>> lDataList =  SReadCSV.readCSVHashMap(aFile, CAirport.class, ",");
		if(lDataList.isEmpty()) return;
		
		/*
		 * Create Airport Table
		 */
		for(String key : lDataList.keySet()) {
			// Create Object
			
			
			// Extract Data
			HashMap<String,String> lData = lDataList.get(key);
			
			// Create Object Properties - String
			String lACType = lData.get("AircraftType");
			String lRange = lData.get("Range");
			EWTC lWTC   = EWTC.valueOf(lData.get("WTC"));
			EAPC lAPC   = EAPC.valueOf(lData.get("APC"));
			EADG lADG   = EADG.valueOf(lData.get("ADG"));
			
			String[] lACSubTypeListString = lData.get("AircraftSubTypeList").split("/");
			
			
			// Get AC Performance
			CAircraftPerformance lACPerformance =new CAircraftPerformance(parseDouble(lData.get("TaxiingSpeedMax")),	parseDouble(lData.get("TaxiingSpeedNorm")),	parseDouble(lData.get("AccelerationOnGroundMax")),	parseDouble(lData.get("DecelerationOnGroundMax")),	parseDouble(lData.get("AccelerationOnRunwayMax")),	parseDouble(lData.get("DecelerationOnRunwayMax")),	parseDouble(lData.get("ExitSpeedNorm")),	parseDouble(lData.get("TakeoffSpeedNorm")),	parseDouble(lData.get("ClimbSpeed1000Norm")), lADG,lAPC, lWTC);
			// Create Basic A/C
			CAircraftType lAircraftType = new CAircraftType(lACType,lACPerformance,lRange);
			lACPerformance.setOwnerAircraftType(lAircraftType); // Connect Each other
			// Merge to Table			
			addElement(lAircraftType);
			
			// Create SubType Aircraft
			for(int i = 0; i < lACSubTypeListString.length; i++) {
				// Create Object Properties - Class
				CAircraftPerformance lACSubtypePerformance =new CAircraftPerformance(parseDouble(lData.get("TaxiingSpeedMax")),	parseDouble(lData.get("TaxiingSpeedNorm")),	parseDouble(lData.get("AccelerationOnGroundMax")),	parseDouble(lData.get("DecelerationOnGroundMax")),	parseDouble(lData.get("AccelerationOnRunwayMax")),	parseDouble(lData.get("DecelerationOnRunwayMax")),	parseDouble(lData.get("ExitSpeedNorm")),	parseDouble(lData.get("TakeoffSpeedNorm")),	parseDouble(lData.get("ClimbSpeed1000Norm")), lADG,lAPC, lWTC);
				CAircraftType lAircraftSubType = new CAircraftType(lACSubTypeListString[i],lACSubtypePerformance,lRange);
				lACSubtypePerformance.setOwnerAircraftType(lAircraftSubType); // Connect Each other
				addElement(lAircraftSubType);
			} //for(int i = 0; i < lACSubTypeListString.length; i++) {
			
		} // for(String key : lDataList.keySet()) 
	}
	
		
	
	
	public static void main(String args[]) {
		CAircraftTypeTable my = new CAircraftTypeTable();
		ArrayList<File> aFile = new ArrayList<File>();
		aFile.add(new File("C:\\Users\\cp511\\git\\cp5113\\ATSOL_SIM\\SimStudy\\data\\aircraft\\AircraftType.csv"));
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






