package parking.refactored;

import java.io.FileWriter;
import java.io.IOException;

/** DIP: the file-writing detail implements the log contract used by ParkingService. */
public class FileReceiptLog implements ReceiptLog {
    private final String fileName;

    public FileReceiptLog(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void log(String receipt) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(receipt + System.lineSeparator());
        }
    }
}
