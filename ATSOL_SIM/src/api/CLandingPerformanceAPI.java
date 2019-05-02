package api;

import api.inf.ILandingPerformance;
import elements.property.EWTC;

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

public class CLandingPerformanceAPI implements ILandingPerformance {

	@Override
	public double calculateTargetAcceleration(EWTC aWTC, double aCurrentSpeed, double aRandomNumber, double aTimeFromThresholdInSecond) {

		double targetDeceleration = 0;
		double targetDeceleration2 = 0; // Time Dependent
		double f1p1x= 0;
		double f1p1y= 0.25;
		double f1p2x= 22;
		double f1p2y= 3.0;
		double f2p1x= 0;
		double f2p1y= 0.25;
		double f2p2x= 22;
		double f2p2y= 3.0;
		double f3p1x= 0;
		double f3p1y= 0.25;
		double f3p2x= 22;
		double f3p2y= 3.0;
		double ldecel1 = 0;
		double ldecel2 = 0;
		double ldecel3 = 0;
		
		double std = 0;
		switch(aWTC) {
		case M:
			std = 0.0;
			
				
			// Formulation1 (Stabilization + Increasing to target N1)
			f1p1x= 38;
			f1p1y= -3  +(std*aRandomNumber-std/2);
			f1p2x= 93;
			f1p2y= 1.0  +(std*aRandomNumber-std/2);
			ldecel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aCurrentSpeed - f1p1x) + f1p1y;

			// Formulation2 (Reaching Target N1)			
			f2p1x= 0;
			f2p1y= -1.7  +(std*aRandomNumber-std/2);
			f2p2x= 100;
			f2p2y= -1.7  +(std*aRandomNumber-std/2);
			ldecel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aCurrentSpeed - f2p1x) + f2p1y;

