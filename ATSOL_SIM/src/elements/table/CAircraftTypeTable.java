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
import elements.property.ERECATEU;
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
			ERECATEU lRECATEU;
			try {
				 lRECATEU = ERECATEU.valueOf(lData.get("RECATEU").toUpperCase());
			}catch(Exception e) {
				 lRECATEU = ERECATEU.NaN;
			}
			
			
			// Get AC Performance
			CAircraftPerformance lACPerformance =new CAircraftPerformance(parseDouble(lData.get("TaxiingSpeedMax")),	parseDouble(lData.get("TaxiingSpeedNorm")),	parseDouble(lData.get("AccelerationOnGroundMax")),	parseDouble(lData.get("DecelerationOnGroundMax")),	parseDouble(lData.get("AccelerationOnRunwayMax")),	parseDouble(lData.get("DecelerationOnRunwayMax")),	parseDouble(lData.get("ExitSpeedNorm")),	parseDouble(lData.get("TakeoffSpeedNorm")),	parseDouble(lData.get("ClimbSpeed1000Norm")), lADG,lAPC, lWTC);
			// Create Basic A/C
			CAircraftType lAircraftType = new CAircraftType(lACType,lACPerformance,lRange);
			lACPerformance.setOwnerAircraftType(lAircraftType); // Connect Each other

			lAircraftType.setLength(parseDouble(lData.get("Length")));
			lAircraftType.setHeight(parseDouble(lData.get("Height")));
			lAircraftType.setWidth(parseDouble(lData.get("Wingspan")));
			
			
			switch (lAPC) {
			case A:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 16.25);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 16.25);
				break;
			case B:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 21.5);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 21.5);
				break;
			case C:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 26);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 26);
				break;
			case D:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 40.5);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 40.5);
				break;
			case E:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 47.5);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 47.5);
				break;
			case F:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 57.5);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 57.5);
				break;
			case NaN:
				lAircraftType.setSafetyDistanceLength(lAircraftType.getLength()/2 + 16.25);
				lAircraftType.setSafetyDistanceWidth(lAircraftType.getWidth()/2 + 16.25);
				break;
			}
			
			
			
			
			lACPerformance.setAccelerationOnGroundMax(parseDouble(lData.get("AccelerationOnGroundMax")));
			lACPerformance.setAccelerationOnRunwayMax(parseDouble(lData.get("AccelerationOnRunwayMax")));
			lACPerformance.setAccomodation(lData.get("Accomodation"));			
			lACPerformance.setAircraftSubTypeList(lData.get("AircraftSubTypeList"));
			lACPerformance.setAlternativeName(lData.get("AlternativeName"));
			lACPerformance.setCeiling_Cruise(parseDouble(lData.get("Ceiling_Cruise")));
			lACPerformance.setClimbSpeed1000Norm(parseDouble(lData.get("ClimbSpeed1000Norm")));
			lACPerformance.setDecelerationOnGroundMax(parseDouble(lData.get("DecelerationOnGroundMax")));
			lACPerformance.setDecelerationOnRunwayMax(parseDouble(lData.get("DecelerationOnRunwayMax")));
			lACPerformance.setExitSpeedNorm(parseDouble(lData.get("ExitSpeedNorm")));
			lACPerformance.setIAS_Approach(parseDouble(lData.get("IAS_Approach")));
			lACPerformance.setIAS_to_5000(parseDouble(lData.get("IAS_to_5000")));
			lACPerformance.setIAS_to_FL100_Dec(parseDouble(lData.get("IAS_to_FL100_Dec")));
			lACPerformance.setIAS_to_FL150(parseDouble(lData.get("IAS_to_FL150")));
			lACPerformance.setIAS_to_FL240(parseDouble(lData.get("IAS_to_FL240")));
			lACPerformance.setIAS_to_FL240_Dec(parseDouble(lData.get("IAS_to_FL240_Dec")));
			lACPerformance.setIAS_to_MCAH(parseDouble(lData.get("IAS_to_MCAH")));
			lACPerformance.setLandingDistance(parseDouble(lData.get("LandingDistance")));
			lACPerformance.setMACH_Cruise(parseDouble(lData.get("MACH_Cruise")));
			lACPerformance.setMCS_Approach(parseDouble(lData.get("MCS_Approach")));
			lACPerformance.setMTOW(parseDouble(lData.get("MTOW")));
			lACPerformance.setNote(lData.get("Note"));
			lACPerformance.setPax(parseDouble(lData.get("Pax")));
			lACPerformance.setPowerPlant(lData.get("PowerPlant"));
			lACPerformance.setRange_Cruise(parseDouble(lData.get("Range_Cruise")));
			lACPerformance.setRECATEU(lRECATEU);
			lACPerformance.setROC_to_5000(parseDouble(lData.get("ROC_to_5000")));
			lACPerformance.setROC_to_FL150(parseDouble(lData.get("ROC_to_FL150")));
			lACPerformance.setROC_to_FL240(parseDouble(lData.get("ROC_to_FL240")));
			lACPerformance.setROC_to_MACH(parseDouble(lData.get("ROC_to_MACH")));
			lACPerformance.setROD_Approach(parseDouble(lData.get("ROD_Approach")));
			lACPerformance.setROD_to_FL100(parseDouble(lData.get("ROD_to_FL100")));
			lACPerformance.setROD_to_FL240(parseDouble(lData.get("ROD_to_FL240")));
			lACPerformance.setTakeoffDistance(parseDouble(lData.get("TakeoffDistance")));
			lACPerformance.setTakeoffSpeedNorm(parseDouble(lData.get("TakeoffSpeedNorm")));
			lACPerformance.setTAS_Cruise(parseDouble(lData.get("TAS_Cruise")));
			lACPerformance.setTaxiingSpeedMax(parseDouble(lData.get("TaxiingSpeedMax")));
			lACPerformance.setTaxiingSpeedNorm(parseDouble(lData.get("TaxiingSpeedNorm")));
			lACPerformance.setV2(parseDouble(lData.get("V2")));
			lACPerformance.setVat(parseDouble(lData.get("Vat")));
			
			
			
			
			
			// Merge to Table			
			addElement(lAircraftType);
			
			
			
			
			
			// Create SubType Aircraft
			String[] lACSubTypeListString = lData.get("AircraftSubTypeList").split("/");
			for(int i = 0; i < lACSubTypeListString.length; i++) {
				// Create Object Properties - Class
				CAircraftPerformance lACSubtypePerformance =new CAircraftPerformance(parseDouble(lData.get("TaxiingSpeedMax")),	parseDouble(lData.get("TaxiingSpeedNorm")),	parseDouble(lData.get("AccelerationOnGroundMax")),	parseDouble(lData.get("DecelerationOnGroundMax")),	parseDouble(lData.get("AccelerationOnRunwayMax")),	parseDouble(lData.get("DecelerationOnRunwayMax")),	parseDouble(lData.get("ExitSpeedNorm")),	parseDouble(lData.get("TakeoffSpeedNorm")),	parseDouble(lData.get("ClimbSpeed1000Norm")), lADG,lAPC, lWTC);
				CAircraftType lAircraftSubType = new CAircraftType(lACSubTypeListString[i],lACSubtypePerformance,lRange);
				lACSubtypePerformance.setOwnerAircraftType(lAircraftSubType); // Connect Each other
				
				lAircraftSubType.setLength(parseDouble(lData.get("Length")));
				lAircraftSubType.setHeight(parseDouble(lData.get("Height")));
				lAircraftSubType.setWidth(parseDouble(lData.get("Wingspan")));
				
				
				lACSubtypePerformance.setAccelerationOnGroundMax(parseDouble(lData.get("AccelerationOnGroundMax")));
				lACSubtypePerformance.setAccelerationOnRunwayMax(parseDouble(lData.get("AccelerationOnRunwayMax")));
				lACSubtypePerformance.setAccomodation(lData.get("Accomodation"));			
				lACSubtypePerformance.setAircraftSubTypeList(lData.get("AircraftSubTypeList"));
				lACSubtypePerformance.setAlternativeName(lData.get("AlternativeName"));
				lACSubtypePerformance.setCeiling_Cruise(parseDouble(lData.get("Ceiling_Cruise")));
				lACSubtypePerformance.setClimbSpeed1000Norm(parseDouble(lData.get("ClimbSpeed1000Norm")));
				lACSubtypePerformance.setDecelerationOnGroundMax(parseDouble(lData.get("DecelerationOnGroundMax")));
				lACSubtypePerformance.setDecelerationOnRunwayMax(parseDouble(lData.get("DecelerationOnRunwayMax")));
				lACSubtypePerformance.setExitSpeedNorm(parseDouble(lData.get("ExitSpeedNorm")));
				lACSubtypePerformance.setIAS_Approach(parseDouble(lData.get("IAS_Approach")));
				lACSubtypePerformance.setIAS_to_5000(parseDouble(lData.get("IAS_to_5000")));
				lACSubtypePerformance.setIAS_to_FL100_Dec(parseDouble(lData.get("IAS_to_FL100_Dec")));
				lACSubtypePerformance.setIAS_to_FL150(parseDouble(lData.get("IAS_to_FL150")));
				lACSubtypePerformance.setIAS_to_FL240(parseDouble(lData.get("IAS_to_FL240")));
				lACSubtypePerformance.setIAS_to_FL240_Dec(parseDouble(lData.get("IAS_to_FL240_Dec")));
				lACSubtypePerformance.setIAS_to_MCAH(parseDouble(lData.get("IAS_to_MCAH")));
				lACSubtypePerformance.setLandingDistance(parseDouble(lData.get("LandingDistance")));
				lACSubtypePerformance.setMACH_Cruise(parseDouble(lData.get("MACH_Cruise")));
				lACSubtypePerformance.setMCS_Approach(parseDouble(lData.get("MCS_Approach")));
				lACSubtypePerformance.setMTOW(parseDouble(lData.get("MTOW")));
				lACSubtypePerformance.setNote(lData.get("Note"));
				lACSubtypePerformance.setPax(parseDouble(lData.get("Pax")));
				lACSubtypePerformance.setPowerPlant(lData.get("PowerPlant"));
				lACSubtypePerformance.setRange_Cruise(parseDouble(lData.get("Range_Cruise")));
				lACSubtypePerformance.setRECATEU(lRECATEU);
				lACSubtypePerformance.setROC_to_5000(parseDouble(lData.get("ROC_to_5000")));
				lACSubtypePerformance.setROC_to_FL150(parseDouble(lData.get("ROC_to_FL150")));
				lACSubtypePerformance.setROC_to_FL240(parseDouble(lData.get("ROC_to_FL240")));
				lACSubtypePerformance.setROC_to_MACH(parseDouble(lData.get("ROC_to_MACH")));
				lACSubtypePerformance.setROD_Approach(parseDouble(lData.get("ROD_Approach")));
				lACSubtypePerformance.setROD_to_FL100(parseDouble(lData.get("ROD_to_FL100")));
				lACSubtypePerformance.setROD_to_FL240(parseDouble(lData.get("ROD_to_FL240")));
				lACSubtypePerformance.setTakeoffDistance(parseDouble(lData.get("TakeoffDistance")));
				lACSubtypePerformance.setTakeoffSpeedNorm(parseDouble(lData.get("TakeoffSpeedNorm")));
				lACSubtypePerformance.setTAS_Cruise(parseDouble(lData.get("TAS_Cruise")));
				lACSubtypePerformance.setTaxiingSpeedMax(parseDouble(lData.get("TaxiingSpeedMax")));
				lACSubtypePerformance.setTaxiingSpeedNorm(parseDouble(lData.get("TaxiingSpeedNorm")));
				lACSubtypePerformance.setV2(parseDouble(lData.get("V2")));
				lACSubtypePerformance.setVat(parseDouble(lData.get("Vat")));
				
				
				
				
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






