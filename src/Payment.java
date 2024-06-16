import java.time.LocalDate;

public class Payment {
    private static int paymentCount = 0;
    private final int id;
    private final int billId;
    private final double amount;
    private final String paymentDate;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String state;

    public Payment(int billId, double amount, String state, String paymentDate) {
        this.id = ++paymentCount;
        this.billId = billId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getBillId() {
        return billId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }
}
