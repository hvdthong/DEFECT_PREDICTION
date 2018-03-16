package fr.jayasoft.ivy.conflict;

public class StrictConflictException extends RuntimeException {

    public StrictConflictException() {
        super();
    }

    public StrictConflictException(String msg) {
        super(msg);
    }

    public StrictConflictException(Throwable t) {
        super(t);
    }

    public StrictConflictException(String msg, Throwable t) {
        super(msg, t);
    }

}
