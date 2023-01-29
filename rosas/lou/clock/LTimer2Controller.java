//////////////////////////////////////////////////////////////////////
package rosas.lou.clock;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LTimer2Controller implements ActionListener,
KeyListener, ItemListener{
   private LTimer2              _timer = null;
   private ClockSubscriber _subscriber = null;

   /*
   */
   public LTimer2Controller(LTimer2 timer){
      this._timer      = timer;
   }

   public void addClockSubscriber(ClockSubscriber subscriber){
      this._subscriber = subscriber;
      this._timer.addSubscriber(this._subscriber);
   }

   ////////////////////Interface Implementation///////////////////////
   /*
    * */
   public void actionPerformed(ActionEvent e){
      try{
         JButton button = (JButton)e.getSource();
         String command = button.getActionCommand().toUpperCase();
         if(command.equals("START")){
            this._timer.start();
         }
         else if(command.equals("STOP")){
            this._timer.stop();
         }
         else if(command.equals("RESET")){
            this._timer.reset();
         }
         else if(command.equals("LAP")){
            this._timer.lap();
         }
      }
      catch(ClassCastException cce){}
   }

   /*
    * */
   public void keyPressed(KeyEvent ke){}

   /*
    * */
   public void keyReleased(KeyEvent ke){}

   /*
    * */
   public void keyTyped(KeyEvent ke){}

   /*
    * */
   public void itemStateChanged(ItemEvent ie){}
}
//////////////////////////////////////////////////////////////////////
