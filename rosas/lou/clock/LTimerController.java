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
   public void actionPerformed(ActionEvent e){}

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
}
