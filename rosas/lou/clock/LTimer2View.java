//////////////////////////////////////////////////////////////////////
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
import java.time.temporal.*;
import myclasses.*;
import rosas.lou.clock.*;

public class LTimer2View extends GenericJFrame
implements ClockSubscriber{
   private final static short WIDTH  = 340;
   private final static short HEIGHT = 160;

   private LTimer2Controller _controller = null;

   private ButtonGroup buttonGroup = null;
   private ButtonGroup menuGroup   = null;

   private long currentSeconds = -1L;

   ///////////////////////Constructors////////////////////////////////
   /*
    * */
   public LTimer2View(String title, LTimer2Controller controller){
      super(title);
      this._controller = controller;
      this.setUpGUI();
   }

   ////////////////////////Public Methods/////////////////////////////
   ///////////////////////Protected Methods///////////////////////////
   ////////////////////////Private Methods////////////////////////////
   /*
    * */
   private JMenuBar addJMenuBar(){
      //Add menu item button group
      JMenuBar menuBar = new JMenuBar();
      menuBar.add(this.setUpFileMenu());
      menuBar.add(this.setUpHelpMenu());
      return menuBar;
   }

   /*
    * */
   private String convertToFullTimeString(long millis){
      int  millisecs = (int)millis%1000;
      long totalSecs = millis/1000;
      int  secs      = (int)totalSecs%60;
      long totalMins = totalSecs/60;
      int  mins      = (int)totalMins%60;
      long totalHours= totalMins/60;
      int  hours     = (int)totalHours%24;
      int  days      = (int)totalHours/24;
      return String.format("%d %02d:%02d:%02d.%03d",
                                      days,hours,mins,secs,millisecs);
   }

   /*
    * */
   private void displayTime(long millis){
      System.out.println(millis);
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JLabel label = (JLabel)panel.getComponent(0);
      //label.setText(millis+"");
      label.setText(this.convertToFullTimeString(millis));
   }

   /*
    * */
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setBorder(BorderFactory.createEtchedBorder());
      JLabel label = new JLabel("This is a Test Panel");
      panel.add(label);
      return panel;
   }

   /*
    * This will need to change!!
    * */
   private JMenu setUpFileMenu(){
      JMenu file = new JMenu("File");
      file.setMnemonic(KeyEvent.VK_F);

      JMenuItem save = new JMenuItem("Save");
      save.setActionCommand("MenuItemSave");
      save.addActionListener(this._controller);
      file.add(save);

      file.addSeparator();

      JMenuItem quit = new JMenuItem("Quit");
      quit.setActionCommand("MenuItemQuit");
      quit.addActionListener(this._controller);
      file.add(quit);

      return file;
   }

   /*
    * */
   private void setUpGUI(){
      this.setLayout(new BorderLayout());
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
      JPanel centerPanel = this.setUpCenterPanel();
      JPanel southPanel  = this.setUpSouthPanel();
      this.getContentPane().add(centerPanel,BorderLayout.CENTER);
      this.getContentPane().add(southPanel, BorderLayout.SOUTH);
      this.setJMenuBar(this.addJMenuBar());
      this.setVisible(true);
   }

   /*
    * This will need to change!!!
    * */
   private JMenu setUpHelpMenu(){
      JMenu help = new JMenu("Help");
      help.setMnemonic(KeyEvent.VK_H);

      return help;
   }

   /*
    * */
   private JPanel setUpSouthPanel(){
      JPanel panel = new JPanel();
      this.buttonGroup = new ButtonGroup();

      JButton start = new JButton("Start");
      start.setActionCommand("Start");
      start.setMnemonic(KeyEvent.VK_S);
      start.addActionListener(this._controller);
      start.addKeyListener(this._controller);
      this.buttonGroup.add(start);
      panel.add(start);

      JButton stop = new JButton("Stop");
      stop.setActionCommand("Stop");
      stop.setMnemonic(KeyEvent.VK_T);
      stop.addActionListener(this._controller);
      stop.addKeyListener(this._controller);
      stop.setEnabled(false);
      this.buttonGroup.add(stop);
      panel.add(stop);

      JButton lap = new JButton("Lap");
      lap.setActionCommand("Lap");
      lap.setMnemonic(KeyEvent.VK_L);
      lap.addActionListener(this._controller);
      lap.addKeyListener(this._controller);
      lap.setEnabled(false);
      this.buttonGroup.add(lap);
      panel.add(lap);

      JButton reset = new JButton("Reset");
      reset.setActionCommand("Reset");
      reset.setMnemonic(KeyEvent.VK_R);
      reset.addActionListener(this._controller);
      reset.addKeyListener(this._controller);
      reset.setEnabled(false);
      this.buttonGroup.add(reset);
      panel.add(reset);

      return panel;
   }
   ////////////////////Interface Implementations//////////////////////
   /*
    * */
   public void error(String type, String message){}

   /*
    * */
   public void update(String time){}

   /*
    * */
   public void update(java.time.Instant instant){}

   /*
    * */
   public void update(Duration duration){
      long seconds = duration.getSeconds();
      if(this.currentSeconds != seconds){
         this.displayTime(duration.toMillis());
         this.currentSeconds = seconds;
      }
   }

   /*
    * */
   public void update(boolean isRunning){
      Enumeration<AbstractButton> e = this.buttonGroup.getElements();
      while(e.hasMoreElements()){
         AbstractButton b = e.nextElement();
         String command = b.getActionCommand().toUpperCase();
         if(isRunning){
            if(command.equals("START")||command.equals("RESET")){
               b.setEnabled(false);
            }
            else if(command.equals("STOP")||command.equals("LAP")){
               b.setEnabled(true);
            }
         }
         else{}
      }
   }

   /*
    * */
   public void update(String time, String type){}

   /*
    * */
   public void update(java.util.List<?> list){}
}
//////////////////////////////////////////////////////////////////////
