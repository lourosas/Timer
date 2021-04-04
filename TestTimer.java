import java.lang.*;
import java.util.*;


public class TestTimer{
   public static void main(String [] args){
      new TestTimer();
   }

   public TestTimer(){
      boolean run = true;
      long time1 = Calendar.getInstance().getTimeInMillis();
      System.out.println(time1);
      while(run){
         try{ Thread.sleep(0,200); }
         catch(InterruptedException ie){ run = false;}
         long time2 = Calendar.getInstance().getTimeInMillis();
         if((time2%1000) == (time1%1000)){
            System.out.println(time2);
            time1 = time2;
         }
      }
   }
}
