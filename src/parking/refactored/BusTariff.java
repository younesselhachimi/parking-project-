package parking.refactored;

/** Question 10a - OCP: adds BUS pricing without changing ParkingService. */
public class BusTariff implements Tariff {
    @Override
    public String name() {
        return "BUS";
    }

    @Override
    public double fee(int hours) {
        return hours * 15.0;
    }
}
