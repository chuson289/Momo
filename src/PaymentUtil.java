import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentUtil {
    public static List<Bill> loadBillsFromJson(String json) {
        List<Bill> bills = new ArrayList<>();

        // Remove whitespace and brackets
        json = json.trim().replaceAll("\\s+", "");
        json = json.substring(json.indexOf('[') + 1, json.lastIndexOf(']'));

        // Split the JSON string into individual bill strings
        String[] billStrings = json.split("\\},\\{");

        for (String billString : billStrings) {
            billString = billString.replaceAll("[\\[\\]{}\"]", "");

            String[] fields = billString.split(",");
            String type = "";
            double amount = 0;
            String dueDate = "";
            String provider = "";
            String state = "";

            for (String field : fields) {
                String[] keyValue = field.split(":");
                String key = keyValue[0];
                String value = keyValue[1];

                switch (key) {
                    case "type":
                        type = value;
                        break;
                    case "amount":
                        amount = Double.parseDouble(value);
                        break;
                    case "dueDate":
                        dueDate = value;
                        break;
                    case "provider":
                        provider = value;
                        break;
                    case "status":
                        state = value;
                        break;
                }
            }

            Bill bill = new Bill(type, amount, dueDate, provider);
            bill.setState(state);
            bills.add(bill);
        }

        return bills;
    }
}
