/********************************************************************
* Copyright (C) 2021 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* This code is distributed in the hope that it will be
* useful, but WITHOUT ANY WARRANTY; without even the implied
* warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.
* If not, see <http://www.gnu.org/licenses/>.
********************************************************************/

package rosas.lou.clock;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.io.File;
import java.awt.event.*;
import javax.swing.*;
import rosas.lou.clock.CountdownTimer;
import rosas.lou.clock.CountdownTimerView;

public class CountdownTimerController implements ActionListener,
KeyListener{
   private CountdownTimer     _timer;
   private CountdownTimerView _view;

   {
      _timer = null;
      _view  = null;
   };

   ////////////////////////Constructors//////////////////////////////
   /**/
   public CountdownTimerController(){}

   /**/
   public CountdownTimerController
   (
      CountdownTimer     ct,
      CountdownTimerView ctv
   ){
      this._timer = ct;
      this._view  = ctv;
   }

   /////////////////////Public Methods////////////////////////////////
   /**/
   public void addModel(CountdownTimer timer){
      this._timer = timer;
   }

   /**/
   public void addView(CountdownTimerView view){
      this._view = view;
   }


   ////////////////////Interface Implementations//////////////////////
   /**/
   public void actionPerformed(ActionEvent e){
      this.handleJButton(e);
      this.handleJMenuItem(e);
      this.handleJTextField(e);
   }

   /**/
   public void keyPressed(KeyEvent k){
      //this.handleJTextField(k);
      this.handleJButton(k);
   }

   /**/
   public void keyReleased(KeyEvent k){
      //this.handleJTextField(k);
      //this.handleJButton(k);
   }

   /**/
   public void keyTyped(KeyEvent k){
      this.handleJTextField(k);
      //this.handleJButton(k);
   }

   /////////////////////Private Methods///////////////////////////////
   /**/
   private void handleJButton(ActionEvent e){
      try{
         JButton button = (JButton)e.getSource();
         if(button.getActionCommand().equals("Start")){
            Hashtable<String, String> times=this._view.requestTimes();
            Enumeration<String> keys = times.keys();
            while(keys.hasMoreElements()){
               String key = (String)keys.nextElement();
               if(key.toUpperCase().contains("HOURS")){
                  this._timer.inputHours(times.get(key));
               }
               else if(key.toUpperCase().contains("MINS")){
                  this._timer.inputMins(times.get(key));
               }
               else if(key.toUpperCase().contains("SECS")){
                  this._timer.inputSecs(times.get(key));
               }
            }
            this._timer.start();
         }
         else if(button.getActionCommand().equals("Stop")){
            this._timer.stop();
         }
         else if(button.getActionCommand().equals("Reset")){
         }
      }
      catch(ClassCastException cce){}
      catch(IllegalArgumentException iae){}
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /**/
   private void handleJButton(KeyEvent k){
      try{
         if(k.getKeyCode() == KeyEvent.VK_ENTER){
            JButton button = ((JButton)k.getSource());
            button.doClick(100);
         }
      }
      catch(ClassCastException cce){}
   }

   /**/
   private void handleJMenuItem(ActionEvent e){
      try{
         JMenuItem item = (JMenuItem)e.getSource();
         if(item.getActionCommand().equals("Quit")){
            System.out.println("Exiting...");
            System.exit(0);
         }
         else if(item.getActionCommand().equals("Start")){
            this._timer.start();
         }
         else if(item.getActionCommand().equals("Stop")){
            this._timer.stop();
         }
         else if(item.getActionCommand().equals("Reset")){
            System.out.println(item.getActionCommand());
         }
      }
      catch(ClassCastException cce){}
      catch(IllegalArgumentException iae){}
      catch(NullPointerException npe){}
   }

   /**/
   private void handleJTextField(ActionEvent e){
      try{
          JTextField jtf = (JTextField)e.getSource();
          Integer.parseInt(jtf.getText());
          String name = jtf.getName();
          /*
          if(name.equals("Set Hours")){
             this._timer.inputHours(jtf.getText());
          }
          else if(name.equals("Set Mins")){
             this._timer.inputMins(jtf.getText());
          }
          else if(name.equals("Set Secs")){
             this._timer.inputSecs(jtf.getText());
         }
         */
         this._view.requestNextFocus(name);
      }
      catch(NumberFormatException nfe){}
      catch(ClassCastException cce){}
      catch(IllegalArgumentException iae){}
      catch(NullPointerException npe){
         npe.printStackTrace();
      }
   }

   /**/
   private void handleJTextField(KeyEvent k){
      try{
         JTextField jtf = (JTextField)k.getComponent();
         char c = k.getKeyChar();
         if(!Character.isDigit(c)){
            k.consume();
         }
	 /*else{
	    System.out.println(jtf.getText());
         }*/
      }
      catch(NumberFormatException nfe){}
      catch(ClassCastException cce){}
      catch(IllegalArgumentException iae){}
      catch(NullPointerException npe){ npe.printStackTrace(); }
   }
}
