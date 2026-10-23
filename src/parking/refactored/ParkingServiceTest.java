package parking.refactored;

import java.util.ArrayList;
import java.util.List;

/** DIP check: an in-memory ReceiptLog can replace the file logger in a test. */
public class ParkingServiceTest {
    public static void main(String[] args) throws Exception {
        InMemoryReceiptLog log = new InMemoryReceiptLog();
        ParkingService service = new ParkingService(log, new ParkingFormatter());
        Vehicle car = new Vehicle("12345-A-6", new CarTariff());
        Vehicle bike = new Vehicle("67890-B-1", new MotorbikeTariff());
        Vehicle truck = new Vehicle("24680-D-8", new TruckTariff());

        checkFee(15.0, service.fee(car, 3), "car");
        checkFee(4.0, service.fee(bike, 2), "motorbike");
        checkFee(60.0, service.fee(truck, 4), "truck");

        CardPayment card = new CardPayment("4242");
        CashPayment cash = new CashPayment();
        service.checkout(car, 3, card);
        service.checkout(bike, 2, cash);
        service.checkout(truck, 4, card);
        String deskMessage = service.cancelAtDesk(bike, 2);
        service.cancel(truck, 4, card);

        if (!deskMessage.equals("Refund 4.0 MAD at the desk for 67890-B-1")) {
            throw new AssertionError("Unexpected desk refund: " + deskMessage);
        }
        if (log.lines.size() != 3) {
            throw new AssertionError("Expected 3 logged receipts, got " + log.lines.size());
        }
        if (!service.dailyReport().endsWith("Total: 15.0 MAD")) {
            throw new AssertionError("Unexpected report total: " + service.dailyReport());
        }
        checkExtensions();
        System.out.println("ParkingServiceTest passed");
    }

    private static void checkFee(double expected, double actual, String vehicle) {
        if (Double.compare(expected, actual) != 0) {
            throw new AssertionError(vehicle + " fee: expected " + expected + ", got " + actual);
        }
    }

    private static final class InMemoryReceiptLog implements ReceiptLog {
        private final List<String> lines = new ArrayList<>();

        @Override
        public void log(String receipt) {
            lines.add(receipt);
        }
    }

    // OCP and DIP: question 10 examples use new types without editing the service.
    private static void checkExtensions() throws Exception {
        InMemoryReceiptLog log = new InMemoryReceiptLog();
        ParkingService service = new ParkingService(log, new CsvParkingFormatter());

        Vehicle bus = new Vehicle("BUS-1", new BusTariff());
        MobileWalletPayment wallet = new MobileWalletPayment("1234");
        if (Double.compare(service.fee(bus, 2), 30.0) != 0) {
            throw new AssertionError("BUS should cost 30.0 MAD for 2 hours");
        }

        service.checkout(bus, 2, wallet);
        service.cancel(bus, 2, wallet);

        if (log.lines.size() != 1
                || !log.lines.get(0).equals("BUS-1 | BUS | 2 h | 30.0 MAD | " + wallet.name())) {
            throw new AssertionError("In-memory logger did not receive the receipt");
        }
        String csv = service.dailyReport();
        if (!csv.startsWith("plate,vehicle_type,hours,amount,payment_method\n")
                || !csv.contains("BUS-1,BUS,2,30.0," + wallet.name() + "\n")
                || !csv.endsWith("TOTAL,,,0.0,")) {
            throw new AssertionError("Unexpected CSV report: " + csv);
        }
        System.out.println("BUS, wallet and CSV extension checks passed");
    }

}

/*
The result of the test :
[CARD ****4242] paid 15.0 MAD
[CASH] received 4.0 MAD
[CARD ****4242] paid 60.0 MAD
[CARD ****4242] refunded 60.0 MAD
[MobileWallet@1234] paid 30.0 MAD
[MobileWallet@1234] refunded 30.0 MAD
BUS, wallet and CSV extension checks passed
ParkingServiceTest passed

*/