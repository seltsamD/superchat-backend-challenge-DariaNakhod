package com.challenge.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinResponse {

   private CoinDataResponse data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class CoinDataResponse {
        private String amount;
        private String currency;
    }
}
