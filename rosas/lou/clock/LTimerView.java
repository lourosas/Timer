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
      this.setUpGUI();
      this.setVisible(true);
   }

   ///////////////////Interface Imeplementations//////////////////////
   /**/
   public void update(String time){}

   /**/
   public void update(java.time.Instant instant){}

   /////////////////////////Private Methods///////////////////////////
   /**/
   private JPanel setCenterPanel(){
      final int RIGHT = SwingConstants.RIGHT;

      JPanel centerPanel = new JPanel();

      Border border = BorderFactory.createEtchedBorder();
      centerPanel.setBorder(border);
      centerPanel.setLayout(new GridLayout(0,2));

      JLabel time         = new JLabel("Time:  ", RIGHT);
      this._currentTimeTF = new JTextField();
      this._currentTimeTF.setEditable(false);

      centerPanel.add(time);
      centerPanel.add(this._currentTimeTF);

      return centerPanel;
   }

   /**/
   private JPanel setNorthPanel(){
      String s = new String("LTimer");
      JPanel northPanel = new JPanel();
      JLabel northLabel = new JLabel(s, SwingConstants.CENTER);

      Border border = BorderFactory.createEtchedBorder();
      northPanel.setBorder(border);
      northPanel.add(northLabel);

      return northPanel;
   }

   /**/
   private JPanel setSouthPanel(){
      JPanel buttonPanel = new JPanel();
      this._buttonGroup  = new ButtonGroup();

      JButton start = new JButton("Start");
      start.setMnemonic(KeyEvent.VK_S);
      buttonPanel.add(start);
      this._buttonGroup.add(start);

      JButton stop = new JButton("Stop");
      stop.setEnabled(false);
      stop.setMnemonic(KeyEvent.VK_T);
      buttonPanel.add(stop);
      this._buttonGroup.add(stop);

      JButton lap = new JButton("Lap");
      lap.setEnabled(false);
      lap.setMnemonic(KeyEvent.VK_L);
      buttonPanel.add(lap);
      this._buttonGroup.add(lap);

      JButton reset = new JButton("Reset");
      reset.setMnemonic(KeyEvent.VK_R);
      buttonPanel.add(reset);
      this._buttonGroup.add(reset);

      if(this._controller != null){
         start.addActionListener(this._controller);
         start.addKeyListener(this._controller);
         stop.addActionListener(this._controller);
         stop.addKeyListener(this._controller);
         lap.addActionListener(this._controller);
         lap.addKeyListener(this._controller);
         reset.addActionListener(this._controller);
         reset.addKeyListener(this._controller);
      }

      return buttonPanel;
   }

   /**/
   private void setUpGUI(){
      final short WIDTH  = 340;
      final short HEIGHT = 160;
      //Get the Content Pane
      Container contentPane = this.getContentPane();
      this.setSize(WIDTH, HEIGHT);
      //set up the rest of the GUi
      contentPane.add(this.setNorthPanel(),  BorderLayout.NORTH);
      contentPane.add(this.setCenterPanel(),BorderLayout.CENTER);
      contentPane.add(this.setSouthPanel(),  BorderLayout.SOUTH);
      //Set up the Menu Bar
      //this.setJMenuBar(this.setUpMenuBar());
      this.setResizable(false);
   }
}
