package api;

import api.inf.ITakeoffPerformance;
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
 * @date : 2019. 4. 28.
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * 2019. 4. 28. : Coded by S. J. Yun.
 *
 *
 */

public class CTakeoffPerformance implements ITakeoffPerformance {

	
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
	@Override
	public double calculateTargetAcceleration(EWTC aWTC, double aCurrentSpeed, double aRandomNumber) {

		double targetAcceleration = 0;
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
		double laccel1 = 0;
		double laccel2 = 0;
		double laccel3 = 0;
		
		double std = 0;
		switch(aWTC) {
		case M:
			std = 0.2;
			
			if(aCurrentSpeed>=25.24) {
				System.out.println();
			}
				
			// Formulation1 (Stabilization + Increasing to target N1)
			f1p1x= 0;
			f1p1y= 0.25  +(std*aRandomNumber-std/2);
			f1p2x= 23;
			f1p2y= 3.0  +(std*aRandomNumber-std/2);
			laccel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aCurrentSpeed - f1p1x) + f1p1y;

			// Formulation2 (Reaching Target N1)			
			f2p1x= 0;
			f2p1y= 1.75  +(std*aRandomNumber-std/2);
			f2p2x= 44;
			f2p2y= 3.0  +(std*aRandomNumber-std/2);
			laccel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aCurrentSpeed - f2p1x) + f2p1y;

			// Formulation3 (Maintain Target N1)			
			f3p1x= 0;
			f3p1y= 2.8  +(std*aRandomNumber-std/2);
			f3p2x= 100;
			f3p2y= 1.45  +(std*aRandomNumber-std/2);
			laccel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aCurrentSpeed - f3p1x) + f3p1y;
			
			if(laccel1<=laccel2 && laccel1<=laccel3) {
				targetAcceleration = laccel1;
			}else if(laccel2<=laccel1 && laccel2<=laccel3) {
				targetAcceleration = laccel2;
			}else if(laccel3<=laccel1 && laccel3<=laccel2) {
				targetAcceleration = laccel3;
			}
			
			
			return targetAcceleration;
		case H:
			std = 0.24;
			// Formulation1 (Stabilization + Increasing to target N1)
			f1p1x= 0;
			f1p1y= 0.25  +(std*aRandomNumber-std/2);
			f1p2x= 23;
			f1p2y= 3.0  +(std*aRandomNumber-std/2);
			laccel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aCurrentSpeed - f1p1x) + f1p1y;

			// Formulation2 (Reaching Target N1)			
			f2p1x= 0;
			f2p1y= 1.53  +(std*aRandomNumber-std/2);
			f2p2x= 47;
			f2p2y= 3.0  +(std*aRandomNumber-std/2);
			laccel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aCurrentSpeed - f2p1x) + f2p1y;

			// Formulation3 (Maintain Target N1)			
			f3p1x= 0;
			f3p1y= 2.8  +(std*aRandomNumber-std/2);
			f3p2x= 100;
			f3p2y= 1.45  +(std*aRandomNumber-std/2);
			laccel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aCurrentSpeed - f3p1x) + f3p1y;

			if(laccel1<=laccel2 && laccel1<=laccel3) {
				targetAcceleration = laccel1;
			}else if(laccel2<=laccel1 && laccel2<=laccel3) {
				targetAcceleration = laccel2;
			}else if(laccel3<=laccel1 && laccel3<=laccel2) {
				targetAcceleration = laccel3;
			}

			
			return targetAcceleration;
		case SH:
			std = 0.14;
			// Formulation1 (Stabilization + Increasing to target N1)
			f1p1x= 0;
			f1p1y= 0.23  +(std*aRandomNumber-std/2);
			f1p2x= 25;
			f1p2y= 3.0  +(std*aRandomNumber-std/2);
			laccel1 = (f1p2y-f1p1y)/(f1p2x-f1p1x) * (aCurrentSpeed - f1p1x) + f1p1y;

			// Formulation2 (Reaching Target N1)			
			f2p1x= 0;
			f2p1y= 1.0  +(std*aRandomNumber-std/2);
			f2p2x= 47;
			f2p2y= 3.0  +(std*aRandomNumber-std/2);
			laccel2 = (f2p2y-f2p1y)/(f2p2x-f2p1x) * (aCurrentSpeed - f2p1x) + f2p1y;

			// Formulation3 (Maintain Target N1)			
			f3p1x= 0;
			f3p1y= 2.1  +(std*aRandomNumber-std/2);
			f3p2x= 100;
			f3p2y= 1.12  +(std*aRandomNumber-std/2);
			laccel3 = (f3p2y-f3p1y)/(f3p2x-f3p1x) * (aCurrentSpeed - f3p1x) + f3p1y;

			if(laccel1<=laccel2 && laccel1<=laccel3) {
				targetAcceleration = laccel1;
			}else if(laccel2<=laccel1 && laccel2<=laccel3) {
				targetAcceleration = laccel2;
			}else if(laccel3<=laccel1 && laccel3<=laccel2) {
				targetAcceleration = laccel3;
			}			
			return targetAcceleration;
		case NaN:
			return 0;		
		}
		
		return targetAcceleration;
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






