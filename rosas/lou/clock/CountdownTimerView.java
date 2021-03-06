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
   //private JTextField                  _countDownTF;

   {
      _controller     = null;
      _buttonGroup    = null;
      _menuItemGroup  = null;
      //_countDownTF    = null;
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
   /**/
   public void requestNextFocus(String currentName){
      Container cp = this.getContentPane();
      if(currentName.equals("Set Hours") ||
         currentName.equals("Set Mins")){
         JPanel panel = (JPanel)cp.getComponent(0);
         for(int i = 0; i < panel.getComponentCount(); ++i){
            try{
               JTextField jtf = (JTextField)panel.getComponent(i);
               if((currentName.equals("Set Hours") &&
                  jtf.getName().equals("Set Mins")) ||
                  (currentName.equals("Set Mins") &&
                  jtf.getName().equals("Set Secs"))){
                  jtf.requestFocus();
               }
            }
            catch(ClassCastException cce){}
         }
      }
      else if(currentName.equals("Set Secs")){
         JPanel panel = (JPanel)cp.getComponent(2);
         panel.getComponent(0).requestFocus();
      }
   }

   /**/
   Hashtable<String,String> requestTimes(){
      Hashtable<String,String> values=new Hashtable<String,String>();
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField jtf = (JTextField)panel.getComponent(i);
            String key   = jtf.getName();
            String value = jtf.getText();
            values.put(key, value);
         }
         catch(ClassCastException cce){}
      }
      return values;
   }

   //////////////////////Interface Implementations////////////////////
   /**/
   public void error(String type, String message){}

   /**/
   public void update(String time){
      this.setCountdownTime(time);
   }

   /**/
   public void update(java.time.Instant instant){}

   /**/
   public void update(boolean isRunning){
      this.updateHrsMinsSecsPanel(isRunning);
      this.updateButtonPanel(isRunning);
   }

   /**/
   public void update(String time, String type){}

   /**/
   public void update(java.util.List<?> list){
      this.setCountdownTime((String)list.get(0));
      this.updateHrsMinsSecsPanel((String)list.get(1));
      this.updateButtonPanel((String)list.get(1));
      this.updateFileMenu((String)list.get(1));
   }

   ///////////////////////Private Methods/////////////////////////////
   /**/
   private JPanel setCenterPanel(){
      final int RIGHT = SwingConstants.RIGHT;

      JPanel centerPanel = new JPanel();

      Border border = BorderFactory.createEtchedBorder();
      centerPanel.setBorder(border);
      centerPanel.setLayout(new GridLayout(0,2));

      JLabel countdownTime = new JLabel("Countdown Time:  ", RIGHT);
      JTextField jtf       = new JTextField();
      jtf.setEditable(false);

      centerPanel.add(countdownTime);
      centerPanel.add(jtf);

      return centerPanel;
   }

   /**/
   private void setCountdownTime(String time){
      JPanel panel = (JPanel)this.getContentPane().getComponent(1);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField jtf = (JTextField)panel.getComponent(i);
            jtf.setText(time);
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private JPanel setNorthPanel(){
      JPanel northPanel = new JPanel();

      Border border = BorderFactory.createEtchedBorder();
      northPanel.setBorder(border);

      JLabel hrsLbl     = new JLabel("Hours:  ");
      JLabel minsLbl    = new JLabel("Mins: ");
      JLabel secsLbl    = new JLabel("Secs: ");
      JTextField hrsTF  = new JTextField(3);
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
/*
         hrsTF.addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent k){
               JTextField jtf = (JTextField)k.getSource();
               System.out.println(jtf.getText());
            }
         });
*/
         minsTF.addKeyListener(_controller);
         secsTF.addKeyListener(_controller);
      }
      northPanel.add(hrsLbl);  northPanel.add(hrsTF);
      northPanel.add(minsLbl); northPanel.add(minsTF);
      northPanel.add(secsLbl); northPanel.add(secsTF);

      return northPanel;
   }

   /**/
   private void setHrsMinSecsPanelResetState(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JTextField hrTF  = null;
      JTextField minTF = null;
      JTextField secTF = null;
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField jtf = (JTextField)panel.getComponent(i);
            if(jtf.getName().equals("Set Secs")){ secTF = jtf; }
            else if(jtf.getName().equals("Set Mins")){ minTF = jtf; }
            else if(jtf.getName().equals("Set Hours")){ hrTF = jtf; }
         }
         catch(ClassCastException cce){}
      }
      hrTF.setEditable(true);
      hrTF.setText("");
      hrTF.requestFocus();
      hrTF.selectAll();
      minTF.setEditable(true);
      minTF.setText("");
      secTF.setEditable(true);
      secTF.setText("");
   }

   /**/
   private void setHrsMinSecsPanelRunState(){
      int hours = 0;
      int mins  = 0;
      int secs  = 0;
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JTextField hrTF  = null;
      JTextField minTF = null;
      JTextField secTF = null;
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField jtf = (JTextField)panel.getComponent(i);
            if(jtf.getName().equals("Set Secs")){ secTF = jtf; }
            else if(jtf.getName().equals("Set Mins")){ minTF = jtf; }
            else if(jtf.getName().equals("Set Hours")){ hrTF = jtf; }
         }
         catch(ClassCastException cce){}
      }
      try{ secs = Integer.parseInt(secTF.getText()); }
      catch(NumberFormatException npe){}
      try{ mins = Integer.parseInt(minTF.getText()); }
      catch(NumberFormatException npe){}
      try{ hours= Integer.parseInt(hrTF.getText()); }
      catch(NumberFormatException npe){}
      if(secs >= 60){
         mins += secs/60;
         secs  = secs % 60;
         minTF.setText("" + mins);
         secTF.setText("" + secs);
      }
      if(mins >= 60){
         hours += mins/60;
         mins   = mins % 60;
         hrTF.setText("" + hours);
         minTF.setText("" + mins);
      }
      hrTF.setEditable(false);
      minTF.setEditable(false);
      secTF.setEditable(false);
   }

   /**/
   private void setHrsMinSecsPanelStopState(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JTextField hrTF  = null;
      JTextField minTF = null;
      JTextField secTF = null;
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JTextField jtf = (JTextField)panel.getComponent(i);
            if(jtf.getName().equals("Set Secs")){ secTF = jtf; }
            else if(jtf.getName().equals("Set Mins")){ minTF = jtf; }
            else if(jtf.getName().equals("Set Hours")){ hrTF = jtf; }
         }
         catch(ClassCastException cce){}
      }
      hrTF.setEditable(false);
      minTF.setEditable(false);
      secTF.setEditable(false);
   }

   /**/
   private JPanel setSouthPanel(){
      JPanel buttonPanel = new JPanel();

      JButton start = new JButton("Start");
      start.setMnemonic(KeyEvent.VK_S);
      buttonPanel.add(start);

      JButton stop = new JButton("Stop");
      stop.setEnabled(false);
      stop.setMnemonic(KeyEvent.VK_T);
      buttonPanel.add(stop);

      JButton reset = new JButton("Reset");
      reset.setEnabled(false);
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
      stop.setEnabled(false);
      file.add(stop);

      JMenuItem reset = new JMenuItem("Reset", 'R');
      reset.setEnabled(false);
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
      JFrame frame = this;

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

      about.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            showAboutDialog();
         }
      });
      gnuInfo.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            showGNUDialog();
         }
      });
      helpItem.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e){
            showHelpDialog();
         }
      });

      return help;
   }

   /**/
   private JMenuBar setUpMenuBar(){
      JMenuBar jmenuBar = new JMenuBar();
      jmenuBar.add(this.setUpFileMenu());
      jmenuBar.add(this.setUpHelpMenu());
      return jmenuBar;
   }

   /**/
   private void showAboutDialog(){
      int type = JOptionPane.INFORMATION_MESSAGE;

      String message = new String("LTimer Version 1.0\n");
      message += "Copyright \u00A9 2021\n";
      message += "LTimer Created by Lou Rosas\n";
      message += "lourosas@gmail.com\n\n";
      message += "Pay it forward!\n";
      message += "Consider giving to your\n";
      message += "favorite charity in gratitude\n";

      JOptionPane.showMessageDialog(this, message, null, type);
   }

   /**/
   private void showGNUDialog(){
      int type = JOptionPane.INFORMATION_MESSAGE;

      String message=new String("This application is distributed\n");
      message += "in the hope that it will be useful,\n";
      message += "but WITHOUT WARRANTY; without even\n";
      message += "the implied waranty of MERCHANTABILITY\n";
      message += "or FITNESS FOR A PARTICULAR PURSPOSE.\n";
      message += "This application is distributed under\n";
      message += "the GNU General Public License.\n";
      message += "That license can be found at:\n";
      message += "http://www.gnu.org/license/ \n\n";
      message += "This application is free software:  you\n";
      message += "can redistribute it and/or modify it\n";
      message += "under the terms of the GNU General Public\n";
      message += "License as published by the Free Software\n";
      message += "Foundation:  either version 3 of the License,\n";
      message += "or (at your option), a later version\n";

      JOptionPane.showMessageDialog(this, message, null, type);
   }

   /**/
   private void showHelpDialog(){
      CountdownTimerHelpJDialog dialog =
                       CountdownTimerHelpJDialog.instance(this, null);
      dialog.setVisible(true);
   }

   /**/
   private void updateButtonPanel(boolean currentlyRunning){
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JButton button = (JButton)panel.getComponent(i);
            if(currentlyRunning){
               if(button.getText().equals("Start") ||
                  button.getText().equals("Reset")){
                  button.setEnabled(false);
               }
               else{
                  button.setEnabled(true);
               }
            }
            else{
               if(button.getText().equals("Start") ||
                  button.getText().equals("Reset")){
                  button.setEnabled(true);
               }
               else{
                  button.setEnabled(false);
               }
            }
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void updateButtonPanel(String state){
      JPanel panel = (JPanel)this.getContentPane().getComponent(2);
      for(int i = 0; i < panel.getComponentCount(); ++i){
         try{
            JButton button = (JButton)panel.getComponent(i);
            if(state.equals("RUN")){
               if(button.getText().equals("Start") ||
                  button.getText().equals("Reset")){
                  button.setEnabled(false);
               }
               else{
                  button.setEnabled(true);
               }
            }
            else if(state.equals("RESET")){
               if(button.getText().equals("Start")){
                  button.setEnabled(true);
               }
               else{
                  button.setEnabled(false);
               }
            }
            else if(state.equals("STOP")){
               if(button.getText().equals("Stop")){
                  button.setEnabled(false);
               }
               else{
                  button.setEnabled(true);
               }
            }
         }
         catch(ClassCastException cce){}
      }
   }

   /**/
   private void updateFileMenu(boolean currentlyRunning){
      JMenuBar jmb = this.getJMenuBar();
      for(int i = 0; i < jmb.getMenu(0).getItemCount(); ++i){
         try{
            JMenuItem jmi = (JMenuItem)jmb.getMenu(0).getItem(i);
            if(currentlyRunning){
               if(jmi.getText().equals("Start") ||
                  jmi.getText().equals("Reset")){
                  jmi.setEnabled(false);
               }
               else{
                  jmi.setEnabled(true);
               }
            }
            else{
               if(jmi.getText().equals("Stop")){
                  jmi.setEnabled(false);
               }
               else{
                  jmi.setEnabled(true);
               }
            }
         }
         catch(ClassCastException cce){}
         catch(NullPointerException npe){}
      }
   }

   /**/
   private void updateFileMenu(String state){
      JMenuBar jmb = this.getJMenuBar();
      for(int i = 0; i < jmb.getMenu(0).getItemCount(); ++i){
         try{
            JMenuItem jmi = (JMenuItem)jmb.getMenu(0).getItem(i);
            if(state.equals("RUN")){
               if(jmi.getText().equals("Start") ||
                  jmi.getText().equals("Reset")){
                  jmi.setEnabled(false);
               }
               else{
                  jmi.setEnabled(true);
               }
            }
            else if(state.equals("RESET")){
               if(jmi.getText().equals("Start") ||
                  jmi.getText().equals("Quit")){
                  jmi.setEnabled(true);
               }
               else{
                  jmi.setEnabled(false);
               }
            }
            else if(state.equals("STOP")){
               if(jmi.getText().equals("Stop")){
                  jmi.setEnabled(false);
               }
               else{
                  jmi.setEnabled(true);
               }
            }
         }
         catch(ClassCastException cce){}
         catch(NullPointerException npe){}
      }
   }

   /**/
   private void updateHrsMinsSecsPanel(boolean currentlyRunning){
      if(currentlyRunning){
         this.setHrsMinSecsPanelRunState();
      }
      else{
         this.setHrsMinSecsPanelResetState();
      }
   }

   /**/
   private void updateHrsMinsSecsPanel(String state){
      if(state.equals("RUN")){
         this.setHrsMinSecsPanelRunState();
      }
      else if(state.equals("RESET")){
         this.setHrsMinSecsPanelResetState();
      }
      else if(state.equals("STOP")){
         this.setHrsMinSecsPanelStopState();
      }
   }
}
