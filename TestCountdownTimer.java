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
   }
}
