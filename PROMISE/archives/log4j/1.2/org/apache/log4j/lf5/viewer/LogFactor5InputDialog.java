package org.apache.log4j.lf5.viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * LogFactor5InputDialog
 *
 * Creates a popup input dialog box so that users can enter
 * a URL to open a log file from.
 *
 * @author Richard Hurst
 * @author Brad Marlborough
 */


public class LogFactor5InputDialog extends LogFactor5Dialog {
  public static final int SIZE = 30;

  private JTextField _textField;

  /**
   * Configures an input dialog box using a defualt size for the text field.
   * param jframe the frame where the dialog will be loaded from.
   * param title the title of the dialog box.
   * param label the label to be put in the dialog box.
   */
  public LogFactor5InputDialog(JFrame jframe, String title, String label) {
    this(jframe, title, label, SIZE);
  }

  /**
   * Configures an input dialog box.
   * param jframe the frame where the dialog will be loaded from.
   * param title the title of the dialog box.
   * param label the label to be put in the dialog box.
   * param size the size of the text field.
   */
  public LogFactor5InputDialog(JFrame jframe, String title, String label,
      int size) {
    super(jframe, title, true);

    JPanel bottom = new JPanel();
    bottom.setLayout(new FlowLayout());

    JPanel main = new JPanel();
    main.setLayout(new FlowLayout());
    main.add(new JLabel(label));
    _textField = new JTextField(size);
    main.add(_textField);

    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          hide();
        }
      }
    });

    JButton ok = new JButton("Ok");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        hide();
      }
    });

    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        hide();
        _textField.setText("");
      }
    });

    bottom.add(ok);
    bottom.add(cancel);
    getContentPane().add(main, BorderLayout.CENTER);
    getContentPane().add(bottom, BorderLayout.SOUTH);
    pack();
    centerWindow(this);
    show();
  }

  public String getText() {
    String s = _textField.getText();

    if (s != null && s.trim().length() == 0) {
      return null;
    }

    return s;

  }



}
