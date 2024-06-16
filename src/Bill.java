public class Bill {
    private static int billCount = 0;
    private final int id;
    private final String type;
    private final double amount;

    public static int getBillCount() {
        return billCount;
    }

    private final String dueDate;
    private final String provider;
    private String state;

    public Bill(String type, double amount, String dueDate, String provider) {
        this.id = ++billCount;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = provider;
        this.state = "NOT_PAID";
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getProvider() {
        return provider;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
