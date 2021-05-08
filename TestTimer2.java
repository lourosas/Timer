import java.lang.*;
import java.util.*;
import rosas.lou.clock.*;

public class TestTimer2{
   public static void main(String [] args){
      new TestTimer2();
   }

   public TestTimer2(){
      Clock clock = new Clock();
      rosas.lou.clock.Timer timer = new rosas.lou.clock.Timer(clock);
      Thread t = new Thread(clock, "clock");
      t.start();
      timer.start();
      try{
         Thread.sleep(5000);
         timer.stop();
	 Thread.sleep(2000);
	 timer.start();
      }
      catch(InterruptedException ie){}
   }
}
