public class SysoutConfigurator implements Configurator {
  public
  void
  doConfigure(java.net.URL url,  LoggerRepository hierarchy) {
    Category.getRoot().addAppender(
        new ConsoleAppender(
            new SimpleLayout(), ConsoleAppender.SYSTEM_OUT));
  }
}
