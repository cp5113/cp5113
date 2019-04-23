/**
 * ATSOL_SIM
 * elements.mobile.vehicle
 * CAircraftPerformance.java
 */
package elements.property;
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
 * @date : May 15, 2017
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * May 15, 2017 : Coded by S. J. Yun.
 *
 *
 */

import java.util.ArrayList;

import elements.mobile.vehicle.CAircraft;

/**
 * @author S. J. Yun
 *
 */
public class CAircraftPerformance extends AVehiclePerformance implements Cloneable {
	/*
	================================================================
	
			           Initializing Section
	
	================================================================
	*/
	private CAircraftType iOwnerAircraftType;	
	private double TaxiingSpeedMax;
	private double TaxiingSpeedNorm;
	private double AccelerationOnGroundMax;
	private double DecelerationOnGroundMax;
	private double AccelerationOnRunwayMax;
	private double DecelerationOnRunwayMax;
	private double ExitSpeedNorm;
	private double TakeoffSpeedNorm;
	private double ClimbSpeed1000Norm;
	
	
	private EADG	ADG;
	private EAPC	APC;
	private EWTC    WTC;
	private ERECATEU	RECATEU;
	
	private double 	V2;
	private double 	TakeoffDistance;
	
	private double	MTOW;
	private double	ROC_to_5000;
	private double	IAS_to_5000;
	private double	ROC_to_FL150;
	private double	IAS_to_FL150;
	private double	ROC_to_FL240;
	private double	IAS_to_FL240;
	private double	ROC_to_MACH;
	private double	IAS_to_MCAH;
	private double	TAS_Cruise;
	private double 	MACH_Cruise;
	private double	Ceiling_Cruise;
	private double	Range_Cruise;
	private double	IAS_to_FL240_Dec;
	private double	ROD_to_FL240;
	private double	IAS_to_FL100_Dec;
	private double	ROD_to_FL100;
	private double	IAS_Approach;
	private double	MCS_Approach;
	private double	ROD_Approach;
	private double	Vat;
	
	private double	LandingDistance;
	private String	AircraftSubTypeList;
	private String	Accomodation;
	private String	Note;
	private String	AlternativeName;
	
	private String PowerPlant;
	private double Pax;
	
	
	
			
	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Apr 3, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Apr 3, 2019 : Coded by S. J. Yun.
	 */
	public CAircraftPerformance(CAircraftType aOwnerAircraftType, double aTaxiingSpeedMax, double aTaxiingSpeedNorm,
			double aAccelerationOnGroundMax, double aDecelerationOnGroundMax, double aAccelerationOnRunwayMax,
			double aDecelerationOnRunwayMax, double aExitSpeedNorm, double aTakeoffSpeedNorm,
			double aClimbSpeed1000Norm, EADG aADG, EAPC aAPC, EWTC aWTC) {
		super();
		iOwnerAircraftType = aOwnerAircraftType;
		TaxiingSpeedMax = aTaxiingSpeedMax;
		TaxiingSpeedNorm = aTaxiingSpeedNorm;
		AccelerationOnGroundMax = aAccelerationOnGroundMax;
		DecelerationOnGroundMax = aDecelerationOnGroundMax;
		AccelerationOnRunwayMax = aAccelerationOnRunwayMax;
		DecelerationOnRunwayMax = aDecelerationOnRunwayMax;
		ExitSpeedNorm = aExitSpeedNorm;
		TakeoffSpeedNorm = aTakeoffSpeedNorm;
		ClimbSpeed1000Norm = aClimbSpeed1000Norm;
		ADG = aADG;
		APC = aAPC;
		WTC = aWTC;
	}

	/**
	 * The Constructor
	 * 
	 * Do What
	 * 
	 * @date : Mar 25, 2019
	 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
	 *
	 * @version : 
	 * Mar 25, 2019 : Coded by S. J. Yun.
	 */
	public CAircraftPerformance( 
			double aTaxiingSpeedMax, double aTaxiingSpeedNorm, double aAccelerationOnGroundMax,
			double aDecelerationOnGroundMax, double aAccelerationOnRunwayMax, double aDecelerationOnRunwayMax,
			double aExitSpeedNorm, double aTakeoffSpeedNorm, double aClimbSpeed1000Norm, EADG aADG, EAPC aAPC, EWTC aWTC) {
		super();		
		TaxiingSpeedMax = aTaxiingSpeedMax;
		TaxiingSpeedNorm = aTaxiingSpeedNorm;
		AccelerationOnGroundMax = aAccelerationOnGroundMax;
		DecelerationOnGroundMax = aDecelerationOnGroundMax;
		AccelerationOnRunwayMax = aAccelerationOnRunwayMax;
		DecelerationOnRunwayMax = aDecelerationOnRunwayMax;
		ExitSpeedNorm = aExitSpeedNorm;
		TakeoffSpeedNorm = aTakeoffSpeedNorm;
		ClimbSpeed1000Norm = aClimbSpeed1000Norm;
		ADG = aADG;
		APC = aAPC;
		WTC = aWTC;
	}

