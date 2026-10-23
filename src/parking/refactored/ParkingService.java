package parking.refactored;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import parking.refactored.ParkingFormatter.Receipt;

/**
 * SRP: coordinates parking transactions while collaborators own pricing,
 * formatting, and logging. DIP: receives its log through the ReceiptLog contract.
 */
public class ParkingService {
    private final ReceiptLog receiptLog;
    private final ParkingFormatter formatter;
    private final List<Receipt> receipts = new ArrayList<>();
    private double total;

    public ParkingService(ReceiptLog receiptLog, ParkingFormatter formatter) {
        this.receiptLog = receiptLog;
        this.formatter = formatter;
    }

    public double fee(Vehicle vehicle, int hours) {
        return vehicle.getTariff().fee(hours);
    }

    public void checkout(Vehicle vehicle, int hours, PaymentMethod method)
            throws IOException {
        double amount = fee(vehicle, hours);
        method.pay(amount);
        total += amount;

        Receipt receipt = new Receipt(vehicle.getPlate(), vehicle.getType(), hours,
                amount, method.name());
        receipts.add(receipt);
        receiptLog.log(formatter.formatReceipt(receipt));
    }

    public void cancel(Vehicle vehicle, int hours, RefundablePayment method) {
        double amount = fee(vehicle, hours);
        method.refund(amount);
        total -= amount;
    }

    /** Records a refund handled at the desk; the caller must confirm the cash payout. */
    public String cancelAtDesk(Vehicle vehicle, int hours) {
        double amount = fee(vehicle, hours);
        String message = formatter.formatDeskRefund(vehicle.getPlate(), amount);
        total -= amount;
        return message;
    }

    public String dailyReport() {
        return formatter.formatDailyReport(List.copyOf(receipts), total);
    }
     public double getTotal() {
        return total;
    }
}
