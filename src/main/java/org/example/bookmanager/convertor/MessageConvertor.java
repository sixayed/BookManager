package org.example.bookmanager.convertor;

import org.example.bookmanager.entity.Book;
import org.example.bookmanager.messages.AddMessage;
import org.example.bookmanager.messages.UpdateMessage;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageConvertor {

    private final ModelMapper modelMapper = new ModelMapper();

    public Book convertToBook(AddMessage addMessage) {
        return modelMapper.map(addMessage, Book.class);
    }

    public Book convertToBook(UpdateMessage updateMessage) {
        return modelMapper.map(updateMessage, Book.class);
    }
}
