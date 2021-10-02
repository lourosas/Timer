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
   public void actionPerformed(ActionEvent e){}

   /**/
   public void keyPressed(KeyEvent k){}

   /**/
   public void keyReleased(KeyEvent k){}

   /**/
   public void keyTyped(KeyEvent k){}
}
