//////////////////////////////////////////////////////////////////////
/*
* Copyright (C) 2021 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* CountdownTimer is distributed in the hope that it will be
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

public class CountDownTime{
   private final int MILLIS = 1000;

   private int    _days;
   private int    _hours;
   private int    _minutes;
   private double _seconds;
   private double _totalSeconds;
   private long   _totalMilliSeconds;
   private String _day;
   private String _hour;
   private String _minute;
   private String _second;

   {
      _days              = 0;
      _hours             = 0;
      _minutes           = 0;
      _seconds           = 0.0;
      _totalSeconds      = 0.0;
      _totalMilliSeconds = 0;
      _day               = null;
      _hour              = null;
      _minute            = null;
      _second            = null;
   };

   //////////////////////////Constructors/////////////////////////////
   /**/
   public CountDownTime(){}

   /**/
   public CountDownTime(String hrs, String mins, String secs) throws
   NumberFormatException, NullPointerException{
      try{
         this._hour    = new String(hrs);
         this._minute  = new String(mins);
         this._second  = new String(secs);
         this.setAllTimes();
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /**/
   public CountDownTime(int hour, int minute, double second) throws
   NumberFormatException, NullPointerException{
      try{
         this._hour   = new String("" + hour);
         this._minute = new String("" + minute);
         this._second = new String("" + second);
         this.setAllTimes();
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /**/
   public CountDownTime(double seconds) throws NumberFormatException,
   NullPointerException{
      this._totalSeconds = seconds;
      this.setTotalMilliSeconds();
      try{
         this.setTimeProperly();
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /**/
   public CountDownTime(long milliseconds) throws
   NumberFormatException, NullPointerException{
      this._totalMilliSeconds = milliseconds;
      try{
         this.setTimeProperly();
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /////////////////////////Public Methods////////////////////////////
   /**/
   public CountDownTime add(double seconds){
      return new CountDownTime(this.currentSeconds() + seconds);
   }

   /**/
   public CountDownTime add(long milliseconds){
      long diff = this.currentMilliSeconds() + milliseconds;
      double diffSecs = diff/MILLIS;
      return new CountDownTime(diffSecs);
   }

   /**/
   public double currentSeconds(){
      return this._totalSeconds;
   }

   /**/
   public long currentMilliSeconds(){
      return this._totalMilliSeconds;
   }

   /**/
   public void hour(String hour) throws NumberFormatException{
      try{
         this._hours = Integer.parseInt(hour);
         this.hour(this._hours);
      }
      catch(NumberFormatException nfe){
         throw new NumberFormatException("CountDownTime.hour()");
      }
   }

   /**/
   public void hour(int hour){
      this._hour  = new String("" + hour);
      this._hours = hour;
      this.calculateTotalSeconds();
      this.setTotalMilliSeconds();
   }

   /**/
   public void minute(String min){
      try{
         this._minutes = Integer.parseInt(min);
         this.minute(this._minutes);         
      }
      catch(NumberFormatException nfe){
         throw new NumberFormatException("CountDownTime.minute()");
      }
   }

   /**/
   public void minute(int min){
      this._minute  = new String("" + min);
      this._minutes = min;
      this.calculateTotalSeconds();
      this.setTotalMilliSeconds();
   }

   /**/
   public void second(String second){
      try{
         this._seconds = Double.parseDouble(second);
         this.second(this._seconds);
      }
      catch(NumberFormatException nfe){
         throw new NumberFormatException("CountDownTime.second()");
      }
   }

   /**/
   public void second(double second){
      this._second  = new String("" + second);
      this._seconds = second;
      this.calculateTotalSeconds();
      this.setTotalMilliSeconds();
   }

   /**/
   public CountDownTime subtract(double seconds){
      return new CountDownTime(this.currentSeconds() - seconds);
   }

   /**/
   public CountDownTime subtract(long milliseconds){
      long diff = this.currentMilliSeconds() - milliseconds;
      return new CountDownTime(diff);
   }

   /**/
   public String toString(){
      String countdown = null;
      Calendar cal = Calendar.getInstance();
      cal.setTimeInMillis(this._totalMilliSeconds);
      try{
         SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
         sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
         countdown = sdf.format(cal.getTime());
         String[] values = countdown.split(" ");
         this._days    = Integer.parseInt(values[0]) - 1;
         this._day     = new String(this._days + "");
         String[] hms  = values[1].split(":");
         this._hours   = Integer.parseInt(hms[0]);
         this._hour    = new String(this._hours + "");
         this._minutes = Integer.parseInt(hms[1]);
         this._minute  = new String(this._minutes + "");
         this._seconds = Integer.parseInt(hms[2]);
         this._second  = new String(this._seconds + "");
         countdown     = this._day + " " + values[1];
      }
      catch(NumberFormatException nfe){
         //Temporary for now...print this out for the time being
         nfe.printStackTrace();
         countdown = null;
      }
      catch(ArrayIndexOutOfBoundsException oob){
         //Temporary for now...print this out for the time being
         oob.printStackTrace();
         countdown = null;
      }
      return countdown;
   }

   ///////////////////////Private Methods/////////////////////////////
   /**/
   private void calculateTotalSeconds(){
      final int HOUR_TO_SECONDS = 3600;
      final int MIN_TO_SECONDS  =   60;

      this._totalSeconds =  this._hours * HOUR_TO_SECONDS;
      this._totalSeconds += this._minutes * MIN_TO_SECONDS;
      this._totalSeconds += this._seconds;
   }

   /**/
   private void setAllTimes()
   throws NumberFormatException, NullPointerException{
      try{
         this._hours   = Integer.parseInt(this._hour);
         this._minutes = Integer.parseInt(this._minute);
         this._seconds = Double.parseDouble(this._second);
         this.calculateTotalSeconds();
         this.setTotalMilliSeconds();
         if(this._hours>=24||this._minutes>= 60||this._seconds>=60){
            this.setTimeProperly();
         }
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /*
   Lots to say on this....will need to post many a words to help
   understand What The Fuck is actually going on with this!
   */
   private void setTimeProperly() throws NumberFormatException,
   NullPointerException{
      String currentTime = this.toString();
      if(currentTime == null){
         throw new NullPointerException("Not a String!");
      }
      try{
         String[] values = currentTime.split(" ");
         values = values[1].split(":");
         this._hour    = values[0];
         this._minute  = values[1];
         this._second  = values[2];
         this._hours   = Integer.parseInt(this._hour);
         this._minutes = Integer.parseInt(this._minute);
         this._seconds = Double.parseDouble(this._second);
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /**/
   private void setTotalMilliSeconds(){
      double temp = this._totalSeconds * MILLIS;
      Double tempDouble = Double.valueOf(temp);
      this._totalMilliSeconds = tempDouble.longValue();
   }
}
