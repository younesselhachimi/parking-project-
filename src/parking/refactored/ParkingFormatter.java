package parking.refactored;

import java.util.List;

/**
 * SRP: owns presentation of parking receipts, desk refunds, and reports.
 * OCP: a subclass can override reporting to provide another format.
 */
public class ParkingFormatter {
    /** SRP: structured receipt data belongs with its presentation model. */
    public record Receipt(String plate, String vehicleType, int hours,
                          double amount, String paymentName) {
    }

    public String formatReceipt(Receipt receipt) {
        return receipt.plate() + " | " + receipt.vehicleType() + " | "
                + receipt.hours() + " h | " + receipt.amount() + " MAD | "
                + receipt.paymentName();
    }

    public String formatDeskRefund(String plate, double amount) {
        return "Refund " + amount + " MAD at the desk for " + plate;
    }

    public String formatDailyReport(List<Receipt> receipts, double total) {
        StringBuilder report = new StringBuilder("=== Daily report ===\n");
        for (Receipt receipt : receipts) {
            report.append(formatReceipt(receipt)).append("\n");
        }
        report.append("Total: ").append(total).append(" MAD");
        return report.toString();
    }
}
