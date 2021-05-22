//////////////////////////////////////////////////////////////////////
/*
* Copyright (C) 2021 Lou Rosas
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
*/
package rosas.lou.clock;

import java.lang.*;
import java.util.*;
import java.time.Instant;
import rosas.lou.clock.*;

public class ClockNotifier implements Runnable{
   private long                _currentTime;
   private Instant             _instant;
   private boolean             _quit;
   private boolean             _triggered;
   private List<ClockObserver> _observers;

   {
      _currentTime = 0;
      _instant     = null;
      _quit        = false;
      _triggered   = false;
      _observers   = null;
   };

   /*
   */
   public ClockNotifier(){}

   /**/
   public void addObserver(ClockObserver observer){
      try{
         this._observers.add(observer);
      }
      catch(NullPointerException npe){
         this._observers = new LinkedList<ClockObserver>();
         this._observers.add(observer);
      }
   }

   /*
   */
   public void quit(boolean quit){
      this._quit = quit;
      if(this._quit){
         this.trigger(true);
      }
   }

   /**/
   public void removeObserver(ClockObserver observer){}

   /*
   */
   public void setTime(long time){
      this._currentTime = time;
   }

   /*
   */
   public void setTime(Instant instant){
      this._instant = instant;
   }

   /*
   */
   public synchronized void trigger(boolean trigger){
      this._triggered = trigger;
      if(this._triggered){
         this.notify();
      }
   }

   //////////////////Runnable Interface Implementation///////////////
   /*
   */
   public void run(){
      while(!this._quit){
         try{
            synchronized(this){
               while(!this._triggered){
                  this.wait();
               }
            }
            if(!this._quit){
               Iterator it = this._observers.iterator();
               while(it.hasNext()){
                  ClockObserver ob = (ClockObserver)it.next();
                  ob.updateTime(this._currentTime);
               }
            }
            this.trigger(false);
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
         }
      }
   }
}