	public double getAccelerationOnGroundMax() {
		return AccelerationOnGroundMax;
	}
	public double getAccelerationOnRunwayMax() {
		return AccelerationOnRunwayMax;
	}
	public synchronized String getAccomodation() {
		return Accomodation;
	}
	public synchronized EADG getADG() {
		return ADG;
	}
	public synchronized String getAircraftSubTypeList() {
		return AircraftSubTypeList;
	}
	public synchronized String getAlternativeName() {
		return AlternativeName;
	}
	public synchronized EAPC getAPC() {
		return APC;
	}
	public synchronized double getCeiling_Cruise() {
		return Ceiling_Cruise;
	}
	public double getClimbSpeed1000Norm() {
		return ClimbSpeed1000Norm;
	}
	public double getDecelerationOnGroundMax() {
		return DecelerationOnGroundMax;
	}

	public double getDecelerationOnRunwayMax() {
		return DecelerationOnRunwayMax;
	}

	public double getExitSpeedNorm() {
		return ExitSpeedNorm;
	}

	public synchronized double getIAS_Approach() {
		return IAS_Approach;
	}

	public synchronized double getIAS_to_5000() {
		return IAS_to_5000;
	}

	public synchronized double getIAS_to_FL100_Dec() {
		return IAS_to_FL100_Dec;
	}

	public synchronized double getIAS_to_FL150() {
		return IAS_to_FL150;
	}

	public synchronized double getIAS_to_FL240() {
		return IAS_to_FL240;
	}

	public synchronized double getIAS_to_FL240_Dec() {
		return IAS_to_FL240_Dec;
	}

	public synchronized double getIAS_to_MCAH() {
		return IAS_to_MCAH;
	}

	public synchronized double getLandingDistance() {
		return LandingDistance;
	}

	public synchronized double getMACH_Cruise() {
		return MACH_Cruise;
	}

	public synchronized double getMCS_Approach() {
		return MCS_Approach;
	}

	public synchronized double getMTOW() {
		return MTOW;
	}

	public synchronized String getNote() {
		return Note;
	}

	/*
	================================================================
	
						Methods Section
	
	================================================================
	 */
	public CAircraftType getOwnerAircraftType() {
		return iOwnerAircraftType;
	}

	public synchronized double getPax() {
		return Pax;
	}

	public synchronized String getPowerPlant() {
		return PowerPlant;
	}

	public synchronized double getRange_Cruise() {
		return Range_Cruise;
	}

	public synchronized ERECATEU getRECATEU() {
		return RECATEU;
	}

	public synchronized double getROC_to_5000() {
		return ROC_to_5000;
	}

	public synchronized double getROC_to_FL150() {
		return ROC_to_FL150;
	}

	public synchronized double getROC_to_FL240() {
		return ROC_to_FL240;
	}

	public synchronized double getROC_to_MACH() {
		return ROC_to_MACH;
	}

	public synchronized double getROD_Approach() {
		return ROD_Approach;
	}

	public synchronized double getROD_to_FL100() {
		return ROD_to_FL100;
	}

	public synchronized double getROD_to_FL240() {
		return ROD_to_FL240;
	}

	public synchronized double getTakeoffDistance() {
		return TakeoffDistance;
	}

	public double getTakeoffSpeedNorm() {
		return TakeoffSpeedNorm;
	}

	public synchronized double getTAS_Cruise() {
		return TAS_Cruise;
	}

	public double getTaxiingSpeedMax() {
		return TaxiingSpeedMax;
	}

	public double getTaxiingSpeedNorm() {
		return TaxiingSpeedNorm;
	}

	public synchronized double getV2() {
		return V2;
	}

	public synchronized double getVat() {
		return Vat;
	}

	public synchronized EWTC getWTC() {
		return WTC;
	}

	public synchronized void setAccelerationOnGroundMax(double aAccelerationOnGroundMax) {
		AccelerationOnGroundMax = aAccelerationOnGroundMax;
	}

	public synchronized void setAccelerationOnRunwayMax(double aAccelerationOnRunwayMax) {
		AccelerationOnRunwayMax = aAccelerationOnRunwayMax;
	}

	public synchronized void setAccomodation(String aAccomodation) {
		Accomodation = aAccomodation;
	}

	public synchronized void setADG(EADG aADG) {
		ADG = aADG;
	}

	public synchronized void setAircraftSubTypeList(String aAircraftSubTypeList) {
		AircraftSubTypeList = aAircraftSubTypeList;
	}

	public synchronized void setAlternativeName(String aAlternativeName) {
		AlternativeName = aAlternativeName;
	}

	public synchronized void setAPC(EAPC aAPC) {
		APC = aAPC;
	}

	public synchronized void setCeiling_Cruise(double aCeiling_Cruise) {
		Ceiling_Cruise = aCeiling_Cruise;
	}

