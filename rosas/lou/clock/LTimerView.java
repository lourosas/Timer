/********************************************************************
* Copyright (C) 2021 Lou Rosas
* This file is part of many applications registered with
* the GNU General Public License as published
* by the Free Software Foundation; either version 3 of the License,
* or (at your option) any later version.
* LTimer is distributed in the hope that it will be
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

   public void save(){
      JFileChooser chooser = new JFileChooser();
      try{
         FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Text Files","txt","tex");
         chooser.setFileFilter(filter);
         int choice = chooser.showSaveDialog(this);
         if(choice == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            this._controller.save(file);
         }
      }
      catch(HeadlessException he){}
   }

   ///////////////////Interface Imeplementations//////////////////////
   /**/
   public void update(java.util.List<?> list){
      this.setUpTheLapTextArea(list);
   }

   /**/
   public void update(String time){
      this._currentTimeTF.setText(time);
   }

   /**/
   public void update(String time, String type){
      if(type.toUpperCase().equals("LAP")){
         if(this._lapTF == null){
            this.setUpTheLapTextField();
         }
         this._lapTF.setText(time);
      }
      else if(type.toUpperCase().equals("ELAPSED")){
         this.update(time);
      }
      else if(type.toUpperCase().equals("RESET")){
         if(this._lapTF != null){
            this.resetTheTimerView();
         }
         this.reflectState(false);
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
               ab.getActionCommand().equals("Reset") ||
               ab.getActionCommand().equals("Save")){
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
            else if(ab.getActionCommand().equals("Save")){
               if(!(this._currentTimeTF.getText().equals("0 00:00:00.000"))
                  || (this._lapTF != null) || this._lapsTA != null){
                  ab.setEnabled(true);
               }
               else{
                  ab.setEnabled(false);
               }
            }
         }
      }
   }

   /**/
   private void resetTheTimerView(){
      Container contentPane = this.getContentPane();
      JPanel panel = (JPanel)contentPane.getComponent(1);
      panel.remove(3);
      panel.remove(2);
      this.revalidate();
      this.repaint();
      this._lapTF = null;
      this._lapsFrame.setVisible(false);
      this._lapsFrame = null;
      this._lapsTA.setText("");
      this._lapsTA = null;
      this._lapsSP = null;
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

      JMenuItem save = new JMenuItem("Save", 'V');
      save.setEnabled(false);
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_V, ctrl);
      save.setAccelerator(ks);
      this._menuItemGroup.add(save);
      file.add(save);

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
         save.addActionListener(this._controller);
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

   /*
   */
   private void setUpTheLapTextField(){
      Container contentPane = this.getContentPane();
      JPanel panel = (JPanel)contentPane.getComponent(1);
      JLabel lap  = new JLabel("Lap:  ",SwingConstants.RIGHT);
      this._lapTF = new JTextField();
      this._lapTF.setEditable(false);
      panel.add(lap);
      panel.add(this._lapTF);
      this.revalidate();
      this.repaint();
   }

   /*
   */
   private void setUpTheLapTextArea(java.util.List<?> list){
      int count = 1;
      if(this._lapsFrame == null){
         this._lapsFrame = new GenericJInteractionFrame("Laps");
         this._lapsFrame.setSize(200,100);
         this._lapsFrame.setLocation(340,0);//Temporary for the time
      }
      this._lapsFrame.setVisible(true);
      this._lapsFrame.setResizable(false);
      if(this._lapsTA == null){
         this._lapsTA = new JTextArea();
         this._lapsTA.setEditable(false);
         this._lapsFrame.add(this._lapsTA);
      }
      if(this._lapsSP == null){
         this._lapsSP = new JScrollPane(this._lapsTA);
         this._lapsSP.createHorizontalScrollBar();
         this._lapsSP.createVerticalScrollBar();
         this._lapsFrame.add(this._lapsSP);
      }
      this._lapsTA.setText("");
      Iterator<?> it = list.iterator();
      while(it.hasNext()){
         Object obj = it.next();
         if(obj instanceof String){
            String last = (String)obj;
            String previous = this._lapsTA.getText();
            this._lapsTA.setText(previous+"Lap "+count+": "+last);
            this._lapsTA.setText(this._lapsTA.getText() + "\n");
            ++count;
         }
      }
   }
}
