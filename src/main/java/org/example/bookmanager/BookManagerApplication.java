package org.example.bookmanager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class BookManagerApplication {

    private final RabbitAdmin rabbitAdmin;
    private final Queue queue;

    @PostConstruct
    public void declareQueues() {
        rabbitAdmin.declareQueue(queue);
    }

    public static void main(String[] args) {
        SpringApplication.run(BookManagerApplication.class, args);
    }

}
