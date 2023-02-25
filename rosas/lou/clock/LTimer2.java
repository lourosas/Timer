//////////////////////////////////////////////////////////////////////
/*
LTimer2 Apoplication
Copyright (C) 2023 Lou Rosas
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
//////////////////////////////////////////////////////////////////////
package rosas.lou.clock;

import java.lang.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
//import java.awt.*;
import java.time.temporal.*;
import java.time.Instant;
import java.time.Duration;
import rosas.lou.clock.*;

public class LTimer2 implements ActionListener{
   private State state               = State.STOP;
   private boolean run               = false;
   private short days                = -1;
   private int hours                 = -1;
   private int minutes               = -1;
   private int seconds               = -1;
   private int milliseconds          = -1;
   private Instant start             = null;
   private Instant aLap              = null;
   private Duration current          = null;
   private Duration lSave            = null;
   private javax.swing.Timer timer   = null;

   private List<ClockSubscriber> observers = null;
   private List<Duration>     lapDurations = null;

   /*
    * */
   public LTimer2(){
      this.timer = new javax.swing.Timer(0,this);
      this.timer.setCoalesce(true);
      //a delay of 10 millisecs appears to work well
      this.timer.setDelay(10);
      this.state   = State.STOP;
      this.current = Duration.ZERO;
      this.lSave   = Duration.ZERO;
   }

   /*
    * */
   public void addSubscriber(ClockSubscriber subscriber){
      try{
         this.observers.add(subscriber);
      }
      catch(NullPointerException npe){
         this.observers = new LinkedList<ClockSubscriber>();
         this.observers.add(subscriber);
      }
   }

   /*
    * */
   public void lap(){
      if(this.run == true){ //this.state == State.RUN
         if(aLap != null){
            //Normally, do try{} catch(){} exception handling, but
            //time is of the essence
            Duration d = Duration.between(this.aLap,Instant.now());
            if(this.lapDurations == null){
               this.lapDurations = new LinkedList<Duration>();
            }
            //I need to figure out how to get this to work properly
            this.lapDurations.add(d.plus(this.lSave));
            Iterator<ClockSubscriber> it = this.observers.iterator();
            while(it.hasNext()){
               ((ClockSubscriber)it.next()).updateLaps(
                                                   this.lapDurations);
            }
            this.lSave = Duration.ZERO;
         }
         aLap = Instant.now();
      }
   }

   /*
    * */
   public void reset(){
      if(this.run == false){ //this.state == State.STOP
         try{
            this.start = null;
            this.aLap  = null;
            this.run   = false;
            this.current = Duration.ZERO;
            this.lSave   = Duration.ZERO;
            this.lapDurations.clear();
            this.lapDurations = null;
         }
         catch(NullPointerException npe){}
         finally{
            Iterator<ClockSubscriber> it = this.observers.iterator();
            while(it.hasNext()){
               ClockSubscriber cs = (ClockSubscriber)it.next();
               cs.updateReset();
            }
         }
      }
   }

   /*
    * */
   public void save(File file){
      PrintWriter outs = null;
      try{
         FileWriter fw = new FileWriter(file,true);
         outs          = new PrintWriter(fw,true);
         long millis = this.current.toMillis();
         if(millis > 0){
            outs.println(this.convertToFullTimeString(millis));
         }
         millis = this.lSave.toMillis();
         if(millis > 0){
            outs.println(this.convertToFullTimeString(millis));
         }
         if(this.lapDurations != null){
            Iterator<Duration> it = this.lapDurations.iterator();
            while(it.hasNext()){
               Duration d = (Duration)it.next();
               long time = d.toMillis();
               if(time > 0){
                  outs.println(this.convertToFullTimeString(time));
               }
            }
         }
         outs.println();
      }
      catch(IOException ioe){
         String type = "File I/O Error";
         String message = "Could not save to: " + file.toString();
         Iterator<ClockSubscriber> it = this.observers.iterator();
         while(it.hasNext()){
            ClockSubscriber cs = (ClockSubscriber)it.next();
            cs.error(type, message);
         }
      }
      finally{
         try{
            outs.close();
         }
         catch(Exception e){}
      }
   }

   /*
    * Triggers the Transition
    * Stop-->Run
    * */
   public void start(){
      //Going to have to come up with a better way to do this
      //evantually
      this.timer.start();
      this.state = State.RUN;
      //Probably will not need this conditional...will need to think
      //about this a little--this should address the Start,Stop...
      if(this.start == null){
         this.start = Instant.now();
         if(this.aLap != null){
            this.aLap = this.start;
         }
      }
      this.run = true;
      Iterator<ClockSubscriber> it = this.observers.iterator();
      while(it.hasNext()){
         ClockSubscriber cs = (ClockSubscriber)it.next();
         cs.updateRun();
      }
   }

   /*
    * */
   public void stop(){
      this.run   = false;
      this.state = State.STOP;
      Instant now = Instant.now();
      Duration d = Duration.between(this.start, now);
      Duration l = null;
      if(this.aLap != null){
         l = Duration.between(this.aLap,now);
      }
      this.current = this.current.plus(d);
      this.timer.stop();
      Iterator<ClockSubscriber> it = this.observers.iterator();
      while(it.hasNext()){
         ClockSubscriber cs = (ClockSubscriber)it.next();
         //cs.update(this.current, this.run);
         //cs.update(State.STOP);
         cs.updateStop();
         //The rest of this is TBD!!!
         //cs.update(this.current, this.state, "ELAPSED");
         cs.updateElapsed(this.current);
         if(this.aLap != null){
            this.lSave = this.lSave.plus(l);
            cs.updateLap(this.lSave);
         }
      }
      this.start = null;
   }

   ////////////////////////Private Methods////////////////////////////
   /*
    * */
   private String convertToFullTimeString(long millis){
      int  millisecs = (int)millis%1000;
      long totalSecs = millis/1000;
      int  secs      = (int)totalSecs%60;
      long totalMins = totalSecs/60;
      int  mins      = (int)totalMins%60;
      long totalHours= totalMins/60;
      int  hours     = (int)totalHours%24;
      int  days      = (int)totalHours/24;
      return String.format("%d %02d:%02d:%02d.%03d",
                                      days,hours,mins,secs,millisecs); 
   }

   ///////////////////////Interface Implementation////////////////////
   /*
    * */
   public void actionPerformed(ActionEvent e){
      try{
         javax.swing.Timer t = (javax.swing.Timer)e.getSource();
         Instant now = Instant.now();
         Duration d = Duration.between(this.start,now);
         Duration l = null;
         if(this.aLap != null){
            l = Duration.between(this.aLap,now);
         }
         Iterator<ClockSubscriber> it = this.observers.iterator();
         while(it.hasNext()){
            //Will need to assess how handle the Start, Stop, Reset
            ClockSubscriber cs = (ClockSubscriber)it.next();
            cs.updateElapsed(d.plus(this.current));
            if(this.aLap != null){
               //this DEFINITELY NEEDS TO CHANGE!!!
               //cs.update(l,this.state,"LAP");
               cs.updateLap(l.plus(this.lSave));
            }
         }
      }
      catch(ClassCastException cce){}
   }
}
//////////////////////////////////////////////////////////////////////
