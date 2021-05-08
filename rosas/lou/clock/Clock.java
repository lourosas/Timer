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
package rosas.lou.clock;

import java.util.*;
import java.lang.*;
import java.text.DateFormat;
import rosas.lou.clock.*;

public class Clock implements Runnable{
   private long time;
   private Date date;
   private final int MOD = 1000;
   private ClockNotifier clockNotifier;

   {
      time          = 0;
      date          = null;
      clockNotifier = null;
   }

   //*********************Constructor********************************
   /*
   Constructor of no arguments
   */
   public Clock(){
      this.clockNotifier = new ClockNotifier();
   }

   /*
   */
   public void addObserver(ClockObserver clockObserver){
      this.clockNotifier.addObserver(clockObserver);
   }

   /*
   */
   public void removeObserver(ClockObserver clockObserver){}

   //****************Interface Implementations***********************
   /*
   Implementation of the Runnable Interface--run() method
   */
   public void run(){
      try{
         boolean run = true;
         this.time = Calendar.getInstance().getTimeInMillis();
         this.clockNotifier.setTime(this.time);
         this.clockNotifier.trigger(true);
         Thread th = new Thread(this.clockNotifier,"notifier");
         th.start();
         while(run){
            Thread.sleep(0,100);
            long time2 = Calendar.getInstance().getTimeInMillis();
            if((this.time%this.MOD) == (time2%this.MOD)){
               //Alert the Observers somehow
               //Somehow, I need to off load the time...not sure the
               //best way just yet...
               this.time = time2;
               this.clockNotifier.setTime(this.time);
               this.clockNotifier.trigger(true);
            }
         }
         this.clockNotifier.quit(true);
         th.join();
      }
      catch(InterruptedException e){
         e.printStackTrace();
      }
   }

   //*********************Public Methods*****************************
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
