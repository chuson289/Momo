import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentService {
    private double availableBalance;
    private final List<Bill> bills;
    private final List<Payment> payments;

    public PaymentService(double initialBalance) {
        this.availableBalance = initialBalance;
        this.bills = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public List<Bill> getAllBills() { return bills;}

    public List<Payment> getAllPayment() {return payments;}
    public List<Bill> searchBillByProvider(String provider) {
         return bills.stream()
                    .filter(bill -> bill.getProvider().equalsIgnoreCase(provider))
                    .toList();
    }

    public void initBills(List<Bill> billList) { bills.addAll(billList); }


    public void cashIn(double amount) {
        availableBalance += amount;
        System.out.println("Your available balance: " + availableBalance);
    }

    public void addBill(String type, double amount, String dueDate, String provider) {
        bills.add(new Bill(type, amount, dueDate, provider));
    }

    public void payBill(List<Integer> billIds) {
        // Validate and calculate total amount to be paid
        double totalAmountToPay = 0.0;
        List<Bill> billsToPay = new ArrayList<>();

        for (int billId : billIds) {
            Optional<Bill> optionalBill = bills.stream()
                    .filter(bill -> bill.getId() == billId)
                    .findFirst();

            if (optionalBill.isEmpty()) {
                System.out.println("Sorry! Not found a bill with id " + billId);
                return; // Exit early if any bill is not found
            }

            Bill billToPay = optionalBill.get();
            if (billToPay.getState().equalsIgnoreCase("PAID")) {
                System.out.println("Bill with id: " + billId + " is aleady paid.");
                return;
            }
            totalAmountToPay += billToPay.getAmount();
            billsToPay.add(billToPay);
        }

        // Process payments
        if (availableBalance >= totalAmountToPay) {
            processPayments(billsToPay);
        } else {
            createPendingPayments(billsToPay);
        }
    }

    private void processPayments(List<Bill> billsToPay) {
        for (Bill billToPay : billsToPay) {
            availableBalance -= billToPay.getAmount();
            bills.stream().filter(bill -> bill.getId() == billToPay.getId()).forEach(bill -> bill.setState("PAID"));
            createUpdatePayment(billToPay, "PROCESSED");
            System.out.println("Payment completed for Bill with id " + billToPay.getId() + ".");
        }
        System.out.println("Your current balance is: " + availableBalance);
    }

    private void createUpdatePayment(Bill billToPay, String state) {
        Optional<Payment> existingPayment = payments.stream()
                .filter(payment -> payment.getBillId() == billToPay.getId())
                .findFirst();
        if (existingPayment.isPresent()) {
            Payment paym = existingPayment.get();
            if (state.equalsIgnoreCase("PROCESSED")) {
                payments.stream().filter(payment -> payment.getId() == paym.getId()).forEach(payment -> payment.setState(state));
            }
            return;
        }
        Payment payment = new Payment(billToPay.getId(), billToPay.getAmount(), state, billToPay.getDueDate());
        payments.add(payment);
        if (state.equalsIgnoreCase("PENDING")) {
            System.out.println("Payment initiated with PENDING status for bill with id: " + billToPay.getId());
        }
    }

    private void createPendingPayments(List<Bill> billsToPay) {
        for (Bill billToPay : billsToPay) {
            createUpdatePayment(billToPay, "PENDING");
        }
        System.out.println("Sorry! Not enough fund to proceed with payment.");
    }


    public List<Bill> dueBills() {
        return bills.stream()
                .filter(bill -> "NOT_PAID".equals(bill.getState()))
                .collect(Collectors.toList());
    }

    public void schedulePayment(int billId, String scheduledDate) {
        Bill billToSchedule = bills.stream()
                .filter(bill -> bill.getId() == billId)
                .findFirst()
                .orElse(null);

        if (billToSchedule != null) {
            billToSchedule.setState("SCHEDULED");
            System.out.println("Payment for bill id " + billId + " is scheduled on " + scheduledDate);
        } else {
            System.out.println("Sorry! Bill with id " + billId + " not found.");
        }
    }

    public void listPayments() {
        System.out.printf(" %-5s  %-8s  %-12s  %-15s %-10s \n", "ID", "Bill ID", "Amount", "Payment Date", "State");
        System.out.println("------------------------------------------------------------------");

        for (Payment payment : payments) {
            System.out.printf(" %-5d  %-8d  %-12.2f  %-15s  %-10s\n",
                    payment.getId(), payment.getBillId(), payment.getAmount(), payment.getPaymentDate(), payment.getState());
        }
    }


    public void viewCurrentBalance() {
        System.out.println("Your current balance is: " + availableBalance);
    }
}
