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

public class MultiThreadWaitAndNotifyTestDrive3 {
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
		
	
		MultiThreadWaitAndNotifyTestDrive3 mymain = new MultiThreadWaitAndNotifyTestDrive3();
		mymain.runMain();
		
		
	}
	
	public void runMain() {
		Timer timer = new Timer(new DispatchThread());
		Thread timerThread = new Thread(timer,"Timer");
		
		for(int i = 0; i < 1000; i++) {
			AC lAC = new AC("AC " + i, timer,timer.LockerTimer,timer.dispatcher);
			timer.addAC(lAC);
			Thread lACThread = new Thread(lAC,lAC.name);
			lACThread.start();
			
		}

		timerThread.start();
	}
	
	public class DispatchThread{
		
		Integer doneObjectCount = 0;
		boolean isTimerWait = false;
		public synchronized void imDone(Timer time, AC anAC) {
//			System.out.println(anAC.name + " is im done");
			synchronized (this) {
				if(!isTimerWait) {
					try {
						//					System.out.println(anAC.name + " is waiting in  im done");
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			doneObjectCount++;
			if(time.aclist.size() != doneObjectCount) {
				return;
			}
			doneObjectCount =0;
			
			synchronized (this) {
				this.notify();
			}
		}
		
		public synchronized void waitingElement() {


			isTimerWait = true;
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			isTimerWait = false;
		}
		
	}
	
	public class Timer implements Runnable{
		ArrayList<Object> LockerACList = new ArrayList<Object>();
		Object LockerTimer = new Object();
		
		ArrayList<Integer> test1IsDone;
		int time = 0;
		ArrayList<AC> aclist = new ArrayList<AC>();
		
		DispatchThread dispatcher;
		
		public Timer(DispatchThread adispatcher) {
			dispatcher=adispatcher;
		}
		public void addAC(AC aAC) {
			aclist.add(aAC);
			LockerACList.add(aAC.LockerAC);
		}
		
		@Override
		public void run() {


			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(true) {
				
				time++;
				System.out.println("Time : " + time);
				
				
				synchronized (LockerTimer) {
					LockerTimer.notifyAll();
				}
				
				dispatcher.waitingElement();
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				
				
			}
			
		}
		
	}
	
	
	public class AC implements Runnable{
		
		Object LockerTimer;
		Object LockerAC = new Object();;
		Timer timer;
		String name;
		int count = 0;
		DispatchThread dispatcher;
		
		int previousT = 0;
		int currentT = 1;
		
		public AC(String aname,Timer atimer, Object atest2,DispatchThread aDispather) {
			name = aname;
			timer = atimer;
			LockerTimer = atest2;
			dispatcher = aDispather;
		}
		@Override
		public void run() {
			while(true) {
				
				synchronized (LockerTimer) {
					try {
//						System.out.println(name + " is waiting in while");
						LockerTimer.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				currentT = timer.time;
				if(previousT >= currentT) {
					System.out.println("Some");
					continue;
				}
				
				try {
					Thread.sleep((int)(Math.random()*10+1));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				count++;
//				System.out.println(name + " worked at " + count++);		
				if(count != timer.time) {
					try {
						System.out.println(this.name);
						System.out.println(count);
						System.out.println(timer.time);
						throw new Exception();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				dispatcher.imDone(timer,this);
				previousT = currentT;
			}
		}

		
	}
	
	
}






