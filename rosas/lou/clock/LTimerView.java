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
   private LTimerController         _controller;
   private ButtonGroup              _buttonGroup;
   private ButtonGroup              _menuItemGroup;
   private JTextField               _currentTimeTF;
   private JTextField               _lapTF;
   private JTextArea                _lapsTA;
   private JScrollPane              _lapsSP;
   private GenericJInteractionFrame _lapsFrame;

   {
      _controller     = null;
      _buttonGroup    = null;
      _menuItemGroup  = null;
      _currentTimeTF  = null;
      _lapTF          = null;
      _lapsTA         = null;
      _lapsSP         = null;
      _lapsFrame      = null;
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
   public void update(String time){
      this._currentTimeTF.setText(time);
   }

   /**/
   public void update(String time, String type){
      Container contentPane = this.getContentPane();
      JPanel panel = (JPanel)contentPane.getComponent(1);
      System.out.println(panel.getComponentCount());
      if(type.toUpperCase().equals("LAP")){
         if(this._lapTF == null){
            JLabel lap  = new JLabel("Lap:  ",SwingConstants.RIGHT);
            this._lapTF = new JTextField();
            this._lapTF.setEditable(false);
            panel.add(lap);
            panel.add(this._lapTF);
            this.revalidate();
            this.repaint();
         }
         this._lapTF.setText(time);
         System.out.println(time);
      }
      else if(type.toUpperCase().equals("ELAPSED")){
         this.update(time);
      }
   }

   /**/
   public void update(java.time.Instant instant){}

   /**/
   public void update(boolean isRunning){
      this.reflectState(isRunning);
   }

   /////////////////////////Private Methods///////////////////////////
   /**/
   private void reflectState(boolean isRunning){
      Enumeration<AbstractButton> e = this._buttonGroup.getElements();
      Enumeration<AbstractButton> m=this._menuItemGroup.getElements();
      while(e.hasMoreElements()){
         AbstractButton ab = e.nextElement();
         if(isRunning){
            if(ab.getActionCommand().equals("Start") ||
               ab.getActionCommand().equals("Reset")){
               ab.setEnabled(false);
            }
            else if(ab.getActionCommand().equals("Stop") ||
                    ab.getActionCommand().equals("Lap")){
               ab.setEnabled(true);
            }
         }
         else{
            if(ab.getActionCommand().equals("Start") ||
               ab.getActionCommand().equals("Reset")){
               ab.setEnabled(true);
            }
            else if(ab.getActionCommand().equals("Stop") ||
                    ab.getActionCommand().equals("Lap")){
               ab.setEnabled(false);
            }
         }
      }
      while(m.hasMoreElements()){
         AbstractButton ab = m.nextElement();
         if(isRunning){
            if(ab.getActionCommand().equals("Start") ||
               ab.getActionCommand().equals("Reset")){
               ab.setEnabled(false);
            }
            else if(ab.getActionCommand().equals("Stop") ||
                    ab.getActionCommand().equals("Lap")){
               ab.setEnabled(true);
            }
         }
         else{
            if(ab.getActionCommand().equals("Start") ||
               ab.getActionCommand().equals("Reset")){
               ab.setEnabled(true);
            }
            else if(ab.getActionCommand().equals("Stop") ||
                    ab.getActionCommand().equals("Lap")){
               ab.setEnabled(false);
            }
         }
      }
   }
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
   private JMenu setUpFileMenu(){
      int ctrl = InputEvent.CTRL_DOWN_MASK;
      KeyStroke ks = null;

      JMenu file = new JMenu("File");
      file.setMnemonic(KeyEvent.VK_F);

      JMenuItem start = new JMenuItem("Start", 'S');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrl);
      start.setAccelerator(ks);
      this._menuItemGroup.add(start);
      file.add(start);

      JMenuItem stop = new JMenuItem("Stop", 'T');
      stop.setEnabled(false);
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_T, ctrl);
      stop.setAccelerator(ks);
      this._menuItemGroup.add(stop);
      file.add(stop);

      JMenuItem lap = new JMenuItem("Lap", 'L');
      lap.setEnabled(false);
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_L, ctrl);
      lap.setAccelerator(ks);
      this._menuItemGroup.add(lap);
      file.add(lap);

      JMenuItem reset = new JMenuItem("Reset", 'R');
      //ks = KeyStroke.getKeyStroke(KeyEvent.VK_R, ctrl);
      //reset.setAccelerator(ks);
      this._menuItemGroup.add(reset);
      file.add(reset);

      file.addSeparator();

      JMenuItem quit = new JMenuItem("Quit", 'Q');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctrl);
      quit.setAccelerator(ks);
      this._menuItemGroup.add(quit);
      file.add(quit);


      if(this._controller != null){
         start.addActionListener(this._controller);
         stop.addActionListener(this._controller);
         lap.addActionListener(this._controller);
         reset.addActionListener(this._controller);
         quit.addActionListener(this._controller);
      }

      return file;
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
      this._menuItemGroup.add(helpItem);
      help.add(helpItem);


      help.addSeparator();

      JMenuItem about = new JMenuItem("About", 'A');
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
      about.setAccelerator(ks);
      this._menuItemGroup.add(about);
      help.add(about);

      if(this._controller != null){
         helpItem.addActionListener(this._controller);
         about.addActionListener(this._controller);
      }
      return help;
   }

   /**/
   private JMenuBar setUpMenuBar(){
      this._menuItemGroup = new ButtonGroup();
      JMenuBar jmenuBar = new JMenuBar();
      jmenuBar.add(this.setUpFileMenu());
      jmenuBar.add(this.setUpHelpMenu());
      return jmenuBar;
   }
}
