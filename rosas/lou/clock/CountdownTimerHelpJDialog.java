//////////////////////////////////////////////////////////////////////
/*
The LTimer Application.
Copyright (C) 2021 Lou Rosas
This file is part of the CountdownTimer
CountdownTimer is free software; you can redistribute it
and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.
LTimer is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
//////////////////////////////////////////////////////////////////////
package rosas.lou.clock;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

//////////////////////////////////////////////////////////////////////

public class CountdownTimerHelpJDialog extends JDialog{
   private static final short WIDTH  = 350;
   private static final short HEIGHT = 400;

   private static CountdownTimerHelpJDialog _instance;
   private String _helpText;

   {
      _instance = null;
      _helpText = null;
   }

   ////////////////////Public Methods/////////////////////////////////
   /**/
   public static CountdownTimerHelpJDialog instance
   (
      JFrame frame,
      String txt
   ){
      if(_instance == null){
         _instance = new CountdownTimerHelpJDialog(frame, txt);
      }
      return _instance;
   }

   /**/
   public static CountdownTimerHelpJDialog instance(){
      if(_instance == null){
         _instance = new CountdownTimerHelpJDialog(null, "");
      }
      return _instance;
   }

   /**/
   public void setLocation(int w, int h){
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      int width  = (int)dim.getWidth();
      int height = (int)dim.getHeight();

      super.setLocation((width+w+WIDTH)/2, (height-HEIGHT-h)/2);
   }

   ////////////////////////Private Contructors////////////////////////
   /**/
   private CountdownTimerHelpJDialog(JFrame frame, String text){
      super(frame, "Help", false);
      if(text != null){
         this.setText(text);
      }
      Container contentPane = this.getContentPane();
      contentPane.add(this.setNorthPanel(), BorderLayout.NORTH);
      contentPane.add(this.setCenterPanel(), BorderLayout.CENTER);
      contentPane.add(this.setButtonPanel(), BorderLayout.SOUTH);
      this.setSize(WIDTH, HEIGHT);
      this.setResizable(false);
   }

   /**/
   private String helpText(){
      String ht = new String("Countdown Timer Help Guide\n\n");
      ht += "This is a typical countdown timer, except is in app";
      ht += " format\n\n";
      ht += "To start, press 'Start' or chose 'Start' (ctrl-S) from";
      ht += " the 'File Menu'\n\n";
      ht += "To stop, press 'Stop' or chose 'Stop' (ctrl-T) from ";
      ht += "the 'File Menu'\n\n";
      ht += "To reset, press 'Reset' or chose 'Reset' from ";
      ht += "the 'File Menu'\n\n";
      return ht;
   }

   /**/
   private JPanel setButtonPanel(){
      JPanel  bPanel = new JPanel();
      JButton ok     = new JButton("OK");
      JButton cancel = new JButton("Cancel");

      bPanel.setBorder(BorderFactory.createEmptyBorder(0,15,10,15));

      ok.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae){
            setVisible(false);
         }
      });

      cancel.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent ae){
            setVisible(false);
          }
      });

      bPanel.add(ok);
      bPanel.add(cancel);

      return bPanel;
   }

   /**/
   private JPanel setCenterPanel(){
      JPanel cPanel = new JPanel();

      cPanel.setBorder(BorderFactory.createEmptyBorder(0,15,25,15));
      cPanel.setLayout(new BorderLayout());

      JTextArea textArea = new JTextArea();

      if(this._helpText == null){
         textArea.setText(this.helpText());
      }
      else{
         textArea.setText(this._helpText);
      }

      Font font = new Font("Arial", Font.BOLD, 12);
      textArea.setFont(font);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setEditable(false);
      textArea.setSelectionStart(0);
      textArea.setSelectionEnd(0);

      JScrollPane scrollPane = new JScrollPane(textArea);
      scrollPane.setVerticalScrollBarPolicy(
                               JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

      cPanel.add(scrollPane, BorderLayout.CENTER);

      return cPanel;
   }

   /**/
   private JPanel setNorthPanel(){
      String label  = new String("Simple Help Overview");
      JPanel nPanel = new JPanel();

      nPanel.setBorder(BorderFactory.createEtchedBorder());
      nPanel.add(new JLabel(label, SwingConstants.CENTER));

      return nPanel;
   }

   /**/
   private void setText(String text){
      this._helpText = new String(text);
   }
}
