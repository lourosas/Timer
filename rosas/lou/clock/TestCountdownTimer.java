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
      countdownTimer.start(87090);
   }
}
