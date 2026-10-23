package parking.refactored;

import java.io.IOException;

/** DIP through dependency injection: wires concrete collaborators into ParkingService. */
public class Main {
    public static void main(String[] args) throws IOException {
        ParkingService service = new ParkingService(
                new FileReceiptLog("parking.log"), new ParkingFormatter());

        Vehicle car = new Vehicle("12345-A-6", new CarTariff());
        Vehicle bike = new Vehicle("67890-B-1", new MotorbikeTariff());
        Vehicle truck = new Vehicle("24680-D-8", new TruckTariff());

        CardPayment card = new CardPayment("4242");
        CashPayment cash = new CashPayment();

        service.checkout(car, 3, card);
        service.checkout(bike, 2, cash);
        service.checkout(truck, 4, card);

        System.out.println(service.cancelAtDesk(bike, 2));
        service.cancel(truck, 4, card);

        System.out.println(service.dailyReport());
    }
}
