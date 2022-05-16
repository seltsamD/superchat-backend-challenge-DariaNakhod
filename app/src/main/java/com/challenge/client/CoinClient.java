package com.challenge.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinClient {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    @Value("${com.challenge.client.coin.url}")
    private String coinUrl;

    public String getCurrentBitcoinPrice() {
        Request request = new Request.Builder()
                .url(coinUrl)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            CoinResponse coinResponse = objectMapper.readValue(response.body().string(), CoinResponse.class);
            return coinResponse.getData().getAmount() + " " + coinResponse.getData().getCurrency();
        } catch (IOException e) {
            log.warn("Unable to get current bitcoin price", e);
            return "undefined";
        }
    }
}

