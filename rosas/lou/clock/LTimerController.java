/********************************************************************
* Copyright (C) 2021 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* PaceCalculator is distributed in the hope that it will be
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
import rosas.lou.clock.LTimer;
import rosas.lou.clock.LTimerView;

public class LTimerController implements ActionListener,
KeyListener{
   private LTimer      _lTimer;
   private LTimerView  _lTimerView;

   {
      _lTimer      = null;
      _lTimerView  = null;
   };

   /////////////////////////Public Methods////////////////////////////
   /**/
   public LTimerController(){}

   /**/
   public LTimerController(LTimer timer, LTimerView ltv){
      this._lTimer = timer;
      this._lTimerView = ltv;
      this._lTimer.addSubscriber(this._lTimerView);
   }

   /**/
   public void addView(LTimerView view){
      this._lTimerView = view;
   }

   /**/
   public void addModel(LTimer model){
      this._lTimer = model;
   }

   /////////////////////Interface Implementaions//////////////////////
   /**/
   public void actionPerformed(ActionEvent e){
      this.handleJButton(e);
      this.handleJMenuItem(e);
   }

   /**/
   public void keyPressed(KeyEvent k){
      try{
         int code = k.getKeyCode();
         if(code == KeyEvent.VK_ENTER){
            JButton button = ((JButton)k.getSource());
            button.doClick(100);
         }
      }
      catch(ClassCastException cce){}
   }

   /**/
   public void keyReleased(KeyEvent k){}

   /**/
   public void keyTyped(KeyEvent k){}

   /**/
   public void save(File file){
      this._lTimer.save(file);
   }

   //////////////////////////Private Methods//////////////////////////
   /**/
   private void handleJButton(ActionEvent e){
      try{
         JButton button = (JButton)e.getSource();
         if(button.getActionCommand().equals("Start")){
            this._lTimer.start();
         }
         else if(button.getActionCommand().equals("Stop")){
            this._lTimer.stop();
         }
         else if(button.getActionCommand().equals("Reset")){
            this._lTimer.reset();
         }
         else if(button.getActionCommand().equals("Lap")){
            this._lTimer.lap();
         }
      }
      catch(ClassCastException cce){}
      catch(IllegalArgumentException iae){}
   }

   /**/
   private void handleJMenuItem(ActionEvent e){
      try{
         JMenuItem menuItem = (JMenuItem)e.getSource();
         if(menuItem.getActionCommand().equals("Start")){
            this._lTimer.start();
         }
         else if(menuItem.getActionCommand().equals("Stop")){
            this._lTimer.stop();
         }
         else if(menuItem.getActionCommand().equals("Reset")){
            this._lTimer.reset();
         }
         else if(menuItem.getActionCommand().equals("Lap")){
            this._lTimer.lap();
         }
         else if(menuItem.getActionCommand().equals("Save")){
            this._lTimerView.save();
         }
         else if(menuItem.getActionCommand().equals("Quit")){
            System.out.println("Exiting...");
            System.exit(0);
         }
         else if(menuItem.getActionCommand().equals("About")){
            this._lTimer.about(this._lTimerView);
         }
         else if(menuItem.getActionCommand().equals("Help")){
            this._lTimer.help(this._lTimerView);
         }
         else if(menuItem.getActionCommand().equals("GNU Info")){
            this._lTimer.info(this._lTimerView,
                                         menuItem.getActionCommand());
         }
      }
      catch(ClassCastException eec){}
      catch(IllegalArgumentException iae){}
   }
}
