package com.challenge.service.replacer;

import com.challenge.client.CoinClient;
import com.challenge.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BitcoinPriceReplacer implements PlaceholderReplacer {

    private static final String PATTERN_TO_REPLACE = "_price_";
    private final CoinClient client;

    @Override
    public boolean canHandle(String message) {
        return message.contains(PATTERN_TO_REPLACE);
    }

    @Override
    public String replace(Message message) {
        String currentBitcoinPrice = client.getCurrentBitcoinPrice();

        String body = message.getBody();
        return body.replaceAll(PATTERN_TO_REPLACE, currentBitcoinPrice);
    }
}
