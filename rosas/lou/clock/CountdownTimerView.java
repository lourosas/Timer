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
   private JTextField                  _countDownTF;

   {
      _controller     = null;
      _buttonGroup    = null;
      _menuItemGroup  = null;
      _countDownTF    = null;
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

   //////////////////////Interface Implementations////////////////////
   /**/
   public void error(String type, String message){}

   /**/
   public void update(String time){}

   /**/
   public void update(java.time.Instant instant){}

   /**/
   public void update(boolean isRunning){}

   /**/
   public void update(String time, String type){}

   /**/
   public void update(java.util.List<?> list){}

   ///////////////////////Private Methods/////////////////////////////
   /**/
   private JPanel setCenterPanel(){
      final int RIGHT = SwingConstants.RIGHT;

      JPanel centerPanel = new JPanel();

      Border border = BorderFactory.createEtchedBorder();
      centerPanel.setBorder(border);
      centerPanel.setLayout(new GridLayout(0,2));
      
      JLabel countdownTime = new JLabel("Countdown Time:  ", RIGHT);
      this._countDownTF    = new JTextField();
      this._countDownTF.setEditable(false);

      centerPanel.add(countdownTime);
      centerPanel.add(this._countDownTF);

      return centerPanel;
   }

   /**/
   private JPanel setNorthPanel(){
      JPanel northPanel = new JPanel();

      Border border = BorderFactory.createEtchedBorder();
      northPanel.setBorder(border);

      JLabel hrsLbl     = new JLabel("Hours:  ");
      JLabel minsLbl    = new JLabel("Mins: ");
      JLabel secsLbl    = new JLabel("Secs: ");
      JTextField hrsTF  = new JTextField(4);
      JTextField minsTF = new JTextField(2);
      JTextField secsTF = new JTextField(2);
      hrsTF.setName("Set Hours");
      minsTF.setName("Set Mins");
      secsTF.setName("Set Secs");

      if(this._controller != null){
         hrsTF.addActionListener(_controller);
         minsTF.addActionListener(_controller);
         secsTF.addActionListener(_controller);
         hrsTF.addKeyListener(_controller);
         minsTF.addKeyListener(_controller);
         secsTF.addKeyListener(_controller);
      }
      northPanel.add(hrsLbl);  northPanel.add(hrsTF);
      northPanel.add(minsLbl); northPanel.add(minsTF);
      northPanel.add(secsLbl); northPanel.add(secsTF);
      
      return northPanel;
   }

   /**/
   private JPanel setSouthPanel(){
      JPanel buttonPanel = new JPanel();

      JButton start = new JButton("Start");
      start.setMnemonic(KeyEvent.VK_S);
      buttonPanel.add(start);

      JButton stop = new JButton("Stop");
      //stop.setEnabled(false);
      stop.setMnemonic(KeyEvent.VK_T);
      buttonPanel.add(stop);

      JButton reset = new JButton("Reset");
      reset.setMnemonic(KeyEvent.VK_R);
      buttonPanel.add(reset);

      if(this._controller != null){
         start.addActionListener(this._controller);
         start.addKeyListener(this._controller);
         stop.addActionListener(this._controller);
         stop.addKeyListener(this._controller);
         reset.addActionListener(this._controller);
         reset.addKeyListener(this._controller);
      }
      return buttonPanel;
   }

   /**/
   private JMenu setUpFileMenu(){
      int ctrl     = InputEvent.CTRL_DOWN_MASK;
      KeyStroke ks = null;

      JMenu file = new JMenu("File");
      file.setMnemonic(KeyEvent.VK_F);

      JMenuItem start = new JMenuItem("Start", 'S');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrl);
      start.setAccelerator(ks);
      file.add(start);

      JMenuItem stop = new JMenuItem("Stop", 'T');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_T, ctrl);
      stop.setAccelerator(ks);
      file.add(stop);

      JMenuItem reset = new JMenuItem("Reset", 'R');
      file.add(reset);

      file.addSeparator();

      JMenuItem quit = new JMenuItem("Quit", 'Q');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctrl);
      quit.setAccelerator(ks);
      file.add(quit);

      if(this._controller != null){
         start.addActionListener(this._controller);
         stop.addActionListener(this._controller);
         reset.addActionListener(this._controller);
         quit.addActionListener(this._controller);
      }

      return file;
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

   /**/
   private JMenu setUpHelpMenu(){
      KeyStroke ks = null;

      JMenu help = new JMenu("Help");
      help.setMnemonic(KeyEvent.VK_H);

      JMenuItem helpItem = new JMenuItem("Help", 'H');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
      helpItem.setAccelerator(ks);
      help.add(helpItem);

      help.addSeparator();

      JMenuItem gnuInfo = new JMenuItem("GNU Info", 'G');
      help.add(gnuInfo);

      help.addSeparator();
      
      JMenuItem about = new JMenuItem("About", 'A');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
      about.setAccelerator(ks);
      help.add(about);

      if(this._controller != null){
         helpItem.addActionListener(this._controller);
         about.addActionListener(this._controller);
         gnuInfo.addActionListener(this._controller);
      }

      return help;
   }

   /**/
   private JMenuBar setUpMenuBar(){
      JMenuBar jmenuBar = new JMenuBar();
      jmenuBar.add(this.setUpFileMenu());
      jmenuBar.add(this.setUpHelpMenu());
      return jmenuBar;
   }
}
