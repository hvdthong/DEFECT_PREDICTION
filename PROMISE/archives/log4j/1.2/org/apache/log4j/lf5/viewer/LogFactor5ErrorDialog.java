package org.apache.log4j.lf5.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * LogFactor5ErrorDialog
 *
 * @author Richard Hurst
 * @author Brad Marlborough
 */


public class LogFactor5ErrorDialog extends LogFactor5Dialog {



  public LogFactor5ErrorDialog(JFrame jframe, String message) {
    super(jframe, "Error", true);

    JButton ok = new JButton("Ok");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        hide();
      }
    });

    JPanel bottom = new JPanel();
    bottom.setLayout(new FlowLayout());
    bottom.add(ok);

    JPanel main = new JPanel();
    main.setLayout(new GridBagLayout());
    wrapStringOnPanel(message, main);

    getContentPane().add(main, BorderLayout.CENTER);
    getContentPane().add(bottom, BorderLayout.SOUTH);
    show();

  }



}
