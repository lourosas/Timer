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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import rosas.lou.clock.*;

public class Timer implements ClockObserver{
   private boolean _run;
   private boolean _receive;

   private short _days;
   private short _hours;
   private short _minutes;
   private short _seconds;
   private long  _milliseconds;
   private long  _differenceTime;
   private long  _currentTime; //In milliseconds
   private long  _updatedTime; //In milliseconds
   private Clock _clock;

   {
      _run            = false;
      _receive        = false;
      _days           = 0;
      _hours          = 0;
      _minutes        = 0;
      _seconds        = 0;
      _milliseconds   = 0;
      _differenceTime = 0;
      _currentTime    = 0;
      _updatedTime    = 0;
      _clock          = null;
   };

   //////////////////////////Constructors/////////////////////////////
   /*
   */
   public Timer(){}

   /*
   */
   public Timer(Clock clock){
      this.setClock(clock);
   }

   //////////////////////////Public Methods///////////////////////////
   /*
   */
   public void setClock(Clock clock){
      //If already connected to a clock, disconnect from it TBD
      if(this._clock != null){
         this._clock.removeObserver(this);
      }
      this._clock = clock;
      this._clock.addObserver(this);
   }

   /*
   */
   public void start(){
      this.setRun(true);
      this.setReceive(true);
   }

   /*
   */
   public void stop(){
      this.setRun(false);
      this.setReceive(false);
   }

   ////////////////Clock Observer Implementation methods//////////////
   /*
   */
   public void updateTime(long milliseconds){
      if(this._run && this._receive){
         this._milliseconds = milliseconds;
         System.out.println(this._milliseconds);
      }
   }

   /////////////////////Private Methods///////////////////////////////
   /*
   */
   private void calculateTime(long time){}

   /*
   */
   private void notifyObservers(){}

   /*
   */
   private void setRun(boolean run){
      this._run = run;
   }

   /*
   */
   private void setReceive(boolean receive){
      this._receive = receive;
   }

   /*
   */
   private void setTime(long time){}

   /*
   */
   private void setTimeValues(){}
}
