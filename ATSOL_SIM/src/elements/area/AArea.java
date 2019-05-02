/**
 * ATSOL_SIM
 * elements.network
 * AArea.java
 */
package elements.area;
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
import java.util.LinkedList;
import java.util.List;

import elements.AElement;
import elements.mobile.human.AATCController;
import elements.network.ANode;
import elements.util.geo.CAltitude;
import elements.util.geo.CCoordination;

/**
 * @author S. J. Yun
 *
 */
public abstract class AArea extends AElement{
	/*
	================================================================

			           Initializing Section

	================================================================
	 */

	protected   List<Double> 			iXcooordList = Collections.synchronizedList(new ArrayList<Double>());
	protected   List<Double> 			iYcooordList = Collections.synchronizedList(new ArrayList<Double>());
	protected	List<CAltitude>			iLowLimit = Collections.synchronizedList(new ArrayList<CAltitude>());
	protected	List<CAltitude>			iUpperLimit= Collections.synchronizedList(new ArrayList<CAltitude>());

	protected   List<AATCController>	iControllerList = Collections.synchronizedList(new ArrayList<AATCController>());
	protected	List<String>			iControllerListString = Collections.synchronizedList(new ArrayList<String>());
	public synchronized List<Double> getXcooordList() {
		return iXcooordList;
	}
	public synchronized void addXcooordList(Double aX) {
		iXcooordList.add(aX);
	}
	public synchronized List<Double> getYcooordList() {
		return iYcooordList;
	}
	public synchronized void addYcooordList(Double aY) {
		iYcooordList.add(aY);
	}
	public synchronized void addAltitudeMin(CAltitude aAltitude) {
		iLowLimit.add(aAltitude);
	}
	public synchronized void addAltitudeMax(CAltitude aAltitude) {
		iUpperLimit.add(aAltitude);
	}
	public synchronized List<CAltitude> getLowLimit() {
		return iLowLimit;
	}
	public synchronized void setLowLimit(List<CAltitude> aLowLimit) {
		iLowLimit = aLowLimit;
	}
	public synchronized List<CAltitude> getUpperLimit() {
		return iUpperLimit;
	}
	public synchronized void setUpperLimit(List<CAltitude> aUpperLimit) {
		iUpperLimit = aUpperLimit;
	}
	public synchronized List<AATCController> getControllerList() {
		return iControllerList;
	}
	public synchronized void addControllerList(AATCController aController) {
		iControllerList.add(aController);
	}
	public synchronized List<String> getControllerListString() {
		return iControllerListString;
	}
	public synchronized void addControllerListString(String aControllerString) {
		iControllerListString.add(aControllerString);
	}






	/*
	================================================================

						Methods Section

	================================================================
	 */

	private class Point
	{
		double x, y;

		Point()
		{}

		Point(double p, double q)
		{
			x = p;
			y = q;
		}
	}

	private boolean onSegment(Point p, Point q, Point r)
	{
		if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x)
				&& q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
			return true;
		return false;
	}

	private int orientation(Point p, Point q, Point r)
	{
		double val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

		if (val == 0)
			return 0;
		return (val > 0) ? 1 : 2;
	}

	private boolean doIntersect(Point p1, Point q1, Point p2, Point q2)
	{

		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);

		if (o1 != o2 && o3 != o4)
			return true;

		if (o1 == 0 && onSegment(p1, p2, q1))
			return true;

		if (o2 == 0 && onSegment(p1, q2, q1))
			return true;

		if (o3 == 0 && onSegment(p2, p1, q2))
			return true;

		if (o4 == 0 && onSegment(p2, q1, q2))
			return true;

		return false;
	}

	private boolean isInside(Point polygon[], int n, Point p)
	{
		double INF = 1000000000;
		if (n < 3)
			return false;

		Point extreme = new Point(INF, p.y);

		int count = 0, i = 0;
		do
		{
			int next = (i + 1) % n;
			if (doIntersect(polygon[i], polygon[next], p, extreme))
			{
				if (orientation(polygon[i], p, polygon[next]) == 0)
					return onSegment(polygon[i], p, polygon[next]);

				count++;
			}
			i = next;
		} while (i != 0);

		return (count & 1) == 1 ? true : false;
	}

	public boolean isInside(CCoordination aPoint){
		
		int n = this.iXcooordList.size();
		
		Point[] polygon  = new Point[n];
		
		for(int i = 0; i < n ; i++) {
			polygon[i] = new Point(this.iXcooordList.get(i), this.iYcooordList.get(i));
		}
		
		Point p = new Point(aPoint.getXCoordination(),aPoint.getYCoordination());
		
		
		return isInside(polygon, n, p);
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






