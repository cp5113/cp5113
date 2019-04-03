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

public class MultiThreadWaitAndNotifyTestDrive {
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
		
	
		MultiThreadWaitAndNotifyTestDrive mymain = new MultiThreadWaitAndNotifyTestDrive();
		mymain.runMain();
		
		
	}
	
	public void runMain() {
		Timer timer = new Timer();
		Thread timerThread = new Thread(timer);
		ArrayList<Object> objectList = new ArrayList<Object>();
		objectList.add(new Object());
		objectList.add(new Object());
		objectList.add(new Object());
		objectList.add(new Object());
		objectList.add(new Object());
		timer.test1 = objectList;
		timer.test2 = new Object();
		timer.addAC(new Thread(new AC("AC 1",timerThread,timer.test2)));
		timer.addAC(new Thread(new AC("AC 2",timerThread,timer.test2)));
		
		
		
//		timer.addAC(new Thread(new AC("AC 1",timerThread,objectList.get(0))));
//		timer.addAC(new Thread(new AC("AC 1",timerThread,objectList.get(1))));
//		timer.addAC(new Thread(new AC("AC 1",timerThread,objectList.get(2))));
//		timer.addAC(new Thread(new AC("AC 1",timerThread,objectList.get(3))));
//		timer.addAC(new Thread(new AC("AC 1",timerThread,objectList.get(4))));
//		
		timerThread.start();
		
		
//		timer.addAC(new Thread(new AC("AC 2",timer)));
//		timer.addAC(new Thread(new AC("AC 3",timer)));
//		timer.addAC(new Thread(new AC("AC 4",timer)));
//		timer.addAC(new Thread(new AC("AC 5",timer)));
//		timer.addAC(new Thread(new AC("AC 6",timer)));
		for(int i=0; i<timer.aclist.size(); i++) {
			timer.aclist.get(i).start();
		}
		
		
		
	}
	
	public class Timer implements Runnable{
		ArrayList<Object> test1;
		Object test2;
		
		int time = 0;
		ArrayList<Thread> aclist = new ArrayList<Thread>();
		Thread ac;
		public void addAC(Thread aAC) {
			aclist.add(aAC);
			ac=aAC;
		}
		
		@Override
		public void run() {

			while(true) {
				
				time++;
				System.out.println("Time : " + time);
				
				
				int countAC = 0;
				while(countAC < aclist.size()) {
					synchronized (test2) {
						try {
							test2.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						countAC++;
					}			
				}

				
				
			}
			
		}
		
	}
	
	
	public class AC implements Runnable{
		
		Object test2;
		Thread timer;
		String name;
		public AC(String aname,Thread atimer, Object atest2) {
			name = aname;
			timer = atimer;
			test2 = atest2;
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
			synchronized (test2) {
				test2.notify();
			}
			
		}
		
	}
	
	
}






