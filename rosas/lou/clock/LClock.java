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
import java.time.Instant;
import java.text.DateFormat;
import rosas.lou.clock.*;

public class LClock implements Runnable{
   private long time;
   private Date date;
   private final int MOD = 1000;
   private ClockNotifier clockNotifier;
   private boolean toRun;

   {
      time          = 0;
      date          = null;
      clockNotifier = null;
      toRun         = false;
   };

   //************************Constructor******************************
   /*
   Constructor of no arguments
   */
   public LClock(){
      this.clockNotifier = new ClockNotifier();
   }

   /*
   */
   public void addObserver(ClockObserver clockObserver){
      this.clockNotifier.addObserver(clockObserver);
   }

   /*
   */
   public void removeObserver(ClockObserver clockObserver){
      this.clockNotifier.removeObserver(clockObserver);
   }

   //****************Interface Implementations***********************
   /*
   Implementation of the Runnable Interface--run() method
   */
   public void run(){
      try{
         this.toRun = true;
         this.time  = Calendar.getInstance().getTimeInMillis();
         Thread th  = new Thread(this.clockNotifier,"notifier");
         th.start();
         this.clockNotifier.setTime(Instant.now());
         this.clockNotifier.trigger(true);
         while(this.toRun){
            Thread.sleep(0,100000);
            this.clockNotifier.setTime(Instant.now());
            this.clockNotifier.trigger(true);
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
   public long getTimeInMillis(){
      //return this.time;
      return Calendar.getInstance().getTimeInMillis();
   }

   /*
   */
   public Instant getInstant(){
      return Instant.now();
   }

   /*
   */
   public Date getDate(){
      this.date = Calendar.getInstance().getTime();
      return this.date;
   }

   /*
   */
   public synchronized void stop(){
      this.toRun = false;
   }
}
