package parking.refactored;

/** ISP and LSP: only methods able to honor the refund contract implement this role. */
public interface RefundablePayment extends PaymentMethod {
    void refund(double amount);
}
