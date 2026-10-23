package parking.refactored;

/** OCP: keeps the car price in a strategy instead of a service switch. */
public class CarTariff implements Tariff {
    @Override
    public String name() {
        return "CAR";
    }

    @Override
    public double fee(int hours) {
        return hours * 5.0;
    }
}