	public synchronized void setClimbSpeed1000Norm(double aClimbSpeed1000Norm) {
		ClimbSpeed1000Norm = aClimbSpeed1000Norm;
	}

	public synchronized void setDecelerationOnGroundMax(double aDecelerationOnGroundMax) {
		DecelerationOnGroundMax = aDecelerationOnGroundMax;
	}

	public synchronized void setDecelerationOnRunwayMax(double aDecelerationOnRunwayMax) {
		DecelerationOnRunwayMax = aDecelerationOnRunwayMax;
	}

	public synchronized void setExitSpeedNorm(double aExitSpeedNorm) {
		ExitSpeedNorm = aExitSpeedNorm;
	}

	public synchronized void setIAS_Approach(double aIAS_Approach) {
		IAS_Approach = aIAS_Approach;
	}

	public synchronized void setIAS_to_5000(double aIAS_to_5000) {
		IAS_to_5000 = aIAS_to_5000;
	}

	public synchronized void setIAS_to_FL100_Dec(double aIAS_to_FL100_Dec) {
		IAS_to_FL100_Dec = aIAS_to_FL100_Dec;
	}

	public synchronized void setIAS_to_FL150(double aIAS_to_FL150) {
		IAS_to_FL150 = aIAS_to_FL150;
	}

	public synchronized void setIAS_to_FL240(double aIAS_to_FL240) {
		IAS_to_FL240 = aIAS_to_FL240;
	}

	public synchronized void setIAS_to_FL240_Dec(double aIAS_to_FL240_Dec) {
		IAS_to_FL240_Dec = aIAS_to_FL240_Dec;
	}

	public synchronized void setIAS_to_MCAH(double aIAS_to_MCAH) {
		IAS_to_MCAH = aIAS_to_MCAH;
	}

	public synchronized void setLandingDistance(double aLandingDistance) {
		LandingDistance = aLandingDistance;
	}

	public synchronized void setMACH_Cruise(double aMACH_Cruise) {
		MACH_Cruise = aMACH_Cruise;
	}

	public synchronized void setMCS_Approach(double aMCS_Approach) {
		MCS_Approach = aMCS_Approach;
	}

	public synchronized void setMTOW(double aMTOW) {
		MTOW = aMTOW;
	}

	public synchronized void setNote(String aNote) {
		Note = aNote;
	}

	public void setOwnerAircraftType(CAircraftType aAircraftType) {
		iOwnerAircraftType = aAircraftType;
	}

	public synchronized void setPax(double aPax) {
		Pax = aPax;
	}

	public synchronized void setPowerPlant(String aPowerPlant) {
		PowerPlant = aPowerPlant;
	}

	public synchronized void setRange_Cruise(double aRange_Cruise) {
		Range_Cruise = aRange_Cruise;
	}

	public synchronized void setRECATEU(ERECATEU aRECATEU) {
		RECATEU = aRECATEU;
	}

	public synchronized void setROC_to_5000(double aROC_to_5000) {
		ROC_to_5000 = aROC_to_5000;
	}

	public synchronized void setROC_to_FL150(double aROC_to_FL150) {
		ROC_to_FL150 = aROC_to_FL150;
	}

	public synchronized void setROC_to_FL240(double aROC_to_FL240) {
		ROC_to_FL240 = aROC_to_FL240;
	}

	public synchronized void setROC_to_MACH(double aROC_to_MACH) {
		ROC_to_MACH = aROC_to_MACH;
	}

	public synchronized void setROD_Approach(double aROD_Approach) {
		ROD_Approach = aROD_Approach;
	}

	public synchronized void setROD_to_FL100(double aROD_to_FL100) {
		ROD_to_FL100 = aROD_to_FL100;
	}

	public synchronized void setROD_to_FL240(double aROD_to_FL240) {
		ROD_to_FL240 = aROD_to_FL240;
	}

	public synchronized void setTakeoffDistance(double aTakeoffDistance) {
		TakeoffDistance = aTakeoffDistance;
	}

	public synchronized void setTakeoffSpeedNorm(double aTakeoffSpeedNorm) {
		TakeoffSpeedNorm = aTakeoffSpeedNorm;
	}

	public synchronized void setTAS_Cruise(double aTAS_Cruise) {
		TAS_Cruise = aTAS_Cruise;
	}

	public synchronized void setTaxiingSpeedMax(double aTaxiingSpeedMax) {
		TaxiingSpeedMax = aTaxiingSpeedMax;
	}

	public synchronized void setTaxiingSpeedNorm(double aTaxiingSpeedNorm) {
		TaxiingSpeedNorm = aTaxiingSpeedNorm;
	}

	public synchronized void setV2(double aV2) {
		V2 = aV2;
	}

	public synchronized void setVat(double aVat) {
		Vat = aVat;
	}

	public synchronized void setWTC(EWTC aWTC) {
		WTC = aWTC;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
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






