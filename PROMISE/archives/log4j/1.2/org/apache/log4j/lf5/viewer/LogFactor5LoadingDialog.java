package org.apache.log4j.lf5.viewer;

import javax.swing.*;
import java.awt.*;

/**
 * LogFactor5LoadingDialog
 *
 * @author Richard Hurst
 * @author Brad Marlborough
 */


public class LogFactor5LoadingDialog extends LogFactor5Dialog {




  public LogFactor5LoadingDialog(JFrame jframe, String message) {
    super(jframe, "LogFactor5", false);

    JPanel bottom = new JPanel();
    bottom.setLayout(new FlowLayout());

    JPanel main = new JPanel();
    main.setLayout(new GridBagLayout());
    wrapStringOnPanel(message, main);

    getContentPane().add(main, BorderLayout.CENTER);
    getContentPane().add(bottom, BorderLayout.SOUTH);
    show();

  }



}
