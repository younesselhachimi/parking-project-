package parking.refactored;

/** ISP and LSP: cash exposes pay only, avoiding an unsupported refund method. */
public class CashPayment implements PaymentMethod {
    @Override
    public String name() {
        return "CASH";
    }

    @Override
    public void pay(double amount) {
        System.out.println("[CASH] received " + amount + " MAD");
    }
}
