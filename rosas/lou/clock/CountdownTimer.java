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

   private CountDownTime _currentTime;
   private CountDownTime _inputTime;
   private LClock        _clock;
   private boolean       _run;

   {
      _currentTime      = null;
      _inputTime        = null;
      _clock            = null;
      _run              = false;
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
   public void inputTime(String hrs, String mins, String secs){
      this._inputTime = new CountDownTime(hrs, mins, secs);
      System.out.println(this._inputTime);
   }

   /**/
   public void inputTime(int hour, int minute, double second){
      this._inputTime = new CountDownTime(hour,minute,second);
   }

   /**/
   public void inputTime(double secs){
      this._inputTime = new CountDownTime(secs);
      System.out.println(this._inputTime);
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
      if(this._inputTime != null){
         this._currentTime = this._inputTime;
         this.setRun(true);
      }
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

   ////////////////Clock Observer Implementation methods//////////////
   /**/
   public void updateTime(long millis){}

   /**/
   public void updateTime(Instant instant){
      if(this._run){
      }
   }

   ////////////////////////Private Methods////////////////////////////
   /**/
   private void setRun(boolean toRun){
      this._run = toRun;
   }
}
