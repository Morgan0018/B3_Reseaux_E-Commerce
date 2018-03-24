package statistics.exception;

/**
 * @author Morgan
 */
public class RServeStatsException extends Exception {

    /**
     * Creates a new instance of <code>RServeStatsException</code> without detail message.
     */
    public RServeStatsException() {
    }


    /**
     * Constructs an instance of <code>RServeStatsException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public RServeStatsException(String msg) {
        super(msg);
    }
}
