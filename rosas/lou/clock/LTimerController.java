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
   private LTimverView _lTimerView;

   {
      _lTimer      = null;
      _lTimerView  = null;
   };

   /////////////////////////Public Methods////////////////////////////
   /**/
   public LTimerController(LTimer timer, LTimerView ltv){
      this._lTimer = timer;
      this._lTimerView = ltv;
      this._lTimer.addSubscriber(this._lTimerView);
   }

   /////////////////////Interface Implementaions//////////////////////
   /**/
   public void actionPerformed(ActiveEvent e){}

   /**/
   public
}
