package org.apache.log4j.varia;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.Configurator;
import java.net.URL;
import  org.apache.log4j.spi.LoggerRepository;

public class ReloadingPropertyConfigurator implements Configurator {


  PropertyConfigurator delegate = new PropertyConfigurator();

  
  public ReloadingPropertyConfigurator() {    
  }

  public
  void doConfigure(URL url, LoggerRepository repository) {
  }

}
