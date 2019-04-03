package test.multithread;

import java.util.ArrayList;

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
 * @date : Apr 2, 2019
 * @author : S. J. Yun - cp5113@naver.com, +82-10-9254-5153
 *
 * @version : 
 * Apr 2, 2019 : Coded by S. J. Yun.
 *
 *
 */

public class MultiThreadWaitAndNotifyTestDrive2 {
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
	public static void main(String args[]) {
		
	
		MultiThreadWaitAndNotifyTestDrive2 mymain = new MultiThreadWaitAndNotifyTestDrive2();
		mymain.runMain();
		
		
	}
	
	public void runMain() {
		Timer timer = new Timer();
		Thread timerThread = new Thread(timer);
		ArrayList<Object> objectList = new ArrayList<Object>();
		objectList.add(new Object());
		objectList.add(new Object());

		
		timer.addAC(new Thread(new AC("AC 1",timerThread,objectList.get(0))));
		timer.addAC(new Thread(new AC("AC 2",timerThread,objectList.get(1))));
		timerThread.start();
		
		
	}
	
	public class Timer implements Runnable{
		ArrayList<Object> test1;
		Object LockerTimer;
		ArrayList<Integer> test1IsDone;
		int time = 0;
		ArrayList<Thread> aclist = new ArrayList<Thread>();
		Thread ac;
		public void addAC(Thread aAC) {
			aclist.add(aAC);
//			test1IsDone.add(0);
		}
		
		@Override
		public void run() {

			while(true) {
				
				time++;
				System.out.println("Time : " + time);

				synchronized (LockerTimer) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				
			}
			
		}
		
	}
	
	
	public class AC implements Runnable{
		
		Object LockerTimer;
		Object LockerAC;
		Thread timer;
		String name;
		public AC(String aname,Thread atimer, Object atest2) {
			name = aname;
			timer = atimer;
			LockerTimer = atest2;
		}
		@Override
		public void run() {
			while(true) {
				doSomething();
			}
		}
		
		public void doSomething() {
			System.out.println(name + " is waiting..");
			try {
				Thread.sleep((int) (Math.random()*5000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(name + " notified to timer");
			synchronized (LockerTimer) {
				LockerTimer.notify();
			}
			
		}
		
	}
	
	
}






