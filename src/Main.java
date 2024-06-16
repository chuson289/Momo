import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static List<Bill> bills = new ArrayList<>();

    static {
        String json = "{\n" +
                "  \"bills\": [\n" +
                "    {\n" +
                "      \"number\": 1,\n" +
                "      \"type\": \"ELECTRIC\",\n" +
                "      \"provider\": \"EVN HCMC\",\n" +
                "      \"amount\": 200000,\n" +
                "      \"dueDate\": \"25/10/2020\",\n" +
                "      \"status\": \"NOT_PAID\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 2,\n" +
                "      \"type\": \"WATER\",\n" +
                "      \"provider\": \"SAVACO HCMC\",\n" +
                "      \"amount\": 175000,\n" +
                "      \"dueDate\": \"30/10/2020\",\n" +
                "      \"status\": \"NOT_PAID\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"number\": 3,\n" +
                "      \"type\": \"INTERNET\",\n" +
                "      \"provider\": \"VNPT\",\n" +
                "      \"amount\": 800000,\n" +
                "      \"dueDate\": \"30/11/2020\",\n" +
                "      \"status\": \"NOT_PAID\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        bills = PaymentUtil.loadBillsFromJson(json);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PaymentService paymentService = new PaymentService(0);
        paymentService.initBills(bills);
        System.out.println("Welcome to the Payment Service System!");


        while (true) {
            System.out.println("\nEnter your transaction:");
            System.out.println("1. CASH_IN");
            System.out.println("2. LIST_BILL");
            System.out.println("3. PAY");
            System.out.println("4. DUE_DATE");
            System.out.println("5. SCHEDULE");
            System.out.println("6. LIST_PAYMENT");
            System.out.println("7. SEARCH_BILL_BY_PROVIDER");
            System.out.println("8. CREATE_BILL");
            System.out.println("9. VIEW_CURRENT_BALANCE");
            System.out.println("10. EXIT");

            System.out.print("Enter your choice (1-10): ");
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "1":
                case "CASH_IN":
                    System.out.print("Enter amount to cash in: ");
                    double amount = readDoubleInput(scanner);
                    paymentService.cashIn(amount);
                    break;
                case "2":
                case "LIST_BILL":
                    printBills(paymentService.getAllBills());
                    break;
                case "3":
                case "PAY":
                    List<Integer> payIds = new ArrayList<>();
                    System.out.print("Enter bill id to pay: ");
                    String line = scanner.nextLine();
                    Scanner lineScanner = new Scanner(line);
                    while (lineScanner.hasNextInt()) {
                        int number = readIntegerInput(lineScanner);
                        payIds.add(number);
                    }
                    paymentService.payBill(payIds);
                    break;
                case "4":
                case "DUE_DATE":
                    paymentService.dueBills();
                    break;
                case "5":
                case "SCHEDULE":
                    System.out.print("Enter bill id to schedule payment: ");
                    int scheduleBillId = readIntegerInput(scanner);
                    System.out.print("Enter scheduled date (DD/MM/YYYY): ");
                    String scheduledDate = scanner.nextLine().trim();
                    paymentService.schedulePayment(scheduleBillId, scheduledDate);
                    break;
                case "6":
                case "LIST_PAYMENT":
                    paymentService.listPayments();
                    break;
                case "7":
                case "SEARCH_BILL_BY_PROVIDER":
                    System.out.print("Enter provider name: ");
                    String provider = scanner.nextLine().trim();
                    printBills(paymentService.searchBillByProvider(provider));
                    break;
                case "8":
                case "CREATE_BILL":
                    System.out.print("Enter bill type: ");
                    String type = scanner.nextLine().trim();
                    System.out.print("Enter bill amount: ");
                    double billAmount = readDoubleInput(scanner);
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                    System.out.print("Enter due date (DD/MM/YYYY): ");
                    String dueDate = scanner.nextLine().trim();
                    System.out.print("Enter provider: ");
                    String billProvider = scanner.nextLine().trim();
                    paymentService.addBill(type, billAmount, dueDate, billProvider);
                    break;
                case "9":
                case "VIEW_CURRENT_BALANCE":
                    paymentService.viewCurrentBalance();
                    break;
                case "10":
                case "EXIT":
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid input. Please enter a valid option.");
            }
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
    }

    private static double readDoubleInput(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a valid amount: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }

    private static int readIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid bill ID: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void printBills(List<Bill> bills)  {
        System.out.printf("%-5s %-15s %-10s %-15s %-20s %-10s%n", "ID", "Type", "Amount", "Due Date", "Provider", "State");
        System.out.println("----------------------------------------------------------------------");
        for (Bill bill : bills) {
            System.out.printf("%-5d %-15s %-10.2f %-15s %-20s %-10s%n",
                    bill.getId(), bill.getType(), bill.getAmount(), bill.getDueDate(), bill.getProvider(), bill.getState());
        }
    }
}