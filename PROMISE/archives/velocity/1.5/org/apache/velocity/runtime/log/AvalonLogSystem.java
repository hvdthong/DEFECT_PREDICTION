public class AvalonLogSystem extends AvalonLogChute implements LogSystem
{
    /**
     *  @param level
     * @param message
     * @deprecated Use log(level, message).
     */
    public void logVelocityMessage(int level, String message)
    {
        log(level, message);
    }
}
