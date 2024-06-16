import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void testCashIn() {
        PaymentService paymentService = new PaymentService(0);
        paymentService.cashIn(1000);
        assertEquals(1000, paymentService.getAvailableBalance());
    }

    @Test
    void testAddBill() {
        PaymentService paymentService = new PaymentService(0);
        paymentService.addBill("Electricity", 100, "2024-06-30", "Provider");
        assertEquals(1, paymentService.getAllBills().size());
    }

    @Test
    void testPayBill() {
        PaymentService paymentService = new PaymentService(1000);
        paymentService.addBill("Electricity", 100, "2024-06-30", "Provider");
        List<Integer> payID = new ArrayList<>();
        payID.add(1);
        paymentService.payBill(payID);
        assertEquals(1, paymentService.getAllPayment().size());
        assertEquals(900, paymentService.getAvailableBalance());
    }

    @Test
    void testSchedule() {
        PaymentService paymentService = new PaymentService(1000);
        paymentService.addBill("Electricity", 100, "2024-06-30", "Provider");
        paymentService.schedulePayment(1, "2024-06-25");
        assertEquals("SCHEDULED", paymentService.getAllBills().get(0).getState());
    }

    @Test
    void testSearchBillByProvider() {
        PaymentService paymentService = new PaymentService(0);
        paymentService.addBill("Electricity", 100, "2024-06-30", "Provider");
        paymentService.addBill("Water", 50, "2024-06-30", "Provider");
        paymentService.addBill("Gas", 75, "2024-06-30", "AnotherProvider");
        assertEquals(2, paymentService.searchBillByProvider("Provider").size());
    }
}
