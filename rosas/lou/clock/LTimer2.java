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
   private Duration current          = null;
   private javax.swing.Timer timer   = null;

   private List<ClockSubscriber> observers = null;

   /*
    * */
   public LTimer2(){
      this.timer = new javax.swing.Timer(0,this);
      this.timer.setDelay(10);//this will change
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
   public void lap(){}

   /*
    * */
   public void reset(){}

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
         ((ClockSubscriber)it.next()).update(this.run);
      }
   }

   /*
    * */
   public void stop(){
   }

   ///////////////////////Interface Implementation////////////////////
   /*
    * */
   public void actionPerformed(ActionEvent e){
      try{
         javax.swing.Timer t = (javax.swing.Timer)e.getSource();
         //Instant now = Instant.now();
         Duration d = Duration.between(this.start,Instant.now());
         Iterator<ClockSubscriber> it = this.observers.iterator();
         while(it.hasNext()){
            //Will need to assess how handle the Start, Stop, Reset
            ((ClockSubscriber)it.next()).update(d.plus(this.current));
         }
      }
      catch(ClassCastException cce){}
   }
}
//////////////////////////////////////////////////////////////////////
