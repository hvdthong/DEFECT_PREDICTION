package org.apache.log4j.lf5.viewer;

import org.apache.log4j.lf5.util.ProductProperties;
import org.apache.log4j.lf5.util.Resource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * LogFactor5AboutDialog
 *
 * @author Michael J. Sikorsky
 * @author Robert Shaw
 * @author Brent Sprecher
 * @author Brad Marlborough
 */


public class LogFactor5AboutDialog extends JDialog {





  public LogFactor5AboutDialog(Frame owner) {
    super(owner, "About LogFactor5", true);

    ProductProperties props = ProductProperties.getInstance();

    setTitle("About LogFactor5");

    JPanel imagePanel = new JPanel();
    Resource aboutResource = new Resource("org/apache/log4j/lf5/viewer/images/" +
        "lf5_about.gif");
    URL aboutIconURL = aboutResource.getURL();
    ImageIcon aboutIcon = null;
    if (aboutIconURL != null) {
      aboutIcon = new ImageIcon(aboutIconURL);
    }

    JLabel imageLabel = new JLabel();

    if (aboutIcon != null) {
      imageLabel.setIcon(aboutIcon);
    }
    imagePanel.add(imageLabel);

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new GridLayout(3, 1));


    int numberOfRows = 13;
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(numberOfRows, 1));
    JLabel aboutLF5 = new JLabel("LogFactor5 v" + props.getProductVersionNumber(), JLabel.CENTER);
    aboutLF5.setHorizontalAlignment(SwingConstants.CENTER);
    JLabel donate = new JLabel("Contributed by ThoughtWorks Inc.");
    donate.setHorizontalAlignment(SwingConstants.CENTER);
    mainPanel.add(aboutLF5);
    mainPanel.add(Box.createVerticalStrut(10));
    mainPanel.add(donate);
    mainPanel.add(Box.createVerticalStrut(10));

    JButton ok = new JButton("OK");
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new FlowLayout());
    southPanel.add(ok);
    ok.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            hide();
          }
        }

    );
    getContentPane().add(imagePanel, BorderLayout.NORTH);
    getContentPane().add(mainPanel, BorderLayout.CENTER);
    getContentPane().add(southPanel, BorderLayout.SOUTH);
    setSize(414, 400);
    center();
  }

  protected void center() {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension comp = getSize();

    setLocation(((screen.width - comp.width) / 2),
        ((screen.height - comp.height) / 2));

  }



}






