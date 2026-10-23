package parking.refactored;

/** OCP: keeps the truck price in a strategy instead of a service switch. */
public class TruckTariff implements Tariff {
    @Override
    public String name() {
        return "TRUCK";
    }

    @Override
    public double fee(int hours) {
        return 20 + hours * 10.0;
    }
}