			// Formulation3 (Maintain Target N1)			
			f3p1x= 0;
			f3p1y= 0.25  +(std*aRandomNumber-std/2);
			f3p2x= 80;
			f3p2y= -3  +(std*aRandomNumber-std/2);
			ldecel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aCurrentSpeed - f3p1x) + f3p1y;
			
			if(ldecel1>=ldecel2 && ldecel1>=ldecel3) {
				targetDeceleration = ldecel1;
			}else if(ldecel2>=ldecel1 && ldecel2>=ldecel3) {
				targetDeceleration = ldecel2;
			}else if(ldecel3>=ldecel1 && ldecel3>=ldecel2) {
				targetDeceleration = ldecel3;
			}
			
			


			// Phase 1
			f1p1x= 0;
			f1p1y= 0-(std*aRandomNumber-std/2);
			f1p2x= 28.3;
			f1p2y= -3.0+(std*aRandomNumber-std/2);
			ldecel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aTimeFromThresholdInSecond - f1p1x) + f1p1y;


			//% Phase 2
			f2p1x= 0;
			f2p1y= -1.73 -(std*aRandomNumber-std/2);
			f2p2x= 120;
			f2p2y= -1.73-(std*aRandomNumber-std/2);
			ldecel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aTimeFromThresholdInSecond - f2p1x) + f2p1y;



			// % Phase 3
			f3p1x= 0;
			f3p1y= -2.5-(std*aRandomNumber-std/2);
			f3p2x= 86.8;
			f3p2y= 1-(std*aRandomNumber-std/2);
			ldecel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aTimeFromThresholdInSecond - f3p1x) + f3p1y;


			if(ldecel1>=ldecel2 && ldecel1>=ldecel3) {
				targetDeceleration2 = ldecel1;
			}else if(ldecel2>=ldecel1 && ldecel2>=ldecel3) {
				targetDeceleration2 = ldecel2;
			}else if(ldecel3>=ldecel1 && ldecel3>=ldecel2) {
				targetDeceleration2 = ldecel3;
			}

			
		    if(aTimeFromThresholdInSecond < 20) {
		    	targetDeceleration = targetDeceleration2;
		    }else {
		    	if(targetDeceleration < targetDeceleration2) {
		    		 targetDeceleration = targetDeceleration;
		    	}else {
		    		 targetDeceleration = targetDeceleration2;
		    	}		    		
		    }
		        
			
			
			
			
			
			
			
			
			return targetDeceleration;	
		case H:
			std = 0.0;
			// Formulation1 (Stabilization + Increasing to target N1)
			f1p1x= 33;
			f1p1y= -3.0  +(std*aRandomNumber-std/2);
			f1p2x= 95;
			f1p2y= 1.0  +(std*aRandomNumber-std/2);
			ldecel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aCurrentSpeed - f1p1x) + f1p1y;

			// Formulation2 (Reaching Target N1)			
			f2p1x= 0;
			f2p1y= -1.57  +(std*aRandomNumber-std/2);
			f2p2x= 100;
			f2p2y= -1.57  +(std*aRandomNumber-std/2);
			ldecel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aCurrentSpeed - f2p1x) + f2p1y;

			// Formulation3 (Maintain Target N1)			
			f3p1x= 0;
			f3p1y= 0.25  +(std*aRandomNumber-std/2);
			f3p2x= 80;
			f3p2y= -3.0  +(std*aRandomNumber-std/2);
			ldecel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aCurrentSpeed - f3p1x) + f3p1y;

			if(ldecel1>=ldecel2 && ldecel1>=ldecel3) {
				targetDeceleration = ldecel1;
			}else if(ldecel2>=ldecel1 && ldecel2>=ldecel3) {
				targetDeceleration = ldecel2;
			}else if(ldecel3>=ldecel1 && ldecel3>=ldecel2) {
				targetDeceleration = ldecel3;
			}
	
			
			
			
			// Phase 1
			f1p1x= 0;
			f1p1y= 0-(std*aRandomNumber-std/2);
			f1p2x= 31.8;
			f1p2y= -3.0+(std*aRandomNumber-std/2);
			ldecel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aTimeFromThresholdInSecond - f1p1x) + f1p1y;


			//% Phase 2
			f2p1x= 0;
			f2p1y= -1.6 -(std*aRandomNumber-std/2);
			f2p2x= 120;
			f2p2y= -1.6-(std*aRandomNumber-std/2);
			ldecel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aTimeFromThresholdInSecond - f2p1x) + f2p1y;



			// % Phase 3
			f3p1x= 0;
			f3p1y= -2.28-(std*aRandomNumber-std/2);
			f3p2x= 96.8;
			f3p2y= 1-(std*aRandomNumber-std/2);
			ldecel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aTimeFromThresholdInSecond - f3p1x) + f3p1y;


			if(ldecel1>=ldecel2 && ldecel1>=ldecel3) {
				targetDeceleration2 = ldecel1;
			}else if(ldecel2>=ldecel1 && ldecel2>=ldecel3) {
				targetDeceleration2 = ldecel2;
			}else if(ldecel3>=ldecel1 && ldecel3>=ldecel2) {
				targetDeceleration2 = ldecel3;
			}

			
		    if(aTimeFromThresholdInSecond < 20) {
		    	targetDeceleration = targetDeceleration2;
		    }else {
		    	if(targetDeceleration < targetDeceleration2) {
		    		 targetDeceleration = targetDeceleration;
		    	}else {
		    		 targetDeceleration = targetDeceleration2;
		    	}		    		
		    }
			
			
			
			
			
			
			
			
			
			
			return targetDeceleration;
		case SH:
			std = 0.0;
			// Formulation1 (Stabilization + Increasing to target N1)
			f1p1x= 38.3;
			f1p1y= -3  +(std*aRandomNumber-std/2);
			f1p2x= 88;
			f1p2y= 1  +(std*aRandomNumber-std/2);
			ldecel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aCurrentSpeed - f1p1x) + f1p1y;

			// Formulation2 (Reaching Target N1)			
			f2p1x= 0;
			f2p1y= -2.1+(std*aRandomNumber-std/2);
			f2p2x= 100;
			f2p2y= -0.5  +(std*aRandomNumber-std/2);
			ldecel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aCurrentSpeed - f2p1x) + f2p1y;

			// Formulation3 (Maintain Target N1)			
			f3p1x= 0;
			f3p1y= 0.7  +(std*aRandomNumber-std/2);
			f3p2x= 41.3;
			f3p2y= -3  +(std*aRandomNumber-std/2);
			ldecel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aCurrentSpeed - f3p1x) + f3p1y;

			if(ldecel1>=ldecel2 && ldecel1>=ldecel3) {
				targetDeceleration = ldecel1;
			}else if(ldecel2>=ldecel1 && ldecel2>=ldecel3) {
				targetDeceleration = ldecel2;
			}else if(ldecel3>=ldecel1 && ldecel3>=ldecel2) {
				targetDeceleration = ldecel3;
			}
			

			
			
			
			// Phase 1
			f1p1x= 0;
			f1p1y= 0-(std*aRandomNumber-std/2);
			f1p2x= 38.8;
			f1p2y= -3.0+(std*aRandomNumber-std/2);
			ldecel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aTimeFromThresholdInSecond - f1p1x) + f1p1y;


			//% Phase 2
			f2p1x= 0;
			f2p1y= -1 -(std*aRandomNumber-std/2);
			f2p2x= 120;
			f2p2y= -2.75-(std*aRandomNumber-std/2);
			ldecel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aTimeFromThresholdInSecond - f2p1x) + f2p1y;



			// % Phase 3
			f3p1x= 0;
			f3p1y= -4.9-(std*aRandomNumber-std/2);
			f3p2x= 71;
			f3p2y= 1-(std*aRandomNumber-std/2);
			ldecel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aTimeFromThresholdInSecond - f3p1x) + f3p1y;


			if(ldecel1>=ldecel2 && ldecel1>=ldecel3) {
				targetDeceleration2 = ldecel1;
			}else if(ldecel2>=ldecel1 && ldecel2>=ldecel3) {
				targetDeceleration2 = ldecel2;
			}else if(ldecel3>=ldecel1 && ldecel3>=ldecel2) {
				targetDeceleration2 = ldecel3;
			}

			
		    if(aTimeFromThresholdInSecond < 20) {
		    	targetDeceleration = targetDeceleration2;
		    }else {
		    	if(targetDeceleration < targetDeceleration2) {
		    		 targetDeceleration = targetDeceleration;
		    	}else {
		    		 targetDeceleration = targetDeceleration2;
		    	}		    		
		    }
			
		    
		    
		    
			return targetDeceleration;
		case NaN:
			return 0;		
		}
		
		return targetDeceleration;
	}
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






