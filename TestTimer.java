import java.lang.*;
import java.util.*;
import rosas.lou.clock.Clock;
import rosas.lou.clock.ClockObserver;


public class TestTimer implements ClockObserver{
   public static void main(String [] args){
      new TestTimer();
   }

   public TestTimer(){
      Clock clock = new Clock();
      clock.addObserver(this);
      Thread t = new Thread(clock,"clock");
      t.start();
   }

   /*
   Clock Observer Interface implementation
   */
   public void updateTime(long milliseconds){
      System.out.println(milliseconds);
   }
}
