//////////////////////////////////////////////////////////////////////
/*
*/
package rosas.lou.clock;

import java.lang.*;
import java.util.*;

public class ClockNotifier implements Runnable{
   private long                _currentTime;
   private boolean             _quit;
   private boolean             _triggered;
   private List<ClockObserver> _observers;

   {
      _currentTime = 0;
      _quit        = false;
      _triggered   = false;
      _observers   = null;
   };

   /*
   */
   public ClockNotifier(){}

   /**/
   public void addObserver(ClockObserver observer){
      try{
         this._observers.add(observer);
      }
      catch(NullPointerException npe){
         this._observers = new LinkedList<ClockObserver>();
         this._observers.add(observer);
      }
   }

   /*
   */
   public void quit(boolean quit){
      this._quit = quit;
      if(this._quit){
         this.trigger(true);
      }
   }

   /*
   */
   public void setTime(long time){
      this._currentTime = time;
   }

   /*
   */
   public synchronized void trigger(boolean trigger){
      this._triggered = trigger;
      if(this._triggered){
         this.notify();
      }
   }

   //////////////////Runnable Interface Implementation///////////////
   /*
   */
   public void run(){
      while(!this._quit){
         try{
            synchronized(this){
               while(!this._triggered){
                  this.wait();
               }
            }
            if(!this._quit){
               Iterator it = this._observers.iterator();
               while(it.hasNext()){
                  ClockObserver ob = (ClockObserver)it.next();
                  ob.updateTime(this._currentTime);
               }
            }
            this.trigger(false);
         }
         catch(InterruptedException ie){
            ie.printStackTrace();
         }
      }
   }
}
