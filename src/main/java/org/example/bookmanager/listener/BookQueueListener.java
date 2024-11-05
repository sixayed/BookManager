package org.example.bookmanager.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookmanager.convertor.MessageConvertor;
import org.example.bookmanager.exception.BookNotFoundException;
import org.example.bookmanager.messages.AddMessage;
import org.example.bookmanager.messages.DeleteMessage;
import org.example.bookmanager.messages.UpdateMessage;
import org.example.bookmanager.service.BookService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RabbitListener(queues = "${rabbit.queue.name}")
@RequiredArgsConstructor
public class BookQueueListener {

    private final BookService bookService;
    private final MessageConvertor messageConvertor;

    @RabbitHandler
    public void receiveCreateMessage(AddMessage addMessage) {
        log.info("receive addMessage: {}", addMessage);
        bookService.addBook(messageConvertor.convertToBook(addMessage));
    }

    @RabbitHandler
    public void receiveUpdateMessage(UpdateMessage updateMessage) {
        log.info("receive updateMessage: {}", updateMessage);
        try {
            bookService.updateBook(updateMessage.getId(), messageConvertor.convertToBook(updateMessage));
        } catch (BookNotFoundException e) {
            log.error("Failed to update book: Book with id={} not found", updateMessage.getId());
        }
    }

    @RabbitHandler
    public void receiveDeleteMessage(DeleteMessage deleteMessage) {
        log.info("receive deleteMessage: {}", deleteMessage);
        bookService.deleteBook(deleteMessage.getId());
    }
}
