package com.example.electro.service;

import com.example.electro.dto.payment.PaymentDTO;
import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    public static String generatePaymentLink(PaymentDTO paymentDTO) throws IOException {

        Dotenv dotenv = Dotenv.load();
        Gson gson = new Gson();
        String json = gson.toJson(paymentDTO);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder().url("https://accept.paymob.com/v1/intention/").method("POST", body)
                .addHeader("Authorization", dotenv.get("PAYMENT_SECRET_KEY"))
                .addHeader("Content-Type", "application/json").build();

        try (Response response = client.newCall(request).execute()) {

            JsonObject clientSecret = JsonParser.parseString(response.body().string()).getAsJsonObject();

            System.out.println(clientSecret.get("client_secret").toString());

            return "https://accept.paymob.com/unifiedcheckout/?" +
                    "publicKey=" +
                    dotenv.get("PAYMENT_PUBLIC_KEY") +
                    "&clientSecret=" +
                    clientSecret.get("client_secret").getAsString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }
}
