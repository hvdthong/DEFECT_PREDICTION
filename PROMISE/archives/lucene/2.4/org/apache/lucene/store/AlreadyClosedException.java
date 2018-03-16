public class AlreadyClosedException extends IllegalStateException {
  public AlreadyClosedException(String message) {
    super(message);
  }
}
