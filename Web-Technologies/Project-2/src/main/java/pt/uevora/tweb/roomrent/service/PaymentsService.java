package pt.uevora.tweb.roomrent.service;


import org.json.JSONObject;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import pt.uevora.tweb.roomrent.repositories.PaymentsRepository;
import pt.uevora.tweb.roomrent.model.Advertisement;
import pt.uevora.tweb.roomrent.model.Payment;
import pt.uevora.tweb.roomrent.model.User;
import pt.uevora.tweb.roomrent.model.MbPayment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class PaymentsService {

    private final String PAYMENTS_ENDPOINT = "https://magno.di.uevora.pt/tweb/t2/mbref4payment";

    private final PaymentsRepository payRepo;
    private final HttpClient httpClient;


    /**
     * Constructor
     * @param payRepo The payments repository
     */
    public PaymentsService(PaymentsRepository payRepo) {
        this.payRepo = payRepo;
        this.httpClient = HttpClient.newHttpClient();
    }


    /**
     * Save a payment to the database
     * @param payment The payment to save
     * @return The saved payment
     */
    @Transactional
    public Payment savePayment(Payment payment) {
        return payRepo.save(payment);
    }


    /**
     * Fetch payment information from external API
     * @param amount The amount for which to fetch payment info
     * @return The MbPayment object containing payment details
     */
    public MbPayment fetchPaymentsFromAPI(Double amount) {
        String amountStr = "amount=" + amount;

        HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(PAYMENTS_ENDPOINT))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(amountStr))
                    .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if(status >= 200 && status < 300) {
                String responseBody = response.body();                
                JSONObject responseJson = new JSONObject(responseBody);
                
                String entity = responseJson.getString("mb_entity");
                String reference = responseJson.getString("mb_reference");
                Double mbAmount = responseJson.getDouble("mb_amount");

                MbPayment mbPayment = new MbPayment(entity, reference, mbAmount);
                return mbPayment;
            }
            else {
                throw new RuntimeException("Failed to fetch payment info from API, status code: " + status);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch payment info from API", e);
        }
                    
    }
    
    
    /**
     * Get the payment associated with a specific advertisement.
     * @param ad The advertisement whose payment is to be retrieved.
     * @return The payment associated with the advertisement.
     */
    public Payment getPaymentByAdvertisement(Advertisement ad) {
        return payRepo.findByAdvertisement(ad);
    }

    /**
     * Get all payments associated with advertisements posted by a specific user.
     * @param user The user whose advertisements' payments are to be retrieved.
     * @return A list of payments associated with the user's advertisements.
     */
    public List<Payment> getPaymentsByUser(User user) {
        return payRepo.findByAdvertisementAdvertiser(user);
    }

}