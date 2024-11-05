package org.example.bookmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmanager.messages.BookMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookMessageProducer {

    @Value("${rabbit.queue.name}")
    private String queueName;

    private final RabbitTemplate RabbitTemplate;

    public void sendMessage(BookMessage message) {
        log.info("sending message to queue: {}, {}", queueName, message);
        RabbitTemplate.convertAndSend(queueName, message);
    }
}
