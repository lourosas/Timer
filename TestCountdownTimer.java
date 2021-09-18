import java.lang.*;
import java.util.*;
import rosas.lou.clock.*;
import myclasses.*;

public class TestCountdownTimer{
   public static void main(String [] args){
      new TestCountdownTimer();
   }

   public TestCountdownTimer(){
      LClock clock = new LClock();
      CountdownTimer countdownTimer = new CountdownTimer(clock);
      Thread t = new Thread(clock, "clock");
      t.start();
      countdownTimer.start(400);
      try{ Thread.sleep(20000); }
      catch(InterruptedException ie){}
      countdownTimer.stop();
      try{ Thread.sleep(14000); }
      catch(InterruptedException ie){}
      countdownTimer.start();
      try{ t.join(); }
      catch(InterruptedException ie){}
   }
}
