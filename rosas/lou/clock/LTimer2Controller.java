//////////////////////////////////////////////////////////////////////
/*
LTimer2 Apoplication
Copyright (C) 2023 Lou Rosas
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
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
   private Frame           _frame      = null;

   /*
   */
   public LTimer2Controller(LTimer2 timer){
      this._timer      = timer;
   }

   /*
    * */
   public void addClockSubscriber(ClockSubscriber subscriber){
      this._subscriber = subscriber;
      this._timer.addSubscriber(this._subscriber);
   }

   /*
    * */
   public void addFrame(Frame frame){
      this._frame = frame;
   }

   /*
    * */
   private void handleJButton(JButton button){
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

   /*
    * */
   private void handleJMenuItem(JMenuItem item){
      String command = item.getActionCommand().toUpperCase();
      if(command.equals("MENUITEMSTART")){
         this._timer.start();
      }
      else if(command.equals("MENUITEMSTOP")){
         this._timer.stop();
      }
      else if(command.equals("MENUITEMLAP")){
         this._timer.lap();
      }
      else if(command.equals("MENUITEMRESET")){
         this._timer.reset();
      }
      else if(command.equals("MENUITEMSAVE")){
         try{
            JFileChooser jfc = new JFileChooser();
            FileNameExtensionFilter filter =
                            new FileNameExtensionFilter("text","txt");
            jfc.setFileFilter(filter);
            //will need to pass in the frame, eventually...
            int open = jfc.showSaveDialog(this._frame);
            if(open == JFileChooser.APPROVE_OPTION){
               this._timer.save(jfc.getSelectedFile());
            }
         }
         catch(HeadlessException he){}
      }
      else if(command.equals("MENUITEMHELP")){
         this._subscriber.update("help");
      }
      else if(command.equals("MENUITEMGNUINFO")){
         this._subscriber.update("gnuinfo");
      }
      else if(command.equals("MENUITEMABOUT")){
         this._subscriber.update("about");
      }
      else if(command.equals("MENUITEMQUIT")){
         System.exit(0);  //Just quit
      }
   }

   ////////////////////Interface Implementation///////////////////////
   /*
    * */
   public void actionPerformed(ActionEvent e){
      try{
         JButton button = (JButton)e.getSource();
         this.handleJButton(button);
      }
      catch(ClassCastException cce){}
      try{
         JMenuItem item = (JMenuItem)e.getSource();
         this.handleJMenuItem(item);
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
