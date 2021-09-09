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

   private int    _hours;
   private int    _minutes;
   private double _seconds;
   private double _totalSeconds;
   private long   _totalMilliSeconds;
   private String _hour;
   private String _minute;
   private String _second;

   {
      _hours             = 0;
      _minutes           = 0;
      _seconds           = 0.0;
      _totalSeconds      = 0.0;
      _totalMilliSeconds = 0;
      _hour              = null;
      _minute            = null;
      _second            = null;
   };

   //////////////////////////Constructors/////////////////////////////
   /**/
   public CountDownTime(String hrs, String mins, String secs) throws
   NumberFormatException, NullPointerException{
      try{
         this._hour    = new String(hrs);
         this._minute  = new String(mins);
         this._second  = new String(secs);
         this.setAllTimes(hrs,mins,secs);
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }

   /**/
   public CountDownTime(int hour, int minute, double second){}

   /**/
   public CountDownTime(double seconds){}

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
   private void setAllTimes(String hrs, String mins, String secs)
   throws NumberFormatException, NullPointerException{
      try{
         this._hours   = Integer.parseInt(hrs);
         this._minutes = Integer.parseInt(mins);
         this._seconds = Double.parseDouble(secs);
         this.calculateTotalSeconds();
         double temp = this._totalSeconds * MILLIS;
	 Double tempDouble = Double.valueOf(temp);
         //this._totalMilliSeconds = this._totalSeconds * MILLIS;
      }
      catch(NumberFormatException nfe){ throw nfe; }
      catch(NullPointerException npe){ throw npe; }
   }
}
