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
   private final int DIFFERENCE        = 1000;
   private final int CHANGEDDIFFERENCE = 500;

   private boolean _run;
   private boolean _receive;
   private boolean _lap;

   private int                   _days;
   private long                  _currentTime; //In milliseconds
   private long                  _updatedTime; //In milliseconds
   private Instant               _instantThen;
   private Instant               _instantNow;
   private Instant               _instantLap;
   private Duration              _duration;
   private Duration              _lapDuration;
   private String                _stringTime;
   private LClock                _clock;
   private List<ClockSubscriber> _subscribers;
   private List<Duration>        _lapDurations;
   private Stack<String>         _lapStrings;

   {
      _run            = false;
      _receive        = false;
      _lap            = false;
      _currentTime    = 0;
      _stringTime     = null;
      _clock          = null;
      _instantThen    = null;
      _instantNow     = null;
      _instantLap     = null;
      _duration       = Duration.ZERO;
      _lapDuration    = Duration.ZERO;
      _subscribers    = null;
      _lapDurations   = null;
      _lapStrings     = null;
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
   public void addSubscriber(ClockSubscriber subscriber){
      try{
         this._subscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<ClockSubscriber>();
         this._subscribers.add(subscriber);
      }
      finally{
         this.setTimeValues();
      }
   }

   /*
   */
   public void lap(){
      if(this._lap == true){
         try{
            this._lapDurations.add(this._lapDuration);
         }
         catch(NullPointerException npe){
            this._lapDurations = new LinkedList<Duration>();
            this._lapDurations.add(this._lapDuration);
         }
         System.out.println(this._lapDurations);
      }
      else{
         this._lap = true;
      }
      this._instantLap = this._instantThen;
      this._lapDuration = Duration.ZERO;
   /*
      if(this._instantLap == null){
         this._instantLap = this._instantThen;
      }
      try{
         this._lapDurations.add(Duration.between(this._instantLap,
                                                 this._instantNow));
      }
      catch(NullPointerException npe){
         this._lapDurations = new LinkedList<Duration>();
         this._lapDurations.add(Duration.between(this._instantLap,
                                                 this._instantNow));
      }
      //At some point, add this to the List<Duration>
      //Now, set the next Lap Interval to the current Instant
      this._instantLap = this._instantNow;
   */
   }

   /**/
   public void removeSubscribers(ClockSubscriber subscriber){}

   /*
   */
   public void reset(){
      this.clearAllTimeValues();
   }

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
      this.setRun(false);
   }

   ////////////////Clock Observer Implementation methods//////////////
   /*
   */
   public void updateTime(long milliseconds){
      if(this._run && this._receive){
         this._updatedTime = milliseconds;
      }
   }

   /*
   */
   public void updateTime(Instant instant){
      if(this._receive){
         if(this._instantThen == null){
            this._instantThen = instant;
         }
         if(this._lap){
            this.updateLapTime(instant);
         }
         /*
         if(this._lap && this._instantLap == null){
            this._instantLap = instant;
         }
         */
         if(instant.isAfter(this._instantThen)){
            Duration lapduration = null;
            Duration duration = Duration.between(this._instantThen,
                                               instant);
            /*
            if(this._lap){
               lapduration = Duration.between(this._instantLap,
                                                instant);
            }
            */
            if(this._run && duration.toMillis() >= 1000){
               /*
               if(this._lap){
                  this._lapDuration =
                                  this._lapDuration.plus(lapduration);
                  System.out.println(this._lapDuration.toMillis());
                  this._instantLap = instant;
               }
               */
               this._duration = this._duration.plus(duration);
               this._instantThen = instant;
               this.setTimeValues();
            }
            else if(!this._run){
               this._duration = this._duration.plus(duration);
               this.setReceive(false);
               this._instantThen = null;
               this.setTimeValues();
               /*
               if(this._lap){
                  this._lapDuration =
                                  this._lapDuration.plus(lapduration);
                  this._instantLap = null;
               }
               */
            }
         }
      }
   }

   /////////////////////Private Methods///////////////////////////////
   /*
   */
   private void clearAllTimeValues(){
      this._lap           = false;

      this._currentTime   = 0;
      this._days          = 0;
      this._updatedTime   = 0;
      this._stringTime    = null;
      this._instantThen   = null;
      this._instantNow    = null;
      this._instantLap    = null;
      this._duration      = Duration.ZERO;
      this._lapDuration   = Duration.ZERO;
      this._stringTime    = "";
      this._lapStrings    = null;
      this._lapDurations  = null;
      this.setTimeValues();
      this.setTimeValues(this._lapDuration);
   }

   /*
   */
   private void notifySubscribers(){
      try{
         Iterator<ClockSubscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            (it.next()).update(this._stringTime);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /**/
   private void notifySubscribers(String stringTime, String type){
      try{
         Iterator<ClockSubscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            if(type.toUpperCase().equals("LAP")){
               (it.next()).update(stringTime, "LAP");
            }
            else if(type.toUpperCase().equals("ELAPSED")){
               (it.next()).update(this._stringTime);
            }
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /*
   */
   private void notifySubscribersOfStateChange(){
      try{
         Iterator<ClockSubscriber> it = this._subscribers.iterator();
         while(it.hasNext()){
            (it.next()).update(this._run);
         }
      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /*
   */
   private void updateLapTime(Instant instant){
      if(this._instantLap == null){
         this._instantLap = instant;
      }
      if(instant.isAfter(this._instantLap)){
         Duration lapduration = Duration.between(this._instantLap,
                                                        instant);
         if(this._run && lapduration.toMillis() >= 1000){
            this._lapDuration = this._lapDuration.plus(lapduration);
            this._instantLap = instant;
            //System.out.println(this._lapDuration.toMillis());
            this.setTimeValues(this._lapDuration);
         }
         else if(!this._run){
            this._lapDuration = this._lapDuration.plus(lapduration);
            this._instantLap = null;
            this.setTimeValues(this._lapDuration);
         }
      }
   }

   /*
   */
   private void setRun(boolean run){
      this._run = run;
      this.notifySubscribersOfStateChange();
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
      try{
         cal.setTimeInMillis(this._duration.toMillis());
      }
      catch(NullPointerException npe){
         this._duration = Duration.ZERO;
         cal.setTimeInMillis(this._duration.toMillis());
      }
      try{
         SimpleDateFormat sdf=new SimpleDateFormat("dd HH:mm:ss.SSS");
         sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
         this._stringTime = sdf.format(cal.getTime());
         String[] values_ = this._stringTime.split(" ");
         this._days         = Integer.parseInt(values_[0]) - 1;
         values_            = values_[1].split(":");
         values_            = values_[2].split("\\.");
         if(this._run){
            sdf = new SimpleDateFormat("dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            this._stringTime = sdf.format(cal.getTime());
         }
         values_ = this._stringTime.split(" ");
         this._stringTime = this._days + " " + values_[1];
         this.notifySubscribers();
      }
      catch(NumberFormatException nfe){}
      catch(ArrayIndexOutOfBoundsException oob){}
   }

   /*
   */
   private void setTimeValues(Duration duration){
      Calendar cal = Calendar.getInstance();
      try{
         cal.setTimeInMillis(duration.toMillis());
      }
      catch(NullPointerException npe){
         Duration _duration = Duration.ZERO;
         cal.setTimeInMillis(_duration.toMillis());
      }
      try{
         SimpleDateFormat sdf=new SimpleDateFormat("dd HH:mm:ss.SSS");
         sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
         String time = sdf.format(cal.getTime());
         String[] values = time.split(" ");
         int days = Integer.parseInt(values[0]) - 1;
         if(this._run){
            sdf = new SimpleDateFormat("dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            time = sdf.format(cal.getTime());
         }
         values = time.split(" ");
         time = days + " " + values[1];
         this.notifySubscribers(time, "lap");
      }
      catch(NumberFormatException nfe){ nfe.printStackTrace(); }
      catch(ArrayIndexOutOfBoundsException oob){
         oob.printStackTrace();
      }
   }
}
