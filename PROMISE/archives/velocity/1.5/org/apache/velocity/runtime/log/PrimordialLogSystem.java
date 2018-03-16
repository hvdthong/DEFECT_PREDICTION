public class PrimordialLogSystem extends HoldingLogChute implements LogSystem
{
    /**
     * @param level
     * @param message
     * @deprecated Use log(level, message).
     */
    public void logVelocityMessage(int level, String message)
    {
        log(level, message);
    }

    /**
     * @param newLogger
     * @deprecated use transferTo(LogChute newChute)
     */
    public void dumpLogMessages( LogSystem newLogger )
    {
        transferTo(new LogChuteSystem(newLogger));
    }
}
