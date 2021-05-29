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
import java.time.Duration;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import rosas.lou.clock.*;

public class LTimer implements ClockObserver{
   private final int DIFFERENCE = 1000;

   private boolean _run;
   private boolean _receive;

   private int      _days;
   private int      _hours;
   private int      _minutes;
   private int      _seconds;
   private int      _milliseconds;
   private long     _differenceTime;
   private long     _currentTime; //In milliseconds
   private long     _updatedTime; //In milliseconds
   private long     _stopTime;
   private Instant  _instantThen;
   private Instant  _instantNow;
   private Duration _duration;
   private String   _stringTime;
   private LClock   _clock;

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
      _stopTime       = 0;
      _stringTime     = null;
      _clock          = null;
      _instantThen    = null;
      _instantNow     = null;
      _duration       = null;
   };

   //////////////////////////Constructors/////////////////////////////
   /*
   */
   public LTimer(){}

   /*
   */
   public LTimer(LClock clock){
      this.setClock(clock);
   }

   //////////////////////////Public Methods///////////////////////////
   /*
   */
   public void setClock(LClock clock){
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
      //this._currentTime = this._clock.getTimeInMillis();
      //this.setTimeValues();
      //Just try to notify the Observers-->see where that goes
      this.setRun(true);
      this.setReceive(true);
   }

   /*
   */
   public void stop(){
      //This will need to change but going to do a "proof of concept"
      //Going to try this in this way for the time being
      this._stopTime = this._differenceTime;
      this.setRun(false);
      this.setReceive(false);
   }

   ////////////////Clock Observer Implementation methods//////////////
   /*
   */
   public void updateTime(long milliseconds){
      if(this._run && this._receive){
         this._updatedTime = milliseconds;
         this.calculateTime();
      }
   }

   /*
   */
   public void updateTime(Instant instant){
      if(this._run && this._receive){
         this._instantNow = instant;
         if(this._instantThen == null){
            this._instantThen = instant;
         }
         this.calculateTime();
      }
      /*
      else{
         this._instantThen = instant;
      }
      */
   }

   /////////////////////Private Methods///////////////////////////////
   /*
   */
   private void calculateTime(){
      /*
      this._differenceTime =
               this._updatedTime - this._currentTime + this._stopTime;
      if(this._differenceTime >= DIFFERENCE){
         //this._currentTime = this._updatedTime;
         this.setTimeValues();
      }
      */
      try{
         this._duration = Duration.between(this._instantThen,
                                           this._instantNow);
         //Now, need to figure out how to ONLY display Second time
         //Differences
         if(this._duration.toMillis()%1000 == 0){
            this.setTimeValues();
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

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
   TODO--need to alert the Subscribers...
   */
   private void setTimeValues(){
      Calendar cal = Calendar.getInstance();
      //cal.setTimeInMillis(this._differenceTime);
      cal.setTimeInMillis(this._duration.toMillis());
      SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss.SSS");
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
      this._stringTime = sdf.format(cal.getTime());
      String[] values_ = this._stringTime.split(" ");
      try{
         this._days         = Integer.parseInt(values_[0]) - 1;
         values_            = values_[1].split(":");
         this._hours        = Integer.parseInt(values_[0]);
         this._minutes      = Integer.parseInt(values_[1]);
         values_            = values_[2].split("\\.");
         this._seconds      = Integer.parseInt(values_[0]);
         this._milliseconds = Integer.parseInt(values_[1]);
         if(this._run){
            sdf = new SimpleDateFormat("dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            this._stringTime = sdf.format(cal.getTime());
         }
         values_ = this._stringTime.split(" ");
         this._stringTime = this._days + " " + values_[1];
         System.out.println(this._stringTime);
      }
      catch(NumberFormatException nfe){}
      catch(ArrayIndexOutOfBoundsException oob){}
   }
}
