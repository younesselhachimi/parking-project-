package parking.refactored;

/** LSP: card payment honors both pay and refund promises of RefundablePayment. */
public class CardPayment implements RefundablePayment {
    private final String lastDigits;

    public CardPayment(String lastDigits) {
        this.lastDigits = lastDigits;
    }

    @Override
    public String name() {
        return "CARD";
    }

    @Override
    public void pay(double amount) {
        System.out.println("[CARD ****" + lastDigits + "] paid " + amount + " MAD");
    }

    @Override
    public void refund(double amount) {
        System.out.println("[CARD ****" + lastDigits + "] refunded " + amount + " MAD");
    }
}