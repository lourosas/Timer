/*********************************************************************
* Copyright (C) 2015 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* PaceCalculator is distributed in the hope that it will be
* useful, but WITHOUT ANY WARRANTY; without even the implied
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.
* If not, see <http://www.gnu.org/licenses/>.
*********************************************************************/
//THIS IS DEADLOCKED!!!
package rosas.lou.clock;

import java.util.*;
import java.lang.*;
import java.text.DateFormat;
import rosas.lou.clock.*;

public class Clock implements Runnable{
   private long time;
   private Date date;
   final int MOD = 1000;
   boolean alert;
   List<ClockObserver> observers;

   {
      time      = 0;
      date      = null;
      observers = null;
      alert     = true;
   }

   //*********************Constructor********************************
   /*
   Constructor of no arguments
   */
   public Clock(){}

   /*
   */
   public void addObserver(ClockObserver clockObserver){
      try{
         this.observers.add(clockObserver);
      }
      catch(NullPointerException npe){
         this.observers = new LinkedList<ClockObserver>();
         this.observers.add(clockObserver);
      }
   }

   //****************Interface Implementations***********************
   /*
   Implementation of the Runnable Interface--run() method
   */
   public void run(){
      boolean run = true;
      this.time = Calendar.getInstance().getTimeInMillis();
      this.alertObservers();
      System.out.println(time);
      while(run){
         try{
            Thread.sleep(0,100);
         }
         catch(InterruptedException ie){
            run = false;
         }
         long time2 = Calendar.getInstance().getTimeInMillis();
         if((time%this.MOD) == (time2%this.MOD)){
            //Alert the Observers somehow
            //Somehow, I need to off load the time...not sure the
            //best way just yet...
            synchronized(this){
               this.time = time2;
               this.alert = true;
               this.notify();
            }
         }
      }
   }

   //*********************Public Methods*****************************
   /*
   */
   private synchronized void alertObservers(){
      //System.out.println(Thread.currentThread().getName());
      try{
         synchronized(this){
            while(!this.alert){
               this.wait();
            }
         }
         System.out.println(Thread.currentThread().getName());
         //System.out.println(this.time);
         this.alert = false;
      }
      catch(InterruptedException ie){ ie.printStackTrace(); }
   }

   /*
   */
   public long getTime(){
      return this.time;
   }

   /*
   */
   public Date getDate(){
      this.date = Calendar.getInstance().getTime();
      return this.date;
   }
}
