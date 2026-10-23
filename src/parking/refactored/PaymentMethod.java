package parking.refactored;

/** ISP: exposes only the operations every payment method can perform. */
public interface PaymentMethod {
    String name();

    void pay(double amount);
}
