package parking.refactored;

import java.util.List;
import parking.refactored.ParkingFormatter.Receipt;

/** - OCP: adds CSV reporting without changing ParkingService. */
public class CsvParkingFormatter extends ParkingFormatter {
    @Override
    public String formatDailyReport(List<Receipt> receipts, double total) {
        StringBuilder csv = new StringBuilder("plate,vehicle_type,hours,amount,payment_method\n");
        for (Receipt receipt : receipts) {
            csv.append(escape(receipt.plate())).append(',')
                    .append(escape(receipt.vehicleType())).append(',')
                    .append(receipt.hours()).append(',')
                    .append(receipt.amount()).append(',')
                    .append(escape(receipt.paymentName())).append('\n');
        }
        csv.append("TOTAL,,,").append(total).append(',');
        return csv.toString();
    }

    private String escape(String field) {
        if (field.contains(",") || field.contains("\"")
                || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}
