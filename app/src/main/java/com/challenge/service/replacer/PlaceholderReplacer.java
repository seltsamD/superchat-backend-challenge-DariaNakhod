package com.challenge.service.replacer;

import com.challenge.domain.Message;

public interface PlaceholderReplacer {

    boolean canHandle(String message);

    String replace(Message message);
}
