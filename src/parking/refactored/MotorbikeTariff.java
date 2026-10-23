package parking.refactored;

/** OCP: keeps the motorbike price in a strategy instead of a service switch. */
public class MotorbikeTariff implements Tariff {
    @Override
    public String name() {
        return "MOTORBIKE";
    }

    @Override
    public double fee(int hours) {
        return hours * 2.0;
    }
}
