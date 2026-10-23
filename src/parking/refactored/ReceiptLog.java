package parking.refactored;

import java.io.IOException;

/**
 */
public interface ReceiptLog {
    void log(String receipt) throws IOException;
}
