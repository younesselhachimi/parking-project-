package parking.refactored;

/** OCP: new vehicle prices are added through new Tariff implementations. */
public interface Tariff {
    String name();

    double fee(int hours);
}
