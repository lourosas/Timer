//////////////////////////////////////////////////////////////////////
/*
* Copyright (C) 2021 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* LCountdownTimer is distributed in the hope that it will be
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
import java.io.*;
import java.time.Instant;
import java.time.Duration;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import rosas.lou.clock.*;

public class CountdownTimer implements ClockObserver{
   private final int INCREMENT       = 1;
   private final int MILLISINCREMENT = 1000;
   private enum  State { RESET, RUN, STOP }

   private CountDownTime         _currentTime;
   private CountDownTime         _inputTime;
   private LClock                _clock;
   private Instant               _instantThen;
   private boolean               _run;
   private List<ClockSubscriber> _subscribers;
   private State                 _state;
   {
      _currentTime      = null;
      _inputTime        = null;
      _clock            = null;
      _instantThen      = null;
      _run              = false;
      _subscribers      = null;
      _state            = State.RESET;
   };

   ////////////////////////Constructors///////////////////////////////
   /**/
   public CountdownTimer(){}

   /**/
   public CountdownTimer(LClock clock){
      this.setClock(clock);
   }


   /////////////////////////Public Methods////////////////////////////
   /**/
   public void addSubscriber(ClockSubscriber subscriber){
      try{
         this._subscribers.add(subscriber);
      }
      catch(NullPointerException npe){
         this._subscribers = new LinkedList<ClockSubscriber>();
         this._subscribers.add(subscriber);
      }
   }

   /**/
   public void inputHours(String hrs){
      final int ZERO = 0;
      if(this._state == State.RESET){
         try{
            this._inputTime.hour(hrs);
         }
         catch(NullPointerException npe){
            this._inputTime = new CountDownTime();
            try{
               this._inputTime.hour(hrs);
            }
            catch(NumberFormatException nfe){
               this._inputTime.hour(ZERO);
            }
         }
         catch(NumberFormatException nfe){
            this._inputTime.hour(ZERO);
         }
      }
   }

   /**/
   public void inputMins(String mins){
      final int ZERO = 0;
      if(this._state == State.RESET){
         try{
            this._inputTime.minute(mins);
         }
         catch(NullPointerException npe){
            this._inputTime = new CountDownTime();
            try{
               this._inputTime.minute(mins);
            }
            catch(NumberFormatException nfe){
               this._inputTime.minute(ZERO);
            }
         }
         catch(NumberFormatException nfe){
            this._inputTime.minute(ZERO);
         }
      }
   }

   /**/
   public void inputSecs(String secs){
      /*This works easier than the above code, but I will keep the
      above code as an example of Exception Handling*/
      final double ZERO = 0.;
      if(this._state == State.RESET){
         if(this._inputTime == null){
            this._inputTime = new CountDownTime();
         }
         try{
            this._inputTime.second(secs);
         }
         catch(NumberFormatException nfe){
            this._inputTime.second(ZERO);
         }
      }
   }

   /**/
   public void inputTime(String hrs, String mins, String secs){
      if(this._state == State.RESET){
         this._inputTime = new CountDownTime(hrs, mins, secs);
      }
   }

   /**/
   public void inputTime(int hour, int minute, double second){
      if(this._state == State.RESET){
         this._inputTime = new CountDownTime(hour,minute,second);
      }
   }

   /**/
   public void inputTime(double secs){
      if(this._state == State.RESET){
         this._inputTime = new CountDownTime(secs);
      }
   }

   /**/
   public void setClock(LClock clock){
      if(this._clock != null){
         this._clock.removeObserver(this);
      }
      this._clock = clock;
      this._clock.addObserver(this);
   }

   /**/
   public void start(){
      if(this._inputTime == null){
         this._inputTime = new CountDownTime();
      }
      this._currentTime = this._inputTime;
      Iterator<ClockSubscriber> it = this._subscribers.iterator();
      while(it.hasNext()){
         ClockSubscriber sub = it.next();
         sub.update(this._inputTime.toString());
         sub.update(true);
      }
      this.setRun(true);
      /*
      try{
         if(this._inputTime == null){
            this.inputTime(this._hours, this._minutes, this._seconds);
         }
         this._currentTime = this._inputTime;
         this.setRun(true);

      }
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
      catch(NumberFormatException nfe){
         nfe.printStackTrace();
      }
      if(this._inputTime != null){
         this._currentTime = this._inputTime;
         this.setRun(true);
      }
      else{
         this.inputTime(this._hours, this._minutes, this._seconds);
         this._currentTime = this._inputTime;
         this.setRun(true);
      }
      */
   }

   /**/
   public void start(String hrs, String mins, String secs){
      this.inputTime(hrs,mins,secs);
      this.start();
   }

   /**/
   public void start(int hour, int minute, double second){
      this.inputTime(hour,minute,second);
      this.start();
   }

   /**/
   public void start(double secs){
      this.inputTime(secs);
      this.start();
   }

   /**/
   public void stop(){
      Iterator<ClockSubscriber> it = this._subscribers.iterator();
      this.setRun(false);
      while(it.hasNext()){
         ClockSubscriber sub = it.next();
         sub.update(false);
      }
      this._inputTime   = this._currentTime;
      this._instantThen = null;
   }

   ////////////////Clock Observer Implementation methods//////////////
   /**/
   public void updateTime(long millis){}

   /**/
   public void updateTime(Instant instant){
      if(this._run){
         if(this._instantThen == null){
            this._instantThen = instant;
         }
         Iterator<ClockSubscriber> it = this._subscribers.iterator();
         Duration dur = Duration.between(this._instantThen, instant);
         if(dur.toMillis() >= 1000 &&
            this._currentTime.currentMilliSeconds() > 0){
            System.out.println(this._instantThen);
            System.out.println(instant);
            System.out.println(this._currentTime.currentMilliSeconds());
            this._currentTime =
                          this._currentTime.subtract(dur.toMillis());
            if(this._currentTime.currentMilliSeconds() >= 0){
               System.out.println(this._currentTime.currentMilliSeconds());
               System.out.println(this._currentTime + "\n");
               while(it.hasNext()){
                  ClockSubscriber sub = it.next();
                  sub.update(this._currentTime.toString());
               }
            }
            else{
               this.stop();
               //this.reset();
            }
            this._instantThen = instant;
         }
      }
   }

   ////////////////////////Private Methods////////////////////////////
   /**/
   private void setRun(boolean toRun){
      this._run = toRun;
      this._state = (this._run) ? State.RUN : State.STOP;
   }
}
