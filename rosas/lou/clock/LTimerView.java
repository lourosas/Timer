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
import javax.swing.text.*;
import javax.swing.border.*;
import java.time.*;
import myclasses.*;
import rosas.lou.clock.ClockSubscriber;

public class LTimerView extends GenericJFrame
implements ClockSubscriber{
   private LTimerController _controller;
   private ButtonGroup      _buttonGroup;
   private ButtonGroup      _menuItemGroup;
   private JTextField       _currentTimeTF;
   private JTextArea        _lapsTA;
   private JScrollPane      _lapsSP;
   private GenericJInteractionFrame _lapsFrame;

   {
      _controller                  = null;
      _buttonGroup                 = null;
      _menuItemGroup               = null;
      _currentTimeTF               = null;
      _lapsTA                      = null;
      _lapsSP                      = null;
      _lapsFrame                   = null;
   };
   /////////////////////////Public Methods///////////////////////////
   /**/
   public LTimerView(){
      this("", null);
   }

   /**/
   public LTimerView(String title, LTimerController controller){
      super(title);
      this._controller = controller;
      this.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent w){
            setVisible(false);
            System.exit(0); //This may need to change
         }
      });
   }

   ///////////////////Interface Imeplementations//////////////////////
   /**/
   public void update(String time){}

   /**/
   public void update(java.time.Instant instant){}

   /////////////////////////Private Methods///////////////////////////
}
