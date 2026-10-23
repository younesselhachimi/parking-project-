package parking.refactored;

/** Question 10b - OCP and LSP: adds a payment that honors the refund contract. */
public class MobileWalletPayment implements RefundablePayment {
    private final String id;

    public MobileWalletPayment(String id) {
        this.id = id;
    }

    @Override
    public String name() {
        return "MobileWallet";
    }
    @Override
    public void pay(double amount) {
        System.out.println("[MobileWallet@" + id + "] paid " + amount + " MAD");
    }

    @Override
    public void refund(double amount) {
        System.out.println("[MobileWallet@" + id + "] refunded " + amount + " MAD");
    }
}
