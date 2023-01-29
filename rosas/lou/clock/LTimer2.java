//////////////////////////////////////////////////////////////////////
package rosas.lou.clock;

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
//import java.awt.*;
import java.time.temporal.*;
import java.time.Instant;
import java.time.Duration;
import rosas.lou.clock.*;

public class LTimer2 implements ActionListener{
   private boolean run               = false;
   private short days                = -1;
   private int hours                 = -1;
   private int minutes               = -1;
   private int seconds               = -1;
   private int milliseconds          = -1;
   private Instant start             = null;
   private Instant aLap              = null;
   private Duration current          = null;
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
      this.current = Duration.ZERO;
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
      if(this.run == true){
         if(aLap != null){
            //Normally, do try{} catch(){} exception handling, but
            //time is of the essence
            Duration d = Duration.between(this.aLap,Instant.now());
            if(this.lapDurations == null){
               this.lapDurations = new LinkedList<Duration>();
            }
            this.lapDurations.add(d);
            Iterator<ClockSubscriber> it = this.observers.iterator();
            while(it.hasNext()){
               ((ClockSubscriber)it.next()).update(this.lapDurations);
            }
         }
         aLap = Instant.now();
      }
   }

   /*
    * */
   public void reset(){
      if(this.run == false){
         try{
            this.start = null;
            this.aLap  = null;
            this.run   = false;
            this.current = Duration.ZERO;
            this.lapDurations.clear();
            this.lapDurations = null;
         }
         catch(NullPointerException npe){}
         finally{
            Iterator<ClockSubscriber> it = this.observers.iterator();
            while(it.hasNext()){
               ClockSubscriber cs = (ClockSubscriber)it.next();
               //cs.update(this.current, this.run);
               //cs.update(this.current);
               //cs.update(this.run);
               //cs.update(State.RESET);
               cs.update(this.current, State.RESET);
            }
         }
      }
   }

   /*
    * */
   public void start(){
      //Going to have to come up with a better way to do this
      //evantually
      this.timer.start();
      //Probably will not need this conditional...will need to think
      //about this a little--this should address the Start,Stop...
      if(this.start == null){
         this.start = Instant.now();
      }
      this.run = true;
      Iterator<ClockSubscriber> it = this.observers.iterator();
      while(it.hasNext()){
         ClockSubscriber cs = (ClockSubscriber)it.next();
         //cs.update(this.run);
         //cs.update(this.run);
         cs.update(State.START);
      }
   }

   /*
    * */
   public void stop(){
      this.run = false;
      Duration d = Duration.between(this.start, Instant.now());
      this.current = this.current.plus(d);
      this.timer.stop();
      Iterator<ClockSubscriber> it = this.observers.iterator();
      while(it.hasNext()){
         ClockSubscriber cs = (ClockSubscriber)it.next();
         //cs.update(this.current, this.run);
         //cs.update(State.STOP);
         cs.update(this.current, State.STOP);
      }
      this.start = null;
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
            cs.update(d.plus(this.current));
            if(this.aLap != null){
               cs.update(l, State.LAP);
            }
         }
      }
      catch(ClassCastException cce){}
   }
}
//////////////////////////////////////////////////////////////////////
