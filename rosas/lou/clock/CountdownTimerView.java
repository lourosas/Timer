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
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import javax.swing.border.*;
import java.time.*;
import myclasses.*;
import rosas.lou.clock.*;

public class CountdownTimerView extends GenericJFrame
implements ClockSubscriber{
   private CountdownTimerController    _controller;
   private ButtonGroup                 _buttonGroup;
   private ButtonGroup                 _menuItemGroup;

   {
      _controller     = null;
      _buttonGroup    = null;
      _menuItemGroup  = null;
   };

   ////////////////////////Constructors///////////////////////////////
   /**/
   public CountdownTimerView(){
      this("", null);
   }

   /**/
   public CountdownTimerView(String title,
                             CountdownTimerController controller){
      super(title);
      this._controller = controller;
      this.addWindowListener(new WindowAdapter(){
         public void windowClosing(WindowEvent w){
            setVisible(false);
            System.exit(0);
         }
      });
      this.setUpGUI();
      this.setVisible(true);
   }


   ///////////////////////Public Methods//////////////////////////////

   ///////////////////////Private Methods/////////////////////////////
   /**/
   private JPanel setCenterPanel(){
      final int RIGHT = SwingConstants.RIGHT;

      JPanel centerPanel = new JPanel();
      
      return centerPanel;
   }

   /**/
   private void setUpGUI(){
      final short WIDTH  = 340;
      final short HEIGHT = 160;
      //As always, get the Content Pane first
      Container contentPane = this.getContentPane();
      this.setSize(WIDTH,HEIGHT);
      //Now set up the rest of the GUI (as usual)
      contentPane.add(this.setNorthPanel(),  BorderLayout.NORTH);
      contentPane.add(this.setCenterPanel(), BorderLayout.CENTER);
      contentPane.add(this.setSouthPanel(),  BorderLayout.SOUTH);
      //As Always, set up the Menu Bar...
      this.setJMenuBar(this.setUpMenuBar());
      this.setResizable(false);
   }
}
