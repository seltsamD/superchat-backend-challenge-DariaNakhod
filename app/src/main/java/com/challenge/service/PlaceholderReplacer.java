package com.challenge.service;

import com.challenge.domain.Message;

public interface PlaceholderReplacer {

    boolean canHandle(String message);

    String replace(Message message);
}
