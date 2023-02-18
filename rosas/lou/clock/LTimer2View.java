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

   private JTextArea                lapsTA    = null;
   private JScrollPane              lapsSP    = null;
   private GenericJInteractionFrame lapsFrame = null;

   private State state             = State.STOP;

   private boolean run             = false;

   private long currentSeconds = -1L;
   private long lapSeconds     = -1L;

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
      this.menuGroup = new ButtonGroup();
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
      //System.out.println(millis);
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      JLabel label = (JLabel)panel.getComponent(1);
      label.setText(this.convertToFullTimeString(millis));
   }

   /*
    * */
   private void displayLap(long millis){
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      if(panel.getComponentCount() < 3){
         JLabel lap = new JLabel("Lap:  ", SwingConstants.RIGHT);
         lap.setForeground(Color.BLUE);
         panel.add(lap);
         JLabel time = new JLabel(" Time ", SwingConstants.LEFT);
         time.setForeground(Color.BLUE);
         panel.add(time);
         this.revalidate();
         this.repaint();
      }
      JLabel label = (JLabel)panel.getComponent(3);
      label.setText(this.convertToFullTimeString(millis));
   }

   /*
    * */
   private void displayLaps(java.util.List<?> list){
      int count = 1;
      if(this.lapsFrame == null){
         this.lapsFrame = new GenericJInteractionFrame("Laps");
         this.lapsFrame.setSize(200,100);
         this.lapsFrame.setLocation(340,340);
      }
      this.lapsFrame.setVisible(true);
      this.lapsFrame.setResizable(false);
      if(this.lapsTA == null){
         this.lapsTA = new JTextArea();
         this.lapsTA.setEditable(false);
         this.lapsFrame.add(this.lapsTA);
      }
      this.lapsTA.setText("");
      if(this.lapsSP == null){
         this.lapsSP = new JScrollPane(this.lapsTA);
         this.lapsSP.createHorizontalScrollBar();
         this.lapsSP.createVerticalScrollBar();
         this.lapsFrame.add(this.lapsSP);
      }
      Iterator<?> it = list.iterator();
      while(it.hasNext()){
         Object obj = it.next();
         try{
            String previous = this.lapsTA.getText();
            Duration d = (Duration)obj;
            long millis = d.toMillis();
            String current = this.convertToFullTimeString(millis);
            String place = previous+"Lap "+count+":  "+current+"\n";
            this.lapsTA.setText(place);
            ++count;
         }
         catch(ClassCastException cce){}
      }
   }

   /*
    * */
   private void reflectState(State state){
   }

   /*
    * */
   private void reflectState(State state, boolean reset){

      boolean change = (this.state != state);
      this.state = state;
      this.reflectStateInButtons(change,  reset);
      this.reflectStateInMenuItems(change,reset);

   }

   /*
    * */
   private void reflectStateInButtons(boolean changed, boolean reset){
      Enumeration<AbstractButton> e = this.buttonGroup.getElements();
      while(e.hasMoreElements()){
         AbstractButton b = e.nextElement();
         String command   = b.getActionCommand().toUpperCase();
         if(changed){
            if(command.equals("START")){
               if(this.state == State.RUN){
                  b.setEnabled(false);
               }
               else{
                  b.setEnabled(true);
               }
            }
            else if(command.equals("STOP")){
               if(this.state == State.RUN){
                  b.setEnabled(true);
               }
               else{
                  b.setEnabled(false);
               }
            }
            else if(command.equals("LAP")){
               if(this.state == State.RUN){
                  b.setEnabled(true);
               }
               else{
                  b.setEnabled(false);
               }
            }
         }
         if(command.equals("RESET")){
            if(this.state == State.STOP && !reset){
               b.setEnabled(true);
            }
            else{
               b.setEnabled(false);
            }
         }
      }
   }

   /*
    * */
   private void reflectStateInMenuItems
   (
      boolean changed,
      boolean reset
   ){
      Enumeration<AbstractButton> e = this.menuGroup.getElements();
      while(e.hasMoreElements()){
         AbstractButton mi = e.nextElement();
         String command = mi.getActionCommand().toUpperCase();
         if(changed){
            if(command.equals("MENUITEMSTART")){
               if(this.state == State.RUN){
                  mi.setEnabled(false);
               }
               else{
                  mi.setEnabled(true);
               }
            }
            else if(command.equals("MENUITEMSTOP")){
               if(this.state == State.RUN){
                  mi.setEnabled(true);
               }
               else{
                  mi.setEnabled(false);
               }
            }
            else if(command.equals("MENUITEMLAP")){
               if(this.state == State.RUN){
                  mi.setEnabled(true);
               }
               else{
                  mi.setEnabled(false);
               }
            }
         }
         if(command.equals("MENUITEMRESET")){
            if(this.state == State.STOP && !reset){
               mi.setEnabled(true);
            }
            else{
               mi.setEnabled(false);
            }
         }
      }
   }

   /*
    * */
   private void removeLap(){
      JPanel panel = (JPanel)this.getContentPane().getComponent(0);
      if(panel.getComponentCount() > 2){
         panel.remove(3);
         panel.remove(2);
         this.revalidate();
         this.repaint();
      }
   }

   /*
    * */
   private void removeLaps(){
      try{
         this.lapsTA.setText("");
         this.lapsTA = null;
         this.lapsSP = null;
         this.lapsFrame.setVisible(false);
         this.lapsFrame = null;
      }
      catch(NullPointerException npe){}
   }

   /*
    * */
   private void resetTime(){
      Duration d = Duration.ZERO;
      this.displayTime(d.toMillis());
   }

   /*
    * */
   private JPanel setUpCenterPanel(){
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(0,2));
      panel.setBorder(BorderFactory.createEtchedBorder());
      //Elapsed Label
      JLabel label = new JLabel("  Elapsed:  ");
      label.setHorizontalAlignment(SwingConstants.RIGHT);
      panel.add(label);
      //Label to display the total (elapsed) time
      Duration d = Duration.ZERO;
      label = new JLabel(this.convertToFullTimeString(d.toMillis()));
      label.setHorizontalAlignment(SwingConstants.LEFT);
      panel.add(label);
      return panel;
   }

   /*
    * This will need to change!!
    * */
   private JMenu setUpFileMenu(){
      int ctrl     = InputEvent.CTRL_DOWN_MASK;
      KeyStroke ks = null;

      JMenu file = new JMenu("File");
      file.setMnemonic(KeyEvent.VK_F);

      JMenuItem start = new JMenuItem("Start", 'S');
      start.setActionCommand("MenuItemStart");
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_S, ctrl);
      start.setAccelerator(ks);
      this.menuGroup.add(start);
      start.addActionListener(this._controller);
      file.add(start);

      JMenuItem stop = new JMenuItem("Stop", 'T');
      stop.setActionCommand("MenuItemStop");
      stop.setEnabled(false);
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_T, ctrl);
      stop.setAccelerator(ks);
      this.menuGroup.add(stop);
      stop.addActionListener(this._controller);
      file.add(stop);

      JMenuItem lap = new JMenuItem("Lap",'L');
      lap.setActionCommand("MenuItemLap");
      lap.setEnabled(false);
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_L, ctrl);
      lap.setAccelerator(ks);
      this.menuGroup.add(lap);
      lap.addActionListener(this._controller);
      file.add(lap);

      JMenuItem reset = new JMenuItem("Reset", 'R');
      reset.setEnabled(false);
      reset.setActionCommand("MenuItemReset");
      reset.addActionListener(this._controller);
      this.menuGroup.add(reset);
      file.add(reset);

      file.addSeparator();

      JMenuItem save = new JMenuItem("Save",'V');
      save.setEnabled(false);
      save.setActionCommand("MenuItemSave");
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_V, ctrl);
      save.setAccelerator(ks);
      save.addActionListener(this._controller);
      this.menuGroup.add(save);
      file.add(save);

      file.addSeparator();

      JMenuItem quit = new JMenuItem("Quit",'Q');
      quit.setActionCommand("MenuItemQuit");
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctrl);
      quit.setAccelerator(ks);
      quit.addActionListener(this._controller);
      this.menuGroup.add(quit);
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
      KeyStroke ks = null;

      JMenu help = new JMenu("Help");
      help.setMnemonic(KeyEvent.VK_H);

      JMenuItem helpItem = new JMenuItem("Help",'H');
      helpItem.setActionCommand("MenuItemHelp");
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_F1,0);
      helpItem.setAccelerator(ks);
      this.menuGroup.add(helpItem);
      helpItem.addActionListener(this._controller);
      help.add(helpItem);

      help.addSeparator();

      JMenuItem gnuInfo = new JMenuItem("GNU Info",'G');
      gnuInfo.setActionCommand("MenuItemGNUInfo");
      this.menuGroup.add(gnuInfo);
      gnuInfo.addActionListener(this._controller);
      help.add(gnuInfo);

      help.addSeparator();

      JMenuItem about = new JMenuItem("About", 'A');
      about.setActionCommand("MenuItemAbout");
      ks = KeyStroke.getKeyStroke(KeyEvent.VK_F2,0);
      about.setAccelerator(ks);
      this.menuGroup.add(about);
      about.addActionListener(this._controller);
      help.add(about);

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
   public void updateElapsed(Duration duration){
      long seconds = duration.getSeconds();
      if(this.state == State.STOP){
         this.displayTime(duration.toMillis());
      }
      //Only update once a second in the RUN State...
      else if(this.currentSeconds != seconds){
         this.displayTime(duration.toMillis());
         this.currentSeconds = seconds;
      }
   }

   /*
    * */
   public void updateLap(Duration duration){
      long seconds = duration.getSeconds();
      if(this.state == State.STOP){
         this.displayLap(duration.toMillis());
      }
      //As always, in the RUN State, only update once a second...
      else if(this.lapSeconds != seconds){
         this.lapSeconds = seconds;
         this.displayLap(duration.toMillis());
      }
   }

   /*
    * */
   public void updateLaps(java.util.List<?> laps){
      this.displayLaps(laps);
   }

   /*
    * */
   public void updateRun(){
      this.reflectState(State.RUN, false);
   }

   /*
    * */
   public void updateStop(){
      this.reflectState(State.STOP,false);
   }

   /*
    * */
   public void updateReset(){
      this.removeLap();
      this.removeLaps();
      this.resetTime();
      this.reflectState(this.state, true);
   }

   //Most of what is below is going to go away...to transition to
   //what is above...

   /*
    * */
   public void update(String data){
      System.out.println(data);
   }

   public void update(State state){
      this.reflectState(state);
   }

   /*
    * */
   public void update(java.time.Instant instant){}

   /*
    * */
   public void update(Duration duration){
      long seconds = duration.getSeconds();
      //This needs to change based on State...
      if(this.currentSeconds != seconds){
         this.displayTime(duration.toMillis());
         this.currentSeconds = seconds;
      }
   }

   /*
    * */
   public void update(Duration duration, boolean isRunning){
      if(!isRunning){
         this.displayTime(duration.toMillis());
      }
      else{
         this.update(duration);
      }
      this.update(isRunning);
   }

   /*
    * */
   public void update(Duration duration, State state){
      /*
      if(state == State.STOP){
         this.displayTime(duration.toMillis());
      }
      else if(state == State.LAP){
         long seconds = duration.getSeconds();
         if(this.lapSeconds != seconds){
            System.out.println("Lap:  "+duration.toMillis());
            this.lapSeconds = seconds;
         }
      }
      else{
         this.update(duration);
      }
      this.update(state);
      */
   }

   /*
    * */
   public void update(Duration duration, State state, String type){
   }

   /*
    * */
   public void update(boolean isRunning){
      if(this.run != isRunning){
         this.run = isRunning;
         Enumeration<AbstractButton> e=this.buttonGroup.getElements();
         while(e.hasMoreElements()){
            AbstractButton b = e.nextElement();
            String command = b.getActionCommand().toUpperCase();
            if(command.equals("START")||command.equals("RESET")){
               if(isRunning){
                  b.setEnabled(false);
               }
               else{
                  b.setEnabled(true);
               }
            }
            else if(command.equals("STOP")||command.equals("LAP")){
               if(isRunning){
                  b.setEnabled(true);
               }
               else{
                  b.setEnabled(false);
               }
            }
         }
      }
   }

   /*
    * */
   public void update(String time, String type){}

   /*
    * */
   public void update(java.util.List<?> list){
      System.out.println(list);
   }
}
//////////////////////////////////////////////////////////////////////
